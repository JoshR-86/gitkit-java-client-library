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
      + "  window.google.identitytoolkit.notifyFederatedSuccess(%1$s, %2$s);\n" + "</script>";
  private final String HTML_ERROR = "<script type=text/javascript"
      + " src='https://ajax.googleapis.com/jsapi'></script>\n" + "<script type='text/javascript'>"
      + "  google.load(\"identitytoolkit\", \"1.0\", {packages: [\"notify\"]});\n" + "</script>\n"
      + "<script type='text/javascript'>\n"
      + "  window.google.identitytoolkit.notifyFederatedError(%1$s, %2$s, %3$s);\n" + "</script>";

  /**
   * Create HTML response code for successful federate login.
   * @param registered whether the user is registered
   * @param email the email of the user
   * @param displayName the display name of the user. If you set a value here, the entry value in
   *        account chooser will be updated.
   * @param photoUrl the photo URL of the user. If you set a value here, the entry value in account
   *        chooser will be updated.
   * @param keepPopup whether to keep the pop-up window or not.
   * @return the HTML code to return to the pop-up page
   */
  public String createSuccess(boolean registered, String email, String displayName,
      String photoUrl, boolean keepPopup) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(email));
    JSONObject result = new JSONObject();
    JSONObject options = new JSONObject();
    try {
      result.put("email", email);
      result.put("registered", registered);
      if (!Strings.isNullOrEmpty(displayName)) {
        result.put("displayName", displayName);
      }
      if (!Strings.isNullOrEmpty(photoUrl)) {
        result.put("photoUrl", photoUrl);
      }
      options.put("keepPopup", keepPopup);
    } catch (JSONException e) {
      log.severe(e.getMessage());
    }
    String html = String.format(HTML_SUCCESS, result.toString(), options.toString());
    return html;
  }

  /**
   * Creates the response HTML code for account mismatch.
   * @param validatedEmail the email returned from IDP
   * @param inputEmail the email user input
   * @param purpose the 'rp_purpose' parameter value set by widget
   * @param keepPopup whether to keep the pop-up window or not.
   * @return the HTML code to return to the pop-up page
   */
  public String createAccountMismatch(String validatedEmail, String inputEmail, String purpose,
      boolean keepPopup) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(validatedEmail));
    Preconditions.checkArgument(!Strings.isNullOrEmpty(inputEmail));
    JSONObject result = new JSONObject();
    JSONObject options = new JSONObject();
    try {
      result.put("validatedEmail", validatedEmail);
      result.put("inputEmail", inputEmail);
      if (!Strings.isNullOrEmpty(purpose)) {
        result.put("purpose", purpose);
      }
      options.put("keepPopup", keepPopup);
    } catch (JSONException e) {
      log.severe(e.getMessage());
    }
    String html = String.format(HTML_ERROR, "'accountMismatch'", result.toString(),
        options.toString());
    return html;
  }

  /**
   * Creates the response HTML code for invalid assertion error.
   * @param keepPopup whether to keep the pop-up window or not.
   * @return the HTML code to return to the pop-up page
   */
  public String createInvalidAssertion(boolean keepPopup) {
    return createError("invalidAssertion", keepPopup);
  }

  /**
   * Creates the response HTML code if the IDP is not the email's Email provider.
   * @param keepPopup whether to keep the pop-up window or not.
   * @return the HTML code to return to the pop-up page
   */
  public String createInvalidAssertionEmail(boolean keepPopup) {
    return createError("invalidAssertionEmail", keepPopup);
  }

  /**
   * Creates the response HTML code for unknown error.
   * @param keepPopup whether to keep the pop-up window or not.
   * @return the HTML code to return to the pop-up page
   */
  public String createUnknowError(boolean keepPopup) {
    return createError(null, keepPopup);
  }

  private String createError(String errorType, boolean keepPopup) {
    String strType;
    if (Strings.isNullOrEmpty(errorType)) {
      strType = "undefined";
    } else {
      strType = "'" + errorType + "'";
    }
    JSONObject options = new JSONObject();
    try {
      options.put("keepPopup", keepPopup);
    } catch (JSONException e) {
      log.severe(e.getMessage());
    }
    String html = String.format(HTML_ERROR, strType, "{}", options.toString());
    return html;
  }
}
