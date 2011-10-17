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

import org.json.JSONException;

import com.google.apps.easyconnect.easyrp.client.basic.data.Account;
import com.google.apps.easyconnect.easyrp.client.basic.logic.common.GitRequest;
import com.google.apps.easyconnect.easyrp.client.basic.servlet.ContentType;

/**
 * Defines the actions for the UserStatus handler.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class UserStatusAction {
  private static final Logger log = Logger.getLogger(UserStatusAction.class.getName());
  private boolean returnProfileInfo;

  /**
   * Constructs an action object instance.
   * @param returnProfileInfo whether return DisplaName/PhotoUrl in response.
   */
  public UserStatusAction(boolean returnProfileInfo) {
    this.returnProfileInfo = returnProfileInfo;
  }

  public boolean isReturnProfileInfo() {
    return returnProfileInfo;
  }

  private UserStatusJsonResponseBuilder createResponseBuilder() {
    return new UserStatusJsonResponseBuilder();
  }

  private void send(String response, GitRequest request) throws IOException {
    request.getHttpServletResponse().setContentType(ContentType.JSON);
    request.getHttpServletResponse().getWriter().print(response);
  }

  private void fillProfileInfo(UserStatusJsonResponseBuilder builder, GitRequest request) {
    if (returnProfileInfo) {
      Account account = request.getAccountInDB();
      if (account != null) {
        builder.displayName(account.getDisplayName()).photoUrl(account.getPhotoUrl());
      }
    }
  }

  /**
   * Sends JSON response to widget to indicate an unknown error.
   * <p>
   * This method returns a response that without 'registered' field. This is recognized as as
   * illegal response in the widget.
   * @param request the login request object
   * @throws JSONException if error occurs when generates the JSON response
   * @throws IOException if error occurs when send back response
   */
  public void sendError(GitRequest request) throws JSONException, IOException {
    String response = createResponseBuilder().build();
    log.info("UserStatus response: " + response);
    send(response, request);
  }

  /**
   * Sends JSON response to widget to indicate a registered federated account.
   * @param request the login request object
   * @throws JSONException if error occurs when generates the JSON response
   * @throws IOException if error occurs when send back response
   */
  public void sendRegistered(GitRequest request) throws JSONException, IOException {
    UserStatusJsonResponseBuilder builder = createResponseBuilder().registered(true);
    fillProfileInfo(builder, request);
    String response = builder.build();
    log.info("UserStatus response: " + response);
    send(response, request);
  }

  /**
   * Sends JSON response to widget to indicate a registered legacy account.
   * @param request the login request object
   * @throws JSONException if error occurs when generates the JSON response
   * @throws IOException if error occurs when send back response
   */
  public void sendRegisteredLegacy(GitRequest request) throws JSONException, IOException {
    UserStatusJsonResponseBuilder builder = createResponseBuilder().registered(true).legacy(true);
    fillProfileInfo(builder, request);
    String response = builder.build();
    log.info("UserStatus response: " + response);
    send(response, request);
  }

  /**
   * Sends JSON response to widget to indicate a unregistered account.
   * <p>
   * When the {@code useLocalIdpWhiteList} is set to {@code true}, the response indicates that
   * federated login should be used for this email. When the {@code useLocalIdpWhiteList} is set to
   * {@code false}, the RP server side don't check the domain type of the email. The widget should
   * check email's domain type by {@code tryFederatedFirst}. <br/>
   * See {@code IdpWhiteList} and {@code GitLogicBuilderFactory} for more detail.
   * @param request the login request object
   * @throws JSONException if error occurs when generates the JSON response
   * @throws IOException if error occurs when send back response
   */
  public void sendUnregistered(GitRequest request) throws JSONException, IOException {
    String response = createResponseBuilder().registered(false).build();
    log.info("UserStatus response: " + response);
    send(response, request);
  }

  /**
   * Sends JSON response to widget to indicate a unregistered legacy account.
   * <p>
   * This response can be used only when the {@code useLocalIdpWhiteList} is set to {@code true}.
   * See {@code IdpWhiteList} and {@code GitLogicBuilderFactory} for more detail.
   * @param request the login request object
   * @throws JSONException if error occurs when generates the JSON response
   * @throws IOException if error occurs when send back response
   */
  public void sendUnregisteredLegacy(GitRequest request) throws JSONException, IOException {
    String response = createResponseBuilder().registered(false).legacy(true).build();
    log.info("UserStatus response: " + response);
    send(response, request);
  }
}
