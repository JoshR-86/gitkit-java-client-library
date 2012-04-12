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

import org.json.JSONObject;

import com.google.apps.easyconnect.easyrp.client.basic.data.Account;
import com.google.apps.easyconnect.easyrp.client.basic.data.OauthTokenResponse;

/**
 * Sets/Gets data in session. RP may save session data in Server side (in memory or persistent), or
 * in the client side (cookie). This interface defines a common set of methods for them.
 * @author guibinkong@google.com (Guibin Kong)
 */
public interface SessionManager {

  /**
   * Gets the logged in account for the request.
   * @param request the servlet request object
   * @return the {@code Account} of the logged-in user, or null otherwise
   */
  Account getSessionAccount(HttpServletRequest request);

  /**
   * Saves the logged account information to the session. If parameter {@code account} is null, the
   * data in the session will be cleared.
   * @param request the servlet request object
   * @param response the servlet response object
   * @param account the {@code Account} of the logged-in user, or null if want to clear it.
   */
  void setSessionAccount(HttpServletRequest request, HttpServletResponse response, Account account);

  /**
   * Gets the IDP assertion for the request. For account chooser, if RP server side will create
   * federated user automatically, then this method is optional.
   * @param request the servlet request object
   * @return the {@code JSONObject} of the IDP assertion, or null otherwise
   */
  JSONObject getIdpAssertionData(HttpServletRequest request);

  /**
   * Saves the IDP assertion information to the session. If parameter {@code data} is null, the data
   * in the session will be cleared. For account chooser, if RP server side will create federated
   * user automatically, then this method is optional.
   * @param request the servlet request object
   * @param response the servlet response object
   * @param data {@code JSONObject} of the logged-in user, or null if want to clear it.
   */
  void setIdpAssertionData(HttpServletRequest request, HttpServletResponse response,
      JSONObject data);

  /**
   * Gets the OAuth token for the request.
   *
   * @param request the servlet request object.
   * @return the {@code OauthTokenResponse} of the OAuth token.
   */
  OauthTokenResponse getAccountOauthToken(HttpServletRequest request);

  /**
   * Saves the OAuth token to the session. If parameter {@code data} is null, the data in the
   * session will be cleared.
   *
   * @param request the servlet request object
   * @param response the servlet response object
   * @param data the {@code OauthTokenResponse} of the OAuth token.
   */
  void setAccountOauthToken(HttpServletRequest request, HttpServletResponse response,
      OauthTokenResponse data);
}
