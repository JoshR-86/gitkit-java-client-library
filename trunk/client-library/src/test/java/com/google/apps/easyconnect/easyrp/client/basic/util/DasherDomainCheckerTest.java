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

import java.util.Map;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import com.google.common.collect.MapMaker;

public class DasherDomainCheckerTest extends TestCase {
  private Map<String, Boolean> cache;
  private DasherDomainChecker checker;

  protected void tearDown() throws Exception {
    checker = null;
  }

  private void initCacheModule() {
    cache = new MapMaker().expireAfterWrite(5, TimeUnit.MINUTES).maximumSize(10).makeMap();
    checker = new DasherDomainChecker(cache);
  }

  private void initNoCacheModule() {
    checker = new DasherDomainChecker(null);
  }

  public void testIsDasherDomain_null() {
    initCacheModule();
    assertFalse(checker.isDasherDomain(null));
  }

  public void testIsDasherDomain_empty() {
    initCacheModule();
    assertFalse(checker.isDasherDomain(""));
  }

  public void testIsDasherDomain_google() {
    initCacheModule();
    assertTrue(checker.isDasherDomain("google.com"));
  }

  public void testIsDasherDomain_cache() {
    initCacheModule();
    cache.put("dasher.com", true);
    // This domain is in cache.
    assertTrue(checker.isDasherDomain("dasher.com"));
  }

  public void testIsDasherDomain_163() {
    initCacheModule();
    assertFalse(checker.isDasherDomain("163.com"));
  }

  public void testIsDasherDomain_nocache_null() {
    initNoCacheModule();
    assertFalse(checker.isDasherDomain(null));
  }

  public void testIsDasherDomain_nocache_empty() {
    initNoCacheModule();
    assertFalse(checker.isDasherDomain(""));
  }

  public void testIsDasherDomain_nocache_google() {
    initNoCacheModule();
    assertTrue(checker.isDasherDomain("google.com"));
  }

  public void testIsDasherDomain_nocache_cache() {
    initNoCacheModule();
    assertFalse(checker.isDasherDomain("dasher.com"));
  }

  public void testIsDasherDomain_nocache_163() {
    initNoCacheModule();
    assertFalse(checker.isDasherDomain("163.com"));
  }
}
