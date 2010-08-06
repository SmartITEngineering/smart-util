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
package com.smartitengineering.util.rest.atom;

import java.util.AbstractList;
import java.util.ArrayList;

/**
 * A read-only list backup by an atom feed which is paginated and has open search element total results at the minimum
 * mentioned. The wrapper provided should point to the feed which is the 1st page of result; that is only going next one
 * could discover all the entities.
 * @author imyousuf
 */
public class DynamicPaginatedEntitiesList<T> extends AbstractList<T> {

  private final PaginatedEntitiesWrapper<T> rootWrapper;
  private PaginatedEntitiesWrapper<T> currentWrapper;
  private final int size;
  private final ArrayList<T> backedupList;

  /**
   * Construct the dynamic list formed using the root feed wrapper.
   * @param wrapper Wrapper wrapping the root feed of an entities collection
   * @throws Exception If wrapper is null or open search total result element is missing
   */
  public DynamicPaginatedEntitiesList(PaginatedEntitiesWrapper<T> wrapper) throws Exception {
    if (wrapper == null) {
      throw new IllegalArgumentException("Wrapper can not be null!");
    }
    this.rootWrapper = wrapper;
    if (!ClientUtil.isOpenSearchTotalResultPresent(rootWrapper.getRootFeed())) {
      throw new IllegalArgumentException("Root feed must have total results specified!");
    }
    currentWrapper = rootWrapper;
    size = ClientUtil.getOpenSearchTotalResult(rootWrapper.getRootFeed());
    backedupList = new ArrayList<T>(size);
  }

  @Override
  public T get(int index) {
    if (index >= size) {
      throw new IndexOutOfBoundsException("Size is " + size + " but request index is " + index);
    }
    while (index >= backedupList.size() && currentWrapper != null) {
      backedupList.addAll(currentWrapper.getEntitiesForCurrentPage());
      currentWrapper = currentWrapper.next();
      if (backedupList.size() > index) {
        return backedupList.get(index);
      }
    }
    if (index >= backedupList.size()) {
      return null;
    }
    else {
      return backedupList.get(index);
    }
  }

  @Override
  public int size() {
    return size;
  }
}
