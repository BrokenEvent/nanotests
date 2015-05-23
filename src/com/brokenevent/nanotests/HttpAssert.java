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

import com.brokenevent.nanotests.http.TestGetRequest;
import com.brokenevent.nanotests.http.TestRequest;
import org.apache.http.Header;
import org.junit.Assert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

/**
 * Helper class for http-related asserts.
 * These methods can be used directly:
 * <code>HttpAssert.assertHttpOk(...)</code>, however, they read better if they
 * are referenced through static import:
 *
 * <pre>
 * import static com.brokenevent.nanotests.HttpAssert.*;
 *    ...
 *    assertHttpOk(...);
 * </pre>
 *
 * @author BrokenEvent
 */
public final class HttpAssert {
  /**
   * Protect constructor since it is a static only class
   */
  protected HttpAssert(){}

  private static String normalizeUrl(String value){
    return value.replaceAll(" ", "%20");
  }

  /**
   * Asserts that the http query result code is OK (200). If it isn't, an {@link AssertionError} is thrown.<br>
   * Use this method to make a set of asserts for a single request.
   * To just check the result code for request, use {@link com.brokenevent.nanotests.HttpAssert#assertHttpOk(String)}.
   * @param request {@link TestRequest} to get result from
   */
  public static void assertHttpOk(TestRequest request){
    Assert.assertEquals(request.getRequestUrl(), 200, request.getResponse().getStatusLine().getStatusCode());
  }

  /**
   * Asserts that the http query result code is OK (200). If it isn't, an {@link AssertionError} is thrown with the given message.<br>
   * Use this method to make a set of asserts for a single request.
   * To just check the result code for request, use {@link com.brokenevent.nanotests.HttpAssert#assertHttpOk(String)}.
   * @param message the identifying message for the {@link AssertionError} (<code>null</code> okay)
   * @param request {@link TestRequest} to get result from
   */
  public static void assertHttpOk(String message, TestRequest request){
    Assert.assertEquals(message, 200, request.getResponse().getStatusLine().getStatusCode());
  }

  /**
   * Asserts that the http query result code is equal with the expected. If it isn't, an {@link AssertionError} is thrown.<br>
   * Use this method to make a set of asserts for a single request.
   * To just check the result code for request, use {@link com.brokenevent.nanotests.HttpAssert#assertHttpCode(String, int)}.
   * @param request {@link TestRequest} to get result from
   * @param code expected http code value
   */
  public static void assertHttpCode(TestRequest request, int code){
    Assert.assertEquals(request.getRequestUrl(), code, request.getResponse().getStatusLine().getStatusCode());
  }

  /**
   * Asserts that the http query result code is equal with the expected. If it isn't, an {@link AssertionError} is thrown with the given message.<br>
   * Use this method to make a set of asserts for a single request.
   * To just check the result code for request, use {@link com.brokenevent.nanotests.HttpAssert#assertHttpCode(String, int)}.
   * @param message the identifying message for the {@link AssertionError} (<code>null</code> okay)
   * @param request {@link TestRequest} to get result from
   * @param code expected http code value
   */
  public static void assertHttpCode(String message, TestRequest request, int code){
    Assert.assertEquals(message, code, request.getResponse().getStatusLine().getStatusCode());
  }

  /**
   * Asserts that the http query result contains expected header field and its value is equal to expected.
   * If it isn't equal to the expected or header doesn't contains expected header, an {@link AssertionError} is thrown.
   * @param request {@link TestRequest} to get result from.
   * @param name header field name
   * @param value expected header value
   */
  public static void assertHttpHeader(TestRequest request, String name, String value){
    Header header = request.getResponse().getLastHeader(name);
    Assert.assertNotNull("Header field " + name, header);
    Assert.assertEquals("Header field " + name, value, header.getValue());
  }

  /**
   * Asserts that the http query result contains expected header field and its value is equal to expected.
   * If it isn't equal to the expected or header doesn't contains expected header, an {@link AssertionError} is thrown with the given message.
   * @param message the identifying message for the {@link AssertionError} (<code>null</code> okay)
   * @param request {@link TestRequest} to get result from.
   * @param name header field name
   * @param value expected header value
   */
  public static void assertHttpHeader(String message, TestRequest request, String name, String value){
    Header header = request.getResponse().getLastHeader(name);
    Assert.assertNotNull(message, header);
    Assert.assertEquals(message, value, header.getValue());
  }

  /**
   * Asserts that the http query result contains expected header field.
   * If it isn't, an {@link AssertionError} is thrown with the given message.
   * @param request {@link TestRequest} to get result from.
   * @param name header field name
   */
  public static void assertHttpIsHeader(TestRequest request, String name){
    if (request.getResponse().getLastHeader(name) == null)
      Assert.fail();
  }

  /**
   * Asserts that the http query result contains expected header field.
   * If it isn't, an {@link AssertionError} is thrown with the given message.
   * @param message the identifying message for the {@link AssertionError} (<code>null</code> okay)
   * @param request {@link TestRequest} to get result from.
   * @param name header field name
   */
  public static void assertHttpIsHeader(String message, TestRequest request, String name){
    if (request.getResponse().getLastHeader(name) == null)
      Assert.fail(message);
  }

  /**
   * Asserts that the http query result doesn't contains expected header field.
   * If it is, an {@link AssertionError} is thrown.
   * @param request {@link TestRequest} to get result from.
   * @param name header field name
   */
  public static void assertHttpNoHeader(TestRequest request, String name){
    Assert.assertNull("Header field " + name, request.getResponse().getLastHeader(name));
  }

  /**
   * Asserts that the http query result doesn't contains expected header field.
   * If it is, an {@link AssertionError} is thrown with the given message.
   * @param message the identifying message for the {@link AssertionError} (<code>null</code> okay)
   * @param request {@link TestRequest} to get result from.
   * @param name header field name
   */
  public static void assertHttpNoHeader(String message, TestRequest request, String name){
    Assert.assertNull(message, request.getResponse().getLastHeader(name));
  }

  /**
   * Asserts that the http query result code is OK (200). If it isn't, an {@link AssertionError} is thrown.<br>
   * Use this method to assert the single request. If there are some checks for single URL, use {@link TestRequest} descendants:
   * <pre>
   *   TestGetRequest getRequest = new TestGetRequest(url);
   *   getRequest.execute();
   *
   *   assertHttpCode(getRequest, 200);
   *   // some more asserts
   * </pre>
   * @param resource URL for the request (<code>/resource</code>. Uses host from the {@link com.brokenevent.nanotests.http.TestRequestImpl})
   */
  public static void assertHttpOk(String resource){
    assertHttpCode(resource, 200);
  }

  /**
   * Asserts that the http query result code is Not Found (404). If it isn't, an {@link AssertionError} is thrown.<br>
   * Use this method to assert the single request. If there are some checks for single URL, use {@link TestRequest} descendants:
   * <pre>
   *   TestGetRequest getRequest = new TestGetRequest(url);
   *   getRequest.execute();
   *
   *   assertHttpCode(getRequest, 404);
   *   // some more asserts
   * </pre>
   * @param resource URL for the request (<code>/resource</code>. Uses host from the {@link com.brokenevent.nanotests.http.TestRequestImpl})
   */
  public static void assertHttp404(String resource){
    assertHttpCode(resource, 404);
  }

  /**
   * Asserts that the http query result code is equal to expected value. If it isn't, an {@link AssertionError} is thrown.<br>
   * Use this method to assert the single request. If there are some checks for single URL, use {@link TestRequest} descendants:
   * <pre>
   *   TestGetRequest getRequest = new TestGetRequest(url);
   *   getRequest.execute();
   *
   *   assertHttpCode(getRequest, code);
   *   // some more asserts
   * </pre>
   * @param resource URL for the request (<code>/resource</code>. Uses host from the {@link com.brokenevent.nanotests.http.TestRequestImpl})
   * @param code expected http code value
   */
  public static void assertHttpCode(String resource, int code){
    resource = normalizeUrl(resource);
    TestGetRequest request = new TestGetRequest(resource);
    request.execute();
    if (request.getResponse().getStatusLine().getStatusCode() != code)
      Assert.fail("Response for " + resource + " is " + request.getResponse().getStatusLine().getStatusCode());
  }

  /**
   * Asserts that the http query result content is equal to the expected value. If it isn't, an {@link AssertionError} is thrown.<br>
   * Use this method to make a set of asserts for a single request.
   * To just check the result content for request, use {@link com.brokenevent.nanotests.HttpAssert#assertHttpContent(String, String)}.
   * @param request {@link TestRequest} to get result from
   * @param expected expected content value
   */
  public static void assertHttpContent(TestRequest request, String expected){
    try {
      InputStream stream = request.getResponse().getEntity().getContent();

      ByteArrayOutputStream cache = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int read;
      while ((read = stream.read(buffer)) != -1)
        cache.write(buffer, 0, read);

      Assert.assertEquals(expected, new String(cache.toByteArray()));

    } catch (IOException e) {
      throw new AssertionError("Failed to get content", e);
    }
  }

  /**
   * Asserts that the http query result content is equal to the expected value. If it isn't, an {@link AssertionError} is thrown.<br>
   * Use this method to make a set of asserts for a single request.
   * To just check the result content for request, use {@link com.brokenevent.nanotests.HttpAssert#assertHttpContent(String, byte[])}.
   * @param request {@link TestRequest} to get result from
   * @param expected expected content value
   */
  public static void assertHttpContent(TestRequest request, byte[] expected){
    try {
      InputStream stream = request.getResponse().getEntity().getContent();

      ByteArrayOutputStream cache = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int read;
      while ((read = stream.read(buffer)) != -1)
        cache.write(buffer, 0, read);

      Assert.assertArrayEquals(expected, cache.toByteArray());
    } catch (IOException e) {
      throw new AssertionError("Failed to get content", e);
    }
  }

  /**
   * Asserts that the http query result content matches given {@link Pattern}. If it isn't, an {@link AssertionError} is thrown.<br>
   * Use this method to make a set of asserts for a single request.
   * To just check the result content for request, use {@link com.brokenevent.nanotests.HttpAssert#assertHttpContent(String, Pattern)}.
   * @param request {@link TestRequest} to get result from
   * @param expected regex pattern to match the content value
   */
  public static void assertHttpContent(TestRequest request, Pattern expected){
    try {
      InputStream stream = request.getResponse().getEntity().getContent();

      ByteArrayOutputStream cache = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int read;
      while ((read = stream.read(buffer)) != -1)
        cache.write(buffer, 0, read);

      String content = new String(cache.toByteArray());
      if (!expected.matcher(content).matches())
        throw new AssertionError("Regex check failed: " + expected);

    } catch (IOException e) {
      throw new AssertionError("Failed to get content", e);
    }
  }

  /**
   * Asserts that the http query result content is equal to the expected value. If it isn't, an {@link AssertionError} is thrown.<br>
   * Use this method to assert the single request. If there are some checks for single URL, use {@link TestRequest} descendants:
   * <pre>
   *   TestGetRequest getRequest = new TestGetRequest(url);
   *   getRequest.execute();
   *
   *   assertHttpCode(getRequest, 200);
   *   assertHttpContent(getRequest, expected);
   *   // some more asserts
   * </pre>
   * @param resource URL for the request (<code>/resource</code>. Uses host from the {@link com.brokenevent.nanotests.http.TestRequestImpl})
   * @param expected expected content value
   */
  public static void assertHttpContent(String resource, String expected){
    TestGetRequest request = new TestGetRequest(resource);
    request.execute();
    assertHttpOk(request);
    assertHttpContent(request, expected);
  }

  /**
   * Asserts that the http query result content is equal to the expected value. If it isn't, an {@link AssertionError} is thrown.<br>
   * Use this method to assert the single request. If there are some checks for single URL, use {@link TestRequest} descendants:
   * <pre>
   *   TestGetRequest getRequest = new TestGetRequest(url);
   *   getRequest.execute();
   *
   *   assertHttpCode(getRequest, 200);
   *   assertHttpContent(getRequest, expected);
   *   // some more asserts
   * </pre>
   * @param resource URL for the request (<code>/resource</code>. Uses host from the {@link com.brokenevent.nanotests.http.TestRequestImpl})
   * @param expected expected content value
   */
  public static void assertHttpContent(String resource, byte[] expected){
    TestGetRequest request = new TestGetRequest(resource);
    request.execute();
    assertHttpOk(request);
    assertHttpContent(request, expected);
  }

  /**
   * Asserts that the http query result content matches given {@link Pattern}. If it isn't, an {@link AssertionError} is thrown.<br>
   * Use this method to assert the single request. If there are some checks for single URL, use {@link TestRequest} descendants:
   * <pre>
   *   TestGetRequest getRequest = new TestGetRequest(url);
   *   getRequest.execute();
   *
   *   assertHttpCode(getRequest, 200);
   *   assertHttpContent(getRequest, expected);
   *   // some more asserts
   * </pre>
   * @param resource URL for the request (<code>/resource</code>. Uses host from the {@link com.brokenevent.nanotests.http.TestRequestImpl})
   * @param expected regex pattern to match the content value
   */
  public static void assertHttpContent(String resource, Pattern expected){
    TestGetRequest request = new TestGetRequest(resource);
    request.execute();
    assertHttpOk(request);
    assertHttpContent(request, expected);
  }
}
