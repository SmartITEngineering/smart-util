package com.smartitengineering.util.opensearch.io.impl.dom;

import com.smartitengineering.util.opensearch.api.OpenSearchDescriptor;
import com.smartitengineering.util.opensearch.api.io.OpenSearchIO;
import java.io.InputStream;
import java.io.OutputStream;

public class DomIOImpl implements OpenSearchIO {

  public void writeOpenSearchDescriptor(OutputStream outputStream, OpenSearchDescriptor descriptor) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public OpenSearchDescriptor readOpenSearchDescriptor(InputStream inputStream) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
