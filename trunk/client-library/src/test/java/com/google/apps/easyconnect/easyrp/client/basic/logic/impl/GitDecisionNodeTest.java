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
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.google.apps.easyconnect.easyrp.client.basic.logic.GitNode;
import com.google.apps.easyconnect.easyrp.client.basic.logic.GitRule;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class GitDecisionNodeTest extends TestCase {
  class SomeEvaluator {
    boolean flag1 = false;
    boolean flag2 = false;
    boolean flag3 = false;

    public String decision1(String req) {
      flag1 = true;
      return Strings.isNullOrEmpty(req) ? "empty" : "unempty";
    }

    public String decision2(String req) {
      flag2 = true;
      return Strings.isNullOrEmpty(req) ? "c" : "d";
    }

    public String decision3(String req) {
      flag3 = true;
      return Strings.isNullOrEmpty(req) ? "e" : "f";
    }
  }

  private GitDecisionNode node;
  private SomeEvaluator someEvaluator;

  protected void setUp() throws Exception {
    someEvaluator = new SomeEvaluator();
    node = new GitDecisionNode("start", String.class, someEvaluator, "decision1");
  }

  protected void tearDown() throws Exception {
    node = null;
    someEvaluator = null;
  }

  public void testGitDecisionNode_illegal() {
    try {
      node = new GitDecisionNode("start", String.class, someEvaluator, "unknownMethod");
      fail("Should throw IllegalStateException or IllegalArgumentException if method is invlaid.");
    } catch (IllegalStateException e) {
    } catch (IllegalArgumentException e) {
    }
  }

  public void testFormatKey() {
    String key = "abc";
    Assert.assertEquals(key, GitDecisionNode.formatKey(key));
  }

  public void testFormatKey_lower() {
    String key = "ABC";
    Assert.assertEquals(key.toLowerCase(), GitDecisionNode.formatKey(key));
  }

  public void testFormatKey_null() {
    try {
      GitDecisionNode.formatKey(null);
      fail("Should throw NullPointerException or IllegalArgumentException if parametere is null");
    } catch (IllegalArgumentException e) {
    } catch (NullPointerException e) {
    }
  }

  public void testFormatKey_defaule() {
    String key = "default";
    Assert.assertEquals(key.toUpperCase(), GitDecisionNode.formatKey(key));
    key = "Default";
    Assert.assertEquals(key.toUpperCase(), GitDecisionNode.formatKey(key));
  }

  public void testAddChild() {
    String key = "someChild";
    GitNode child = new GitDecisionNode("id", String.class, someEvaluator, "decision2");
    node.addChild(key, child);
    Assert.assertTrue(node.hasChild(key));
  }

  public void testAddChild_nllKey() {
    String key = null;
    GitNode child = new GitDecisionNode("id", String.class, someEvaluator, "decision2");
    try {
      node.addChild(key, child);
      fail("Should throw NullPointerException when the key of child node is null.");
    } catch (NullPointerException e) {
    }
  }

  public void testAddChild_nullNode() {
    String key = "someChild";
    GitNode child = null;
    try {
      node.addChild(key, child);
      fail("Should throw NullPointerException when the child node is null.");
    } catch (NullPointerException e) {
    }
  }

  public void testHasChild_null() {
    Assert.assertFalse(node.hasChild(null));
  }

  public void testHasChild_notExist() {
    Assert.assertFalse(node.hasChild("notExist"));
  }

  public void testHasChild_caseinsensitive() {
    String key = "someChild";
    GitNode child = new GitDecisionNode("id", String.class, someEvaluator, "decision2");
    node.addChild(key, child);
    Assert.assertTrue(node.hasChild(key.toUpperCase()));
  }

  public void testAddDefaultChild() {
    GitNode child = new GitDecisionNode("id", String.class, someEvaluator, "decision2");
    node.addDefaultChild(child);
    Assert.assertTrue(node.hasChild("default"));
  }

  public void testHasChildren() {
    Assert.assertFalse(node.hasChildren());
    GitNode child = new GitDecisionNode("id", String.class, someEvaluator, "decision2");
    node.addDefaultChild(child);
    Assert.assertTrue(node.hasChildren());
  }

  public void testChildren() {
    GitNode child = new GitDecisionNode("id", String.class, someEvaluator, "decision2");
    node.addDefaultChild(child);
    Iterator<GitNode> it = node.children();
    Assert.assertTrue(it.hasNext());
    Assert.assertEquals(child, it.next());
    Assert.assertFalse(it.hasNext());
  }

  public void testChildren_empty() {
    Iterator<GitNode> it = node.children();
    Assert.assertFalse(it.hasNext());
  }

  public void testFindChild() {
    GitNode child = new GitDecisionNode("id", String.class, someEvaluator, "decision2");
    node.addChild("empty", child);
    GitNode child2 = new GitDecisionNode("id2", String.class, someEvaluator, "decision3");
    node.addChild("unempty", child2);
    Assert.assertEquals(child, node.findChild(""));
    Assert.assertEquals(child2, node.findChild("abc"));
  }

  public void testFindChild_default() {
    GitNode child = new GitDecisionNode("id", String.class, someEvaluator, "decision2");
    node.addDefaultChild(child);
    GitNode child2 = new GitDecisionNode("id2", String.class, someEvaluator, "decision3");
    node.addChild("unempty", child2);
    Assert.assertEquals(child, node.findChild(""));
    Assert.assertEquals(child2, node.findChild("abc"));
  }

  public void testExecute_nullRequest() {
    try {
      node.execute(null);
      fail("Should throw NullPointerException if the request is null.");
    } catch (NullPointerException e) {
    }
  }

  public void testAppendToRuleList() {
    List<GitRule> rules = Lists.newArrayList();
    node.appendToRuleList(rules, "pId", "pValue");
    Assert.assertEquals(1, rules.size());
    GitRule rule = rules.get(0);
    Assert.assertNotNull(rule);
    Assert.assertTrue(rule.getActionMethodNames() == null
        || rule.getActionMethodNames().length == 0);
    Assert.assertEquals("start", rule.getId());
    Assert.assertEquals("pId", rule.getParentId());
    Assert.assertEquals("pValue", rule.getParentValue());
    Assert.assertEquals("decision1", rule.getEvaluatorMethodName());
  }

}
