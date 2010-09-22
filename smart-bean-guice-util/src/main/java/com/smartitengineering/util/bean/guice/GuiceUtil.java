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

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.smartitengineering.util.bean.BeanFactoryRegistrar;
import com.smartitengineering.util.bean.PropertiesLocator;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author imyousuf
 */
public class GuiceUtil {

  public static final String MODULE_CONFIG_PROP_FILE = "com/smartitengineering/util/bean/guice/guice-modules.properties";
  public static final String MODULES_LIST_PROP = "modules";
  public static final String CONTEXT_NAME_PROP = "contextName";

  public static GuiceUtil getInstance() {
    return getInstance(MODULE_CONFIG_PROP_FILE);
  }

  public static GuiceUtil getInstance(Properties properties) {
    GuiceUtil guiceUtil = new GuiceUtil(properties);
    return guiceUtil;
  }

  public static GuiceUtil getInstance(String propFile) {
    GuiceUtil guiceUtil = new GuiceUtil(propFile);
    return guiceUtil;
  }
  private final String contextName;
  private final boolean ignoreMissingDependency;
  private final List<Module>[] modules;

  private GuiceUtil(Properties properties) {
    contextName = properties.getProperty(CONTEXT_NAME_PROP);
    ignoreMissingDependency = Boolean.parseBoolean(properties.getProperty("ignoreMissingDependency"));
    if (StringUtils.isBlank(contextName)) {
      throw new IllegalStateException("Bean factory context name can not be blank");
    }
    final String[] moduleStrs;
    List<String> moduleConfigs = new ArrayList<String>();
    for (Entry<Object, Object> entry : properties.entrySet()) {
      if (entry.getKey().toString().startsWith(MODULES_LIST_PROP)) {
        moduleConfigs.add(entry.getValue().toString());
      }
    }
    moduleStrs = new String[moduleConfigs.size()];
    moduleConfigs.toArray(moduleStrs);
    modules = new List[moduleStrs.length];
    int index = 0;
    for (String modulesStr : moduleStrs) {
      if (StringUtils.isBlank(modulesStr)) {
        throw new IllegalStateException("Modules must be specified in a comma separated list!");
      }
      String[] moduleClassNames = modulesStr.split(",");
      List<Module> moduleSet = new ArrayList<Module>(moduleClassNames.length);
      for (String moduleClassName : moduleClassNames) {
        final Class clazz;
        try {
          clazz = Class.forName(StringUtils.trim(moduleClassName));
        }
        catch (ClassNotFoundException ex) {
          throw new IllegalStateException(ex);
        }
        if (!Module.class.isAssignableFrom(clazz)) {
          throw new IllegalArgumentException("Specified class not instance of Module");
        }
        Class<? extends Module> moduleClass = clazz;
        boolean foundConstructor = false;
        try {
          Constructor<? extends Module> defaultContructor = moduleClass.getConstructor();
          moduleSet.add(defaultContructor.newInstance());
          foundConstructor = true;
        }
        catch (InstantiationException ex) {
          throw new IllegalStateException(ex);
        }
        catch (IllegalAccessException ex) {
          throw new IllegalStateException(ex);
        }
        catch (InvocationTargetException ex) {
          throw new IllegalStateException(ex);
        }
        catch (NoSuchMethodException ex) {
        }
        catch (SecurityException ex) {
        }
        if (!foundConstructor) {
          try {
            Constructor<? extends Module> defaultContructor = moduleClass.getConstructor(Properties.class);
            moduleSet.add(defaultContructor.newInstance(properties));
            foundConstructor = true;
          }
          catch (InstantiationException ex) {
            throw new IllegalStateException(ex);
          }
          catch (IllegalAccessException ex) {
            throw new IllegalStateException(ex);
          }
          catch (InvocationTargetException ex) {
            throw new IllegalStateException(ex);
          }
          catch (NoSuchMethodException ex) {
          }
          catch (SecurityException ex) {
          }
        }
        if (!foundConstructor) {
          throw new IllegalStateException("No supported contructors found - no args and with a properties obj!");
        }
      }
      modules[index++] = moduleSet;
    }
  }

  private GuiceUtil(String propFile) {
    this(loadProperties(propFile));
  }

  private static Properties loadProperties(String propFile) throws IllegalArgumentException, IllegalStateException {
    if (StringUtils.isBlank(propFile)) {
      throw new IllegalArgumentException("Properties file location can not be blank!");
    }
    PropertiesLocator propertiesLocator = new PropertiesLocator();
    propertiesLocator.setSmartLocations(propFile);
    Properties properties = new Properties();
    try {
      propertiesLocator.loadProperties(properties);
    }
    catch (IOException ex) {
      throw new IllegalStateException(ex);
    }
    return properties;
  }

  public void register() {
    Injector[] injectors = new Injector[modules.length];
    for (int i = 0; i < injectors.length; ++i) {
      injectors[i] = Guice.createInjector(modules[i]);
    }
    BeanFactoryRegistrar.registerBeanFactory(contextName, new GoogleGuiceBeanFactory(ignoreMissingDependency, injectors));
  }
}
