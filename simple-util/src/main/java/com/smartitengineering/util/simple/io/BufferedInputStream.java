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

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang.mutable.MutableInt;

/**
 *
 * @author imyousuf
 */
public class BufferedInputStream extends InputStream {

  private final LinkedHashMap<ByteBuffer, MutableInt> buffers;
  private final InputStream wrappedStream;
  private final int bufferSegmentSize;
  private ByteBuffer currentBuffer;
  private ByteBuffer markedBuffer;
  private int markedPosition;
  private boolean eofReached;

  public BufferedInputStream(InputStream wrappedStream) {
    this(wrappedStream, Integer.MAX_VALUE / 2);
  }

  public BufferedInputStream(InputStream wrappedStream, int bufferSegmentSize) {
    if (wrappedStream == null) {
      throw new IllegalArgumentException("Wrapped Stream can not be null!");
    }
    if (bufferSegmentSize <= 0) {
      throw new IllegalArgumentException("Buffer segment size must be positive!");
    }
    this.buffers = new LinkedHashMap<ByteBuffer, MutableInt>();
    this.wrappedStream = wrappedStream;
    this.bufferSegmentSize = bufferSegmentSize;
    this.currentBuffer = null;
    this.eofReached = false;
  }

  @Override
  public int read() throws IOException {
    ByteBuffer buffer = getCurrentBuffer();
    if (available() > 0) {
      return buffer.get();
    }
    int remaining = buffer.remaining();
    if (remaining <= 0) {
      if (hasNextBuffer()) {
        currentBuffer = nextBuffer();
        return read();
      }
      else if (eofReached) {
        return -1;
      }
      else {
        remaining = initializeNewBuffer();
        buffer = getCurrentBuffer();
      }
    }
    byte[] readBuffer = new byte[remaining];
    int read = wrappedStream.read(readBuffer);
    if (read > 0) {
      int position = buffer.position();
      buffer.put(readBuffer, 0, read);
      buffer.position(position);
      get(buffer).add(read);
      return buffer.get();
    }
    else {
      eofReached = true;
      return -1;
    }
  }

  @Override
  public void close() throws IOException {
    super.close();
    wrappedStream.close();
  }

  protected int initializeNewBuffer() {
    int remaining;
    final int capacity;
    if (bufferSegmentSize > 100) {
      capacity = bufferSegmentSize;
    }
    else {
      capacity = bufferSegmentSize / 10;
    }
    currentBuffer = ByteBuffer.allocate(capacity);
    currentBuffer.limit(bufferSegmentSize);
    remaining = currentBuffer.remaining();
    buffers.put(currentBuffer, new MutableInt(0));
    return remaining;
  }

  @Override
  public int available() throws IOException {
    return get(getCurrentBuffer()).intValue() - getCurrentBuffer().position();
  }

  protected ByteBuffer getCurrentBuffer() {
    if (currentBuffer == null) {
      initializeNewBuffer();
    }
    return currentBuffer;
  }

  @Override
  public synchronized void mark(int readlimit) {
    markedBuffer = getCurrentBuffer();
    markedPosition = getCurrentBuffer().position();
  }

  @Override
  public boolean markSupported() {
    return true;
  }

  protected boolean hasNextBuffer() {
    if (currentBuffer == null) {
      return false;
    }
    Iterator<ByteBuffer> bufferIterator = buffers.keySet().iterator();
    while (bufferIterator.hasNext()) {
      ByteBuffer buffer = bufferIterator.next();
      if (buffer == currentBuffer) {
        return bufferIterator.hasNext();
      }
    }
    return false;
  }

  protected ByteBuffer nextBuffer() {
    if (currentBuffer == null) {
      return null;
    }
    Iterator<ByteBuffer> bufferIterator = buffers.keySet().iterator();
    while (bufferIterator.hasNext()) {
      ByteBuffer buffer = bufferIterator.next();
      if (buffer == currentBuffer) {
        if (bufferIterator.hasNext()) {
          final ByteBuffer next = bufferIterator.next();
          next.rewind();
          return next;
        }
        else {
          return null;
        }
      }
    }
    return null;
  }

  @Override
  public synchronized void reset() throws IOException {
    Set<ByteBuffer> bufferSet = buffers.keySet();
    for (ByteBuffer buffer : bufferSet) {
      if (buffer == markedBuffer) {
        currentBuffer = markedBuffer;
        currentBuffer.position(markedPosition);
        return;
      }
    }
  }

  protected MutableInt get(ByteBuffer buffer) {
    Set<Entry<ByteBuffer, MutableInt>> bufferSet = buffers.entrySet();
    for (Entry<ByteBuffer, MutableInt> entry : bufferSet) {
      if (buffer == entry.getKey()) {
        return entry.getValue();
      }
    }
    return null;
  }
}
