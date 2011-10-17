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

public class IdpWhiteListTest extends TestCase {

  public void testIdpWhiteList() {
    IdpWhiteList list = new IdpWhiteList();
    Assert.assertTrue(list.isFederatedDomain("gmail.com"));
  }

  public void testIdpWhiteListStringArray() {
    IdpWhiteList list = new IdpWhiteList("thisisanotexistdomain.com", "hehe.net");
    Assert.assertTrue(list.isFederatedDomain("thisisanotexistdomain.com"));
    Assert.assertTrue(list.isFederatedDomain("thisisanotexistdomain.com"));
    Assert.assertFalse(list.isFederatedDomain("gmail.com"));
  }

  public void testIsFederatedDomain() {
    IdpWhiteList list = new IdpWhiteList();
    Assert.assertTrue(list.isFederatedDomain("gmail.com"));
    Assert.assertFalse(list.isFederatedDomain("thisisanotexistdomain.com"));
    Assert.assertFalse(list.isFederatedDomain(""));
    Assert.assertFalse(list.isFederatedDomain(null));
  }

}
