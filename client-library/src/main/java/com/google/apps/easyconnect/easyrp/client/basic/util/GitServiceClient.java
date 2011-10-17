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

package com.google.apps.easyconnect.easyrp.client.basic.util;

import org.json.JSONObject;

/**
 * Wraps the request to the GITKit service or similar.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public interface GitServiceClient {
  /**
   * Verifying the response of IDP, and return the profile data if success.
   * 
   * @param requestUri the request URI of the IDP response.
   * @param postBody the post data of the IDP response.
   * @return the profile data of the user, or nothing in it if failed.
   */
  JSONObject verifyResponse(String requestUri, String postBody);
}
