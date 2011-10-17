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

package com.google.apps.easyconnect.easyrp.client.basic.logic;

import com.google.apps.easyconnect.easyrp.client.basic.logic.ac.LegacySigninAction;
import com.google.apps.easyconnect.easyrp.client.basic.logic.ac.PopupCallbackAction;
import com.google.apps.easyconnect.easyrp.client.basic.logic.ac.RedirectCallbackAction;
import com.google.apps.easyconnect.easyrp.client.basic.logic.ac.UserStatusAction;
import com.google.apps.easyconnect.easyrp.client.basic.logic.common.GitCallbackEvaluator;
import com.google.apps.easyconnect.easyrp.client.basic.logic.common.GitCallbackRequest;
import com.google.apps.easyconnect.easyrp.client.basic.logic.common.GitEvaluator;
import com.google.apps.easyconnect.easyrp.client.basic.logic.common.GitLoginEvaluator;
import com.google.apps.easyconnect.easyrp.client.basic.logic.common.GitLoginRequest;
import com.google.apps.easyconnect.easyrp.client.basic.logic.common.GitRequest;
import com.google.apps.easyconnect.easyrp.client.basic.logic.impl.GitTreeBuilder;

public class GitLogicBuilderFactory {
  public static GitLogicBuilder getAcUserStatusLogicBuilder(boolean useLocalIdpWhiteList,
      boolean returnProfileInfo) {
    return new GitTreeBuilder(GitRequest.class, new GitEvaluator(useLocalIdpWhiteList),
        new UserStatusAction(returnProfileInfo));
  }

  public static GitLogicBuilder getAcLegacySigninLogicBuilder(boolean useLocalIdpWhiteList,
      boolean returnProfileInfo) {
    return new GitTreeBuilder(GitLoginRequest.class, new GitLoginEvaluator(useLocalIdpWhiteList),
        new LegacySigninAction(returnProfileInfo));
  }

  public static GitLogicBuilder getAcCallbackPopupLogicBuilder(boolean useLocalIdpWhiteList,
      boolean returnProfileInfo) {
    return new GitTreeBuilder(GitCallbackRequest.class, new GitCallbackEvaluator(
        useLocalIdpWhiteList), new PopupCallbackAction(returnProfileInfo));
  }

  public static GitLogicBuilder getAcCallbackRedirectLogicBuilder(boolean useLocalIdpWhiteList,
      boolean returnProfileInfo) {
    return new GitTreeBuilder(GitCallbackRequest.class, new GitCallbackEvaluator(
        useLocalIdpWhiteList), new RedirectCallbackAction(returnProfileInfo));
  }
}
