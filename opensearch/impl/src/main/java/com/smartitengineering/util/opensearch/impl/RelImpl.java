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

import com.smartitengineering.util.opensearch.api.Url.Rel;
import java.net.URL;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author imyousuf
 */
class RelImpl implements Rel {

  private String value;

  public RelImpl(URL value) {
    if(value == null) {
      throw new IllegalArgumentException("URL can not be null!");
    }
    this.value = value.toString();
  }

  public RelImpl(String value) {
    if(StringUtils.isBlank(value) || !StringUtils.isAlphanumeric(value)) {
      throw new IllegalArgumentException("Rel value must be alphanumeric non-blank");
    }
    this.value = value.toLowerCase();
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final RelImpl other = (RelImpl) obj;
    if ((this.value == null) ? (other.value != null) : !this.value.equals(other.value)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 59 * hash + (this.value != null ? this.value.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString() {
    return getValue();
  }
}
