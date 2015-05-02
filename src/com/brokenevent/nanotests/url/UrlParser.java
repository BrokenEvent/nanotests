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

package com.brokenevent.nanotests.url;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper URL parser class to be used in {@link com.brokenevent.nanotests.UrlAssert}
 * @author BrokenEvent
 */
public class UrlParser{
  private String protocol;
  private String domain;
  private String resource;
  private Map<String, String> params = new HashMap<String, String>();

  /**
   * Initializes the instance of the {@link com.brokenevent.nanotests.url.UrlParser} with the URL's parts.
   * @param url URL value to parse
   * @throws UnsupportedEncodingException may be thrown by {@link java.net.URLDecoder} when param decoding is failed
   */
  public UrlParser(String url) throws UnsupportedEncodingException {
    // get protocol
    int i = url.indexOf("://");
    if (i != -1){
      protocol = url.substring(0, i);
      i += 3; // skip ://
    }
    else i = 0;

    // get domain
    int j = url.indexOf('/', i);
    if (j != -1){
      domain = url.substring(i, j);
      i = j;
    } else{
      domain = url.substring(i);
      return;
    }

    // get resource
    j = url.indexOf('?', i);
    if (j == -1){
      resource = url.substring(i);
      return;
    }
    resource = url.substring(i, j);

    i = j + 1; // skip ?

    // get params
    do{
      j = url.indexOf('=', i);
      if (j == -1)
        break;
      String name = URLDecoder.decode(url.substring(i, j), "UTF-8");
      i = j + 1; // skip =

      j = url.indexOf('&', i);
      if (j == -1)
        j = url.length();
      String value = URLDecoder.decode(url.substring(i, j), "UTF-8");
      i = j + 1; // skip &

      params.put(name, value);
    }while(j < url.length());
  }

  /**
   * Gets the protocol part of the URL.
   * @return protocol part of the URL
   */
  public String getProtocol() {
    return protocol;
  }

  /**
   * Gets the domain part of the URL.
   * @return domain part of the URL
   */
  public String getDomain() {
    return domain;
  }

  /**
   * Gets the resource part of the URL.
   * @return resource part of the URL
   */
  public String getResource() {
    return resource;
  }

  /**
   * Gets the param of the URL.
   * @param paramName param name to get
   * @return param value or null if doesn't exist
   */
  public String getParam(String paramName){
    return params.get(paramName);
  }

  /**
   * Checks the param existance in the URL.
   * @param paramName param name to check
   * @return True if param exists and fails if it isn't
   */
  public boolean haveParam(String paramName){
    return params.containsKey(paramName);
  }
}