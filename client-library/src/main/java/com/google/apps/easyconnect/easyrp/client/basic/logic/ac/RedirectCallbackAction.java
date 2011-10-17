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

package com.google.apps.easyconnect.easyrp.client.basic.logic.ac;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.json.JSONObject;

import com.google.apps.easyconnect.easyrp.client.basic.Context;
import com.google.apps.easyconnect.easyrp.client.basic.data.Account;
import com.google.apps.easyconnect.easyrp.client.basic.data.AccountException;
import com.google.apps.easyconnect.easyrp.client.basic.logic.common.GitCallbackRequest;
import com.google.common.base.Preconditions;

/**
 * Defines the actions for the callback handler.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class RedirectCallbackAction {
  private static final Logger log = Logger.getLogger(RedirectCallbackAction.class.getName());
  private boolean returnProfileInfo;
  private RedirectCallbackNotificationBuilder builder;

  /**
   * Constructs an action object instance.
   * @param returnProfileInfo whether return DisplaName/PhotoUrl in response.
   */
  public RedirectCallbackAction(boolean returnProfileInfo) {
    this.returnProfileInfo = returnProfileInfo;
    builder = new RedirectCallbackNotificationBuilder();
  }

  public boolean isReturnProfileInfo() {
    return returnProfileInfo;
  }

  private void showLoginPage(GitCallbackRequest request, JSONObject data)
      throws IOException, ServletException {
    if (data != null) {
      request.getHttpServletRequest().setAttribute(Context.getConfig().getNotificationKey(),
          data.toString());
    }
    request.getHttpServletRequest().getRequestDispatcher(Context.getConfig().getLoginUrl())
        .forward(request.getHttpServletRequest(), request.getHttpServletResponse());
  }

  /**
   * Sends HTML response to widget to indicate the IDP assertion is invalid.
   * @param request the login request object
   * @throws IOException if error occurs when send back response
   */
  public void sendInvalidAssertion(GitCallbackRequest request) throws IOException,
      ServletException {
    log.info("CallbackAction response: sendInvalidAssertion.");
    showLoginPage(request, builder.createInvalidAssertion());
  }

  /**
   * Sends HTML response to widget to indicate the email is not trusted. That is, the IDP is not the
   * Email Provider of current email.
   * @param request the login request object
   * @throws IOException if error occurs when send back response
   */
  public void sendInvalidAssertionEmail(GitCallbackRequest request) throws IOException,
      ServletException {
    log.info("CallbackAction response: sendInvalidAssertionEmail.");
    showLoginPage(request, builder.createInvalidAssertionEmail());
  }

  /**
   * Sends HTML response to widget to indicate account mismatch. That is, the returned email from
   * IDP is different to the email user inputs.
   * @param request the login request object
   * @throws IOException if error occurs when send back response
   */
  public void sendAccountMismatch(GitCallbackRequest request) throws IOException, ServletException {
    log.info("CallbackAction response: sendAccountMismatch.");
    showLoginPage(request,
        builder.createAccountMismatch(request.getIdentifier(), request.getIdentifier(), null));
  }

  /**
   * Sends HTML response to widget to indicate a successful federated login for an registered email.
   * @param request the login request object
   * @throws IOException if error occurs when send back response
   */
  public void sendOKRegistered(GitCallbackRequest request) throws IOException {
    log.info("CallbackAction response: sendOKRegistered.");
    request.getHttpServletResponse().sendRedirect(Context.getConfig().getHomeUrl());
  }

  /**
   * Sends HTML response to widget to indicate a successful federated login for a unregistered
   * email.
   * @param request the login request object
   * @throws IOException if error occurs when send back response
   */
  public void sendOKUnregistered(GitCallbackRequest request) throws IOException {
    log.info("CallbackAction response: sendOKUnregistered.");
    request.getHttpServletResponse().sendRedirect(Context.getConfig().getSignupUrl());
  }

  /**
   * Sets the current email into logged in status.
   * <p>
   * When config the logic tree, you shuold put this action before sendXXX action.
   * @param request the login request object
   * @throws IOException if error occurs when send back response
   */
  public void setLoggedIn(GitCallbackRequest request) throws IOException {
    Preconditions.checkArgument(request.getAccountInDB() != null);
    log.info("CallbackAction response: set user logged in.");
    Context.getSessionManager().setSessionAccount(request.getHttpServletRequest(),
        request.getHttpServletResponse(), request.getAccountInDB());
  }

  /**
   * Saves the IDP assertion into session for later use.
   * <p>
   * When config the logic tree, you shuold put this action before sendXXX action.
   * @param request the login request object
   * @throws IOException if error occurs when send back response
   */
  public void saveIdpAssertion(GitCallbackRequest request) throws IOException, ServletException {
    log.info("CallbackAction response: set user logged in.");
    Context.getSessionManager().setIdpAssertionData(request.getHttpServletRequest(),
        request.getHttpServletResponse(), request.getIdpAssertion());
  }

  /**
   * Upgrades current user to federated account.
   * <p>
   * When config the logic tree, you shuold put this action before sendXXX action.
   * @param request the login request object
   * @throws IOException if error occurs when send back response
   */
  public void upgrade(GitCallbackRequest request) throws IOException {
    Preconditions.checkArgument(request.getAccountInDB() != null);
    log.info("CallbackAction response: upgrade.");
    String email = request.getIdentifier();
    try {
      Context.getAccountService().toFederated(email);
      log.info("Upgrade email '" + email + "' to federated successfully.");
    } catch (AccountException e) {
      log.severe("Failed to upgrade email '" + email + "' with error: " + e.getErrorCode());
    }
    // Refresh Account object in session
    Account account = Context.getAccountService().getAccountByEmail(email);
    request.setAccountInDB(account);
    Context.getSessionManager().setSessionAccount(request.getHttpServletRequest(),
        request.getHttpServletResponse(), account);
  }
}
