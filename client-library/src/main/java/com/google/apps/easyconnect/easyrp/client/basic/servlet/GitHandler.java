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

package com.google.apps.easyconnect.easyrp.client.basic.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.apps.easyconnect.easyrp.client.basic.Context;
import com.google.apps.easyconnect.easyrp.client.basic.logic.GitTree;
import com.google.apps.easyconnect.easyrp.client.basic.logic.common.GitCallbackRequest;
import com.google.apps.easyconnect.easyrp.client.basic.logic.common.GitLoginRequest;
import com.google.apps.easyconnect.easyrp.client.basic.logic.common.GitRequest;
import com.google.apps.easyconnect.easyrp.client.basic.util.Utils;

/**
 * The helper class that really handle GIT RPC requests. All the servlets will delegate their
 * request to suitable methods in GitHandler.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class GitHandler {
  private static final Logger log = Logger.getLogger(GitHandler.class.getName());

  /**
   * Handles the legacy login RPC request.
   * @param req the HTTP request
   * @param resp the HTTP response
   * @throws IOException when IO error occurs.
   */
  public static void handleLogin(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    String email = req.getParameter("email");
    String password = req.getParameter("password");

    if (log.isLoggable(Level.FINE)) {
      StringBuilder buf = new StringBuilder();
      buf.append("Login Request: email=[").append(email).append("],");
      buf.append("password=[").append(password).append("]");
      log.fine(buf.toString());
    }

    GitLoginRequest request = new GitLoginRequest(req, resp, email, password);
    GitTree.getAcLegacySigninLogic(Context.isUseLocalIdpWhiteList(), Context.isReturnProfileInfo())
        .execute(request);
  }

  /**
   * Handles the user status RPC request.
   * @param req the HTTP request
   * @param resp the HTTP response
   * @throws IOException when IO error occurs.
   */
  public static void handleUserStatus(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    String email = req.getParameter("email");
    if (log.isLoggable(Level.INFO)) {
      StringBuilder buf = new StringBuilder();
      buf.append("UserStatus Request: email=[").append(email).append("],");
      log.info(buf.toString());
    }

    GitRequest request = new GitRequest(req, resp, email);
    GitTree.getAcUserStatusLogic(Context.isUseLocalIdpWhiteList(), Context.isReturnProfileInfo())
        .execute(request);
  }

  /**
   * Handles the callback request.
   * @param req the HTTP request
   * @param resp the HTTP response
   * @param redirect whether the login is done by full page redirecting or in popup window.
   * @throws IOException when IO error occurs.
   */
  public static void handleCallback(HttpServletRequest req, HttpServletResponse resp,
      boolean redirect) throws IOException {
    String requestUri = Utils.getRequestUri(req);
    if (log.isLoggable(Level.INFO)) {
      StringBuilder buf = new StringBuilder();
      buf.append("Callback Request: requestUri=[").append(requestUri).append("]. ");
      log.info(buf.toString());
    }

    GitCallbackRequest request = new GitCallbackRequest(req, resp, requestUri);
    if (redirect) {
      GitTree.getAcCallbackRedirectLogic(Context.isUseLocalIdpWhiteList(),
          Context.isReturnProfileInfo()).execute(request);
    } else {
      GitTree.getAcCallbackPopupLogic(Context.isUseLocalIdpWhiteList(),
          Context.isReturnProfileInfo()).execute(request);
    }
  }
}
