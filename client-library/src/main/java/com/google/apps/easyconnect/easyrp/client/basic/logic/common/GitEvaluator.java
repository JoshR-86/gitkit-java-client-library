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

import java.util.logging.Logger;

import com.google.apps.easyconnect.easyrp.client.basic.Context;
import com.google.apps.easyconnect.easyrp.client.basic.data.Account;
import com.google.apps.easyconnect.easyrp.client.basic.util.IdpUtils;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * Defines some common evaluators. An {@code Evaluator} is a java method that takes a request
 * parameter of a specific type and returns a String as result. It is used by a
 * {@code GitDecisionNode} to evaluate the request, and decide which subtree to go.
 * <p>
 * When creating a {@code GitNode} by {@code GitTreeBuilder}, the class of request parameter, the
 * instance of {@code Evaluator} object and {@code Action} object have been injected to the return
 * {@code GitNode} instance. <br>
 * See {@code GitTreeBuilder} for more detail.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class GitEvaluator {
  private static final Logger log = Logger.getLogger(GitEvaluator.class.getName());

  private boolean useLocalIdpWhiteList;

  /**
   * Constructs an evaluator object instance.
   * @param useLocalIdpWhiteList whether to use an IDP white list to check the domain type for an
   *        unregistered email
   */
  public GitEvaluator(boolean useLocalIdpWhiteList) {
    this.useLocalIdpWhiteList = useLocalIdpWhiteList;
  }

  public boolean isUseLocalIdpWhiteList() {
    return useLocalIdpWhiteList;
  }

  /**
   * Checks the format type for an input identifier.
   * <p>
   * Input Status: user's input should be already set by {@code request.setIdentifier(input)}. <br>
   * Return values:
   * <ul>
   * <li>{@code email}</li>: the identifier is a valid email.
   * <li>{@code invalid}</li>: the identifier is NOT a valid email.
   * </ul>
   * @param request the request object
   * @return the format type of the identifier
   */
  public String checkIdentifierType(GitRequest request) {
    String ret;
    if (IdpUtils.isValidEmail(request.getIdentifier())) {
      ret = "email";
    } else {
      ret = "invalid";
    }
    log.info("[checkIdentifierType] result: " + ret);
    return ret;
  }

  /**
   * Checks whether the email is registered.
   * <p>
   * Input Status: user's input should be already set by {@code request.setIdentifier(input)}. <br>
   * Return values:
   * <ul>
   * <li>{@code registered}</li>: the email is registered.
   * <li>{@code unregistered}</li>: the email is unregistered.
   * </ul>
   * <br>
   * Side effect:
   * <ul>
   * <li>If email already registered, the corresponding {@code Account} in database will be read and
   * set to request object by {@code request.setAccountInDB(account)} .</li>
   * </ul>
   * @param request the request object
   * @return whether the email is registered
   */
  public String checkEmailRegistered(GitRequest request) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(request.getIdentifier()));
    Account account = Context.getAccountService().getAccountByEmail(request.getIdentifier());
    request.setAccountInDB(account);
    String ret = (account == null) ? "unregistered" : "registered";
    log.info("[checkEmailRegistered] result: " + ret);
    return ret;
  }

  /**
   * Checks the account type of a registered account.
   * <p>
   * Input Status: account should be registered, and its data has already set by
   * {@code request.setAccountInDB(account)}. <br>
   * Return values:
   * <ul>
   * <li>{@code federated}</li> the email is federated account.
   * <li>{@code legacy}</li> the email is legacy account.
   * </ul>
   * @param request the request object
   * @return the account type of an registered account
   */
  public String checkAccountType(GitRequest request) {
    Preconditions.checkArgument(request.getAccountInDB() != null);
    String ret = request.getAccountInDB().isFederated() ? "federated" : "legacy";
    log.info("[checkAccountType] result: " + ret);
    return ret;
  }

  /**
   * Checks the domain type of a unregistered email. <br>
   * Since an local IDP white list is needed to check an email's domain type, the method can only be
   * called when {@code useLocalIdpWhiteList} is {@code true}. Otherwise an
   * {@code IllegalStateException} will be thrown.
   * <p>
   * Input Status: email should be unregistered. So its type can only be judged by its domain.<br>
   * Return values:
   * <ul>
   * <li>{@code idp}</li> if email's provider is an IDP.
   * <li>{@code non-idp}</li> otherwise.
   * </ul>
   * Side effect:
   * <ul>
   * <li>May send out an HTTP request to check whether a domain is a Google Apps domain, which will
   * also be treated as federated domain.</li>
   * </ul>
   * @param request the request object
   * @return the account type of an registered account
   * @throws IllegalStateException if {@code useLocalIdpWhiteList} is set to false
   */
  public String checkDomainType(GitRequest request) {
    if (!useLocalIdpWhiteList) {
      String msg = "Action [checkDomainType] cannot be called " +
          "when local IDP white list is not used.";
      log.severe(msg);
      throw new IllegalStateException(msg);
    }
    String domain = IdpUtils.getDomain(request.getIdentifier());
    boolean inFederatedDomain = Context.isFederatedDomain(domain);
    boolean isDashDomain = false;
    if (!inFederatedDomain) {
      isDashDomain = Context.getDasherDomainChecker().isDasherDomain(domain);
    }
    String ret = (inFederatedDomain || isDashDomain) ? "idp" : "non-idp";
    log.info("[checkDomainType] result: " + ret);
    return ret;
  }
}
