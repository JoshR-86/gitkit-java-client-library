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

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.google.apps.easyconnect.easyrp.client.basic.logic.GitRule;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class GitActionNodeTest extends TestCase {
  class SomeAction {
    boolean flag1 = false;
    boolean flag2 = false;
    boolean flag3 = false;

    public void action1(String req) {
      flag1 = true;
    }

    public void action2(String req) {
      flag2 = true;
    }

    public void action3(String req) {
      flag3 = true;
    }
  }

  private GitActionNode node;
  private SomeAction someAction;

  protected void setUp() throws Exception {
    someAction = new SomeAction();
    node = new GitActionNode("testId", String.class, someAction);
  }

  protected void tearDown() throws Exception {
    node = null;
    someAction = null;
  }

  public void testAddAction() {
    node.addAction("action1");
    Assert.assertEquals(1, node.getActions().size());
    Assert.assertEquals("action1", node.getActions().get(0));
  }

  public void testAddAction_notExist() {
    try {
      node.addAction("noaction");
      fail("Should throw Exception when method is not found");
    } catch (Exception e) {
    }
    Assert.assertEquals(0, node.getActions().size());
  }

  public void testAddActions() {
    node.addActions(new String[] { "action1", "action2", "action3" });
    Assert.assertEquals(3, node.getActions().size());
    Assert.assertEquals("action1", node.getActions().get(0));
    Assert.assertEquals("action2", node.getActions().get(1));
    Assert.assertEquals("action3", node.getActions().get(2));
  }

  public void testExecute() {
    node.addActions(new String[] { "action1", "action3" });
    node.execute("");
    Assert.assertTrue(someAction.flag1);
    Assert.assertFalse(someAction.flag2);
    Assert.assertTrue(someAction.flag3);
  }

  public void testAppendToRuleList() {
    node.addActions(new String[] { "action1", "action3" });
    List<GitRule> rules = Lists.newArrayList();
    node.appendToRuleList(rules, "pId", "pValue");
    Assert.assertEquals(1, rules.size());
    GitRule rule = rules.get(0);
    Assert.assertNotNull(rule);
    Assert.assertTrue(Strings.isNullOrEmpty(rule.getEvaluatorMethodName()));
    Assert.assertEquals("testId", rule.getId());
    Assert.assertEquals("pId", rule.getParentId());
    Assert.assertEquals("pValue", rule.getParentValue());
    Assert.assertEquals(2, rule.getActionMethodNames().length);
    Assert.assertEquals("action1", rule.getActionMethodNames()[0]);
    Assert.assertEquals("action3", rule.getActionMethodNames()[1]);
  }

}
