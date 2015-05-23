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

package com.brokenevent.nanotests.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Simple SQL connection wrapper for connection pooling.
 * @author BrokenEvent
 */
public class ConnectionWrapper{
  private Connection connection;
  private Statement statement;

  /**
   * Creates and initializes wrapper with a connection.
   * @param connectionString SQL server connection string
   * @param login DB login
   * @param password DB password
   * @throws SQLException when the {@link java.sql.DriverManager} is failed to open connection
   */
  public ConnectionWrapper(String connectionString, String login, String password) throws SQLException {
    connection = DriverManager.getConnection(connectionString, login, password);
  }

  /**
   * Create the SQL statement. You should not use {@code statement.close()} after all operations done.
   * Use {@code wrapper.close()} ({@link ConnectionWrapper#close()}) instead.
   * @return SQL statement
   * @throws SQLException when connection is failed to create statement
   */
  public Statement getStatement() throws SQLException {
    return statement = connection.createStatement();
  }

  /**
   * Checks if the connection wrapper is busy (in use and not released) now.
   * @return True if connection wrapper is in use now
   */
  public boolean isBusy(){
    return statement != null;
  }

  /**
   * Closes the statement created by this wrapper and releases the wrapper for further usage.
   * @throws SQLException when statement is failed to close
   */
  public void close() throws SQLException {
    statement.close();
    statement = null;
  }

  /**
   * Releases the connection wrapper and its SQL connection.
   * @throws SQLException when connection is failed to close
   */
  public void release() throws SQLException {
    connection.close();
    connection = null;
  }
}
