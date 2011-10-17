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

package com.google.apps.easyconnect.easyrp.client.basic.logic.common;

import java.io.IOException;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.apps.easyconnect.easyrp.client.basic.Context;
import com.google.apps.easyconnect.easyrp.client.basic.data.Account;
import com.google.apps.easyconnect.easyrp.client.basic.data.AccountException;
import com.google.apps.easyconnect.easyrp.client.basic.util.GitServiceClient;
import com.google.apps.easyconnect.easyrp.client.basic.util.Utils;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * Defines some evaluators for callback request.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class GitCallbackEvaluator extends GitEvaluator {
  private static final Logger log = Logger.getLogger(GitCallbackEvaluator.class.getName());

  /**
   * Constructs an evaluator object instance.
   * @param useLocalIdpWhiteList whether to use an IDP white list to check the domain type for an
   *        unregistered email
   */
  public GitCallbackEvaluator(boolean useLocalIdpWhiteList) {
    super(useLocalIdpWhiteList);
  }

  /**
   * Checks the value of the 'rp_purpose' parameter in the HTTP request. Currently not used by
   * Account Chooser.
   * <p>
   * Return values:
   * <ul>
   * <li>{@code signin}</li>: normal login request.
   * <li>{@code upgrade}</li>: user want to manually upgrade after successfully legacy login.
   * </ul>
   * Side effect:
   * <ul>
   * <li>The value will be set to the request by {@code request.setPurpose()} .</li>
   * </ul>
   * @param request the request object
   * @return the value of the 'rp_purpose' parameter
   */
  public String checkRpPurpose(GitCallbackRequest request) {
    String ret = request.getHttpServletRequest().getParameter("rp_purpose");
    request.setPurpose(ret);
    log.info("[checkRpPurpose] result: " + ret);
    return ret;
  }

  /**
   * Checks the value of the 'rp_input_email' parameter in the HTTP request.
   * <p>
   * Return values: the email user inputs or saved in the account chooser entry Side effect:
   * <ul>
   * <li>The value will be set to the request by {@code request.setInputEmail()} .</li>
   * </ul>
   * @param request the request object
   * @return the value of the 'rp_input_email' parameter
   */
  public String checkRpInputEmail(GitCallbackRequest request) {
    String inputEmail = request.getHttpServletRequest().getParameter("rp_input_email");
    request.setInputEmail(inputEmail);
    String ret = "match";
    if (!Strings.isNullOrEmpty(inputEmail)
        && !inputEmail.equalsIgnoreCase(request.getIdentifier())) {
      ret = "mismatch";
    }
    log.info("[checkRpInputEmail] result: " + ret);
    return ret;
  }

  /**
   * Checks the result of auto create the federated account.
   * <p>
   * Return values:
   * <ul>
   * <li>{@code created}</li>: successfully create the federated account.
   * <li>{@code not-created}</li>: failed to create the federated account.
   * </ul>
   * Side effect:
   * <ul>
   * <li>The created account will be set to the request by {@code request.setAccountInDB()}, if it
   * has been created successfully.</li>
   * </ul>
   * @param request the request object
   * @return the result of auto create the federated account
   */
  public String tryCreateAccount(GitCallbackRequest request) {
    Preconditions.checkArgument(Context.getAccountService().getAccountByEmail(
        request.getIdentifier()) == null);
    Account account = null;
    try {
      account = Context.getAccountService().createFederatedAccount(request.getIdpAssertion());
      request.setAccountInDB(account);
    } catch (AccountException e) {
      log.info("Failed to create federated automatically: " + e.getErrorCode());
    }
    String ret = account != null ? "created" : "not-created";
    log.info("[tryCreateAccount] result: " + ret);
    return ret;
  }

  /**
   * Checks whether there is an account in logged in status.
   * <p>
   * Return values:
   * <ul>
   * <li>{@code logged-in}</li>: there is an account in logged in status
   * <li>{@code not-logged-in}</li>: no account is in logged in status
   * </ul>
   * @param request the request object
   * @return whether there is an account in logged in status
   */
  public String CheckLoggedIn(GitCallbackRequest request) {
    Account account = Context.getSessionManager()
        .getSessionAccount(request.getHttpServletRequest());
    String ret = account == null ? "not-logged-in" : "logged-in";
    log.info("[CheckLoggedIn] result: " + ret);
    return ret;
  }

  /**
   * Checks whether the email returned from IDP matches the email in the session.
   * <p>
   * Return values:
   * <ul>
   * <li>{@code match}</li>: there is an account in logged in status, and its email equals the email
   * returned from IDP.
   * <li>{@code mismatch}</li>: otherwise.
   * </ul>
   * @param request the request object
   * @return whether there is an account in logged in status
   */
  public String CheckSessionEmailMatch(GitCallbackRequest request) {
    Account account = Context.getSessionManager()
        .getSessionAccount(request.getHttpServletRequest());
    String ret = "mismatch";
    if (account != null && request.getIdentifier().equals(account.getEmail())) {
      ret = "match";
    }
    log.info("[CheckSessionEmailMatch] result: " + ret);
    return ret;
  }

  /**
   * Verifies the IDP assertion.
   * <p>
   * Return values:
   * <ul>
   * <li>{@code trusted}</li>: there is a trusted email returned in the assertion.
   * <li>{@code untrusted}</li>: there is a untrusted email returned in the assertion.
   * <li>{@code error}</li>: otherwise.
   * </ul>
   * Side effect:
   * <ul>
   * <li>The returned IDP assertion will be set to the request by {@code request.setIdpAssertion()},
   * when the result is not 'error'.</li>
   * </ul>
   * @param request the request object
   * @return the result for Verifying the IDP assertion.
   */
  public String verifyAssertion(GitCallbackRequest request) {
    String ret = "error";
    try {
      String requestUri;
      if (!Strings.isNullOrEmpty(request.getRequestUri())) {
        requestUri = request.getRequestUri();
      } else {
        requestUri = Utils.getRequestUri(request.getHttpServletRequest());
        request.setRequestUri(requestUri);
      }

      GitServiceClient apiClient = Context.getGitServiceClient();
      JSONObject idpAssertion = apiClient.verifyResponse(requestUri, null);
      request.setIdpAssertion(idpAssertion);
      if (idpAssertion != null && idpAssertion.has("trusted")) {
        request.setIdentifier(idpAssertion.getString("email"));
        ret = idpAssertion.getBoolean("trusted") ? "trusted" : "untrusted";
      }
    } catch (IOException e) {
      log.severe(e.getMessage());
    } catch (JSONException e) {
      log.severe(e.getMessage());
    }
    log.info("[verifyAssertion] result: " + ret);
    return ret;
  }
}
