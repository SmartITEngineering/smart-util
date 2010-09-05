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
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 * @author russel
 */
@Aggregator(contextName = "com.smartitengineering.user.client")
public final class ConfigFactory {

  @InjectableField
  private ConnectionConfig connectionConfig;
  private static ConfigFactory configFactory;

  public static ConfigFactory getInstance() {
    if (configFactory == null) {
      configFactory = new ConfigFactory();
    }
    return configFactory;
  }

  private ConfigFactory() {
    BeanFactoryRegistrar.aggregate(this);
    if (connectionConfig == null) {
      System.out.println("Dependency not injected!");
      String propFileName = "user-client-config.properties";
      InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
      if (inputStream != null) {
        Properties properties = new Properties();
        try {
          properties.load(inputStream);
          connectionConfig = new ConnectionConfig();
          connectionConfig.setBasicUri(properties.getProperty("baseUri", ""));
          connectionConfig.setContextPath(properties.getProperty("contextPath", "/"));
          connectionConfig.setHost(properties.getProperty("host", "localhost"));
          connectionConfig.setPort(NumberUtils.toInt(properties.getProperty("port", ""), 9090));
        }
        catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    }
  }

  public ConnectionConfig getConnectionConfig() {
    return connectionConfig;
  }
}
