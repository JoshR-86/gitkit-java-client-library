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

package com.google.apps.easyconnect.easyrp.client.basic.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * Utilities to handle federated and dasher account. <br>
 * Currently only following sites are recognized as federated domains: Gmail, AOL domains, LiveID
 * domains, Yahoo domains;<br>
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class IdpUtils {
  private static final String EMAIL_REGEX = "\\w+(\\.\\w+)*@(\\w+(\\.\\w+)+)";
  private static final String USERNAME_REGEX = "\\w+(\\.\\w+)*";

  /**
   * Checks if a user name is valid.
   * 
   * @param username the user name to be checked
   * @return ture for valid, false otherwise
   */
  public static boolean isValidUsername(String username) {
    if (Strings.isNullOrEmpty(username)) {
      return false;
    }
    return username.matches(USERNAME_REGEX);
  }

  /**
   * Checks if a email is valid.
   * 
   * @param email the email address to be checked
   * @return ture for valid, false otherwise
   */
  public static boolean isValidEmail(String email) {
    if (Strings.isNullOrEmpty(email)) {
      return false;
    }
    return email.matches(EMAIL_REGEX);
  }

  /**
   * Parses an email, and return its domain. If no '@' found, the whole string will be recognized as
   * domain.
   * 
   * @param email the email to be parsed
   * @return domain part of the email
   */
  public static String getDomain(String email) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(email));
    if (email.indexOf("@") < 0) {
      return email.toLowerCase();
    }
    Preconditions.checkArgument(isValidEmail(email));
    return email.split("@")[1].toLowerCase();
  }
}
