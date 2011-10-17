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

/**
 * This package provides some servlets for RP to deploy when integrating with GIT.
 * <ul>
 * <li>UserStatusServlet, LoginServlet, and CallbackServlet</li> are servlets handle specific RPC
 * requests.
 * <li>GitServlet</li> can handle all types of GIT RPC requests. Only 1 more URL entry point needed
 * to integrate with GIT by deploying this servlet.
 * <li>AdminServlet</li> helps RP administrators to develop, test the integration. Note: it should
 * only be accessible to administrators. You should only deploy it on the development and testing
 * environment instead of the production environment.
 * </ul>
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
package com.google.apps.easyconnect.easyrp.client.basic.servlet;

