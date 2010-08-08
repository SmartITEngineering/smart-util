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
import com.smartitengineering.util.opensearch.api.Url.Rel;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author imyousuf
 */
public class UrlBuilder {

  private final UrlImpl urlImpl;

  private UrlBuilder() {
    urlImpl = new UrlImpl();
  }

  public static UrlBuilder getBuilder() {
    return new UrlBuilder();
  }

  public Url build() {
    if (StringUtils.isBlank(urlImpl.getTemplate()) || StringUtils.isBlank(urlImpl.getType())) {
      throw new IllegalStateException("Url must have both template and type set!");
    }
    UrlImpl url = new UrlImpl();
    url.setType(urlImpl.getType());
    url.setTemplate(urlImpl.getTemplate());
    url.setRel(urlImpl.getRel());
    url.setPageOffset(urlImpl.getPageOffset());
    url.setIndexOffset(urlImpl.getIndexOffset());
    url.setCustomAttributes(urlImpl.getCustomAttributes());
    return url;
  }

  public UrlBuilder customAttribute(String key, String value) {
    urlImpl.addCustomAttribute(key, value);
    return this;
  }

  public UrlBuilder rel(Rel rel) {
    urlImpl.setRel(rel);
    return this;
  }

  public UrlBuilder pageOffset(int pageOffset) {
    urlImpl.setPageOffset(pageOffset);
    return this;
  }

  public UrlBuilder indexOffset(int indexOffset) {
    urlImpl.setIndexOffset(indexOffset);
    return this;
  }

  public UrlBuilder template(String template) {
    urlImpl.setTemplate(template);
    return this;
  }

  public UrlBuilder type(String type) {
    urlImpl.setType(type);
    return this;
  }
}
