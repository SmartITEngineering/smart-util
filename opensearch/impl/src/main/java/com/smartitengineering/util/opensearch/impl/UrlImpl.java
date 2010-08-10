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

import com.smartitengineering.util.opensearch.api.Url;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author imyousuf
 */
class UrlImpl implements Url {

  private Map<String, String> customAttributes;
  private int indexOffset, pageOffset;
  private String template, type;
  private Collection<Rel> rels;

  UrlImpl() {
    customAttributes = new HashMap<String, String>();
    rels = new ArrayList<Rel>();
  }

  public void setRels(Collection<Rel> rels) {
    if (rels != null) {
      this.rels.clear();
      this.rels.addAll(rels);
    }
  }

  public void addRel(Rel rel) {
    if (rel != null) {
      this.rels.add(rel);
    }
  }

  public void removeRel(Rel rel) {
    if (rel != null) {
      this.rels.remove(rel);
    }
  }

  public void setTemplate(String template) {
    Utils.checkMinLength("Template", 1, template);
    this.template = template;
  }

  public void setType(String type) {
    Utils.checkMinLength("Mime Type", 1, type);
    this.type = type;
  }

  public void setIndexOffset(int indexOffset) {
    this.indexOffset = indexOffset;
  }

  public void setPageOffset(int pageOffset) {
    this.pageOffset = pageOffset;
  }

  public void setCustomAttributes(Map<String, String> customAttributes) {
    if (customAttributes != null && !customAttributes.isEmpty()) {
      this.customAttributes.clear();
      this.customAttributes.putAll(customAttributes);
    }
  }

  public void addCustomAttribute(String key, String value) {
    if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
      this.customAttributes.put(key, value);
    }
  }

  public void addCustomAttributes(Map<String, String> customAttributes) {
    if (customAttributes != null && !customAttributes.isEmpty()) {
      this.customAttributes.putAll(customAttributes);
    }
  }

  @Override
  public String getTemplate() {
    return template;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public Collection<Rel> getRels() {
    if (rels.isEmpty()) {
      return Collections.<Rel>singleton(new RelImpl(RelEnum.getDefault().getValue()));
    }
    return Collections.unmodifiableCollection(rels);
  }

  @Override
  public int getIndexOffset() {
    if (indexOffset < 1) {
      return 1;
    }
    return indexOffset;
  }

  @Override
  public int getPageOffset() {
    if (pageOffset < 1) {
      return 1;
    }
    return pageOffset;
  }

  @Override
  public Map<String, String> getCustomAttributes() {
    return customAttributes;
  }
}
