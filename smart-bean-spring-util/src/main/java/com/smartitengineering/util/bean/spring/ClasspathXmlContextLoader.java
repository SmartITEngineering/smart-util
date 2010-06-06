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
package com.smartitengineering.util.bean.spring;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author imyousuf
 */
public class ClasspathXmlContextLoader
    implements ServletContextListener {

  private ClassPathXmlApplicationContext applicationContext;

  public void contextInitialized(ServletContextEvent sce) {
    final String classpathAppContextLocation = sce.getServletContext().
        getInitParameter("classpathAppContextLocation");
    applicationContext = new ClassPathXmlApplicationContext(classpathAppContextLocation);
  }

  public void contextDestroyed(ServletContextEvent sce) {
    if (applicationContext != null) {
      applicationContext.destroy();
    }
  }
}
