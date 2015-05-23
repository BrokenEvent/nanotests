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

import org.junit.Assert;

import java.util.Date;

/**
 * jUnit extensions class
 * These methods can be used directly:
 * <code>DbAssert.assertDate(...)</code>, however, they read better if they
 * are referenced through static import:
 *
 * <pre>
 * import static com.brokenevent.nanotests.Asserts.*;
 *    ...
 *    assertEquals(...);
 * </pre>
 * @author BrokenEvent
 */
public final class Asserts {

  protected Asserts(){}

  /**
   * Asserts that dates are equal with second precision. If it isn't, an {@link AssertionError} is thrown.
   * @param expected expected result
   * @param actual actual result
   */
  public static void assertEquals(Date expected, Date actual){
    Assert.assertEquals(Math.round(expected.getTime() / 1000f), Math.round(actual.getTime() / 1000f));
  }

  /**
   * Asserts that dates are equal with second precision. If it isn't, an {@link AssertionError} is thrown with the given message.
   * @param expected expected result
   * @param actual actual result
   * @param message the identifying message for the {@link AssertionError} (<code>null</code> okay)
   */
  public static void assertEquals(String message, Date expected, Date actual){
    Assert.assertEquals(message, Math.round(expected.getTime() / 1000f), Math.round(actual.getTime() / 1000f));
  }

  /**
   * Asserts that objects are not equal. If they are, an {@link AssertionError} is thrown.
   * @param expected expected result
   * @param actual actual result
   */
  public static void assertNotEquals(Object expected, Object actual){
    if (!expected.equals(actual))
      throw new AssertionError("Actual value is the same as expected: " + expected);
  }

  /**
   * Asserts that objects are not equal. If they are, an {@link AssertionError} is thrown with the given message.
   * @param expected expected result
   * @param actual actual result
   * @param message the identifying message for the {@link AssertionError} (<code>null</code> okay)
   */
  public static void assertNotEquals(String message, Object expected, Object actual){
    if (!expected.equals(actual))
      throw new AssertionError(message);
  }

  /**
   * Asserts that strings are not equal. If they are, an {@link AssertionError} is thrown.
   * @param expected expected result
   * @param actual actual result
   */
  public static void assertNotEquals(String expected, String actual){
    if (!expected.equals(actual))
      throw new AssertionError("Actual value is the same as expected: " + expected);
  }

  /**
   * Asserts that strings are not equal. If they are, an {@link AssertionError} is thrown with the given message.
   * @param expected expected result
   * @param actual actual result
   * @param message the identifying message for the {@link AssertionError} (<code>null</code> okay)
   */
  public static void assertNotEquals(String message, String expected, String actual){
    if (!expected.equals(actual))
      throw new AssertionError(message);
  }

  /**
   * Asserts that values are not equal. If they are, an {@link AssertionError} is thrown.
   * @param expected expected result
   * @param actual actual result
   */
  public static void assertNotEquals(int expected, int actual){
    if (expected != actual)
      throw new AssertionError("Actual value is the same as expected: " + expected);
  }

  /**
   * Asserts that values are not equal. If they are, an {@link AssertionError} is thrown with the given message.
   * @param expected expected result
   * @param actual actual result
   * @param message the identifying message for the {@link AssertionError} (<code>null</code> okay)
   */
  public static void assertNotEquals(String message, int expected, int actual){
    if (expected != actual)
      throw new AssertionError(message);
  }

  /**
   * Asserts that values are not equal. If they are, an {@link AssertionError} is thrown.
   * @param expected expected result
   * @param actual actual result
   */
  public static void assertNotEquals(byte expected, byte actual){
    if (expected != actual)
      throw new AssertionError("Actual value is the same as expected: " + expected);
  }

  /**
   * Asserts that values are not equal. If they are, an {@link AssertionError} is thrown with the given message.
   * @param expected expected result
   * @param actual actual result
   * @param message the identifying message for the {@link AssertionError} (<code>null</code> okay)
   */
  public static void assertNotEquals(String message, byte expected, byte actual){
    if (expected != actual)
      throw new AssertionError(message);
  }

  /**
   * Asserts that values are not equal. If they are, an {@link AssertionError} is thrown.
   * @param expected expected result
   * @param actual actual result
   */
  public static void assertNotEquals(long expected, long actual){
    if (expected != actual)
      throw new AssertionError("Actual value is the same as expected: " + expected);
  }

  /**
   * Asserts that values are not equal. If they are, an {@link AssertionError} is thrown with the given message.
   * @param expected expected result
   * @param actual actual result
   * @param message the identifying message for the {@link AssertionError} (<code>null</code> okay)
   */
  public static void assertNotEquals(String message, long expected, long actual){
    if (expected != actual)
      throw new AssertionError(message);
  }

  /**
   * Asserts that values are not equal. If they are, an {@link AssertionError} is thrown.
   * @param expected expected result
   * @param actual actual result
   */
  public static void assertNotEquals(float expected, float actual){
    if (Math.abs(expected-actual) < 0.0001f)
      throw new AssertionError("Actual value is the same as expected: " + expected);
  }

  /**
   * Asserts that values are not equal. If they are, an {@link AssertionError} is thrown with the given message.
   * @param expected expected result
   * @param actual actual result
   * @param message the identifying message for the {@link AssertionError} (<code>null</code> okay)
   */
  public static void assertNotEquals(String message, float expected, float actual){
    if (Math.abs(expected-actual) < 0.0001f)
      throw new AssertionError(message);
  }

  /**
   * Asserts that all values of the subset is exist in superset. If they aren't, an {@link AssertionError} is thrown.
   * @param superset set of expected values
   * @param subset set of actual values
   */
  public static <T> void assertInSet(T[] superset, T[] subset){
    for (int i = 0; i < subset.length; i++){
      boolean found = false;
      for (int j = 0; j < superset.length; j++)
        if (subset[i].equals(superset[j])){
          found = true;
          break;
        }

      if (!found)
        throw new AssertionError("Element " + subset[i] + " is not found in superset.");
    }
  }

  /**
   * Asserts that all values of the subset is exist in superset. If they aren't, an {@link AssertionError} is thrown
   * with the given message.
   * @param superset set of expected values
   * @param subset set of actual values
   * @param message the identifying message for the {@link AssertionError} (<code>null</code> okay)
   */
  public static <T> void assertInSet(String message, T[] superset, T[] subset){
    for (int i = 0; i < subset.length; i++){
      boolean found = false;
      for (int j = 0; j < superset.length; j++)
        if (subset[i].equals(superset[j])){
          found = true;
          break;
        }

      if (!found)
        throw new AssertionError(message);
    }
  }
}
