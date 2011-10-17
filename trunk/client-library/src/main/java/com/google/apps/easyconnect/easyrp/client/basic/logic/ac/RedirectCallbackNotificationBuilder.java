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
public class RedirectCallbackNotificationBuilder {
  private static final Logger log =
      Logger.getLogger(RedirectCallbackNotificationBuilder.class.getName());

  /**
   * Creates the response HTML code for account mismatch.
   * @param validatedEmail the email returned from IDP
   * @param inputEmail the email user input
   * @param purpose the 'rp_purpose' parameter value set by widget
   * @return the HTML code to return to the pop-up page
   */
  public JSONObject createAccountMismatch(String validatedEmail, String inputEmail,
      String purpose) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(validatedEmail));
    Preconditions.checkArgument(!Strings.isNullOrEmpty(inputEmail));
    JSONObject result = createError("accountMismatch");
    try {
      result.put("validatedEmail", validatedEmail);
      result.put("inputEmail", inputEmail);
      if (!Strings.isNullOrEmpty(purpose)) {
        result.put("purpose", purpose);
      }
    } catch (JSONException e) {
      log.severe(e.getMessage());
    }
    return result;
  }

  /**
   * Creates the response HTML code for invalid assertion error.
   * @return the HTML code to return to the pop-up page
   */
  public JSONObject createInvalidAssertion() {
    return createError("invalidAssertion");
  }

  /**
   * Creates the response HTML code if the IDP is not the email's Email provider.
   * @return the HTML code to return to the pop-up page
   */
  public JSONObject createInvalidAssertionEmail() {
    return createError("invalidAssertionEmail");
  }

  /**
   * Creates the response HTML code for unknown error.
   * @return the HTML code to return to the pop-up page
   */
  public JSONObject createUnknowError() {
    return createError(null);
  }

  private JSONObject createError(String errorType) {
    JSONObject result = new JSONObject();
    if (!Strings.isNullOrEmpty(errorType)) {
      try {
        result.put("errorType", errorType);
      } catch (JSONException e) {
        log.severe(e.getMessage());
      }
    }
    return result;
  }
}
