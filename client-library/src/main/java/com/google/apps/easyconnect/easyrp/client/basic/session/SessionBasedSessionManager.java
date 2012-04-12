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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.apps.easyconnect.easyrp.client.basic.data.Account;
import com.google.apps.easyconnect.easyrp.client.basic.data.OauthTokenResponse;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * A simple implementation for the {@code SessionManager}. It saves data in the HttpSession Object
 * of the Servlet Container.
 * @author guibinkong@google.com (Guibin Kong)
 */
public class SessionBasedSessionManager implements SessionManager {
  private final RpConfig config;

  public SessionBasedSessionManager(RpConfig config) {
    this.config = config;
  }

  @Override
  public final Account getSessionAccount(HttpServletRequest request) {
    return (Account) findAttributeInSession(request, config.getSessionUserKey());
  }

  @Override
  public void setSessionAccount(HttpServletRequest request, HttpServletResponse response,
      Account account) {
    HttpSession session = request.getSession(true);
    if (account != null) {
      session.setAttribute(config.getSessionUserKey(), account);
    } else {
      session.removeAttribute(config.getSessionUserKey());
    }
  }

  @Override
  public final JSONObject getIdpAssertionData(HttpServletRequest request) {
    String json = (String) findAttributeInSession(request, config.getSessionIdpAssertionKey());
    if (!Strings.isNullOrEmpty(json)) {
      try {
        return new JSONObject(json);
      } catch (JSONException e) {
      }
    }
    return null;
  }

  @Override
  public void setIdpAssertionData(HttpServletRequest request, HttpServletResponse response,
      JSONObject data) {
    HttpSession session = request.getSession(true);
    if (data != null) {
      session.setAttribute(config.getSessionIdpAssertionKey(), data.toString());
    } else {
      session.removeAttribute(config.getSessionIdpAssertionKey());
    }
  }

  @Override
  public final OauthTokenResponse getAccountOauthToken(HttpServletRequest request) {
    String key = config.getSessionOauthTokenKey();
    if (!Strings.isNullOrEmpty(key)) {
      return (OauthTokenResponse) findAttributeInSession(request, key);
    }
    return null;
  }

  @Override
  public void setAccountOauthToken(HttpServletRequest request, HttpServletResponse response,
      OauthTokenResponse data) {
    HttpSession session = request.getSession(true);
    String key = config.getSessionOauthTokenKey();
    if (!Strings.isNullOrEmpty(key)) {
      if (data != null) {
        session.setAttribute(key, data);
      } else {
        session.removeAttribute(key);
      }
    }
  }

  private Object findAttributeInSession(HttpServletRequest request, String name) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
    HttpSession session = request.getSession(false);
    if (session != null) {
      return session.getAttribute(name);
    }
    return null;
  }
}
