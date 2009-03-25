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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author imyousuf
 * @since 0.2
 */
public class FileSystemResource
    implements Resource {

    private final String filePath;
    private final File file;

    public FileSystemResource(final String filePath) {
        if (StringUtils.isBlank(filePath)) {
            throw new IllegalArgumentException("File path must not be blank");
        }
        this.filePath = filePath;
        file = new File(filePath);
    }

    public String getFilename() {
        return file.getAbsolutePath();
    }

    public boolean exists() {
        return file.exists();
    }

    public InputStream getInputStream() {
        if (file.exists()) {
            try {
                return new BufferedInputStream(new FileInputStream(file));
            }
            catch (Exception ex) {
                return null;
            }
        }
        else {
            return null;
        }
    }

    public boolean isReadable() {
        return exists() && file.canRead();
    }
}
