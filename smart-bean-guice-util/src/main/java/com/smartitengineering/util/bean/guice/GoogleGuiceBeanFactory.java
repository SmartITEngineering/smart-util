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

import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.smartitengineering.util.bean.BeanFactory;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author imyousuf
 */
public class GoogleGuiceBeanFactory
    implements BeanFactory {

  private final Injector[] injectors;
  private final boolean ignoreMissingDepedency;

  public GoogleGuiceBeanFactory(Injector... injectors) {
    this(false, injectors);
  }

  public GoogleGuiceBeanFactory(boolean ignoreMissingDependency, Injector... injectors) {
    if (injectors == null) {
      throw new IllegalArgumentException();
    }
    this.injectors = injectors;
    this.ignoreMissingDepedency = ignoreMissingDependency;
  }

  public Injector[] getInjector() {
    return injectors;
  }

  @Override
  public boolean containsBean(String beanName,
                              Class beanClass)
      throws IllegalArgumentException {
    return getBean(beanName, beanClass) != null;
  }

  @Override
  public Object getBean(String beanName,
                        Class beanClass)
      throws IllegalArgumentException {
    final boolean beanNameNotBlank = StringUtils.isNotBlank(beanName);
    ConfigurationException ex = null;
    for (Injector injector : injectors) {
      try {
        if (beanNameNotBlank) {
          return injector.getInstance(Key.get(beanClass, Names.named(beanName)));
        }
        else {
          return injector.getInstance(beanClass);
        }
      }
      catch (ConfigurationException exception) {
        ex = exception;
      }
    }
    if (ignoreMissingDepedency) {
      return null;
    }
    else {
      throw ex;
    }
  }

  @Override
  public boolean isNameMandatory() {
    return false;
  }
}
