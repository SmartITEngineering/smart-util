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
package com.smartitengineering.util.simple.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

/**
 * Abstract class for bufferring the bytes of an input stream into writer. It
 * does not support mark and reset operation.
 * @author imyousuf
 * @since 0.1.1
 */
public class AbstractBufferInputStream
    extends FilterInputStream {

    private Writer buffer;
    private boolean init = false;

    protected AbstractBufferInputStream(final InputStream bufferedStream)
        throws IllegalArgumentException {
        super(bufferedStream);
        if (bufferedStream == null || buffer == null) {
            throw new IllegalArgumentException("Args can't be null!");
        }
    }

    protected void setBuffer(Writer buffer) {
        this.buffer = buffer;
        init = true;
    }
    
    protected void checkInit() {
        if(!init) {
            throw new IllegalStateException("Set buffer has to be called first!");
        }
    }

    public Writer getBuffer() {
        return buffer;
    }
    
    @Override
    public int read()
        throws IOException {
        checkInit();
        int read = super.read();
        if(read > 0) {
            buffer.write(read);
        }
        return read;
    }

    @Override
    public void close()
        throws IOException {
        checkInit();
        super.close();
        buffer.close();
    }

    @Override
    public int read(byte[] b)
        throws IOException {
        if(b == null || b.length < 1) {
            throw new IllegalArgumentException();
        }
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b,
                    int off,
                    int len)
        throws IOException {
        checkInit();
        int read = super.read(b, off, len);
        if(read > 0) {
            int count = Math.min(off + read, off + len);
            for(int i = off; i < count; ++i) {
                buffer.write(b[i]);
            }
        }
        return read;
    }

    @Override
    public synchronized void reset()
        throws IOException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public synchronized void mark(int readlimit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean markSupported() {
        return false;
    }

}
 