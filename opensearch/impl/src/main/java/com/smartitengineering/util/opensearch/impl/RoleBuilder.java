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

/**
 *
 * @author imyousuf
 */
public class RoleBuilder {
  private String roleString;

  public static RoleBuilder getRoleBuilder() {
    return new RoleBuilder();
  }

  protected RoleBuilder() {
  }

  public Role build() {
    RoleImpl role = new RoleImpl(roleString);
    return role;
  }

  public RoleBuilder localRole(Role.LocalOpenSearchRoles localRole) {
    if(localRole != null) {
      roleString = localRole.getValue();
    }
    return this;
  }

  public RoleBuilder extendedRole(String extendedRole) {
    roleString = extendedRole;
    return this;
  }
}
