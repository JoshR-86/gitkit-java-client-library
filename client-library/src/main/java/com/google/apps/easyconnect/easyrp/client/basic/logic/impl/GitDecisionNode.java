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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.apps.easyconnect.easyrp.client.basic.logic.GitNode;
import com.google.apps.easyconnect.easyrp.client.basic.logic.GitRule;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public class GitDecisionNode extends AbstractGitNode {
  private static final Logger log = Logger.getLogger(GitDecisionNode.class.getName());
  private static final String DEFAULT_CHILD_KEY = "DEFAULT";

  private Object evaluatorObject;
  private Method evaluator;
  private Map<String, GitNode> children = Maps.newHashMap();

  public GitDecisionNode(String id, Class<?> requestClass, Object evaluatorObject,
      String evaluatorMethod) {
    super(id, false, requestClass);
    Preconditions.checkNotNull(evaluatorObject);
    Preconditions.checkNotNull(evaluatorMethod);
    this.evaluator = ClassUtil.getMethod(evaluatorObject.getClass(), evaluatorMethod, requestClass);
    if (this.evaluator == null) {
      String msg = "Illegal Tree Statues: Failed to find [" + evaluatorMethod + "] on class ["
          + evaluatorObject.getClass().getSimpleName() + "].";
      log.severe(msg);
      throw new IllegalStateException(msg);
    }
    this.evaluatorObject = evaluatorObject;
  }

  public static String formatKey(String key) {
    Preconditions.checkNotNull(key);
    if (DEFAULT_CHILD_KEY.equalsIgnoreCase(key)) {
      return DEFAULT_CHILD_KEY;
    } else {
      return key.toLowerCase();
    }
  }

  public void addChild(String key, GitNode node) {
    Preconditions.checkNotNull(key);
    Preconditions.checkNotNull(node);
    children.put(formatKey(key), node);
  }

  public boolean hasChild(String key) {
    return key != null && children.containsKey(formatKey(key));
  }

  public void addDefaultChild(GitNode node) {
    children.put(DEFAULT_CHILD_KEY, node);
  }

  public boolean hasChildren() {
    return !this.children.isEmpty();
  }

  public Iterator<GitNode> children() {
    return children.values().iterator();
  }

  @VisibleForTesting
  GitNode findChild(Object request) {
    String key = null;
    GitNode child = null;
    try {
      key = (String) evaluator.invoke(evaluatorObject, request);
    } catch (IllegalArgumentException e) {
      log.severe("Failed to evaluate node: " + e.getMessage());
    } catch (IllegalAccessException e) {
      log.severe("Failed to evaluate node: " + e.getMessage());
    } catch (InvocationTargetException e) {
      log.severe("Failed to evaluate node: " + e.getMessage());
    }
    if (key != null) {
      child = this.children.get(formatKey(key));
      if (child == null) {
        child = this.children.get(DEFAULT_CHILD_KEY);
      }
    } else {
      child = this.children.get(DEFAULT_CHILD_KEY);
    }
    if (child == null) {
      String msg = "Illegal Tree Statues: GitNode [" + this.evaluator.getName()
          + "] return an unknown value [" + key + "].";
      log.severe(msg);
      throw new IllegalStateException(msg);
    }
    return child;
  }

  @Override
  public void execute(Object request) {
    Preconditions.checkNotNull(request);
    if (!this.getRequestClass().isInstance(request)) {
      String msg = "Illegal Tree Statues: GitNode [" + this.evaluator.getName()
          + "] expects parameter type [" + this.getRequestClass().getName()
          + "], but actually it get [" + request.getClass().getName() + "].";
      log.severe(msg);
      throw new IllegalStateException(msg);
    }
    findChild(request).execute(request);
  }

  @Override
  public void appendToRuleList(List<GitRule> rules, String parentId, String parentValue) {
    rules.add(new GitRule(getId(), parentId, parentValue, false, this.evaluator.getName(), null));
    Iterator<String> it = this.children.keySet().iterator();
    while (it.hasNext()) {
      String key = it.next();
      children.get(key).appendToRuleList(rules, getId(), key);
    }
  }
}
