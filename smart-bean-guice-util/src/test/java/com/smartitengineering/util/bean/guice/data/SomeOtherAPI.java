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
package com.smartitengineering.util.bean.guice.data;

import com.smartitengineering.util.bean.BeanFactoryRegistrar;
import com.smartitengineering.util.bean.annotations.Aggregator;
import com.smartitengineering.util.bean.annotations.InjectableField;

/**
 *
 * @author imyousuf
 */
@Aggregator(contextName = "com.smartitengineering.util.bean.guice.test.other")
public class SomeOtherAPI {

  @InjectableField(beanName = "myFirstBean")
  private IFirstBean firstBean;
  @InjectableField(beanName = "myErrorBean")
  private IFirstBean errorBean;
  @InjectableField
  private ISecondBean secondBean;
  @InjectableField(beanName = "mySimpleBean")
  private IFirstBean simpleBean;

  public IFirstBean getSimpleBean() {
    return simpleBean;
  }

  public IFirstBean getErrorBean() {
    return errorBean;
  }

  public IFirstBean getFirstBean() {
    return firstBean;
  }

  public ISecondBean getSecondBean() {
    return secondBean;
  }

  private SomeOtherAPI() {
  }
  public static SomeOtherAPI api;

  public static SomeOtherAPI getInstance() {
    if (api == null) {
      api = new SomeOtherAPI();
      BeanFactoryRegistrar.aggregate(api);
    }
    return api;
  }
}
