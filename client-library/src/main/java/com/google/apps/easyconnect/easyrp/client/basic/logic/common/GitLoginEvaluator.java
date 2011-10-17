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
import com.google.common.base.Strings;

/**
 * Defines some evaluators for login request.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class GitLoginEvaluator extends GitEvaluator {
  private static final Logger log = Logger.getLogger(GitLoginEvaluator.class.getName());

  /**
   * Constructs an evaluator object instance.
   * @param useLocalIdpWhiteList whether to use an IDP white list to check the domain type for an
   *        unregistered email
   */
  public GitLoginEvaluator(boolean useLocalIdpWhiteList) {
    super(useLocalIdpWhiteList);
  }

  /**
   * Checks whether the email/password pair is correct for a legacy account.
   * <p>
   * Input Status: the email and password should be already set to {@code request}. <br>
   * Return values:
   * <ul>
   * <li>{@code correct}</li>: the email/password pair is correct for a legacy account.
   * <li>{@code incorrect}</li>: the email/password pair is incorrect, or the email is not
   * registered, or it is not a legacy account.
   * </ul>
   * @param request the request object
   * @return the format type of the identifier
   */
  public String checkPasswordCorrect(GitLoginRequest request) {
    boolean correct = Context.getAccountService().checkPassword(request.getIdentifier(),
        request.getPassword());
    String ret = correct ? "correct" : "incorrect";
    log.info("[checkPasswordCorrect] result: " + ret);
    return ret;
  }

  /**
   * Checks whether the password is filled for a login request.
   * <p>
   * Input Status: the password should be already set to {@code request}. <br>
   * Return values:
   * <ul>
   * <li>{@code filled}</li>: user input a password.
   * <li>{@code empty}</li>: user doesn't input a password.
   * </ul>
   * @param request the request object
   * @return the format type of the identifier
   */
  public String checkPasswordFilled(GitLoginRequest request) {
    String ret = Strings.isNullOrEmpty(request.getPassword()) ? "filled" : "empty";
    log.info("[checkPasswordCorrect] result: " + ret);
    return ret;
  }
}
