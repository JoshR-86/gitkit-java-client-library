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
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import com.google.apps.easyconnect.easyrp.client.basic.logic.GitRule;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class GitActionNode extends AbstractGitNode {
  private static final Logger log = Logger.getLogger(GitActionNode.class.getName());
  private Object actionObject;
  private List<String> actions = Lists.newArrayList();
  private List<Method> methods = Lists.newArrayList();

  public GitActionNode(String id, Class<?> requestClass, Object actionObject) {
    super(id, true, requestClass);
    Preconditions.checkNotNull(actionObject);
    this.actionObject = actionObject;
  }

  public void addAction(String action) {
    Preconditions.checkNotNull(action);
    Method actionMethod = ClassUtil.getMethod(actionObject.getClass(), action, getRequestClass());
    if (actionMethod == null) {
      String msg = "Illegal Tree Statues: Failed to find [" + action + "("
          + getRequestClass().getSimpleName() + ")] on class ["
          + actionObject.getClass().getSimpleName() + "]. Note the parameter should be "
          + getRequestClass().getSimpleName() + " or its super classes.";
      log.severe(msg);
      throw new IllegalStateException(msg);
    }
    actions.add(action);
    methods.add(actionMethod);
  }

  public void addActions(String[] actions) {
    for (int i = 0; i < actions.length; i++) {
      addAction(actions[i]);
    }
  }

  public List<String> getActions() {
    return Collections.unmodifiableList(this.actions);
  }

  @Override
  public void execute(Object request) {
    for (int i = 0; i < methods.size(); i++) {
      try {
        methods.get(i).invoke(actionObject, request);
      } catch (IllegalArgumentException e) {
        log.severe(e.getMessage());
      } catch (IllegalAccessException e) {
        log.severe(e.getMessage());
      } catch (InvocationTargetException e) {
        log.severe(e.getMessage());
      }
    }
  }

  @Override
  public void appendToRuleList(List<GitRule> rules, String parentId, String parentValue) {
    String[] actionNames = new String[actions.size()];
    rules
        .add(new GitRule(getId(), parentId, parentValue, true, null, actions.toArray(actionNames)));
  }
}
