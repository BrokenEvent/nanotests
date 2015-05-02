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

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

/**
 * XML-related asserts helper.
 * These methods can be used directly:
 * <code>XmlAssert.assertElementContent(...)</code>, however, they read better if they
 * are referenced through static import:
 *
 * <pre>
 * import static com.brokenevent.nanotests.XmlAssert.*;
 *    ...
 *    assertElementContent(...);
 * </pre>
 *
 * @author BrokenEvent
 */
public class XmlAssert {
  /**
   * Protect constructor since it is a static only class
   */
  protected XmlAssert(){}

  /**
   * Helper method to load the document from the string.
   * @param content text content of the document
   * @return {@link org.w3c.dom.Document} of the XML DOM model
   */
  public static Document loadDocument(String content){
    try{
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setIgnoringElementContentWhitespace(true);
      DocumentBuilder builder = factory.newDocumentBuilder();

      return builder.parse(new InputSource(new StringReader(content)));
    }catch (Exception ex){
      throw new AssertionError("XML parsing error", ex);
    }
  }

  /**
   * Asserts that the element text content is equal to the given value. If it isn't, an {@link AssertionError} is thrown.
   * @param doc {@link org.w3c.dom.Document} to check element from
   * @param path XPath expression to find required element in DOM
   * @param expected expected element's text content
   */
  public static void assertElementContent(Document doc, String path, String expected) {
    try{
      XPathFactory xPathfactory = XPathFactory.newInstance();
      XPath xpath = xPathfactory.newXPath();
      XPathExpression expression = xpath.compile(path);
      assertEquals(expected, expression.evaluate(doc, XPathConstants.STRING));
    }catch (XPathExpressionException ex){
      throw new AssertionError("XPath parsing error", ex);
    }
  }

  /**
   * Asserts that the element name is equal to the given value. If it isn't, an {@link AssertionError} is thrown.
   * @param doc {@link org.w3c.dom.Document} to check element from
   * @param path XPath expression to find required element in DOM
   * @param expected expected element's name
   */
  public static void assertElementName(Document doc, String path, String expected) {
    try{
      XPathFactory xPathfactory = XPathFactory.newInstance();
      XPath xpath = xPathfactory.newXPath();
      XPathExpression expression = xpath.compile(path);
      assertEquals(expected, ((Node)expression.evaluate(doc, XPathConstants.NODE)).getNodeName());
    }catch (XPathExpressionException ex){
      throw new AssertionError("XPath parsing error", ex);
    }
  }

  /**
   * Asserts that the count of the elements found by given XPath expression if equals to the given value.
   * If it isn't, an {@link AssertionError} is thrown.
   * @param doc {@link org.w3c.dom.Document} to check element from
   * @param path XPath expression to find required element list in DOM
   * @param expectedCount expected count of the elements
   */
  public static void assertElementsCount(Document doc, String path, int expectedCount) {
    try{
      XPathFactory xPathfactory = XPathFactory.newInstance();
      XPath xpath = xPathfactory.newXPath();
      XPathExpression expression = xpath.compile(path);
      assertEquals(expectedCount, ((NodeList)expression.evaluate(doc, XPathConstants.NODESET)).getLength());
    }catch (XPathExpressionException ex){
      throw new AssertionError("XPath parsing error", ex);
    }
  }
}
