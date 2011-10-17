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

import java.util.Arrays;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class GitRule {
  private static final Logger log = Logger.getLogger(GitRule.class.getName());
  private String id;
  private String parentId;
  private String parentValue;
  private boolean leaf;
  private String evaluatorMethodName;
  private String[] actionMethodNames;

  public GitRule(String id, String parentId, String parentValue, boolean leaf,
      String evaluatorMethodName, String[] actionMethodNames) {
    if (leaf) {
      Preconditions.checkArgument(evaluatorMethodName == null);
    } else {
      Preconditions.checkArgument(actionMethodNames == null);
    }
    this.id = id;
    this.parentId = parentId;
    this.parentValue = parentValue;
    this.leaf = leaf;
    this.evaluatorMethodName = evaluatorMethodName;
    this.actionMethodNames = actionMethodNames;
  }

  public String getId() {
    return id;
  }

  public String getParentId() {
    return parentId;
  }

  public String getParentValue() {
    return parentValue;
  }

  public boolean isLeaf() {
    return leaf;
  }

  public String getEvaluatorMethodName() {
    return evaluatorMethodName;
  }

  public String[] getActionMethodNames() {
    return actionMethodNames;
  }

  public JSONArray toJson() {
    JSONArray json = new JSONArray();
    try {
      JSONObject name = new JSONObject();
      json.put(name);
      name.put("v", this.id);
      if (Strings.isNullOrEmpty(parentId)) {
        name.put("f", "<div class=\"switch\">" + this.evaluatorMethodName + "</div>");
        json.put("");
      } else if (this.isLeaf()) {
        name.put("f", "<div class=\"condition\">" + this.parentValue
            + "</div><div class=\"action\">" + Arrays.toString(this.actionMethodNames) + "</div>");
        json.put(this.parentId);
      } else {
        name.put("f", "<div class=\"condition\">" + this.parentValue
            + "</div><div class=\"switch\">" + this.evaluatorMethodName + "</div>");
        json.put(this.parentId);
      }
      json.put(this.id);
    } catch (JSONException e) {
      log.severe(e.getMessage());
    }
    return json;
  }
}
