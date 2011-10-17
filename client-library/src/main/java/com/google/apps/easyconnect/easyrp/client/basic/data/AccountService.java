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

package com.google.apps.easyconnect.easyrp.client.basic.data;

import org.json.JSONObject;

/**
 * An interface for the client library to access/update RP's account information.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public interface AccountService {

  /**
   * Returns the Account for an email, or null if not found.
   * 
   * @param email the email address to be checked
   * @return the account object, or null if not exist.
   */
  Account getAccountByEmail(String email);

  /**
   * Create federated account according to the IDP assertion. An RP should throw
   * {@code AccountException} if not supporting create federated account automatically.
   * 
   * @param assrtion the assrtion from IDP
   * @return the create account object.
   * @throws AccountException if this action is not allowed.
   */
  Account createFederatedAccount(JSONObject assrtion) throws AccountException;

  /**
   * Returns whether the password is same.
   * 
   * @param email the email user inputs
   * @param password the password user inputs
   */
  boolean checkPassword(String email, String password);

  /**
   * Upgrades an email to federated login. An RP should throw {@code AccountException} if not
   * supporting this operation, or cannot find the account, etc.
   * 
   * @param email the email address to be updated
   * @throws AccountException if this action is not allowed.
   */
  void toFederated(String email) throws AccountException;
}
