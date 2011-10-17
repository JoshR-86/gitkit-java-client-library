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

package com.google.apps.easyconnect.easyrp.client.basic;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.apps.easyconnect.easyrp.client.basic.data.AccountService;
import com.google.apps.easyconnect.easyrp.client.basic.session.RpConfig;
import com.google.apps.easyconnect.easyrp.client.basic.session.SessionManager;
import com.google.apps.easyconnect.easyrp.client.basic.util.DasherDomainChecker;
import com.google.apps.easyconnect.easyrp.client.basic.util.GitServiceClient;
import com.google.apps.easyconnect.easyrp.client.basic.util.GitServiceClientImpl;
import com.google.apps.easyconnect.easyrp.client.basic.util.IdpWhiteList;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.MapMaker;

/**
 * A services locator for RP's account & session services. Also holds some configuration parameters.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class Context {
  private static RpConfig config;
  private static AccountService accountService;
  private static SessionManager sessionManager;
  private static DasherDomainChecker dasherDomainChecker;
  private static GitServiceClient gitServiceClient;
  private static IdpWhiteList idpWhiteList;
  private static boolean useLocalIdpWhiteList;
  private static boolean returnProfileInfo;

  static {
    Map<String, Boolean> cache = new MapMaker().expireAfterWrite(5, TimeUnit.MINUTES)
        .maximumSize(10000).makeMap();
    dasherDomainChecker = new DasherDomainChecker(cache);
    String apiKey = "AIzaSyCWBH-lgC22VCbdcGo95L2qE4FccWQ1VPs";
    gitServiceClient = new GitServiceClientImpl(apiKey);
    useLocalIdpWhiteList = false;
    returnProfileInfo = false;
  }

  /* Factory class, cannot instantiate */
  private Context() {
  }

  public static boolean isUseLocalIdpWhiteList() {
    return useLocalIdpWhiteList;
  }

  public static void setUseLocalIdpWhiteList(boolean useLocalIdpWhiteList) {
    Context.useLocalIdpWhiteList = useLocalIdpWhiteList;
    if (Context.useLocalIdpWhiteList && Context.idpWhiteList == null) {
      Context.idpWhiteList = new IdpWhiteList();
    }
  }

  public static boolean isReturnProfileInfo() {
    return returnProfileInfo;
  }

  public static void setReturnProfileInfo(boolean returnProfileInfo) {
    Context.returnProfileInfo = returnProfileInfo;
  }

  public static void setConfig(RpConfig config) {
    Context.config = config;
  }

  public static void setAccountService(AccountService accountService) {
    Context.accountService = accountService;
  }

  public static void setSessionManager(SessionManager sessionManager) {
    Context.sessionManager = sessionManager;
  }

  public static void setDasherDomainChecker(DasherDomainChecker dasherDomainChecker) {
    Context.dasherDomainChecker = dasherDomainChecker;
  }

  public static void setGitServiceClient(GitServiceClient apiClient) {
    Context.gitServiceClient = apiClient;
  }

  public static void setGoogleApisDeveloperKey(String developerKey) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(developerKey));
    Context.gitServiceClient = new GitServiceClientImpl(developerKey);
  }

  public static RpConfig getConfig() {
    return Context.config;
  }

  public static AccountService getAccountService() {
    return Context.accountService;
  }

  public static SessionManager getSessionManager() {
    return Context.sessionManager;
  }

  public static DasherDomainChecker getDasherDomainChecker() {
    return Context.dasherDomainChecker;
  }

  public static GitServiceClient getGitServiceClient() {
    return Context.gitServiceClient;
  }

  /**
   * Checks if a domain is a federated domain.
   * 
   * @param domain the domain name to be checked
   * @return {@code true} if it is federated domain, {@code false} otherwise
   */
  public static boolean isFederatedDomain(String domain) {
    return useLocalIdpWhiteList && idpWhiteList.isFederatedDomain(domain);
  }
}
