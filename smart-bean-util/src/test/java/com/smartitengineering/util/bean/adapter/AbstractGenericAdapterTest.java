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

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import junit.framework.TestCase;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

/**
 *
 * @author imyousuf
 */
public class AbstractGenericAdapterTest extends TestCase {

  private final Mockery mockery = new Mockery() {

    {
      setImposteriser(ClassImposteriser.INSTANCE);
    }
  };
  final From mockedFrom = mockery.mock(From.class);
  final To mockedTo = mockery.mock(To.class);

  public AbstractGenericAdapterTest(String testName) {
    super(testName);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  /**
   * Test of convert method, of class GenericAdapterImpl.
   */
  public void testConvert_1args_1() {
    final From[] fromBeans = new From[]{mockedFrom, mockedFrom, mockedFrom,};

    final AbstractAdapterHelper<From, To> instance = mockery.mock(AbstractAdapterHelper.class);
    GenericAdapterImpl<From, To> adapter = new GenericAdapterImpl<From, To>();
    adapter.setHelper(instance);
    mockery.checking(new Expectations() {

      {
        exactly(fromBeans.length).of(instance).newTInstance();
        will(returnValue(mockedTo));
        exactly(fromBeans.length).of(instance).mergeFromF2T(mockedFrom, mockedTo);
      }
    });
    Collection result = adapter.convert(fromBeans);
    assertNotNull(result);
    assertTrue(result.size() == fromBeans.length);
    for (Object object : result) {
      assertSame(mockedTo, object);
    }
    assertTrue(adapter.convert((From[]) null).size() == 0);
    assertTrue(adapter.convert(new From[]{}).size() == 0);
  }

  /**
   * Test of convertInversely method, of class GenericAdapterImpl.
   */
  public void testConvertInversely_1args_1() {
    final To[] toBeans = new To[]{mockedTo, mockedTo};
    final AbstractAdapterHelper<From, To> instance = mockery.mock(AbstractAdapterHelper.class);
    GenericAdapterImpl<From, To> adapter = new GenericAdapterImpl<From, To>();
    adapter.setHelper(instance);
    mockery.checking(new Expectations() {

      {
        exactly(toBeans.length).of(instance).convertFromT2F(mockedTo);
        will(returnValue(mockedFrom));
      }
    });
    Collection<From> result = adapter.convertInversely(toBeans);
    assertNotNull(result);
    assertTrue(result.size() == toBeans.length);
    for (Object object : result) {
      assertSame(mockedFrom, object);
    }
    assertTrue(adapter.convertInversely((To[]) null).size() == 0);
    assertTrue(adapter.convertInversely(new To[]{}).size() == 0);
  }

  /**
   * Test of convert method, of class GenericAdapterImpl.
   */
  public void testConvert_1args_2() {
    From fromBean = mockedFrom;
    final AbstractAdapterHelper<From, To> instance = mockery.mock(AbstractAdapterHelper.class);
    GenericAdapterImpl<From, To> adapter = new GenericAdapterImpl<From, To>();
    adapter.setHelper(instance);
    mockery.checking(new Expectations() {

      {
        exactly(1).of(instance).newTInstance();
        will(returnValue(mockedTo));
        exactly(1).of(instance).mergeFromF2T(mockedFrom, mockedTo);
      }
    });
    Object expResult = mockedTo;
    Object result = adapter.convert(fromBean);
    assertSame(expResult, result);
    assertNull(adapter.convert((From) null));
  }

  /**
   * Test of convertInversely method, of class GenericAdapterImpl.
   */
  public void testConvertInversely_1args_2() {
    To toBean = mockedTo;
    final AbstractAdapterHelper<From, To> instance = mockery.mock(AbstractAdapterHelper.class);
    GenericAdapterImpl<From, To> adapter = new GenericAdapterImpl<From, To>();
    adapter.setHelper(instance);
    mockery.checking(new Expectations() {

      {
        exactly(1).of(instance).convertFromT2F(mockedTo);
        will(returnValue(mockedFrom));
      }
    });
    Object expResult = mockedFrom;
    Object result = adapter.convertInversely(toBean);
    assertSame(expResult, result);
    assertNull(adapter.convertInversely((To) null));
  }

  /**
   * Test of merge method, of class GenericAdapterImpl.
   */
  public void testMerge_MapEntry() {
    Entry<From, To> bean = new SimpleEntry<From, To>(mockedFrom, mockedTo);
    final AbstractAdapterHelper<From, To> instance = mockery.mock(AbstractAdapterHelper.class);
    GenericAdapterImpl<From, To> adapter = new GenericAdapterImpl<From, To>();
    adapter.setHelper(instance);
    mockery.checking(new Expectations() {

      {
        exactly(1).of(instance).mergeFromF2T(mockedFrom, mockedTo);
      }
    });
    adapter.merge(bean);
  }

  /**
   * Test of merge method, of class GenericAdapterImpl.
   */
  public void testMerge_Collection() {
    final Collection<Entry<From, To>> beans = new ArrayList<Entry<From, To>>();
    beans.add(new SimpleEntry<From, To>(mockedFrom, mockedTo));
    beans.add(new SimpleEntry<From, To>(mockedFrom, mockedTo));
    beans.add(new SimpleEntry<From, To>(mockedFrom, mockedTo));
    final AbstractAdapterHelper<From, To> instance = mockery.mock(AbstractAdapterHelper.class);
    GenericAdapterImpl<From, To> adapter = new GenericAdapterImpl<From, To>();
    adapter.setHelper(instance);
    mockery.checking(new Expectations() {

      {
        exactly(beans.size()).of(instance).mergeFromF2T(mockedFrom, mockedTo);
      }
    });
    adapter.merge(beans);
  }

  private class From {
  }

  private class To {
  }
}
