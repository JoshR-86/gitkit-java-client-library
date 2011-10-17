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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * Defines the callback request object.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class GitCallbackRequest extends GitRequest {
  private JSONObject idpAssertion;
  private String purpose;
  private String inputEmail;
  private String requestUri;

  public GitCallbackRequest(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, String requestUri) {
    super(httpServletRequest, httpServletResponse);
    this.requestUri = requestUri;
  }

  public GitCallbackRequest(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse) {
    super(httpServletRequest, httpServletResponse);
  }

  public JSONObject getIdpAssertion() {
    return idpAssertion;
  }

  public void setIdpAssertion(JSONObject idpAssertion) {
    this.idpAssertion = idpAssertion;
  }

  public String getPurpose() {
    return purpose;
  }

  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }

  public String getInputEmail() {
    return inputEmail;
  }

  public void setInputEmail(String inputEmail) {
    this.inputEmail = inputEmail;
  }

  public String getRequestUri() {
    return requestUri;
  }

  public void setRequestUri(String requestUri) {
    this.requestUri = requestUri;
  }
}
