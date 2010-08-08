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

import com.smartitengineering.util.opensearch.api.Query;
import com.smartitengineering.util.opensearch.api.Role;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author imyousuf
 */
class QueryImpl implements Query {

  static final String TITLE = "Title";
  static final int TITLE_MAX_LEN = 256;
  private Map<String, String> customAttributes;
  private Role role;
  private String title, searchTerms, language, inputEncoding, outputEncoding;
  private int totalResults, startPage, startIndex, count;

  QueryImpl(Role role) {
    setRole(role);
    customAttributes = new HashMap<String, String>();
  }

  public void setCustomAttributes(Map<String, String> customAttributes) {
    if (customAttributes != null && !customAttributes.isEmpty()) {
      this.customAttributes.clear();
      this.customAttributes.putAll(customAttributes);
    }
  }

  public void addCustomAttribute(String key, String value) {
    if(StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
      this.customAttributes.put(key, value);
    }
  }

  public void addCustomAttributes(Map<String, String> customAttributes) {
    if (customAttributes != null && !customAttributes.isEmpty()) {
      this.customAttributes.putAll(customAttributes);
    }
  }

  public final void setRole(Role role) {
    if (role == null) {
      throw new IllegalArgumentException("Query's role is not optional!");
    }
    this.role = role;
  }

  public void setTitle(String title) {
    Utils.checkMaxLength(TITLE, TITLE_MAX_LEN, title);
    this.title = StringUtils.trim(title);
  }

  public void setSearchTerms(String searchTerms) {
    this.searchTerms = searchTerms;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public void setStartIndex(int startIndex) {
    this.startIndex = startIndex;
  }

  public void setStartPage(int startPage) {
    this.startPage = startPage;
  }

  public void setTotalResults(int totalResults) {
    this.totalResults = totalResults;
  }

  public void setInputEncoding(String inputEncoding) {
    this.inputEncoding = inputEncoding;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public void setOutputEncoding(String outputEncoding) {
    this.outputEncoding = outputEncoding;
  }

  @Override
  public Map<String, String> getCustomAttributes() {
    return Collections.unmodifiableMap(customAttributes);
  }

  @Override
  public Role getRole() {
    return role;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public String getSearchTerms() {
    return searchTerms;
  }

  @Override
  public int getTotalResults() {
    return totalResults;
  }

  @Override
  public int getStartIndex() {
    return startIndex;
  }

  @Override
  public int getStartPage() {
    return startPage;
  }

  @Override
  public int getCount() {
    return count;
  }

  @Override
  public String getLanguage() {
    return language;
  }

  @Override
  public String getInputEncoding() {
    return inputEncoding;
  }

  @Override
  public String getOutputEncoding() {
    return outputEncoding;
  }
}
