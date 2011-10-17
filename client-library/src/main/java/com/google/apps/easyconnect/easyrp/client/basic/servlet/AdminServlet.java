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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.google.apps.easyconnect.easyrp.client.basic.Context;
import com.google.apps.easyconnect.easyrp.client.basic.logic.GitNode;
import com.google.apps.easyconnect.easyrp.client.basic.logic.GitRule;
import com.google.apps.easyconnect.easyrp.client.basic.logic.GitTree;
import com.google.common.collect.Lists;

/**
 * Handles logic visualization & testing.
 * <p>
 * Note: this servlet should only be accessible to administrators.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class AdminServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static final Logger log = Logger.getLogger(AdminServlet.class.getName());

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    doPost(req, resp);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    visualize(req, resp);
  }

  private void visualize(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String whitelist = req.getParameter("whitelist");
    if ("0".equalsIgnoreCase(whitelist) || "false".equalsIgnoreCase(whitelist)
        || "no".equalsIgnoreCase(whitelist)) {
      whitelist = "0";
    } else if ("1".equalsIgnoreCase(whitelist) || "true".equalsIgnoreCase(whitelist)
        || "yes".equalsIgnoreCase(whitelist)) {
      whitelist = "1";
    } else {
      whitelist = Context.isUseLocalIdpWhiteList() ? "1" : "0";
    }
    String showprofile = req.getParameter("showprofile");
    if ("0".equalsIgnoreCase(showprofile) || "false".equalsIgnoreCase(showprofile)
        || "no".equalsIgnoreCase(showprofile)) {
      showprofile = "0";
    } else if ("1".equalsIgnoreCase(showprofile) || "true".equalsIgnoreCase(showprofile)
        || "yes".equalsIgnoreCase(showprofile)) {
      showprofile = "1";
    } else {
      showprofile = Context.isReturnProfileInfo() ? "1" : "0";
    }
    String logic = req.getParameter("logic");

    if (log.isLoggable(Level.INFO)) {
      StringBuilder buf = new StringBuilder();
      buf.append("Admin Servlet: logic=[").append(logic).append("],");
      buf.append("whitelist=[").append(whitelist).append("],");
      buf.append("showprofile=[").append(showprofile).append("]");
      log.info(buf.toString());
    }

    GitNode tree;
    if ("userstatus".equalsIgnoreCase(logic)) {
      tree = GitTree.getAcUserStatusLogic("1".equals(whitelist), "1".equals(showprofile));
    } else if ("login".equalsIgnoreCase(logic)) {
      tree = GitTree.getAcLegacySigninLogic("1".equals(whitelist), "1".equals(showprofile));
    } else {
      tree = GitTree.getAcCallbackPopupLogic("1".equals(whitelist), "1".equals(showprofile));
    }

    resp.setContentType(ContentType.JSON);
    resp.getWriter().print(toJson(tree));
  }

  private static String toJson(GitNode tree) {
    List<GitRule> rules = Lists.newArrayList();
    tree.appendToRuleList(rules, null, null);
    JSONArray json = new JSONArray();
    for (int i = 0; i < rules.size(); i++) {
      json.put(rules.get(i).toJson());
    }
    return json.toString();
  }
}
