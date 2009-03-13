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

import java.io.InputStream;
import java.io.StringWriter;

/**
 * Buffers info read from an input stream into a string writer.
 * @author imyousuf
 * @since 0.1.1
 */
public class StringBufferInputStream
    extends AbstractBufferInputStream<StringWriter>
    implements ContentBuffer<StringWriter> {

    private StringWriter buffer;
    

    {
        buffer = new StringWriter();
    }

    public StringBufferInputStream(InputStream bufferedStream) {
        super(bufferedStream);
        setBuffer(buffer);
    }

    public String getBufferAsString() {
        return getBuffer().toString();
    }
}
