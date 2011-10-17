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

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Strings;

public class IdpWhiteList {
  private List<String> federatedDomains;

  /**
   * Constructs the default {@code IdpWhiteList} instance with following IDP domains.
   * 
   * <pre>
   * // Gmail
   * "gmail.com", "googlemail.com",
   * 
   * // AOL domains.
   * "aol.com", "aim.com", "netscape.net", "cs.com", "ygm.com",
   * "games.com", "love.com", "wow.com",
   * 
   * // LiveID domains.
   * "hotmail.com", "hotmail.co.uk", "hotmail.fr", "hotmail.it",
   * "live.com", "msn.com",
   * 
   * // Yahoo domains.
   * "yahoo.com", "rocketmail.com", "ymail.com", "y7mail.com", "yahoo.com.au", "yahoo.com.cn",
   * "yahoo.cn", "yahoo.com.hk", "yahoo.co.nz", "yahoo.com.pk", "yahoo.com.tw", "kimo.com",
   * "bellsouth.net", "ameritech.net", "att.net", "attworld.com", "flash.net", "nvbell.net",
   * "pacbell.net", "prodigy.net", "sbcglobal.net", "snet.net", "swbell.net", "wans.net",
   * "btinternet.com", "btopenworld.com", "talk21.com", "rogers.com", "nl.rogers.com",
   * "demobroadband.com", "xtra.co.nz", "verizon.net"
   * </pre>
   */
  public IdpWhiteList() {
    this(
        // Gmail
        "gmail.com",
        "googlemail.com",
        // AOL domains.
        "aol.com", "aim.com", "netscape.net",
        "cs.com",
        "ygm.com",
        "games.com",
        "love.com",
        "wow.com",
        // LiveID domains.
        "hotmail.com", "hotmail.co.uk",
        "hotmail.fr",
        "hotmail.it",
        "live.com",
        "msn.com",
        // Yahoo domains.
        "yahoo.com", "rocketmail.com", "ymail.com", "y7mail.com", "yahoo.com.au", "yahoo.com.cn",
        "yahoo.cn", "yahoo.com.hk", "yahoo.co.nz", "yahoo.com.pk", "yahoo.com.tw", "kimo.com",
        "bellsouth.net", "ameritech.net", "att.net", "attworld.com", "flash.net", "nvbell.net",
        "pacbell.net", "prodigy.net", "sbcglobal.net", "snet.net", "swbell.net", "wans.net",
        "btinternet.com", "btopenworld.com", "talk21.com", "rogers.com", "nl.rogers.com",
        "demobroadband.com", "xtra.co.nz", "verizon.net");
  }

  /**
   * Constructs the {@code IdpWhiteList} instance.
   * 
   * @param idps the supported federated domains.
   */
  public IdpWhiteList(String... idps) {
    federatedDomains = Arrays.asList(idps);
  }

  /**
   * Checks if a domain is a federated domain.
   * 
   * @param domain the domain name to be checked
   * @return {@code true} if it is federated domain, {@code false} otherwise
   */
  public boolean isFederatedDomain(String domain) {
    if (Strings.isNullOrEmpty(domain)) {
      return false;
    }
    return federatedDomains.contains(domain.toLowerCase());
  }

}
