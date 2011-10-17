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
 * A builder class to create JSON response for the UserStatus RPC request.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class UserStatusJsonResponseBuilder {
  /**
   * Enumerates the fields in the JSON response.
   */
  protected static enum JsonField {
    REGISTERED, LEGACY, IDP, DISPLAY_NAME, PHOTO_URL
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
          case REGISTERED: {
            Boolean registered = (Boolean) value;
            result.put("registered", registered.booleanValue());
            break;
          }
          case LEGACY: {
            Boolean legacy = (Boolean) value;
            if (legacy != null && legacy.booleanValue()) {
              result.put("legacy", true);
            }
            break;
          }
          case IDP: {
            Boolean legacy = (Boolean) value;
            result.put("idp", legacy.booleanValue());
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
  public UserStatusJsonResponseBuilder reset() {
    values.clear();
    return this;
  }

  /**
   * Sets the registered field.
   * 
   * @param registered whether the user is registered
   * @return the builder itself
   */
  public UserStatusJsonResponseBuilder registered(boolean registered) {
    values.put(JsonField.REGISTERED, registered);
    return this;
  }

  /**
   * Sets the federated field.
   * 
   * @param federated whether the user should use federated login
   * @return the builder itself
   */
  public UserStatusJsonResponseBuilder legacy(boolean legacy) {
    values.put(JsonField.LEGACY, legacy);
    return this;
  }

  /**
   * Sets the idp field.
   * 
   * @param idp whether the user is registered
   * @return the builder itself
   */
  public UserStatusJsonResponseBuilder idp(String idp) {
    values.put(JsonField.IDP, idp);
    return this;
  }

  /**
   * Sets the displayName field.
   * 
   * @param displayName the display name of the user.
   * @return the builder itself
   */
  public UserStatusJsonResponseBuilder displayName(String displayName) {
    values.put(JsonField.DISPLAY_NAME, displayName);
    return this;
  }

  /**
   * Sets the photoUrl field.
   * 
   * @param photoUrl the photo url of the user.
   * @return the builder itself
   */
  public UserStatusJsonResponseBuilder photoUrl(String photoUrl) {
    values.put(JsonField.PHOTO_URL, photoUrl);
    return this;
  }
}
