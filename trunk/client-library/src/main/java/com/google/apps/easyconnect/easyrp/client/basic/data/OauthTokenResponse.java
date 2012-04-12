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

package com.google.apps.easyconnect.easyrp.client.basic.data;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Class for representing OAuth token response. For OAuth 2, it contains an access token, an expire
 * time and a refresh token. For OAuth 1, it contains an refresh token.
 *
 * @author mengcheng@google.com (Mengcheng Duan)
 */
public final class OauthTokenResponse implements Serializable {
  private static final long serialVersionUID = 1L;

  private static final Logger log = Logger.getLogger(OauthTokenResponse.class.getName());

  /**
   * Creates an OAuth 2 token response.
   *
   * @param accessToken OAuth2 access token
   * @param expireIn Lifetime in second for the access token
   * @param refreshToken OAuth2 refresh token
   * @return OAuth2 token response
   */
  public static OauthTokenResponse createOauth2Token(String accessToken, int expireIn,
      String refreshToken) {
    return new OauthTokenResponse(accessToken, expireIn, refreshToken);
  }

  /**
   * Creates an OAuth 1 token response.
   *
   * @param refreshToken OAuth1 request
   * @return OAuth2 token response
   */
  public static OauthTokenResponse createOauth1Token(String refreshToken) {
    return new OauthTokenResponse(refreshToken);
  }

  /**
   * Creates an OAuth token response based on the returned IdP assertion.
   *
   * @param assertion the returned IdP assertion
   * @return either an OAuth1 or OAuth2 token response
   */
  public static OauthTokenResponse createFromAssertion(JSONObject assertion) {
    Preconditions.checkNotNull(assertion);
    try {
      if (assertion.has("oauthAccessToken")) {
        String accessToken = assertion.getString("oauthAccessToken");
        if (Strings.isNullOrEmpty(accessToken)) {
          return null;
        }
        int expireIn = assertion.has("oauthExpireIn") ? assertion.getInt("oauthExpireIn") : 0;
        String refreshToken = assertion.has("oauthRefreshToken")
            ? assertion.getString("oauthRefreshToken") : null;
        return createOauth2Token(accessToken, expireIn, refreshToken);
      } else if (assertion.has("oauthRequestToken")) {
        String requestToken = assertion.getString("oauthRequestToken");
        if (Strings.isNullOrEmpty(requestToken)) {
          return null;
        }
        return createOauth1Token(requestToken);
      }
    } catch (JSONException e) {
      log.severe("Failed to get OAuth token from assertion: " + e.getMessage());
    }
    return null;
  }

  /**
   * Types for OAuth token response. Either an OAUTH1_TOKEN or an OAUTH2_TOKEN.
   */
  public enum OauthTokenType {
    OAUTH1_TOKEN, OAUTH2_TOKEN
  }

  /** Type of this token. Either {@code OAUTH1_TOKEN} or {@code OAUTH2_TOKEN} */
  private final OauthTokenType type;

  /** Access token, required for type {@code OAUTH2_TOKEN} */
  private final String accessToken;

  /** Lifetime in second for the access token, optional for type {@code OAUTH2_TOKEN} */
  private final int expireIn;

  /** Refresh token used to swap a new access token, optional for type {@code OAUTH2_TOKEN} */
  private final String refreshToken;

  /** Request token used to swap an access token, required for type {@code OAUTH2_TOKEN} */
  private final String requestToken;

  private OauthTokenResponse(String accessToken, int expireIn, String refreshToken) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(accessToken));
    this.type = OauthTokenType.OAUTH2_TOKEN;
    this.accessToken = accessToken;
    this.expireIn = expireIn;
    this.refreshToken = refreshToken;
    this.requestToken = null;
  }

  private OauthTokenResponse(String requestToken) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(requestToken));
    this.type = OauthTokenType.OAUTH1_TOKEN;
    this.requestToken = requestToken;
    this.accessToken = null;
    this.expireIn = 0;
    this.refreshToken = null;
  }

  public OauthTokenType getType() {
    return type;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public int getExpireIn() {
    return expireIn;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public String getRequestToken() {
    return requestToken;
  }
}
