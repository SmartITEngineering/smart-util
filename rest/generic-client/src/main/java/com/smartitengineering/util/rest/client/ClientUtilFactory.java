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

import com.smartitengineering.util.bean.BeanFactoryRegistrar;
import com.smartitengineering.util.bean.annotations.Aggregator;
import com.smartitengineering.util.bean.annotations.InjectableField;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author imyousuf
 */
@Aggregator(contextName = "com.smartitengineering.user.client")
public class ClientUtilFactory {

  private static ClientUtilFactory clientUtilFactory;

  public static ClientUtilFactory getInstance() {
    if (clientUtilFactory == null) {
      clientUtilFactory = new ClientUtilFactory();
    }
    return clientUtilFactory;
  }
  @InjectableField
  private Map<Class, ClientUtil> clientUtils;

  private ClientUtilFactory() {
    BeanFactoryRegistrar.aggregate(this);
    if (clientUtils == null) {
      clientUtils = new HashMap<Class, ClientUtil>();
    }
  }

  public ClientUtil getClientUtil(Class clazz) {
    if (clazz == null) {
      return null;
    }
    return clientUtils.get(clazz);
  }
}
