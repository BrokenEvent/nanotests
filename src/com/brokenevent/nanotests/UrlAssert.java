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

import com.brokenevent.nanotests.url.UrlParser;
import org.junit.Assert;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

/**
 * Helper class for URL-related asserts.
 * These methods can be used directly:
 * <code>UrlAssert.assertUrlProtocol(...)</code>, however, they read better if they
 * are referenced through static import:
 *
 * <pre>
 * import static com.brokenevent.nanotests.UrlAssert.*;
 *    ...
 *    assertUrlProtocol(...);
 * </pre>
 *
 * @author BrokenEvent
 */
public class UrlAssert {
  /**
   * Protect constructor since it is a static only class
   */
  protected UrlAssert() {}

  /**
   * Asserts that the given URL's protocol is equal to the expected value. If it isn't, an {@link AssertionError} is thrown.<br>
   * URL example: <b>http</b>://test.com/testpage
   * @param expected expected protocol value
   * @param url url to check
   */
  public static void assertUrlProtocol(String expected, String url) {
    try {
      Assert.assertEquals(expected, new UrlParser(url).getProtocol());
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError("Failed to decode URL: " + url, e);
    }
  }

  /**
   * Asserts that the given URL's protocol is equal to the expected value. If it isn't, an {@link AssertionError} is thrown with the given message.<br>
   * URL example: <b>http</b>://test.com/testpage
   * @param message the identifying message for the {@link AssertionError} (<code>null</code> okay)
   * @param expected expected protocol value
   * @param url url to check
   */
  public static void assertUrlProtocol(String message, String expected, String url) {
    try {
      assertEquals(message, expected, new UrlParser(url).getProtocol());
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError("Failed to decode URL: " + url, e);
    }
  }

  /**
   * Asserts that the given URL's domain part is equal to the expected value. If it isn't, an {@link AssertionError} is thrown.<br>
   * URL example: http://<b>test.com</b>/testpage
   * @param expected expected domain value
   * @param url url to check
   */
  public static void assertUrlDomain(String expected, String url) {
    try {
      assertEquals(expected, new UrlParser(url).getDomain());
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError("Failed to decode URL: " + url, e);
    }
  }

  /**
   * Asserts that the given URL's domain part is equal to the expected value. If it isn't, an {@link AssertionError} is thrown with the given message.<br>
   * URL example: http://<b>test.com</b>/testpage
   * @param message the identifying message for the {@link AssertionError} (<code>null</code> okay)
   * @param expected expected domain value
   * @param url url to check
   */
  public static void assertUrlDomain(String message, String expected, String url) {
    try {
      assertEquals(message, expected, new UrlParser(url).getDomain());
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError("Failed to decode URL: " + url, e);
    }
  }

  /**
   * Asserts that the given URL's resource part is equal to the expected value. If it isn't, an {@link AssertionError} is thrown.<br>
   * URL example: http://test.com<b>/testpage</b>
   * @param expected expected resource value
   * @param url url to check
   */
  public static void assertUrlResource(String expected, String url) {
    try {
      assertEquals(expected, new UrlParser(url).getResource());
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError("Failed to decode URL: " + url, e);
    }
  }

  /**
   * Asserts that the given URL's resource part is equal to the expected value. If it isn't, an {@link AssertionError} is thrown with the given message.<br>
   * URL example: http://test.com<b>/testpage</b>
   * @param message the identifying message for the {@link AssertionError} (<code>null</code> okay)
   * @param expected expected resource value
   * @param url url to check
   */
  public static void assertUrlResource(String message, String expected, String url) {
    try {
      assertEquals(message, expected, new UrlParser(url).getResource());
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError("Failed to decode URL: " + url, e);
    }
  }

  /**
   * Asserts that the given URL contains given param and it equals the given value. If it isn't, an {@link AssertionError} is thrown.
   * @param expectedValue expected param's value
   * @param url url to check
   * @param paramName param name to check its value
   */
  public static void assertUrlParam(String expectedValue, String url, String paramName) {
    try {
      assertEquals(expectedValue, new UrlParser(url).getParam(paramName));
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError("Failed to decode URL: " + url, e);
    }
  }

  /**
   * Asserts that the given URL contains given param and it equals the given value. If it isn't, an {@link AssertionError} is thrown with the given message.
   * @param message the identifying message for the {@link AssertionError} (<code>null</code> okay)
   * @param expectedValue expected param's value
   * @param url url to check
   * @param paramName param name to check its value
   */
  public static void assertUrlParam(String message, String expectedValue, String url, String paramName) {
    try {
      assertEquals(message, expectedValue, new UrlParser(url).getParam(paramName));
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError("Failed to decode URL: " + url, e);
    }
  }

  /**
   * Asserts that the given URL have param with the given name. If it isn't, an {@link AssertionError} is thrown.
   * @param url url to check
   * @param paramName param name to check its existance
   */
  public static void assertUrlHaveParam(String url, String paramName) {
    try {
      assertTrue(new UrlParser(url).haveParam(paramName));
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError("Failed to decode URL: " + url, e);
    }
  }

  /**
   * Asserts that the given URL have param with the given name. If it isn't, an {@link AssertionError} is thrown with the given message.
   * @param message the identifying message for the {@link AssertionError} (<code>null</code> okay)
   * @param url url to check
   * @param paramName param name to check its existance
   */
  public static void assertUrlHaveParam(String message, String url, String paramName) {
    try {
      assertTrue(message, new UrlParser(url).haveParam(paramName));
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError("Failed to decode URL: " + url, e);
    }
  }
}