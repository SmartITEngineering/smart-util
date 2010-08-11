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
import com.smartitengineering.util.opensearch.api.Role.LocalOpenSearchRoles;

/**
 *
 * @author imyousuf
 */
public class QueryBuilder {

  private QueryImpl queryImpl;

  private QueryBuilder() {
    queryImpl = new QueryImpl(RoleBuilder.getRoleBuilder().localRole(LocalOpenSearchRoles.EXAMPLE).build());
  }

  /**
   * Returns a query builder instance with example local role set as default.
   * @return A new query builder instance
   */
  public static QueryBuilder getBuilder() {
    return new QueryBuilder();
  }

  public Query build() {
    QueryImpl query = new QueryImpl(queryImpl.getRole());
    query.setCount(queryImpl.getCount());
    query.setCustomAttributes(queryImpl.getCustomAttributes());
    query.setInputEncoding(queryImpl.getInputEncoding());
    query.setLanguage(queryImpl.getLanguage());
    query.setOutputEncoding(queryImpl.getOutputEncoding());
    query.setSearchTerms(queryImpl.getSearchTerms());
    query.setStartIndex(queryImpl.getStartIndex());
    query.setStartPage(queryImpl.getStartPage());
    query.setTitle(queryImpl.getTitle());
    query.setTotalResults(queryImpl.getTotalResults());
    return query;
  }

  public QueryBuilder role(Role role) {
    queryImpl.setRole(role);
    return this;
  }

  public QueryBuilder count(int count) {
    queryImpl.setCount(count);
    return this;
  }

  public QueryBuilder customAttribute(String key, String value) {
    queryImpl.addCustomAttribute(key, value);
    return this;
  }

  public QueryBuilder inputEncodings(String inputEncodings) {
    queryImpl.setInputEncoding(inputEncodings);
    return this;
  }

  public QueryBuilder language(String language) {
    queryImpl.setLanguage(language);
    return this;
  }

  public QueryBuilder outputEncodings(String outputEncodings) {
    queryImpl.setOutputEncoding(outputEncodings);
    return this;
  }

  public QueryBuilder searchTerms(String searchTerms) {
    queryImpl.setSearchTerms(searchTerms);
    return this;
  }

  public QueryBuilder startIndex(int startIndex) {
    queryImpl.setStartIndex(startIndex);
    return this;
  }

  public QueryBuilder startPage(int startPage) {
    queryImpl.setStartPage(startPage);
    return this;
  }

  public QueryBuilder title(String title) {
    queryImpl.setTitle(title);
    return this;
  }

  public QueryBuilder totalResults(int totalResults) {
    queryImpl.setTotalResults(totalResults);
    return this;
  }
}
