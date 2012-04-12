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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.apps.easyconnect.easyrp.client.basic.data.Account;
import com.google.apps.easyconnect.easyrp.client.basic.data.AccountService;
import com.google.apps.easyconnect.easyrp.client.basic.data.OauthTokenResponse;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * A session manager that uses Cookie to save session data.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class CookieBasedSessionManager implements SessionManager {
  private final RpConfig config;
  private final AccountService accountService;
  private final TokenGenerator tokenGenerator;

  public CookieBasedSessionManager(RpConfig config, AccountService accountService,
      TokenGenerator tokenGenerator) {
    this.config = config;
    this.accountService = accountService;
    this.tokenGenerator = tokenGenerator;
  }

  @Override
  public Account getSessionAccount(HttpServletRequest request) {
    Cookie cookie = findCookieByName(request, config.getSessionCookieName());
    if (cookie != null) {
      String email = tokenGenerator.verifySessionToken(cookie.getValue());
      if (!Strings.isNullOrEmpty(email)) {
        return accountService.getAccountByEmail(email);
      }
    }
    return null;
  }

  @Override
  public void setSessionAccount(HttpServletRequest request, HttpServletResponse response,
      Account account) {
    String token = null;
    if (account != null) {
      token = tokenGenerator.generateSessionToken(account.getEmail());
    }
    Cookie cookie = new Cookie(config.getSessionCookieName(), token);
    if (config.getMaxAgeOfSession() > 0) {
      cookie.setMaxAge(config.getMaxAgeOfSession());
    }
    if (!Strings.isNullOrEmpty(config.getDomain())) {
      cookie.setDomain(config.getDomain());
    }
    if (!Strings.isNullOrEmpty(config.getPath())) {
      cookie.setPath(config.getPath());
    }
    response.addCookie(cookie);
  }

  @Override
  public JSONObject getIdpAssertionData(HttpServletRequest request) {
    Cookie cookie = findCookieByName(request, config.getIdpAssertionCookieName());
    if (cookie != null) {
      return tokenGenerator.verifyIdpAssertionToken(cookie.getValue());
    }
    return null;
  }

  @Override
  public void setIdpAssertionData(HttpServletRequest request, HttpServletResponse response,
      JSONObject data) {
    String token = null;
    if (data != null) {
      token = tokenGenerator.generateIdpAssertionToken(data);
    }
    Cookie cookie = new Cookie(config.getIdpAssertionCookieName(), token);
    if (config.getMaxAgeOfIdpAssertion() > 0) {
      cookie.setMaxAge(config.getMaxAgeOfIdpAssertion());
    }
    if (!Strings.isNullOrEmpty(config.getDomain())) {
      cookie.setDomain(config.getDomain());
    }
    if (!Strings.isNullOrEmpty(config.getPath())) {
      cookie.setPath(config.getPath());
    }
    response.addCookie(cookie);
  }

  @Override
  public OauthTokenResponse getAccountOauthToken(HttpServletRequest request) {
    String cookieName = config.getOauthTokenCookieName();
    if (!Strings.isNullOrEmpty(cookieName)) {
      Cookie cookie = findCookieByName(request, cookieName);
      if (cookie != null) {
        return tokenGenerator.verifyOauthToken(cookie.getValue());
      }
    }
    return null;
  }

  @Override
  public void setAccountOauthToken(HttpServletRequest request, HttpServletResponse response,
      OauthTokenResponse data) {
    String cookieName = config.getOauthTokenCookieName();
    if (Strings.isNullOrEmpty(cookieName)) {
      return;
    }
    String token = data == null ? null : tokenGenerator.generateOauthToken(data);
    Cookie cookie = new Cookie(cookieName, token);
    if (config.getMaxAgeOfOauthToken() > 0) {
      cookie.setMaxAge(config.getMaxAgeOfOauthToken());
    }
    if (!Strings.isNullOrEmpty(config.getDomain())) {
      cookie.setDomain(config.getDomain());
    }
    if (!Strings.isNullOrEmpty(config.getPath())) {
      cookie.setPath(config.getPath());
    }
    response.addCookie(cookie);
  }

  private Cookie findCookieByName(HttpServletRequest request, String name) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
    Cookie[] cookies = request.getCookies();
    for (int i = 0; i < cookies.length; i++) {
      if (name.equals(cookies[i].getName())) {
        return cookies[i];
      }
    }
    return null;
  }
}
