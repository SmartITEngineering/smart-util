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
package com.smartitengineering.util.opensearch.impl;

import com.smartitengineering.util.opensearch.api.Image;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;

/**
 * A builder that can be used to build an image
 * @author imyousuf
 */
public class ImageBuilder {

  private URI imageUri;
  private int height, width;
  private String mimeType;

  public static ImageBuilder getImageBuilder() {
    return new ImageBuilder();
  }

  protected ImageBuilder() {
  }

  public Image build() {
    if (imageUri == null) {
      throw new IllegalStateException("Image URI is must!");
    }
    ImageImpl image = new ImageImpl(imageUri);
    image.setHeight(height);
    image.setWidth(width);
    image.setMimeType(mimeType);
    return image;
  }

  public ImageBuilder height(int height) {
    this.height = height;
    return this;
  }

  public ImageBuilder width(int width) {
    this.width = width;
    return this;
  }

  public ImageBuilder mimeType(String mimeType) {
    this.mimeType = mimeType;
    return this;
  }

  public ImageBuilder imageUri(URI imageUri) {
    if (imageUri == null) {
      throw new IllegalArgumentException("Image URI can not be null!");
    }
    this.imageUri = imageUri;
    return this;
  }

  public ImageBuilder imageUri(String imageUri) {
    if (StringUtils.isBlank(imageUri)) {
      throw new IllegalArgumentException("Image URI can not be null!");
    }
    try {
      this.imageUri = new URI(imageUri);
    }
    catch (URISyntaxException ex) {
      throw new IllegalArgumentException(ex);
    }
    return this;
  }
}
