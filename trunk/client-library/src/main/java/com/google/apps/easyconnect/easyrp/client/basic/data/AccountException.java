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

/**
 * The {@code Exception} to indicate the errors in {@code AccountService}.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class AccountException extends Exception {
  private static final long serialVersionUID = 1L;

  /**
   * The account already exist.
   */
  public static final String ACCOUNT_ALREADY_EXIST = "ACCOUNT_ALREADY_EXIST";

  /**
   * Cannot find the account.
   */
  public static final String ACCOUNT_NOT_FOUND = "ACCOUNT_NOT_FOUND";

  /**
   * Cannot apply the action on the account.
   */
  public static final String ACTION_NOT_ALLOWED = "ACTION_NOT_ALLOWED";

  /**
   * Unknown error.
   */
  public static final String UNKNOWN_ERROR = "UNKNOWN_ERROR";

  private String errorCode;

  /**
   * Construct a {@code AccountException} instance with unknown error type.
   */
  public AccountException() {
    this.errorCode = UNKNOWN_ERROR;
  }

  /**
   * Construct a {@code AccountException} instance with the error type.
   * 
   * @param errorCode the error type
   */
  public AccountException(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorCode() {
    return errorCode;
  }
}
