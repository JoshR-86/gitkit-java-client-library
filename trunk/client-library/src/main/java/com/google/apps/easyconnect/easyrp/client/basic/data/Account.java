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
 * An account in RP. RP must provide an implementation for this interface.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public interface Account {

  /**
   * Whether the account is a federated account.
   * 
   * @return {@code true} if the account is a federated account, {@code false} otherwise
   */
  public boolean isFederated();

  /**
   * The email address of the account.
   * 
   * @return the email address of the account
   */
  public String getEmail();

  /**
   * The display name of the account.
   * 
   * @return the display name of the account.
   */
  public String getDisplayName();

  /**
   * The photo url of the account. If return {@code null} or empty string, the account chooser
   * widget will use its default user photo for this account.
   * 
   * @return the photo url of the account
   */
  public String getPhotoUrl();
}
