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

import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.Maps;

/**
 * A builder class to create JSON response for the Legacy Login RPC handler.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class LegacySigninJsonResponseBuilder {
  public static enum Status {
    OK("ok"), PASSWORD_ERROR("passwordError"), FEDERATED("federated"), EMAIL_NOT_EXIST(
        "emailNotExist");

    private String jsonString;

    private Status(String jsonString) {
      this.jsonString = jsonString;
    }

    public String toJsonString() {
      return this.jsonString;
    }
  }

  /**
   * Enumerates the fields in the JSON response.
   */
  protected static enum JsonField {
    STATUS, IDP, DISPLAY_NAME, PHOTO_URL
  }

  /**
   * A map holds the value for each field.
   */
  protected Map<JsonField, Object> values = Maps.newEnumMap(JsonField.class);

  /**
   * Builds the JSON format response string.
   * 
   * @return the JSON format response string
   * @throws JSONException if error occurs when building the JSONObject
   */
  public String build() throws JSONException {
    JSONObject result = new JSONObject();
    Iterator<JsonField> iterator = values.keySet().iterator();
    while (iterator.hasNext()) {
      JsonField key = iterator.next();
      Object value = values.get(key);
      if (value != null) {
        switch (key) {
          case STATUS: {
            Status status = (Status) value;
            result.put("status", status.toJsonString());
            break;
          }
          case IDP: {
            result.put("idp", value);
            break;
          }
          case DISPLAY_NAME: {
            result.put("displayName", value);
            break;
          }
          case PHOTO_URL: {
            result.put("photoUrl", value);
            break;
          }
        }
      }
    }
    return result.toString();
  }

  /**
   * Clears the builder.
   * 
   * @return the builder itself
   */
  public LegacySigninJsonResponseBuilder reset() {
    values.clear();
    return this;
  }

  /**
   * Sets the registered field.
   * 
   * @param registered whether the user is registered
   * @return the builder itself
   */
  public LegacySigninJsonResponseBuilder status(Status status) {
    if (status != null) {
      values.put(JsonField.STATUS, status);
    }
    return this;
  }

  /**
   * Sets the idp field.
   * 
   * @param idp whether the user is registered
   * @return the builder itself
   */
  public LegacySigninJsonResponseBuilder idp(String idp) {
    values.put(JsonField.IDP, idp);
    return this;
  }

  /**
   * Sets the displayName field.
   * 
   * @param displayName the display name of the user.
   * @return the builder itself
   */
  public LegacySigninJsonResponseBuilder displayName(String displayName) {
    values.put(JsonField.DISPLAY_NAME, displayName);
    return this;
  }

  /**
   * Sets the photoUrl field.
   * 
   * @param photoUrl the photo url of the user.
   * @return the builder itself
   */
  public LegacySigninJsonResponseBuilder photoUrl(String photoUrl) {
    values.put(JsonField.PHOTO_URL, photoUrl);
    return this;
  }
}
