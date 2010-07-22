/*
 * This is a utility project for wide range of applications
 *
 * Copyright (C) 8  Imran M Yousuf (imyousuf@smartitengineering.com)
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
package com.smartitengineering.util.opensearch.api;

import java.util.Map;

/**
 *
 * @author imyousuf
 */
public interface Url {

  public String getTemplate();

  public String getType();

  public Rel getRel();

  public int getIndexOffset();

  public int getPageOffset();

  public Map<String, String> getCustomAttributes();

  public enum Rel {

    RESULTS("results"), SUGGESTIONS("suggestions"), SELF("self"), COLLECTION("collection");
    private final String value;

    Rel(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    public static Rel getDefault() {
      return RESULTS;
    }
  }
}
