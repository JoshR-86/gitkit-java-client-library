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

package com.google.apps.easyconnect.easyrp.client.basic.session;

import java.util.Properties;

import junit.framework.Assert;
import junit.framework.TestCase;

public class RpConfigTest extends TestCase {

  public void testRpConfigProperties() {
    Properties properties = new Properties();
    properties.put("RP.Config.sessionCookieName", "EMAIL");
    properties.put("RP.Config.idpAssertionCookieName", "IDPASSERTION");
    properties.put("RP.Config.maxAgeOfSession", "-1");
    properties.put("RP.Config.maxAgeOfIdpAssertion", "1800");
    properties.put("RP.Config.domain", "/login");
    properties.put("RP.Config.path", "/");
    properties.put("RP.Config.sessionUserKey", "login_account");
    properties.put("RP.Config.sessionIdpAssertionKey", "idp_assertion");
    properties.put("RP.Config.siteUrl", "http://localhost:8888");
    properties.put("RP.Config.homeUrl", "/home.jsp");
    properties.put("RP.Config.loginUrl", "/login.jsp");
    properties.put("RP.Config.signupUrl", "/signup.jsp");
    RpConfig config = new RpConfig(properties);
    Assert.assertEquals("EMAIL", config.getSessionCookieName());
    Assert.assertEquals("IDPASSERTION", config.getIdpAssertionCookieName());
    Assert.assertEquals(-1, config.getMaxAgeOfSession());
    Assert.assertEquals(1800, config.getMaxAgeOfIdpAssertion());
    Assert.assertEquals("/login", config.getDomain());
    Assert.assertEquals("/", config.getPath());
    Assert.assertEquals("login_account", config.getSessionUserKey());
    Assert.assertEquals("http://localhost:8888", config.getSiteUrl());
    Assert.assertEquals("/home.jsp", config.getHomeUrl());
    Assert.assertEquals("/login.jsp", config.getLoginUrl());
    Assert.assertEquals("/signup.jsp", config.getSignupUrl());
  }

  public void testRpConfigBuilder() {
    RpConfig.Builder builder = new RpConfig.Builder();
    RpConfig config = builder.sessionCookieName("EMAIL").idpAssertionCookieName("IDPASSERTION")
        .maxAgeOfSession(-1).maxAgeOfIdpAssertion(1800).domain("/login").path("/")
        .sessionUserKey("login_account").sessionIdpAssertionKey("idp_assertion")
        .siteUrl("http://localhost:8888").homeUrl("/home.jsp").loginUrl("/login.jsp")
        .signupUrl("/signup.jsp").build();
    Assert.assertEquals("EMAIL", config.getSessionCookieName());
    Assert.assertEquals("IDPASSERTION", config.getIdpAssertionCookieName());
    Assert.assertEquals(-1, config.getMaxAgeOfSession());
    Assert.assertEquals(1800, config.getMaxAgeOfIdpAssertion());
    Assert.assertEquals("/login", config.getDomain());
    Assert.assertEquals("/", config.getPath());
    Assert.assertEquals("login_account", config.getSessionUserKey());
    Assert.assertEquals("http://localhost:8888", config.getSiteUrl());
    Assert.assertEquals("/home.jsp", config.getHomeUrl());
    Assert.assertEquals("/login.jsp", config.getLoginUrl());
    Assert.assertEquals("/signup.jsp", config.getSignupUrl());
  }
}
