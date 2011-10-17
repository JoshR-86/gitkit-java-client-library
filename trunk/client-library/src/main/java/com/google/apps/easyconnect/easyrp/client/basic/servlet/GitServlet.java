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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.apps.easyconnect.easyrp.client.basic.Context;
import com.google.common.base.Strings;

/**
 * Handles all three types of GIT RPC requests. An RP can support all GIT functions by adding a
 * single entry point for this servlet.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class GitServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static final Logger log = Logger.getLogger(GitServlet.class.getName());
  private boolean redirectMode = false;
  private boolean disableCallback = false;

  @Override
  public void init(ServletConfig config) throws ServletException {
    String setting = config.getInitParameter("fullPageRedirect");
    this.redirectMode = "true".equalsIgnoreCase(setting) || "1".equals(setting);
    setting = config.getInitParameter("disableCallback");
    this.disableCallback = "true".equalsIgnoreCase(setting) || "1".equals(setting);
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
    String rpTarget = req.getParameter("rp_target");

    if (log.isLoggable(Level.FINE)) {
      StringBuilder buf = new StringBuilder();
      buf.append("Git Servlet: rp_target=[").append(rpTarget).append("].");
      log.fine(buf.toString());
    }

    // Starts a new sign-in process so the IDPAssertion will be cleared.
    Context.getSessionManager().setIdpAssertionData(req, resp, null);
    if ("userstatus".equalsIgnoreCase(rpTarget)) {
      GitHandler.handleUserStatus(req, resp);
    } else if ("login".equalsIgnoreCase(rpTarget)) {
      GitHandler.handleLogin(req, resp);
    } else if ("callback".equalsIgnoreCase(rpTarget)) {
      if (this.disableCallback) {
        log.warning("Git Servlet: rp_target 'callback' is disabled, ignored.");
      } else {
        GitHandler.handleCallback(req, resp, isRedirectMode(req));
      }
    } else {
      log.warning("Git Servlet: Unkown rp_target '" + rpTarget + "' received, ignored.");
    }
  }
}
