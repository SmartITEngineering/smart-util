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

import java.io.Writer;

/**
 * Contents that will be buffered should implement this so that this can be used
 * to retrieve the writer and String representing the buffer.
 * @author imyousuf
 * @since 0.1.1
 */
public interface ContentBuffer {

    /**
     * Get the String representing the buffer.
     * @return String rep of the buffer
     */
    public String getBufferAsString();

    /**
     * Writer to which the the buffer is written to
     * @return Writer written to
     */
    public Writer getBuffer();
}
