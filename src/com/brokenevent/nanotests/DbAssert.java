/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Broken Event.
 * http://brokenevent.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.brokenevent.nanotests;

import com.brokenevent.nanotests.db.ConnectionWrapper;
import org.junit.Assert;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for DataBase-related asserts.
 * These methods can be used directly:
 * <code>DbAssert.assertQueryNotNull(...)</code>, however, they read better if they
 * are referenced through static import:
 *
 * <pre>
 * import static com.brokenevent.nanotests.DbAssert.*;
 *    ...
 *    assertQueryNotNull(...);
 * </pre>
 *
 * @author BrokenEvent
 */
public final class DbAssert {
  /**
   * Protect constructor since it is a static only class
   */
  protected DbAssert(){}

  private static List<ConnectionWrapper> wrappers = new ArrayList<ConnectionWrapper>();
  private static final Object lock = new Object();
  private static String connectionString;
  private static String login;
  private static String password;

  /**
   * Initialize DbAssert before usage. Should be called from {@link org.junit.Before} or {@link org.junit.BeforeClass} method of the testcase.
   * @param connectionString jdbc connection string
   * @param login database login
   * @param password database password
   * @param driverClassName driver classname
   */
  public static void initDbAssert(String connectionString, String login, String password, String driverClassName){
    try {
      Class.forName(driverClassName);
      DbAssert.connectionString = connectionString;
      DbAssert.login = login;
      DbAssert.password = password;
    } catch (ClassNotFoundException e) {
      throw new AssertionError("Failed to register db driver: " + driverClassName, e);
    }
  }

  private static ConnectionWrapper getConnection() throws SQLException {
    synchronized (lock){
      for (ConnectionWrapper wrapper: wrappers)
        if (!wrapper.isBusy())
          return wrapper;

      ConnectionWrapper wrapper = new ConnectionWrapper(connectionString, login, password);
      wrappers.add(wrapper);
      return wrapper;
    }
  }

  /**
   * Shutdown db connection. Should be called from {@link org.junit.After} or {@link org.junit.AfterClass} method of the testcase.
   */
  public static void shutdownDbAssert(){
    try{
      for(ConnectionWrapper wrapper: wrappers)
        if (wrapper.isBusy())
          return;

      for (ConnectionWrapper wrapper: wrappers)
        wrapper.release();

      wrappers.clear();
    }catch (SQLException e){
      throw new AssertionError("Failed to disconnect from db", e);
    }
  }

  /**
   * Helper method to execute query with the existing connection.
   * @param sql query text
   * @return query result
   */
  public static List<Map<String, Object>> query(String sql){
    ConnectionWrapper wrapper = null;
    try {
      wrapper = getConnection();
      Statement statement = wrapper.getStatement();
      return processResultSet(statement.executeQuery(sql));
    } catch (SQLException e) {
      throw new AssertionError("Fail on process result set for " + sql);
    } finally {
      if (wrapper != null)
        try {
          wrapper.close();
        } catch (SQLException e) {
          Assert.fail("Failed to close SQL statement");
        }
    }
  }

  /**
   * Helper method to execute query with a single-row result with the existing connection.
   * @param sql query text
   * @return query result
   */
  public static Map<String, Object> querySingle(String sql){
    ConnectionWrapper wrapper = null;
    try {
      wrapper = getConnection();
      Statement statement = wrapper.getStatement();
      return processResultSetSingle(statement.executeQuery(sql));
    } catch (SQLException e) {
      e.printStackTrace();
      throw new AssertionError("Fail on process result set for " + sql);
    } finally {
      if (wrapper != null)
        try {
          wrapper.close();
        } catch (SQLException e) {
          Assert.fail("Failed to close SQL statement");
        }
    }
  }

  /**
   * Helper method to execute query with a single-column result with the existing connection.
   * @param sql query text
   * @return query result
   */
  public static <T> List<T> querySingleColumn(String sql){
    ConnectionWrapper wrapper = null;
    try {
      wrapper = getConnection();
      Statement statement = wrapper.getStatement();
      return processResultSetSingleColumn(statement.executeQuery(sql));
    } catch (SQLException e) {
      e.printStackTrace();
      throw new AssertionError("Fail on process result set for " + sql);
    } finally {
      if (wrapper != null)
        try {
          wrapper.close();
        } catch (SQLException e) {
          Assert.fail("Failed to close SQL statement");
        }
    }
  }

  /**
   * Helper method to execute non-query statement with the existing connection.
   * @param sql query text
   */
  public static void execute(String sql){
    ConnectionWrapper wrapper = null;
    try {
      wrapper = getConnection();
      Statement statement = wrapper.getStatement();
      statement.execute(sql);
    } catch (SQLException e) {
      throw new AssertionError("Fail on execute: " + sql, e);
    } finally {
      if (wrapper != null)
        try {
          wrapper.close();
        } catch (SQLException e) {
          Assert.fail("Failed to close SQL statement");
        }
    }
  }

  private static List<Map<String, Object>> processResultSet(ResultSet resultSet) throws SQLException {
    List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
    ResultSetMetaData meta = resultSet.getMetaData();

    while (resultSet.next()){
      Map<String, Object> map = new HashMap<String, Object>();
      for (int i = 0; i < meta.getColumnCount(); i++)
        map.put(meta.getColumnLabel(i + 1), resultSet.getObject(i + 1));

      result.add(map);
    }

    return result;
  }

  private static Map<String, Object> processResultSetSingle(ResultSet resultSet) throws SQLException {
    ResultSetMetaData meta = resultSet.getMetaData();

    if (resultSet.next()){
      Map<String, Object> map = new HashMap<String, Object>();
      for (int i = 0; i < meta.getColumnCount(); i++)
        map.put(meta.getColumnLabel(i + 1), resultSet.getObject(i + 1));

      return map;
    }

    return null;
  }

  @SuppressWarnings(value = "unchecked")
  private static <T> List<T> processResultSetSingleColumn(ResultSet resultSet) throws SQLException {
    List<T> result = new ArrayList<T>();
    while (resultSet.next())
      result.add((T) resultSet.getObject(1));

    return result;
  }

  /**
   * Asserts that query result is not empty. If it is, an {@link AssertionError} is thrown.
   * @param sql SQL query text
   */
  public static void assertQueryNotNull(String sql){
    ConnectionWrapper wrapper = null;
    try {
      wrapper = getConnection();
      Statement statement = wrapper.getStatement();
      ResultSet resultSet = statement.executeQuery(sql);
      if (!resultSet.next())
        Assert.fail("Query <" + sql + "> result is empty, but expected to be not empty");
    } catch (SQLException e) {
      throw new AssertionError("Fail on resultSet.next()");
    } finally {
      if (wrapper != null)
        try {
          wrapper.close();
        } catch (SQLException e) {
          Assert.fail("Failed to close SQL statement");
        }
    }
  }

  /**
   * Asserts that query result is empty. If it isn't, an {@link AssertionError} is thrown.
   * @param sql SQL query text
   */
  public static void assertQueryNull(String sql){
    ConnectionWrapper wrapper = null;
    ResultSet resultSet = null;
    try {
      wrapper = getConnection();
      Statement statement = wrapper.getStatement();
      resultSet = statement.executeQuery(sql);
      if (resultSet.next())
        Assert.fail("Query <" + sql + "> result is not empty, but expected to be empty");
    } catch (SQLException e) {
      throw new AssertionError("Fail on resultSet.next()");
    } finally {
      if (resultSet != null)
        try {
          resultSet.close();
        } catch (SQLException e) {
          Assert.fail("Failed to close SQL ResultSet");
        }
      if (wrapper != null)
        try {
          wrapper.close();
        } catch (SQLException e) {
          Assert.fail("Failed to close SQL statement");
        }
    }
  }

  /**
   * Asserts that the value from the last row of the table equals given value.
   * If it isn't, an {@link AssertionError} is thrown.<br>
   * The last ID will be got by a query:
   * <pre>SELECT MAX(id) FROM table</pre>
   * Beware to use this assertion when running multiple tests interconnected with
   * a single table at once. Some tests can fail due to thread operations intersection.
   * @param table table name to query from
   * @param id table primary key field name
   * @param field field name to assert value from
   * @param expected expected value
   */
  public static void assertLastRow(String table, String id, String field, String expected){
    Map<String, Object> result = querySingle("SELECT " + field + " FROM " + table + " WHERE " + id + " = (SELECT MAX(" + id + ") FROM " + table + ")");
    Assert.assertEquals(expected, result.get(field));
  }

  /**
   * Asserts that the value from the last row of the table equals given value.
   * If it isn't, an {@link AssertionError} is thrown.<br>
   * The last ID will be got by a query:
   * <pre>SELECT MAX(id) FROM table</pre>
   * Beware to use this assertion when running multiple tests interconnected with
   * a single table at once. Some tests can fail due to thread operations intersection.
   * @param table table name to query from
   * @param id table primary key field name
   * @param field field name to assert value from
   * @param expected expected value
   */
  public static void assertLastRow(String table, String id, String field, int expected){
    Map<String, Object> result = querySingle("SELECT " + field + " FROM " + table + " WHERE " + id + " = (SELECT MAX(" + id + ") FROM " + table + ")");
    Assert.assertEquals(expected, ((Number)result.get(field)).intValue());
  }

  /**
   * Asserts that the value from the last row of the table equals given value.
   * If it isn't, an {@link AssertionError} is thrown.<br>
   * The last ID will be got by a query:
   * <pre>SELECT MAX(id) FROM table</pre>
   * Beware to use this assertion when running multiple tests interconnected with
   * a single table at once. Some tests can fail due to thread operations intersection.
   * @param table table name to query from
   * @param id table primary key field name
   * @param field field name to assert value from
   * @param expected expected value
   */
  public static void assertLastRow(String table, String id, String field, java.util.Date expected){
    Map<String, Object> result = querySingle("SELECT " + field + " FROM " + table + " WHERE " + id + " = (SELECT MAX(" + id + ") FROM " + table + ")");
    Assert.assertEquals(expected, result.get(field));
  }

  /**
   * Asserts that the value from the last row of the table equals given value.
   * If it isn't, an {@link AssertionError} is thrown.<br>
   * The last ID will be got by a query:
   * <pre>SELECT MAX(id) FROM table</pre>
   * Beware to use this assertion when running multiple tests interconnected with
   * a single table at once. Some tests can fail due to thread operations intersection.
   * @param table table name to query from
   * @param id table primary key field name
   * @param field field name to assert value from
   * @param expected expected value
   */
  public static void assertLastRow(String table, String id, String field, boolean expected){
    Map<String, Object> result = querySingle("SELECT " + field + " FROM " + table + " WHERE " + id + " = (SELECT MAX(" + id + ") FROM " + table + ")");
    Assert.assertEquals(expected, result.get(field));
  }

  /**
   * Helper method to get the last ID from the table where ID field is numeric.
   * Uses query:<br>
   * <pre>SELECT MAX(id) FROM table</pre>
   * @param table table name to query from
   * @param id table primary key field name
   * @return last ID number
   */
  public static int getLastIdNum(String table, String id){
    return ((Number)getLastId(table, id)).intValue();
  }

  /**
   * Helper method to get the last ID from the table where ID field is string.
   * Uses query:<br>
   * <pre>SELECT MAX(id) FROM table</pre>
   * @param table table name to query from
   * @param id table primary key field name
   * @return last ID string value
   */
  public static String getLastIdStr(String table, String id){
    return getLastId(table, id).toString();
  }

  private static Object getLastId(String table, String id){
    ConnectionWrapper wrapper = null;
    ResultSet resultSet = null;
    String sql = "SELECT MAX(" + id + ") FROM " + table;
    try{
      wrapper = getConnection();
      Statement statement = wrapper.getStatement();
      resultSet = statement.executeQuery(sql);
      if (resultSet.next())
        return resultSet.getObject(1);
      else
        throw new AssertionError("GetLastId returned no data");
    } catch (SQLException e) {
      throw new AssertionError("Fail on execute: " + sql, e);
    } finally {
      if (resultSet != null)
        try {
          resultSet.close();
        } catch (SQLException e) {
          Assert.fail("Failed to close SQL ResultSet");
        }
      if (wrapper != null)
        try {
          wrapper.close();
        } catch (SQLException e) {
          Assert.fail("Failed to close SQL statement");
        }
    }
  }

  /**
   * Asserts that the value from one of the rows of the table equals the expected. If it isn't, an {@link AssertionError} is thrown.
   * @param table table name to query from
   * @param id table primary key field name (or other field to use in WHERE clause)
   * @param idValue value of the primary key field (or other field to use on comparison of the WHERE clause)
   * @param field field name to assert value from
   * @param expected expected value
   */
  public static void assertRow(String table, String id, String idValue, String field, String expected){
    Map<String, Object> result = querySingle("SELECT " + field + " FROM " + table + " WHERE " + id + "='" + idValue + "'");
    Assert.assertEquals(expected, result.get(field));
  }

  /**
   * Asserts that the value from one of the rows of the table equals the expected. If it isn't, an {@link AssertionError} is thrown.
   * @param table table name to query from
   * @param id table primary key field name (or other field to use in WHERE clause)
   * @param idValue value of the primary key field (or other field to use on comparison of the WHERE clause)
   * @param field field name to assert value from
   * @param expected expected value
   */
  public static void assertRow(String table, String id, String idValue, String field, int expected){
    Map<String, Object> result = querySingle("SELECT " + field + " FROM " + table + " WHERE " + id + "='" + idValue + "'");
    Assert.assertEquals(expected, ((Number)result.get(field)).intValue());
  }

  /**
   * Asserts that the value from one of the rows of the table equals the expected. If it isn't, an {@link AssertionError} is thrown.
   * @param table table name to query from
   * @param id table primary key field name (or other field to use in WHERE clause)
   * @param idValue value of the primary key field (or other field to use on comparison of the WHERE clause)
   * @param field field name to assert value from
   * @param expected expected value
   */
  public static void assertRow(String table, String id, String idValue, String field, boolean expected){
    Map<String, Object> result = querySingle("SELECT " + field + " FROM " + table + " WHERE " + id + "='" + idValue + "'");
    Assert.assertEquals(expected, result.get(field));
  }

  /**
   * Asserts that the value from one of the rows of the table equals the expected. If it isn't, an {@link AssertionError} is thrown.
   * @param table table name to query from
   * @param id table primary key field name (or other field to use in WHERE clause)
   * @param idValue value of the primary key field (or other field to use on comparison of the WHERE clause)
   * @param field field name to assert value from
   * @param expected expected value
   */
  public static void assertRow(String table, String id, int idValue, String field, String expected){
    Map<String, Object> result = querySingle("SELECT " + field + " FROM " + table + " WHERE " + id + "=" + idValue);
    Assert.assertEquals(expected, result.get(field));
  }

  /**
   * Asserts that the value from one of the rows of the table equals the expected. If it isn't, an {@link AssertionError} is thrown.
   * @param table table name to query from
   * @param id table primary key field name (or other field to use in WHERE clause)
   * @param idValue value of the primary key field (or other field to use on comparison of the WHERE clause)
   * @param field field name to assert value from
   * @param expected expected value
   */
  public static void assertRow(String table, String id, int idValue, String field, int expected){
    Map<String, Object> result = querySingle("SELECT " + field + " FROM " + table + " WHERE " + id + "=" + idValue);
    Assert.assertEquals(expected, ((Number)result.get(field)).intValue());
  }

  /**
   * Asserts that the value from one of the rows of the table equals the expected. If it isn't, an {@link AssertionError} is thrown.
   * @param table table name to query from
   * @param id table primary key field name (or other field to use in WHERE clause)
   * @param idValue value of the primary key field (or other field to use on comparison of the WHERE clause)
   * @param field field name to assert value from
   * @param expected expected value
   */
  public static void assertRow(String table, String id, int idValue, String field, boolean expected){
    Map<String, Object> result = querySingle("SELECT " + field + " FROM " + table + " WHERE " + id + "=" + idValue);
    Assert.assertEquals(expected, result.get(field));
  }
}
