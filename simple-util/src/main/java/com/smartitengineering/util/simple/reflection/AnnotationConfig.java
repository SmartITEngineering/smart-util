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

/**
 *
 * @author imyousuf
 */
public class AnnotationConfig
    extends Config {

    protected String annotationName;

    public String getAnnotationName() {
        return annotationName;
    }

    public void setAnnotationName(String annotationName) {
        this.annotationName = annotationName;
    }

    @Override
    public boolean equals(Object obj) {
        System.out.println(obj.getClass());
        if (obj instanceof AnnotationConfig && super.equals(obj)) {
            AnnotationConfig other = (AnnotationConfig) obj;
            System.out.println(other.getAnnotationName());
            if (this.annotationName == other.annotationName ||
                (this.annotationName != null && this.className.equals(
                other.className))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash =
            89 * hash +
            (this.annotationName != null ? this.annotationName.hashCode() : 0) +
            super.hashCode();
        return hash;
    }
}
