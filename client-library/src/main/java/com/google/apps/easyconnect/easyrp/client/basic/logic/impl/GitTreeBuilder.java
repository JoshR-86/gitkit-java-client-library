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

import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import com.google.apps.easyconnect.easyrp.client.basic.logic.GitLogicBuilder;
import com.google.apps.easyconnect.easyrp.client.basic.logic.GitNode;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

public class GitTreeBuilder implements GitLogicBuilder {
  private static final Logger log = Logger.getLogger(GitTreeBuilder.class.getName());
  private Class<?> requestClass;
  private Object evaluatorObject;
  private Object actionObject;

  private boolean built;
  private Map<String, GitNode> nodes;
  private GitNode root;

  public GitTreeBuilder(Class<?> requestClass, Object evaluatorObject, Object actionObject) {
    this.requestClass = requestClass;
    this.evaluatorObject = evaluatorObject;
    this.actionObject = actionObject;
    this.nodes = Maps.newHashMap();
    this.built = false;
  }

  public Class<?> getRequestClass() {
    return requestClass;
  }

  public Object getEvaluatorObject() {
    return evaluatorObject;
  }

  public Object getActionObject() {
    return actionObject;
  }

  private GitDecisionNode createDecisionNode(String id, String evaluatorMethodName) {
    return new GitDecisionNode(id, requestClass, evaluatorObject, evaluatorMethodName);
  }

  private GitActionNode createActionNode(String id, String[] actionMethodNames) {
    GitActionNode node = new GitActionNode(id, requestClass, actionObject);
    node.addActions(actionMethodNames);
    return node;
  }

  private void checkBuildStatus() {
    if (this.built) {
      String msg = "Failed to build tree: cannot create node after build() is called!";
      log.severe(msg);
      throw new IllegalStateException(msg);
    }
  }

  private void checkNodeId(String id) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(id),
        "Failed to build tree: Node id cannot be empty!");
    Preconditions.checkArgument(!nodes.containsKey(id), "Failed to build tree: Node id [" + id
        + "] duplicated!");
  }

  private void checkParentNodeId(String parentId) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(parentId),
        "Failed to build tree: Parent node id cannot be empty!");
    Preconditions.checkArgument(nodes.containsKey(parentId),
        "Failed to build tree: Cannot find parent node [" + parentId
            + "]! Should define parent firstly.");
    Preconditions.checkArgument(nodes.get(parentId) instanceof GitDecisionNode,
        "Failed to build tree: Parent node [" + parentId + "] is not a decision node!");
  }

  public GitTreeBuilder start(String id, String evaluatorMethodName) {
    checkBuildStatus();
    checkNodeId(id);
    if (root != null) {
      String msg = "Failed to build tree: Tree root duplicated!";
      log.severe(msg);
      throw new IllegalStateException(msg);
    }
    root = createDecisionNode(id, evaluatorMethodName);
    nodes.put(id, root);
    return this;
  }

  private void appendToParentNode(GitNode node, String parentId, String parentValue) {
    GitDecisionNode parent = (GitDecisionNode) nodes.get(parentId);
    if (parent.hasChild(parentValue)) {
      String msg = "Failed to build tree: the value [" + parentId + "] of [" + parentValue
          + "] are duplicated!";
      log.severe(msg);
      throw new IllegalStateException(msg);
    }
    parent.addChild(parentValue, node);
  }

  public GitTreeBuilder decision(String id, String parentId, String parentValue,
      String evaluatorMethodName) {
    checkBuildStatus();
    checkNodeId(id);
    checkParentNodeId(parentId);
    GitDecisionNode node = createDecisionNode(id, evaluatorMethodName);
    appendToParentNode(node, parentId, parentValue);
    nodes.put(id, node);
    return this;
  }

  public GitTreeBuilder leaf(String id, String parentId, String parentValue,
      String[] actionMethodNames) {
    checkBuildStatus();
    checkNodeId(id);
    checkParentNodeId(parentId);
    GitActionNode node = createActionNode(id, actionMethodNames);
    appendToParentNode(node, parentId, parentValue);
    nodes.put(id, node);
    return this;
  }

  private void checkNode(GitNode node) {
    if (node instanceof GitDecisionNode) {
      GitDecisionNode decisionNode = (GitDecisionNode) node;
      if (!decisionNode.hasChildren()) {
        String msg = "Failed to build tree: node [" + decisionNode.getId() + "] has no child!";
        log.severe(msg);
        throw new IllegalStateException(msg);
      }
      Iterator<GitNode> children = decisionNode.children();
      while (children.hasNext()) {
        checkNode(children.next());
      }
    }
  }

  private boolean checkTree() {
    checkNode(root);
    return true;
  }

  public GitNode build() {
    if (this.built) {
      String msg = "Failed to build tree: cannot call build() twice!";
      log.severe(msg);
      throw new IllegalStateException(msg);
    }
    if (this.root == null) {
      String msg = "Failed to build tree: the tree is empty!";
      log.severe(msg);
      throw new IllegalStateException(msg);
    }
    if (!checkTree()) {
      String msg = "Failed to build tree: Tree has error!";
      log.severe(msg);
      throw new IllegalStateException(msg);
    }
    this.built = true;
    return root;
  }
}
