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

package com.google.apps.easyconnect.easyrp.client.basic.logic;

import java.util.List;

public interface GitNode {
  /**
   * Evaluate the request to find the suitable child {@code GitNode}.
   * 
   * @param request a wrapped request object.
   */
  void execute(Object request);

  /**
   * Transforms the node to an array of rules.
   * 
   * @param parentId The id of the parent node.
   * @param parentValue The key of this node in children map of parent node.
   * 
   */
  void appendToRuleList(List<GitRule> rules, String parentId, String parentValue);
}
