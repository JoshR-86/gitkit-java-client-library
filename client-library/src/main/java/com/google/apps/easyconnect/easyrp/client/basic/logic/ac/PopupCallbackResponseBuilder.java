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

import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * A builder to construct Callback JSON response.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class PopupCallbackResponseBuilder {
  private static final Logger log = Logger.getLogger(PopupCallbackResponseBuilder.class.getName());
  private final String HTML_SUCCESS = "<script type=text/javascript"
      + " src='https://ajax.googleapis.com/jsapi'></script>\n" + "<script type='text/javascript'>"
      + "  google.load(\"identitytoolkit\", \"1.0\", {packages: [\"notify\"]});\n" + "</script>\n"
      + "<script type='text/javascript'>\n"
      + "  window.google.identitytoolkit.notifyFederatedSuccess(%1$s);\n" + "</script>";
  private final String HTML_ERROR = "<script type=text/javascript"
      + " src='https://ajax.googleapis.com/jsapi'></script>\n" + "<script type='text/javascript'>"
      + "  google.load(\"identitytoolkit\", \"1.0\", {packages: [\"notify\"]});\n" + "</script>\n"
      + "<script type='text/javascript'>\n"
      + "  window.google.identitytoolkit.notifyFederatedError(%1$s, %2$s);\n" + "</script>";

  /**
   * Create HTML response code for successful federate login.
   * @param registered whether the user is registered
   * @param email the email of the user
   * @param displayName the display name of the user. If you set a value here, the entry value in
   *        account chooser will be updated.
   * @param photoUrl the photo URL of the user. If you set a value here, the entry value in account
   *        chooser will be updated.
   * @return the HTML code to return to the pop-up page
   */
  public String createSuccess(boolean registered, String email, String displayName,
      String photoUrl) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(email));
    JSONObject result = new JSONObject();
    try {
      result.put("email", email);
      result.put("registered", registered);
      if (!Strings.isNullOrEmpty(displayName)) {
        result.put("displayName", displayName);
      }
      if (!Strings.isNullOrEmpty(photoUrl)) {
        result.put("photoUrl", photoUrl);
      }
    } catch (JSONException e) {
      log.severe(e.getMessage());
    }
    String html = String.format(HTML_SUCCESS, result.toString());
    return html;
  }

  /**
   * Creates the response HTML code for account mismatch.
   * @param validatedEmail the email returned from IDP
   * @param inputEmail the email user input
   * @param purpose the 'rp_purpose' parameter value set by widget
   * @return the HTML code to return to the pop-up page
   */
  public String createAccountMismatch(String validatedEmail, String inputEmail, String purpose) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(validatedEmail));
    Preconditions.checkArgument(!Strings.isNullOrEmpty(inputEmail));
    JSONObject result = new JSONObject();
    try {
      result.put("validatedEmail", validatedEmail);
      result.put("inputEmail", inputEmail);
      if (!Strings.isNullOrEmpty(purpose)) {
        result.put("purpose", purpose);
      }
    } catch (JSONException e) {
      log.severe(e.getMessage());
    }
    String html = String.format(HTML_ERROR, "'accountMismatch'", result.toString());
    return html;
  }

  /**
   * Creates the response HTML code for invalid assertion error.
   * @return the HTML code to return to the pop-up page
   */
  public String createInvalidAssertion() {
    return createError("invalidAssertion");
  }

  /**
   * Creates the response HTML code if the IDP is not the email's Email provider.
   * @return the HTML code to return to the pop-up page
   */
  public String createInvalidAssertionEmail() {
    return createError("invalidAssertionEmail");
  }

  /**
   * Creates the response HTML code for unknown error.
   * @return the HTML code to return to the pop-up page
   */
  public String createUnknowError() {
    return createError(null);
  }

  private String createError(String errorType) {
    String strType;
    if (Strings.isNullOrEmpty(errorType)) {
      strType = "undefined";
    } else {
      strType = "'" + errorType + "'";
    }
    String html = String.format(HTML_ERROR, strType, "{}");
    return html;
  }
}
