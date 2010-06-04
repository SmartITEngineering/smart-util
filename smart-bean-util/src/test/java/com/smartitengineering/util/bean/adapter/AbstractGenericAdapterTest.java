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
package com.smartitengineering.util.bean.adapter;

import java.util.Collection;
import java.util.Map.Entry;
import junit.framework.TestCase;

/**
 *
 * @author imyousuf
 */
public class AbstractGenericAdapterTest extends TestCase {
    
    public AbstractGenericAdapterTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

  /**
   * Test of convert method, of class AbstractGenericAdapter.
   */
  public void testConvert_1args_1() {
    System.out.println("convert");
    From[] fromBeans = null;
    AbstractGenericAdapter instance = new AbstractGenericAdapterImpl();
    Collection expResult = null;
    Collection result = instance.convert(fromBeans);
    assertNotNull(result);
  }

  /**
   * Test of convertInversely method, of class AbstractGenericAdapter.
   */
  public void testConvertInversely_1args_1() {
    System.out.println("convertInversely");
    To[] toBeans = null;
    AbstractGenericAdapter instance = new AbstractGenericAdapterImpl();
    Collection expResult = null;
    Collection result = instance.convertInversely(toBeans);
    assertNotNull(result);
  }

  /**
   * Test of convert method, of class AbstractGenericAdapter.
   */
  public void testConvert_1args_2() {
    System.out.println("convert");
    Object fromBean = null;
    AbstractGenericAdapter instance = new AbstractGenericAdapterImpl();
    Object expResult = null;
    Object result = instance.convert(fromBean);
    assertEquals(expResult, result);
  }

  /**
   * Test of convertInversely method, of class AbstractGenericAdapter.
   */
  public void testConvertInversely_1args_2() {
    System.out.println("convertInversely");
    Object toBean = null;
    AbstractGenericAdapter instance = new AbstractGenericAdapterImpl();
    Object expResult = null;
    Object result = instance.convertInversely(toBean);
    assertEquals(expResult, result);
  }

  /**
   * Test of merge method, of class AbstractGenericAdapter.
   */
  public void testMerge_MapEntry() {
    System.out.println("merge");
    Entry<From, To> bean = null;
    AbstractGenericAdapter instance = new AbstractGenericAdapterImpl();
    instance.merge(bean);
  }

  /**
   * Test of merge method, of class AbstractGenericAdapter.
   */
  public void testMerge_Collection() {
    System.out.println("merge");
    Collection<Entry<From, To>> beans = null;
    AbstractGenericAdapter instance = new AbstractGenericAdapterImpl();
    instance.merge(beans);
  }

  private class From{}
  private class To{}

  private class AbstractGenericAdapterImpl extends AbstractGenericAdapter<From, To> {

    @Override
    protected To newTInstance() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void mergeFromF2T(From fromBean, To toBean) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected From convertFromT2F(To toBean) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

  }

}
