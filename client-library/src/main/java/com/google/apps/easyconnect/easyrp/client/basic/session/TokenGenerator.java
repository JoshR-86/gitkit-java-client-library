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

package com.google.apps.easyconnect.easyrp.client.basic.session;

import org.json.JSONObject;

/**
 * Generates and verifies the cookie values. Since the cookie is saved at client side, there would
 * be some way the check the value has not been changed by user.
 * @author guibinkong@google.com (Guibin Kong)
 */
public interface TokenGenerator {

  /**
   * Creates the cookie value for a logged-in email
   * @param email the logged-in email address
   * @return the cookie value created
   */
  String generateSessionToken(String email);

  /**
   * Verifies the cookie value is valid. If valid, return the logged-in email address.
   * @param token the cookie value to be verified
   * @return the email address if valid, null otherwise
   */
  String verifySessionToken(String token);

  /**
   * Creates the cookie value to save the IDP assertion.
   * @param data the assertion from IDP.
   * @return the cookie value created
   */
  String generateIdpAssertionToken(JSONObject data);

  /**
   * Verifies the cookie value is valid. If valid, return the IDP assertion.
   * @param token the cookie value to be verified
   * @return the IDP assertion if valid, null otherwise
   */
  JSONObject verifyIdpAssertionToken(String token);
}
