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

/**
 * A utilities class.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class ClassUtil {

  /**
   * Gets method with expected methodName, and with one parameter whose type is requestClass or its
   * super class.
   * <p>
   * This method is used to find right method on evaluator and action classes. See
   * {@code GitEvaluator} and {@code UserStatusAction} as an example.
   * @param klass the target class to find method
   * @param methodName the name of the method
   * @param requestClass the parameter type for the expected emthod
   * @return the match method object, or null if not found
   */
  public static Method getMethod(Class<?> klass, String methodName, Class<?> requestClass) {
    Class<?> theRequestClass = requestClass;
    while (theRequestClass != Object.class) {
      try {
        return klass.getMethod(methodName, theRequestClass);
      } catch (SecurityException e) {
      } catch (NoSuchMethodException e) {
      }
      theRequestClass = theRequestClass.getSuperclass();
    }
    return null;
  }
}
