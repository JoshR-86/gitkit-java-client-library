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

package com.google.apps.easyconnect.easyrp.client.basic.servlet;

/**
 * Some string constants for the returned content type.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public final class ContentType {

  /**
   * String literal for servlet returned content type.
   */
  public static final String JSON = "text/json";

  /**
   * String literal for servlet returned content type.
   */
  public static final String TEXT = "text/plain";

  /**
   * String literal for servlet returned content type.
   */
  public static final String HTML = "text/html";

  /* Cannot create instance */
  private ContentType() {
  }
}
