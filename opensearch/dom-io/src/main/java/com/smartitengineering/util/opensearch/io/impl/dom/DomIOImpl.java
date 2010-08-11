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
package com.smartitengineering.util.opensearch.io.impl.dom;

import com.smartitengineering.util.opensearch.api.OpenSearchDescriptor;
import com.smartitengineering.util.opensearch.api.io.OpenSearchIO;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DomIOImpl implements OpenSearchIO {

  public void writeOpenSearchDescriptor(OutputStream outputStream, OpenSearchDescriptor descriptor) throws IOException {
    OpenSearchDescriptorWriter writer = new OpenSearchDescriptorWriter(outputStream, descriptor);
    writer.write();
  }

  public OpenSearchDescriptor readOpenSearchDescriptor(InputStream inputStream) throws IOException {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
