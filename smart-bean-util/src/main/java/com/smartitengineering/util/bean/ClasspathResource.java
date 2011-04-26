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
package com.smartitengineering.util.bean;

import java.io.InputStream;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author imyousuf
 * @since 0.2
 */
public class ClasspathResource
    implements Resource {

    private final String resourcePath;

    public ClasspathResource(final String resourcePath) {
        if (StringUtils.isBlank(resourcePath) || !resourcePath.startsWith(
            PropertiesLocator.CLASSPATH_RESOURCE_PREFIX) ||
            resourcePath.length() <=
            PropertiesLocator.CLASSPATH_RESOURCE_PREFIX.length()) {
            throw new IllegalArgumentException("Resource path must begin with " +
                PropertiesLocator.CLASSPATH_RESOURCE_PREFIX);
        }
        this.resourcePath = resourcePath;
    }

    public String getFilename() {
        return resourcePath.substring(PropertiesLocator.CLASSPATH_RESOURCE_PREFIX.
            length());
    }

    public InputStream getInputStream() {
        return getClassLoader().getResourceAsStream(getFilename());
    }

    public boolean exists() {
        return getClassLoader().getResource(getFilename()) != null;
    }

  protected ClassLoader getClassLoader() {
    return Thread.currentThread().getContextClassLoader();
  }

    public boolean isReadable() {
        return exists();
    }
}
