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
package com.smartitengineering.util.bean.guice;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.smartitengineering.util.bean.BeanFactory;

/**
 *
 * @author imyousuf
 */
public class GoogleGuiceBeanFactory
    implements BeanFactory {

  private final Injector injector;

  public GoogleGuiceBeanFactory(Injector injector) {
    if (injector == null) {
      throw new IllegalArgumentException();
    }
    this.injector = injector;
  }

  public Injector getInjector() {
    return injector;
  }

  @Override
  public boolean containsBean(String beanName,
                              Class beanClass)
      throws IllegalArgumentException {
    return injector.getInstance(Key.get(beanClass, Names.named(beanName))) != null;
  }

  @Override
  public Object getBean(String beanName,
                        Class beanClass)
      throws IllegalArgumentException {
    return injector.getInstance(Key.get(beanClass, Names.named(beanName)));
  }
}
