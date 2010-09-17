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
package com.smartitengineering.util.simple;

import com.smartitengineering.util.simple.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;
import junit.framework.TestCase;

/**
 *
 * @author imyousuf
 */
public class BufferedInputStreamTest extends TestCase {

  private byte[] testData;
  private int bufferSize;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < 2000; ++i) {
      builder.append("test string");
    }
    testData = builder.toString().getBytes();
    bufferSize = testData.length / 3;
  }

  public void testBufferRead() {
    System.out.println(":::::: Test Read BufferedInputStream ::::::");
    BufferedInputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(testData), bufferSize);
    byte[] readData = new byte[testData.length];
    try {
      inputStream.read(readData);
    }
    catch (IOException ex) {
      ex.printStackTrace();
      fail(ex.getMessage());
    }
    assertTrue(Arrays.equals(testData, readData));
  }

  public void testClose() {
    InputStream dataStream = null;
    try {
      dataStream = new FileInputStream("pom.xml");
      BufferedInputStream inputStream = new BufferedInputStream(dataStream, bufferSize);
      assertNotNull(inputStream.read());
      inputStream.close();
    }
    catch (IOException ex) {
      ex.printStackTrace();
      fail(ex.getMessage());
    }
    try {
      dataStream.read();
      fail("Underlying stream not closed!");
    }
    catch (IOException ex) {
      //expected!
    }
  }

  public void testMark() {
    BufferedInputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(testData), bufferSize);
    byte[] firstChunk = new byte[bufferSize / 3];
    try {
      inputStream.read(firstChunk);
    }
    catch (IOException ex) {
      ex.printStackTrace();
      fail(ex.getMessage());
    }
    assertTrue(Arrays.equals(Arrays.copyOfRange(testData, 0, firstChunk.length), firstChunk));
    byte[] restData = new byte[testData.length - firstChunk.length];
    inputStream.mark(new Random().nextInt());
    try {
      inputStream.read(restData);
    }
    catch (IOException ex) {
      ex.printStackTrace();
      fail(ex.getMessage());
    }
    assertTrue(Arrays.equals(Arrays.copyOfRange(testData, firstChunk.length, testData.length), restData));
    try {
      inputStream.reset();
      inputStream.read(restData);
    }
    catch (IOException ex) {
      ex.printStackTrace();
      fail(ex.getMessage());
    }
    assertTrue(Arrays.equals(Arrays.copyOfRange(testData, firstChunk.length, testData.length), restData));
  }
}
