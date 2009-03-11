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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

public class StringInputStream extends InputStream
{

    private StringReader stringReader;
    private String string;
    private int readCount;
    private int lastMarked;
    
    public StringInputStream(final String string) {
        if(string == null) {
            throw new IllegalArgumentException();
        }
        stringReader = new StringReader(string);
        this.string = string;
        readCount = 0;
        lastMarked = 0;
    }
    
    @Override
    public int read()
        throws IOException {
        readCount++;
        return stringReader.read();
    }

    @Override
    public int available()
        throws IOException {
        int available = this.string.length() - readCount;
        return available > -1 ? available : 0;
    }

    @Override
    public void close()
        throws IOException {
        stringReader.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        try {
            stringReader.mark(readlimit);
            lastMarked = readCount;
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean markSupported() {
        return stringReader.markSupported();
    }

    @Override
    public int read(byte[] b)
        throws IOException {
        return super.read(b);
    }

    @Override
    public int read(byte[] b,
                    int off,
                    int len)
        throws IOException {
        return super.read(b, off, len);
    }

    @Override
    public synchronized void reset()
        throws IOException {
        stringReader.reset();
        readCount = lastMarked;
    }

    @Override
    public long skip(long n)
        throws IOException {
        return stringReader.skip(n);
    }
    
}
