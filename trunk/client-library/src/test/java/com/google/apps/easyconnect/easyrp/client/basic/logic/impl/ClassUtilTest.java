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

import java.lang.reflect.Method;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ClassUtilTest extends TestCase {
  static class AbstractRequest {
  }

  static class SomeRequest extends AbstractRequest {
    public void childMethod() {
    }
  }

  static class Parent {
    public void parentMethod0(AbstractRequest req) {
    }

    public void parentMethod1(SomeRequest req) {
    }

    public void parentMethod2(String req) {
    }
  }

  static class Child extends Parent {
    public void childMethod0(AbstractRequest req) {
    }

    public void childMethod1(SomeRequest req) {
    }

    public void childMethod2() {
    }
  }

  public void testGetMethod_notExist() {
    Method method = ClassUtil.getMethod(Child.class, "noExistMethod", SomeRequest.class);
    Assert.assertNull(method);
  }

  public void testGetMethod_childWithChild() {
    Method method = ClassUtil.getMethod(Child.class, "childMethod1", SomeRequest.class);
    Assert.assertNotNull(method);
  }

  public void testGetMethod_childWithParent() {
    Method method = ClassUtil.getMethod(Child.class, "childMethod0", SomeRequest.class);
    Assert.assertNotNull(method);
  }

  public void testGetMethod_parentWithChild() {
    Method method = ClassUtil.getMethod(Parent.class, "parentMethod1", SomeRequest.class);
    Assert.assertNotNull(method);
  }

  public void testGetMethod_parentWithParent() {
    Method method = ClassUtil.getMethod(Parent.class, "parentMethod0", SomeRequest.class);
    Assert.assertNotNull(method);
  }

  public void testGetMethod_childWrongParameter() {
    Method method = ClassUtil.getMethod(Child.class, "childMethod2", SomeRequest.class);
    Assert.assertNull(method);
  }

  public void testGetMethod_parentWrongParameter() {
    Method method = ClassUtil.getMethod(Parent.class, "noExistMethod", SomeRequest.class);
    Assert.assertNull(method);
  }

}
