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

package com.google.apps.easyconnect.easyrp.client.basic.util;

import junit.framework.Assert;
import junit.framework.TestCase;

public class IdpUtilsTest extends TestCase {

  public void testIsValidUsername() {
    Assert.assertTrue(IdpUtils.isValidUsername("abc"));
    Assert.assertTrue(IdpUtils.isValidUsername("abc_"));
    Assert.assertTrue(IdpUtils.isValidUsername("abc.bc"));
    Assert.assertFalse(IdpUtils.isValidUsername(null));
    Assert.assertFalse(IdpUtils.isValidUsername(""));
    Assert.assertFalse(IdpUtils.isValidUsername(" "));
    Assert.assertFalse(IdpUtils.isValidUsername("."));
    Assert.assertFalse(IdpUtils.isValidUsername("a."));
    Assert.assertFalse(IdpUtils.isValidUsername(".b"));
    Assert.assertFalse(IdpUtils.isValidUsername("a b"));
  }

  public void testIsValidEmail() {
    Assert.assertTrue(IdpUtils.isValidEmail("abc@c.com"));
    Assert.assertTrue(IdpUtils.isValidEmail("abc_@abc.com"));
    Assert.assertTrue(IdpUtils.isValidEmail("abc.bc@a.com"));
    Assert.assertFalse(IdpUtils.isValidEmail(null));
    Assert.assertFalse(IdpUtils.isValidEmail(""));
    Assert.assertFalse(IdpUtils.isValidEmail(" "));
    Assert.assertFalse(IdpUtils.isValidEmail("."));
    Assert.assertFalse(IdpUtils.isValidEmail("abc"));
    Assert.assertFalse(IdpUtils.isValidEmail("a@c"));
    Assert.assertFalse(IdpUtils.isValidEmail("a@c.c."));
    Assert.assertFalse(IdpUtils.isValidEmail("a@c."));
    Assert.assertFalse(IdpUtils.isValidEmail("a@.b.c."));
    Assert.assertFalse(IdpUtils.isValidEmail("a@b.c@d.e"));
    Assert.assertFalse(IdpUtils.isValidEmail("a@@b.c"));
    Assert.assertFalse(IdpUtils.isValidEmail("a @b.c"));
  }

  public void testGetDomain() {
    Assert.assertEquals("a.com", IdpUtils.getDomain("abc@a.com"));
    Assert.assertEquals("a.com", IdpUtils.getDomain("abc.def@a.com"));
  }

  public void testGetDomain_lowercase() {
    Assert.assertEquals("a.com", IdpUtils.getDomain("abc@A.com"));
    Assert.assertEquals("a.com", IdpUtils.getDomain("abc.def@a.COM"));
  }

  public void testGetDomain_invalid() {
    try {
      IdpUtils.getDomain(null);
      fail("getDomain should throw exception for invalid email.");
    } catch (Exception e) {
    }
    try {
      IdpUtils.getDomain("abc@def.d.");
      fail("getDomain should throw exception for invalid email.");
    } catch (Exception e) {
    }
  }

  public void testGetDomain_domain() {
    Assert.assertEquals("abc.com", IdpUtils.getDomain("abc.com"));
  }
}
