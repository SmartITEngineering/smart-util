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

import com.smartitengineering.util.opensearch.api.Role;
import org.apache.commons.lang.StringUtils;

/**
 * Implementation of {@link Role}
 * @author imyousuf
 */
class RoleImpl implements Role {

  private String roleAsString;

  public RoleImpl(String roleAsString) {
    if(StringUtils.isBlank(roleAsString)) {
      throw new IllegalArgumentException("Role string value can not be blank!");
    }
    this.roleAsString = roleAsString;
  }

  public String getRoleString() {
    return roleAsString;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final RoleImpl other = (RoleImpl) obj;
    if ((this.roleAsString == null) ? (other.roleAsString != null) : !this.roleAsString.equals(other.roleAsString)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 61 * hash + (this.roleAsString != null ? this.roleAsString.hashCode() : 0);
    return hash;
  }

}
