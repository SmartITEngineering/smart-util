/*
 * This is a utility project for wide range of applications
 *
 * Copyright (C) 2010  Imran M Yousuf (imyousuf@smartitengineering.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  10-1  USA
 */
package com.smartitengineering.util.opensearch.impl;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author imyousuf
 */
public class Utils {

  public static void checkMaxLength(String name, int maxLength, String testString) {
    checkMaxLength(name, maxLength, testString, true);
  }

  public static void checkMaxLength(String name, int maxLength, String testString, boolean trim) {
    final String finalTestString;
    if (trim) {
      finalTestString = StringUtils.trim(testString);
    }
    else {
      finalTestString = testString;
    }
    if (StringUtils.length(finalTestString) > maxLength) {
      throw new IllegalArgumentException(new StringBuilder(name).append(" must be fewer or equal to ").append(maxLength).
          append(" characters").toString());
    }
  }

  public static void checkMinLength(String name, int minLength, String testString) {
    checkMinLength(name, minLength, testString, true);
  }

  public static void checkMinLength(String name, int minLength, String testString, boolean trim) {
    final String finalTestString;
    if (trim) {
      finalTestString = StringUtils.trim(testString);
    }
    else {
      finalTestString = testString;
    }
    if (StringUtils.length(finalTestString) < minLength) {
      throw new IllegalArgumentException(new StringBuilder(name).append(" must not be fewer than ").append(minLength).
          append(" characters").toString());
    }
  }

  public static void checkMinMaxLength(String name, int minLength, int maxLength, String testString) {
    checkMinMaxLength(name, minLength, maxLength, testString, true);
  }

  public static void checkMinMaxLength(String name, int minLength, int maxLength, String testString, boolean trim) {
    checkMinLength(name, minLength, testString, trim);
    checkMaxLength(name, maxLength, testString, trim);
  }
}
