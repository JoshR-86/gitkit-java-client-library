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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.apps.easyconnect.easyrp.client.basic.Context;
import com.google.common.base.Strings;

/**
 * Handles the callback from IDP after successfully Callback login.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class CallbackServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private boolean redirectMode = false;

  @Override
  public void init(ServletConfig config) throws ServletException {
    String setting = config.getInitParameter("fullPageRedirect");
    this.redirectMode = "true".equalsIgnoreCase(setting) || "1".equals(setting);
    super.init(config);
  }

  private boolean isRedirectMode(HttpServletRequest req) {
    boolean redirect = this.redirectMode;
    String rpRedirectMode = req.getParameter("rp_fullPageRedirect");
    if (!Strings.isNullOrEmpty(rpRedirectMode)) {
      if ("false".equalsIgnoreCase(rpRedirectMode) || "0".equalsIgnoreCase(rpRedirectMode)) {
        redirect = false;
      } else if ("true".equalsIgnoreCase(rpRedirectMode) || "1".equalsIgnoreCase(rpRedirectMode)) {
        redirect = true;
      }
    }
    return redirect;
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    doPost(req, resp);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    // Starts a new sign-in process so the IDPAssertion will be cleared.
    Context.getSessionManager().setIdpAssertionData(req, resp, null);
    GitHandler.handleCallback(req, resp, isRedirectMode(req));
  }
}
