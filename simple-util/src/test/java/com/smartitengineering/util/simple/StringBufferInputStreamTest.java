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

import com.smartitengineering.util.simple.io.AbstractBufferInputStream;
import java.io.IOException;
import java.io.InputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author imyousuf
 */
public class StringBufferInputStreamTest
    extends TestCase {

    public StringBufferInputStreamTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(StringBufferInputStreamTest.class);
    }

    public void testBufferring() {
        String string = "test";
        InputStream stream = IOFactory.getStringInputStream(string);
        AbstractBufferInputStream bufferInputStream = IOFactory.
            getStringBufferingIOStream(stream);
        try {
            bufferInputStream.read();
            assertEquals(string.subSequence(0, 1), bufferInputStream.
                getBufferAsString());
            bufferInputStream.read(new byte[3], 0, 2);
            assertEquals(string.subSequence(0, 3), bufferInputStream.
                getBufferAsString());
            bufferInputStream.read();
            assertEquals(string, bufferInputStream.getBufferAsString());
        }
        catch (IOException ex) {
            fail(ex.getMessage());
        }
    }
}
