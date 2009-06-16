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

import java.util.regex.Pattern;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * This implementation tests for direct super interface or class of a class;
 * @author imyousuf
 */
public class ClassInstanceVisitorImpl
    implements ClassVisitor {

    private String directParentNamePattern;
    private VisitCallback<Config> visitCallback;
    private Pattern pattern;

    public ClassInstanceVisitorImpl(VisitCallback<Config> visitCallback) {
        this(null, visitCallback);
    }

    public ClassInstanceVisitorImpl(String directParentNamePattern,
                                    VisitCallback<Config> visitCallback) {
        if (visitCallback == null) {
            throw new IllegalArgumentException();
        }
        this.directParentNamePattern = directParentNamePattern;
        this.visitCallback = visitCallback;
        if (this.directParentNamePattern == null) {
            pattern = Pattern.compile(".*", Pattern.DOTALL);
        }
        else {
            pattern = Pattern.compile(this.directParentNamePattern);
        }
    }

    public void visit(int version,
                      int access,
                      String name,
                      String signature,
                      String superName,
                      String[] interfaces) {
        boolean matches = false;
        matches = matches || pattern.matcher(superName).matches();
        for (String superInterface : interfaces) {
            matches = matches || pattern.matcher(superInterface).matches();
        }
        if (matches) {
            Config config = new Config();
            config.setClassName(name);
            config.setSuperClassName(superName);
            config.setInterfaces(interfaces);
            visitCallback.handle(config);
        }
    }

    public void visitSource(String arg0,
                            String arg1) {
    }

    public void visitOuterClass(String arg0,
                                String arg1,
                                String arg2) {
    }

    public AnnotationVisitor visitAnnotation(String arg0,
                                             boolean arg1) {
        return null;
    }

    public void visitAttribute(Attribute arg0) {
    }

    public void visitInnerClass(String arg0,
                                String arg1,
                                String arg2,
                                int arg3) {
    }

    public FieldVisitor visitField(int arg0,
                                   String arg1,
                                   String arg2,
                                   String arg3,
                                   Object arg4) {
        return null;
    }

    public MethodVisitor visitMethod(int arg0,
                                     String arg1,
                                     String arg2,
                                     String arg3,
                                     String[] arg4) {
        return null;
    }

    public void visitEnd() {
    }
}
