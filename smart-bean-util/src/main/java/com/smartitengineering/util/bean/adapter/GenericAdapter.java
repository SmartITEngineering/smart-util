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

import java.util.Collection;
import java.util.Map;

/**
 *
 * @author imyousuf
 */
public interface GenericAdapter<F, T> {

  public T convert(F fromBean);

  public Collection<T> convert(F... fromBeans);

  public void merge(Map.Entry<F, T> bean);

  public void merge(Collection<Map.Entry<F, T>> beans);

  public F convertInversely(T toBean);

  public Collection<F> convertInversely(T... toBeans);
}
