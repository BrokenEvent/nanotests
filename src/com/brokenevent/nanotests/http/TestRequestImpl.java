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

package com.brokenevent.nanotests.http;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Common implementation for {@link com.brokenevent.nanotests.http.TestRequest}
 * @author BrokenEvent
 */
class TestRequestImpl implements TestRequest {
  protected HttpClient client;
  protected HttpRequest request;
  protected HttpResponse response;
  private String resource;

  protected static String hostUrl = "http://localhost";
  protected static HttpHost host = HttpHost.create(hostUrl);

  /**
   * Sets the server URL for all the requests. Default value is <code>http://localhost</code>
   * @param hostUrl server URL
   */
  public static void setHostUrl(String hostUrl) {
    TestRequestImpl.hostUrl = hostUrl;
  }

  TestRequestImpl(String resource) {
    client = HttpClients.createDefault();
    this.resource = resource;
  }

  /**
   * Set the header for the http request. If the header with the such header already exists, it will be overwritten.
   * @param name name of the header to set
   * @param value value for the header
   */
  public void setHeader(String name, String value){
    request.setHeader(name, value);
  }

  /**
   * Remove the header from the http request
   * @param name name of the header to remove
   */
  public void removeHeader(String name){
    request.removeHeaders(name);
  }

  /**
   * Sets the User-Agent http header field to the given value
   * @param value User-Agent new value
   */
  public void setUserAgent(String value){
    request.setHeader("User-Agent", value);
  }

  /**
   * Executes the http request.
   */
  public void execute(){
    try {
      response = client.execute(host, request);
    } catch (IOException e) {
      throw new AssertionError("Failed to do GET to " + hostUrl, e);
    }
  }

  /**
   * Gets the fully qualified request URL.
   * @return fully qualified request URL
   */
  @Override
  public String getRequestUrl() {
    return hostUrl + resource;
  }

  /**
   * Gets the http response object.
   * @return http response object
   */
  @Override
  public HttpResponse getResponse() {
    return response;
  }

  /**
   * Helper method to get content of the response for the http request.
   * @return text content of the response
   */
  public String getStringContent(){
    if (response == null)
      return null;

    try {
      InputStream stream = response.getEntity().getContent();

      ByteArrayOutputStream cache = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int read;
      while ((read = stream.read(buffer)) != -1)
        cache.write(buffer, 0, read);

      return new String(cache.toByteArray());

    } catch (IOException e) {
      throw new AssertionError("Failed to get content", e);
    }
  }
}
