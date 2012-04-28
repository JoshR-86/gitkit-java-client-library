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
import java.util.logging.Logger;

import com.google.common.base.Strings;

/**
 * The configuration parameters.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class RpConfig {
  private static final Logger log = Logger.getLogger(RpConfig.class.getName());
  private String sessionCookieName;
  private String idpAssertionCookieName;
  private String oauthTokenCookieName;
  private int maxAgeOfSession;
  private int maxAgeOfIdpAssertion;
  private int maxAgeOfOauthToken;
  private String domain;
  private String path;

  private String sessionUserKey;
  private String sessionIdpAssertionKey;
  private String sessionOauthTokenKey;

  private String notificationKey;
  private String cdsActionKey;

  @Deprecated
  private String siteUrl;
  private String homeUrl;
  private String loginUrl;
  private String signupUrl;

  /** Default constructor */
  public RpConfig() {
  }

  /** Construct an instance by reading information from a {@code Properties} */
  public RpConfig(Properties properties) {
    loadFromProperties(properties);
  }

  private void loadFromProperties(Properties properties) {
    this.sessionCookieName = properties.getProperty("RP.Config.sessionCookieName");
    this.idpAssertionCookieName = properties.getProperty("RP.Config.idpAssertionCookieName");
    this.oauthTokenCookieName = properties.getProperty("RP.Config.oauthTokenCookieName");
    String str = properties.getProperty("RP.Config.maxAgeOfSession");
    if (!Strings.isNullOrEmpty(str)) {
      try {
        this.maxAgeOfSession = Integer.parseInt(str);
      } catch (NumberFormatException e) {
        log.severe("Failed to parse maxAgeOfSession: " + e.getMessage());
      }
    }
    str = properties.getProperty("RP.Config.maxAgeOfIdpAssertion");
    if (!Strings.isNullOrEmpty(str)) {
      try {
        this.maxAgeOfIdpAssertion = Integer.parseInt(str);
      } catch (NumberFormatException e) {
        log.severe("Failed to parse maxAgeOfIdpAssertion: " + e.getMessage());
      }
    }
    str  = properties.getProperty("RP.Config.maxAgeOfOauthToken");
    if (!Strings.isNullOrEmpty(str)) {
      try {
        this.maxAgeOfOauthToken = Integer.parseInt(str);
      } catch (NumberFormatException e) {
        log.severe("Failed to parse maxAgeOfOauthToken: " + e.getMessage());
      }
    }
    this.domain = properties.getProperty("RP.Config.domain");
    this.path = properties.getProperty("RP.Config.path");
    this.sessionUserKey = properties.getProperty("RP.Config.sessionUserKey");
    this.sessionIdpAssertionKey = properties.getProperty("RP.Config.sessionIdpAssertionKey");
    this.sessionOauthTokenKey = properties.getProperty("RP.Config.sessionOauthTokenKey");
    this.notificationKey = properties.getProperty("RP.Config.notificationKey");
    this.cdsActionKey = properties.getProperty("RP.Config.cdsActionKey");
    this.siteUrl = properties.getProperty("RP.Config.siteUrl");
    this.homeUrl = properties.getProperty("RP.Config.homeUrl");
    this.loginUrl = properties.getProperty("RP.Config.loginUrl");
    this.signupUrl = properties.getProperty("RP.Config.signupUrl");
  }

  public String getSessionCookieName() {
    return sessionCookieName;
  }

  public String getIdpAssertionCookieName() {
    return idpAssertionCookieName;
  }

  public String getOauthTokenCookieName() {
    return oauthTokenCookieName;
  }

  public int getMaxAgeOfSession() {
    return maxAgeOfSession;
  }

  public int getMaxAgeOfIdpAssertion() {
    return maxAgeOfIdpAssertion;
  }

  public int getMaxAgeOfOauthToken() {
    return maxAgeOfOauthToken;
  }

  public String getDomain() {
    return domain;
  }

  public String getPath() {
    return path;
  }

  public String getSessionUserKey() {
    return sessionUserKey;
  }

  public String getSessionIdpAssertionKey() {
    return sessionIdpAssertionKey;
  }

  public String getSessionOauthTokenKey() {
    return sessionOauthTokenKey;
  }

  public String getNotificationKey() {
    return notificationKey;
  }

  public String getCdsActionKey() {
    return cdsActionKey;
  }

  @Deprecated
  public String getSiteUrl() {
    return siteUrl;
  }

  public String getHomeUrl() {
    return homeUrl;
  }

  public String getLoginUrl() {
    return loginUrl;
  }

  public String getSignupUrl() {
    return signupUrl;
  }

  /**
   * Builder class to setup a {@code RpConfig} instance.
   * @author guibinkong@google.com (Guibin Kong)
   */
  public static class Builder {
    private static final String DEFAULT_CDS_ACTION_KEY = "CdsAction";
    private String sessionCookieName;
    private String idpAssertionCookieName;
    private String oauthTokenCookieName;
    private int maxAgeOfSession;
    private int maxAgeOfIdpAssertion;
    private int maxAgeOfOauthToken;
    private String domain;
    private String path;

    private String sessionUserKey;
    private String sessionIdpAssertionKey;
    private String sessionOauthTokenKey;

    private String notificationKey;
    private String cdsActionKey;

    @Deprecated
    private String siteUrl;
    private String homeUrl;
    private String loginUrl;
    private String signupUrl;

    public Builder sessionCookieName(String val) {
      this.sessionCookieName = val;
      return this;
    }

    public Builder idpAssertionCookieName(String val) {
      this.idpAssertionCookieName = val;
      return this;
    }

    public Builder oauthTokenCookieName(String val) {
      this.oauthTokenCookieName = val;
      return this;
    }

    public Builder maxAgeOfSession(int val) {
      this.maxAgeOfSession = val;
      return this;
    }

    public Builder maxAgeOfIdpAssertion(int val) {
      this.maxAgeOfIdpAssertion = val;
      return this;
    }

    public Builder maxAgeOfOauthToken(int val) {
      this.maxAgeOfOauthToken = val;
      return this;
    }

    public Builder domain(String val) {
      this.domain = val;
      return this;
    }

    public Builder path(String val) {
      this.path = val;
      return this;
    }

    public Builder sessionUserKey(String val) {
      this.sessionUserKey = val;
      return this;
    }

    public Builder sessionIdpAssertionKey(String val) {
      this.sessionIdpAssertionKey = val;
      return this;
    }

    public Builder sessionOauthTokenKey(String val) {
      this.sessionOauthTokenKey = val;
      return this;
    }

    public Builder notificationKey(String val) {
      this.notificationKey = val;
      return this;
    }

    public Builder cdsActionKey(String val) {
      this.cdsActionKey = val;
      return this;
    }

    @Deprecated
    public Builder siteUrl(String val) {
      this.siteUrl = val;
      return this;
    }

    public Builder homeUrl(String val) {
      this.homeUrl = val;
      return this;
    }

    public Builder loginUrl(String val) {
      this.loginUrl = val;
      return this;
    }

    public Builder signupUrl(String val) {
      this.signupUrl = val;
      return this;
    }

    public RpConfig build() {
      RpConfig config = new RpConfig();
      config.sessionCookieName = this.sessionCookieName;
      config.idpAssertionCookieName = this.idpAssertionCookieName;
      config.oauthTokenCookieName = this.oauthTokenCookieName;
      config.maxAgeOfSession = this.maxAgeOfSession;
      config.maxAgeOfIdpAssertion = this.maxAgeOfIdpAssertion;
      config.maxAgeOfOauthToken = this.maxAgeOfOauthToken;
      config.domain = this.domain;
      config.path = this.path;
      config.sessionUserKey = this.sessionUserKey;
      config.sessionIdpAssertionKey = this.sessionIdpAssertionKey;
      config.sessionOauthTokenKey = this.sessionOauthTokenKey;
      config.notificationKey = this.notificationKey;
      config.cdsActionKey = Strings.isNullOrEmpty(this.cdsActionKey)
          ? DEFAULT_CDS_ACTION_KEY : this.cdsActionKey;
      config.siteUrl = this.siteUrl;
      config.homeUrl = this.homeUrl;
      config.loginUrl = this.loginUrl;
      config.signupUrl = this.signupUrl;
      return config;
    }
  }
}
