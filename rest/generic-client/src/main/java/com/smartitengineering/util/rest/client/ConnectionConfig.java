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
package com.smartitengineering.util.rest.client;

/**
 *
 * @author russel
 */
public class ConnectionConfig {

    private String basicUri;
    private int port;
    private String host;
    private String contextPath;

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

    public String getBasicUri() {
        return basicUri;
    }

    public void setBasicUri(String basicUrl) {
        this.basicUri = basicUrl;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ConnectionConfig other = (ConnectionConfig) obj;
    if ((this.basicUri == null) ? (other.basicUri != null) : !this.basicUri.equals(other.basicUri)) {
      return false;
    }
    if (this.port != other.port) {
      return false;
    }
    if ((this.host == null) ? (other.host != null) : !this.host.equals(other.host)) {
      return false;
    }
    if ((this.contextPath == null) ? (other.contextPath != null) : !this.contextPath.equals(other.contextPath)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + (this.basicUri != null ? this.basicUri.hashCode() : 0);
    hash = 97 * hash + this.port;
    hash = 97 * hash + (this.host != null ? this.host.hashCode() : 0);
    hash = 97 * hash + (this.contextPath != null ? this.contextPath.hashCode() : 0);
    return hash;
  }
}
