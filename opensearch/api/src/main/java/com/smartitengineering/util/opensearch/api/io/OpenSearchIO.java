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
package com.smartitengineering.util.opensearch.api.io;

import com.smartitengineering.util.opensearch.api.OpenSearchDescriptor;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The Input Output API for Open Search Descriptor. The API intends to hide the underlying document format & complexity.
 * @author imyousuf
 */
public interface OpenSearchIO {

  /**
   * Write the open search descriptor as per specification to the output stream. The mime-type, format will be
   * determined as per the instance of the IO API.
   * @param outputStream The stream to write to
   * @param descriptor The descriptor to serialize
   */
  public void writeOpenSearchDescriptor(OutputStream outputStream, OpenSearchDescriptor descriptor);

  /**
   * Read the open search descriptor as per specification to the output stream. The mime-type, format will be determined
   * as per the instance of the IO API.
   * @param inputStream The input stream to read the descriptor data from
   * @return The deserialized descriptor
   */
  public OpenSearchDescriptor readOpenSearchDescriptor(InputStream inputStream);
}
