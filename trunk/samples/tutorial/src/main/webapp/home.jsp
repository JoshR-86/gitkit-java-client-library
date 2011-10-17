<%-- Copyright 2011 Google Inc. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
 
    http://www.apache.org/licenses/LICENSE-2.0
 
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.apps.easyconnect.demos.easyrpbasic.web.data.User" %>
<%@ page import="com.google.apps.easyconnect.demos.easyrpbasic.web.util.Constants" %>
<%@ page import="com.google.apps.easyconnect.demos.easyrpbasic.web.util.HttpUtil" %>
<%
User user = (User)session.getAttribute(Constants.SESSION_KEY_LOGIN_USER);

// For log-in box
String email = request.getParameter("email");
if (email == null) {
  email = "";
}
String message = "";
if ("PWD_ERR".equals(request.getParameter("error"))) {
  message = "Incorrect username or password!";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-control" content="no-cache">
<title>Demo Relying Party site using GIT</title>
<link rel="icon" href="image/git.png" type="image/png">
<link type=text/css rel=stylesheet href="stylesheet/codesite.css" />
<link type=text/css rel=stylesheet href="stylesheet/rp.css" />
</head>
<body>
<center>
<div style="width:780px;">
  <% if (user != null) { %>
  <div id="navbar" style="padding: 0px 10px 5px 0px; float: right;">
    <div class="navbar-panel">
      <div class="navbar-menu">
        <a href="logout">Sign out</a>
      </div>
      <div class="navbar-seperator">|</div>
      <div class="navbar-menu-email">
        <% if (user.getPhotoUrl() != null && !user.getPhotoUrl().isEmpty()) { %>
          <img src="<%= user.getPhotoUrl() %>"  style="height:12px; margin: 2px 0 0;" />
        <% } %>
        <a class="navbar-email">
          <span style="font-size: 13px; font-weight: bold; color: rgb(0, 0, 0);"><%= user.getEmail() %></span>
        </a>
      </div>
    </div>
  </div>
  <div style="clear: both; width: 100%; border-top: 1px solid #E6E6E6;"></div>
  <% } %>
  <table cellpadding="2" cellspacing="0" border=0 style="padding-left:50px;align:left">
    <tr>
    <td valign="top" width="1%">
      <a href="/">
        <img src="image/rp_logo.png" style="border:0px;width:90px;height:60px;">
      </a>
    </td>
    <td width="98%" style="padding-left: 20px; font-size: 20px; color: #1C94C4;">
        Demo RP Site using <span style="color: #0000CC; font-weight: bold;">GIT</span>
    </td>
    </tr>
  </table>
</div>
<br>
  <!-- BODY -->
  <table width="780" cellpadding="0" border=0>
  <tbody>
  <tr>
  <td height="1%" colspan="2" bgcolor="#FFFFFF">
      <div class="g-unit">
        <div class="g-c-gc-home">

<img src="image/git.png" style="float: left; width: 60px;" width="60" height="60">
<div style="margin-left: 72px; margin-top: 10px;">
  <div style="font-size: 140%; font-weight: bold; padding-top: 19px;">Productivity for developers, performance for users</div>
  <div style="padding-top: 12px; line-height: 125%;">
    GIT provides developers with a powerful set
    of API endpoints to do federated login, attribute exchanges.
    This allows desktop, mobile, and web application developers to integrate
    with different identity provider, and facilitate the user to sign-up and
    sign-in on different sites.
  </div>
</div>
<table class="columns" style="clear: left;">
  <tbody>
    <tr>
      <td>
        <a href="http://code.google.com/apis/identitytoolkit/v1/getting_started.html">
          <img src="image/start_demo.gif" style="float: left; margin-left: 50px; border: 0pt none;" width="48" height="48">
        </a>
        <div style="margin-left: 112px; margin-bottom: 10px;">
          <div style="margin-top: 15px; font-size: 120%; font-weight: bold;"><a href="http://code.google.com/apis/identitytoolkit/v1/getting_started.html">Get Started</a></div>
          <div style="padding-top: 5px; line-height: 125%;">
            Walk through the first steps needed to integrate GIT into your application.
            From there, work through the fundamentals of GIT development with an in-depth tutorial.
          </div>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <a href="http://code.google.com/apis/identitytoolkit/">
          <img src="image/docs.gif" style="float: left; margin-left: 50px; border: 0pt none;" width="48" height="48">
        </a>
        <div style="margin-left: 112px;">
          <div style="margin-top: 15px; font-size: 120%; font-weight: bold;"><a href="http://code.google.com/apis/identitytoolkit/">Read the Docs</a></div>
          <div style="padding-top: 5px; line-height: 125%;">
            Everything you need to know about how to use GIT. 
          </div>
        </div>
      </td>
    </tr>
  </tbody>
</table>

        
      </div><!-- end g-c-gc-home -->
    </div><!-- end g-unit -->
  </td>
  <td width="3%">&nbsp;</td>
  <td width="1%" align="center" valign="top" style="padding-top: 36px;">
    <% if (user == null) {%>
    <div id="login-box">
      <div id="login-box-inner">
        <form action="/login" method="POST">
        <div class="error"><%= message %></div>
        <p>Email<br><input name="email" value="<%= email %>" class="input-box" type="text"></p>
        <p>Password<br><input name="password" class="input-box" type="password"></p>
        <p><center><input class="input-button" value="Sign In" type="submit"></center></p>
        <p><center><a class="new-account" href="/signup.jsp">Create a new account</a></center></p>
        </form>
      </div>
    </div>
    <% } else {%>  
    <div id="outer_box">
      <div id="inner_box">
        <div style="font-size: 140%; font-weight: bold; text-align: left;">You are logged in.</div>
        <div style="padding-top: 12px; line-height: 125%; text-align: left;">
          You can test:
          <ul>
            <li>Switch accounts</li>
            <li>Remove accounts</li>
            <li>Sign out</li>
          </ul>
          Or you can click <a href="/account.jsp" style="color:red;">here</a> to:
          <ul>
            <li>Delete an account to test registering flow again</li>
            <li>Set a federated account to legacy to test upgrade flow</li>
          </ul>
      </div>
    </div>
    <% }%>  
  </td>
  </tr>
  <tr>
  </tr>

  <tr>
  <td colspan="4" valign="top" align="center"><hr noshade size="1" color="#DDDDDD" style="margin-top:5px">
  <p>
   2011 DemoRP -
  <a href="javascript:void 0;">Terms of Service</a>
  -
  <a href="javascript:void 0;">Privacy Policy</a>
  -
  <a href="javascript:void 0;">Help Center</a>

  -
  <a href="javascript:void 0;">
  Getting Started Guide</a>
  </p></td>
  </tr>
  </tbody>
  </table>
  <!-- FOOTER -->
  <br>
</center>
</body>
</html>
