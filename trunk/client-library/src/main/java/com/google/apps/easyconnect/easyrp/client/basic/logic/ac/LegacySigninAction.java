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

import com.google.apps.easyconnect.easyrp.client.basic.Context;
import com.google.apps.easyconnect.easyrp.client.basic.logic.ac.LegacySigninJsonResponseBuilder.Status;
import com.google.apps.easyconnect.easyrp.client.basic.logic.common.GitLoginRequest;
import com.google.apps.easyconnect.easyrp.client.basic.servlet.ContentType;
import com.google.common.base.Preconditions;

/**
 * Defines the actions for the legacy login RPC handler.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class LegacySigninAction {
  private static final Logger log = Logger.getLogger(LegacySigninAction.class.getName());
  private boolean returnProfileInfo;

  /**
   * Constructs an action object instance.
   * @param returnProfileInfo whether return DisplaName/PhotoUrl in response.
   */
  public LegacySigninAction(boolean returnProfileInfo) {
    this.returnProfileInfo = returnProfileInfo;
  }

  public boolean isReturnProfileInfo() {
    return returnProfileInfo;
  }

  private LegacySigninJsonResponseBuilder createResponseBuilder() {
    return new LegacySigninJsonResponseBuilder();
  }

  private void send(String response, GitLoginRequest request) throws IOException {
    request.getHttpServletResponse().setContentType(ContentType.JSON);
    request.getHttpServletResponse().getWriter().print(response);
  }

  /**
   * Sends JSON response to widget to indicate the email is unregistered.
   * @param request the login request object
   * @throws JSONException if error occurs when generates the JSON response
   * @throws IOException if error occurs when send back response
   */
  public void sendEmailNotExist(GitLoginRequest request) throws JSONException, IOException {
    String response = createResponseBuilder().status(Status.EMAIL_NOT_EXIST).build();
    log.info("LegacySignin response: " + response);
    send(response, request);
  }

  /**
   * Sends JSON response to widget to indicate the widget should use federated login for this email.
   * @param request the login request object
   * @throws JSONException if error occurs when generates the JSON response
   * @throws IOException if error occurs when send back response
   */
  public void sendFederated(GitLoginRequest request) throws JSONException, IOException {
    String response = createResponseBuilder().status(Status.FEDERATED).build();
    log.info("LegacySignin response: " + response);
    send(response, request);
  }

  /**
   * Sends JSON response to widget to indicate the password is incorrect.
   * @param request the login request object
   * @throws JSONException if error occurs when generates the JSON response
   * @throws IOException if error occurs when send back response
   */
  public void sendPasswordError(GitLoginRequest request) throws JSONException, IOException {
    String response = createResponseBuilder().status(Status.PASSWORD_ERROR).build();
    send(response, request);
  }

  /**
   * Sends JSON response to widget to indicate that user logged in successfully.
   * @param request the login request object
   * @throws JSONException if error occurs when generates the JSON response
   * @throws IOException if error occurs when send back response
   */
  public void sendOK(GitLoginRequest request) throws JSONException, IOException {
    String response = createResponseBuilder().status(Status.OK).build();
    log.info("LegacySignin response: " + response);
    send(response, request);
  }

  /**
   * Sets the current user to logged in status.
   * <p>
   * When config the logic tree, you shuold put this action before sendXXX action.
   * @param request the login request object
   * @throws IOException if error occurs when send back response
   */
  public void setLoggedIn(GitLoginRequest request) throws IOException {
    Preconditions.checkArgument(request.getAccountInDB() != null);
    Context.getSessionManager().setSessionAccount(request.getHttpServletRequest(),
        request.getHttpServletResponse(), request.getAccountInDB());
    log.info("LegacySignin response: set user logged in.");
  }
}
