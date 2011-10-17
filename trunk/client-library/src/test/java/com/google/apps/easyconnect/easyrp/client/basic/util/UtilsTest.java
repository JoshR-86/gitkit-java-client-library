/* Copyright 2011 Google Inc. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.apps.easyconnect.easyrp.client.basic.util;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.easymock.EasyMock;

public class UtilsTest extends TestCase {

  public void testConvertStreamToString() throws UnsupportedEncodingException {
    String data = "some random data";
    String encoding = "UTF-8";
    ByteArrayInputStream bais = new ByteArrayInputStream(data.getBytes(encoding));
    String result = Utils.streamToString(bais, encoding);
    Assert.assertEquals(data, result);
  }

  public void testConvertStreamToString_null_encoding() throws UnsupportedEncodingException {
    String data = "some random data";
    String encoding = "UTF-8";
    ByteArrayInputStream bais = new ByteArrayInputStream(data.getBytes(encoding));
    String result = Utils.streamToString(bais, null);
    Assert.assertEquals(data, result);
  }

  public void testGetHostUrl() {
    HttpServletRequest req = EasyMock.createMock(HttpServletRequest.class);
    EasyMock.expect(req.getScheme()).andReturn("http").anyTimes();
    EasyMock.expect(req.getServerName()).andReturn("localhost").anyTimes();
    EasyMock.expect(req.getServerPort()).andReturn(8080).anyTimes();
    EasyMock.replay(req);
    String hostUrl = Utils.getHostUrl(req);
    Assert.assertEquals("http://localhost:8080", hostUrl);
  }

  public void testGetHostUrl_http80() {
    HttpServletRequest req = EasyMock.createMock(HttpServletRequest.class);
    EasyMock.expect(req.getScheme()).andReturn("http").anyTimes();
    EasyMock.expect(req.getServerName()).andReturn("localhost").anyTimes();
    EasyMock.expect(req.getServerPort()).andReturn(80).anyTimes();
    EasyMock.replay(req);
    String hostUrl = Utils.getHostUrl(req);
    Assert.assertEquals("http://localhost", hostUrl);
  }

  public void testGetHostUrl_https443() {
    HttpServletRequest req = EasyMock.createMock(HttpServletRequest.class);
    EasyMock.expect(req.getScheme()).andReturn("https").anyTimes();
    EasyMock.expect(req.getServerName()).andReturn("localhost").anyTimes();
    EasyMock.expect(req.getServerPort()).andReturn(443).anyTimes();
    EasyMock.replay(req);
    String hostUrl = Utils.getHostUrl(req);
    Assert.assertEquals("https://localhost", hostUrl);
  }
}
