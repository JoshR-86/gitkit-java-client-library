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

import junit.framework.Assert;
import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.base.Strings;

public class GitServiceClientImplTest extends TestCase {
  private String key = "testingDeveloperKey";
  private GitServiceClientImpl client;

  protected void setUp() throws Exception {
    client = new GitServiceClientImpl(key);
  }

  public void testGitServiceClientImpl() {
    Assert.assertEquals(key, client.getDeveloperKey());
  }

  public void testBuildPostData() {
    String requestUri = "http://test";
    String postBody = "a=b&c=d&e=f";
    JSONArray postData = client.buildPostData(requestUri, postBody);
    Assert.assertNotNull(postData);
    Assert.assertEquals(1, postData.length());
  }

  public void testVerifyResponse() {
  }

  public void testConvertJson_error() throws JSONException {
    JSONObject input = new JSONObject();
    input.put("error", "someError");
    JSONObject output = client.convertJson(input);
    Assert.assertNotNull(output);
    Assert.assertEquals("someError", output.getString("error"));
  }

  public void testConvertJson_invalid() throws JSONException {
    JSONObject input = new JSONObject();
    input.put("unkown", "unkown");
    JSONObject output = client.convertJson(input);
    Assert.assertTrue(output == null || output.length() == 0);
  }

  public void testConvertJson_resultTypeError() throws JSONException {
    JSONObject input = new JSONObject();
    input.put("result", "unkown");
    JSONObject output = client.convertJson(input);
    Assert.assertTrue(output == null || output.length() == 0);
  }

  public void testConvertJson_resultEmpty() throws JSONException {
    JSONObject input = new JSONObject();
    input.put("result", new JSONObject());
    JSONObject output = client.convertJson(input);
    Assert.assertTrue(output == null || output.length() == 0);
  }

  public void testConvertJson_trusted() throws JSONException {
    JSONObject input = new JSONObject();
    JSONObject result = new JSONObject();
    input.put("result", result);
    result.put("verifiedEmail", "a@b.c");
    JSONObject output = client.convertJson(input);
    Assert.assertNotNull(output);
    Assert.assertEquals("a@b.c", output.getString("email"));
    Assert.assertTrue(output.getBoolean("trusted"));
  }

  public void testConvertJson_untrusted() throws JSONException {
    JSONObject input = new JSONObject();
    JSONObject result = new JSONObject();
    input.put("result", result);
    result.put("email", "a@b.c");
    JSONObject output = client.convertJson(input);
    Assert.assertNotNull(output);
    Assert.assertEquals("a@b.c", output.getString("email"));
    Assert.assertFalse(output.getBoolean("trusted"));
  }

  public void testConvertJson_firstName() throws JSONException {
    JSONObject input = new JSONObject();
    JSONObject result = new JSONObject();
    input.put("result", result);
    result.put("verifiedEmail", "a@b.c");
    result.put("firstName", "theFirstName");
    JSONObject output = client.convertJson(input);
    Assert.assertNotNull(output);
    Assert.assertEquals("theFirstName", output.getString("firstName"));
  }

  public void testConvertJson_lastName() throws JSONException {
    JSONObject input = new JSONObject();
    JSONObject result = new JSONObject();
    input.put("result", result);
    result.put("verifiedEmail", "a@b.c");
    result.put("lastName", "theLastName");
    JSONObject output = client.convertJson(input);
    Assert.assertNotNull(output);
    Assert.assertEquals("theLastName", output.getString("lastName"));
  }

  public void testConvertJson_profilePicture() throws JSONException {
    JSONObject input = new JSONObject();
    JSONObject result = new JSONObject();
    input.put("result", result);
    result.put("verifiedEmail", "a@b.c");
    result.put("profilePicture", "theProfilePicture");
    JSONObject output = client.convertJson(input);
    Assert.assertNotNull(output);
    Assert.assertEquals("theProfilePicture", output.getString("photoUrl"));
  }

  public void testConvertJson_fullname() throws JSONException {
    JSONObject input = new JSONObject();
    JSONObject result = new JSONObject();
    input.put("result", result);
    result.put("verifiedEmail", "a@b.c");
    result.put("fullName", "A B");
    JSONObject output = client.convertJson(input);
    Assert.assertNotNull(output);
    Assert.assertEquals("A", output.getString("firstName"));
    Assert.assertEquals("B", output.getString("lastName"));
  }

  public void testConvertJson_fullnameWithoutSpace() throws JSONException {
    JSONObject input = new JSONObject();
    JSONObject result = new JSONObject();
    input.put("result", result);
    result.put("verifiedEmail", "a@b.c");
    result.put("fullName", "AB");
    JSONObject output = client.convertJson(input);
    Assert.assertNotNull(output);
    Assert.assertEquals("AB", output.getString("firstName"));
    Assert.assertTrue(!output.has("lastName")
        || Strings.isNullOrEmpty(output.getString("lastName")));
  }

  public void testConvertJson_fullnameManySpaces() throws JSONException {
    JSONObject input = new JSONObject();
    JSONObject result = new JSONObject();
    input.put("result", result);
    result.put("verifiedEmail", "a@b.c");
    result.put("fullName", " A  B C ");
    JSONObject output = client.convertJson(input);
    Assert.assertNotNull(output);
    Assert.assertEquals("A  B", output.getString("firstName"));
    Assert.assertEquals("C", output.getString("lastName"));
  }

  public void testConvertJson_fullnameLasName() throws JSONException {
    JSONObject input = new JSONObject();
    JSONObject result = new JSONObject();
    input.put("result", result);
    result.put("verifiedEmail", "a@b.c");
    result.put("fullName", "A B");
    result.put("lastName", "C");
    JSONObject output = client.convertJson(input);
    Assert.assertNotNull(output);
    Assert.assertEquals("A", output.getString("firstName"));
    Assert.assertEquals("C", output.getString("lastName"));
  }

  public void testConvertJson_fullnameFirsName() throws JSONException {
    JSONObject input = new JSONObject();
    JSONObject result = new JSONObject();
    input.put("result", result);
    result.put("verifiedEmail", "a@b.c");
    result.put("fullName", "A B");
    result.put("firstName", "C");
    JSONObject output = client.convertJson(input);
    Assert.assertNotNull(output);
    Assert.assertEquals("C", output.getString("firstName"));
    Assert.assertEquals("B", output.getString("lastName"));
  }
}
