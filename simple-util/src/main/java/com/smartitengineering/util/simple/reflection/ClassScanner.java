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
package com.smartitengineering.util.simple.reflection;

import java.io.File;
import org.objectweb.asm.ClassVisitor;

/**
 * An API for scanning classes.
 * @author imyousuf
 * @since 0.2
 */
public interface ClassScanner {

    /**
     * Scan specified packages and use the {@link ClassVisitor} provided to
     * gather necessary information.
     * @param packages The packages to scan classes for.
     * @param classVisitor The visitor which will visit the classes in the
     *                      packages for extracting the information it needs.
     * @throws java.lang.IllegalArgumentException If either of them is null or
     *                                              packages is empty.
     */
    void scan(final String[] packages,
              final ClassVisitor classVisitor)
        throws IllegalArgumentException;

    /**
     * Scan specified paths and use the {@link ClassVisitor} provided to gather
     * necessary information.
     * @param paths The paths to scan classes for.
     * @param classVisitor The visitor which will visit the classes in the
     *                      paths for extracting the information it needs.
     * @throws java.lang.IllegalArgumentException If either parameter is null or
     *                                              paths is empty.
     */
    void scan(final File[] paths,
              final ClassVisitor classVisitor)
        throws IllegalArgumentException;
}
