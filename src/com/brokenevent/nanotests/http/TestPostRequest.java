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

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * POST request for http server tests.
 * @author BrokenEvent
 */
public class TestPostRequest extends TestRequestImpl {
  private BasicHttpEntity entity;

  /**
   * Initializes instance of the POST request for the given resource.
   * @param resource resource name
   */
  public TestPostRequest(String resource) {
    super(resource);
    request = new HttpPost(resource);

    entity = new BasicHttpEntity();
    ((HttpPost)request).setEntity(entity);
  }

  /**
   * Set the content for the POST request from stream.
   * @param stream content source stream
   */
  public void setContent(InputStream stream){
    entity.setContent(stream);
  }

  /**
   * Sets the content for the POST request from byte array.
   * @param data content source array
   */
  public void setContent(byte[] data){
    entity.setContent(new ByteArrayInputStream(data));
  }
}
