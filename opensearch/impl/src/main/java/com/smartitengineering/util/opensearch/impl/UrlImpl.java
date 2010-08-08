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
  private Rel rel;

  public void setRel(Rel rel) {
    this.rel = rel;
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
  public Rel getRel() {
    if (rel == null) {
      return Rel.getDefault();
    }
    return rel;
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
