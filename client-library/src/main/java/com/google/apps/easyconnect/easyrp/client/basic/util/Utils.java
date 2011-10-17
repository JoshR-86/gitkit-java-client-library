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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Strings;

/**
 * Utility functions.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class Utils {
  private static final Logger log = Logger.getLogger(Utils.class.getName());

  /**
   * Reads all the request parameters to construct the Request URI for verifyAssertion.
   * @param req the {@code HttpServletRequest} object
   * @return a string for verifyAssertion.
   * @throws IOException if error occurs when encoding the URL key and parameter.
   */
  public static String getRequestUri(HttpServletRequest req) throws IOException {
    Map<String, String[]> params = req.getParameterMap();
    StringBuilder buf = new StringBuilder();
    buf.append(req.getRequestURL().toString());
    boolean first = true;
    for (String key : params.keySet()) {
      if (first) {
        buf.append("?");
        first = false;
      } else {
        buf.append("&");
      }
      buf.append(URLEncoder.encode(key, "UTF-8"));
      buf.append("=");
      buf.append(URLEncoder.encode(params.get(key)[0], "UTF-8"));
    }
    return buf.toString();
  }

  /**
   * Reads the content of an {@code InputStream}, and returned as a {@code String}.
   * 
   * @param is the input stream
   * @param encoding the encoding of the input stream
   * @return the content String, or "" if error occurs.
   * @throws UnsupportedEncodingException if the encoding is unsupported.
   */
  public static String streamToString(InputStream is, String encoding)
      throws UnsupportedEncodingException {
    InputStreamReader isr;
    if (Strings.isNullOrEmpty(encoding)) {
      isr = new InputStreamReader(is, "UTF-8");
    } else {
      isr = new InputStreamReader(is, encoding);
    }
    BufferedReader reader = new BufferedReader(isr);
    StringBuilder sb = new StringBuilder();

    int ch;
    try {
      while ((ch = reader.read()) >= 0) {
        sb.append((char) ch);
      }
    } catch (IOException e) {
      log.severe(e.getMessage());
    } finally {
      try {
        is.close();
      } catch (IOException e) {
        log.severe(e.getMessage());
      }
    }
    return sb.toString();
  }

  /**
   * Constructs the host URL of the request.
   * 
   * @param req the {@code HttpServletRequest} object
   * @return a string for the host URL.
   */
  public static String getHostUrl(HttpServletRequest req) {
    StringBuilder buf = new StringBuilder();
    buf.append(req.getScheme()).append("://").append(req.getServerName());
    if ((!"http".equalsIgnoreCase(req.getScheme()) || req.getServerPort() != 80)
        && (!"https".equalsIgnoreCase(req.getScheme()) || req.getServerPort() != 443)) {
      buf.append(":").append(req.getServerPort());
    }
    return buf.toString();
  }
}
