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

import com.smartitengineering.util.bean.BeanFactoryRegistrar;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author imyousuf
 */
public class MockServlet
    extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req,
                       HttpServletResponse resp)
      throws ServletException,
             IOException {
    if(BeanFactoryRegistrar.getBeanFactorForContext("testContext") != null) {
      resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
    else {
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }

}
