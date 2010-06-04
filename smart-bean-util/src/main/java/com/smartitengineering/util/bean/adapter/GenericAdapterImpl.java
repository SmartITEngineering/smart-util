/*
 * This is a utility project for wide range of applications
 *
 * Copyright (C) 2010  Imran M Yousuf (imyousuf@smartitengineering.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.smartitengineering.util.bean.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author imyousuf
 */
public class GenericAdapterImpl<F, T>
    implements GenericAdapter<F, T> {

  private AbstractAdapterHelper<F, T> helper;

  public AbstractAdapterHelper<F, T> getHelper() {
    return helper;
  }

  public void setHelper(AbstractAdapterHelper<F, T> helper) {
    this.helper = helper;
  }

  public Collection<T> convert(F... fromBeans) {
    if (fromBeans == null || fromBeans.length <= 0) {
      return Collections.emptyList();
    }
    List<T> result = new ArrayList<T>();
    for (F fromBean : fromBeans) {
      result.add(convert(fromBean));
    }
    return result;
  }

  public Collection<F> convertInversely(T... toBeans) {
    if (toBeans == null || toBeans.length <= 0) {
      return Collections.emptyList();
    }
    List<F> result = new ArrayList<F>();
    for (T toBean : toBeans) {
      result.add(convertInversely(toBean));
    }
    return result;
  }

  public T convert(F fromBean) {
    if (fromBean == null) {
      return null;
    }
    final T newTInstance = getHelper().newTInstance();
    getHelper().mergeFromF2T(fromBean, newTInstance);
    return newTInstance;
  }

  public F convertInversely(T toBean) {
    if (toBean == null) {
      return null;
    }
    return getHelper().convertFromT2F(toBean);
  }

  public void merge(Entry<F, T> bean) {
    if (bean == null || bean.getKey() == null) {
      return;
    }
    if (bean.getValue() == null) {
      bean.setValue(getHelper().newTInstance());
    }
    getHelper().mergeFromF2T(bean.getKey(), bean.getValue());
  }

  public void merge(Collection<Entry<F, T>> beans) {
    if (beans == null || beans.isEmpty()) {
      return;
    }
    for (Entry<F, T> bean : beans) {
      merge(bean);
    }
  }
}
