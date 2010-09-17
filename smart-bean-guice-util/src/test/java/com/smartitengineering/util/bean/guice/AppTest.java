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
import com.smartitengineering.util.bean.guice.data.SomeDefaultAPI;
import com.smartitengineering.util.bean.guice.data.SomeOtherAPI;
import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

  public AppTest(String testName) {
    super(testName);
  }

  public void testApp() {
    GuiceUtil.getInstance("module-config.properties").register();
    GuiceUtil.getInstance().register();
    assertNotNull(SomeDefaultAPI.getInstance().getFirstBean());
    assertNotNull(SomeDefaultAPI.getInstance().getSecondBean());
    assertNotNull(SomeDefaultAPI.getInstance().getFirstBean().getThirdBean());
    assertNotNull(SomeDefaultAPI.getInstance().getFirstBean().getFourthBean());
    assertNotNull(SomeDefaultAPI.getInstance().getSecondBean().getThirdBean());
    assertNotNull(SomeDefaultAPI.getInstance().getSecondBean().getFourthBean());
    assertNotNull(SomeOtherAPI.getInstance().getFirstBean());
    assertNotNull(SomeOtherAPI.getInstance().getSecondBean());
    assertNotNull(SomeOtherAPI.getInstance().getFirstBean().getThirdBean());
    assertNotNull(SomeOtherAPI.getInstance().getFirstBean().getFourthBean());
    assertNotNull(SomeOtherAPI.getInstance().getSecondBean().getThirdBean());
    assertNotNull(SomeOtherAPI.getInstance().getSecondBean().getFourthBean());
    assertNull(SomeOtherAPI.getInstance().getErrorBean());
    SomeOtherAPI.api = null;
    GuiceUtil.getInstance("module-config-error.properties").register();
    try {
      SomeOtherAPI.getInstance().getFirstBean();
      fail("Ignore not working!");
    }
    catch (ConfigurationException exception) {
      //Expected;
    }
  }
}
