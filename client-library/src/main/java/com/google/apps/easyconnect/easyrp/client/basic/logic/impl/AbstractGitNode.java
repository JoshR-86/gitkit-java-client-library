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

package com.google.apps.easyconnect.easyrp.client.basic.logic.impl;

import com.google.apps.easyconnect.easyrp.client.basic.logic.GitNode;
import com.google.common.base.Preconditions;

public abstract class AbstractGitNode implements GitNode {
  private String id;
  private boolean leaf;
  private Class<?> requestClass;

  public AbstractGitNode(String id, boolean leaf, Class<?> requestClass) {
    Preconditions.checkNotNull(id);
    Preconditions.checkNotNull(requestClass);
    this.id = id;
    this.leaf = leaf;
    this.requestClass = requestClass;
  }

  public String getId() {
    return id;
  }

  public boolean isLeaf() {
    return leaf;
  }

  public Class<?> getRequestClass() {
    return this.requestClass;
  }
}
