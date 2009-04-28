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
import org.objectweb.asm.Opcodes;

/**
 *
 * @author imyousuf
 */
public class ClassAnnotationVisitorImpl
    implements ClassVisitor {

    private String className = null;
    private boolean publicModifier = false;
    private VisitCallback<AnnotationConfig> callback;
    private String annotationNamePattern;
    private Pattern pattern;

    /**
     * Same as {@link ClassAnnotationVisitorImpl#ClassAnnotationVisitorImpl(com.smartitengineering.util.simple.reflection.VisitCallback, java.lang.String)}
     * where annotationNamePattern is NULL.
     * @see ClassAnnotationVisitorImpl#ClassAnnotationVisitorImpl(com.smartitengineering.util.simple.reflection.VisitCallback, java.lang.String) 
     */
    public ClassAnnotationVisitorImpl(VisitCallback<AnnotationConfig> callback)
        throws IllegalArgumentException {
        this(callback, null);
    }

    /**
     * Construct a class visitor which will invoke a callback whenever it
     * receives an annotation which matches the pattern, null pattern means
     * matching all.
     * @param callback Callback to invoke 
     * @param annotationNamePattern Pattern to check annotation name against
     * @throws IllegalArgumentException Iff callback is null.
     */
    public ClassAnnotationVisitorImpl(VisitCallback<AnnotationConfig> callback,
                                      String annotationNamePattern)
        throws IllegalArgumentException {
        if (callback == null) {
            throw new IllegalArgumentException();
        }
        this.callback = callback;
        this.annotationNamePattern = annotationNamePattern;
        if (annotationNamePattern == null) {
            pattern = Pattern.compile(".*", Pattern.DOTALL);
        }
        else {
            pattern = Pattern.compile(annotationNamePattern);
        }
    }

    public void visit(int version,
                      int access,
                      String name,
                      String signature,
                      String superName,
                      String[] interfaces) {
        publicModifier = (Opcodes.ACC_PUBLIC & access) == 1;
        className = name;
    }

    public AnnotationVisitor visitAnnotation(String desc,
                                             boolean visible) {
        if (publicModifier) {
            if (pattern.matcher(desc).matches()) {
                AnnotationConfig config = new AnnotationConfig();
                config.setAnnotationName(desc);
                config.setClassName(className);
                callback.handle(config);
            }
        }
        return null;
    }

    public void visitAttribute(Attribute arg0) {
    }

    public void visitEnd() {
    }

    public FieldVisitor visitField(int arg0,
                                   String arg1,
                                   String arg2,
                                   String arg3,
                                   Object arg4) {
        return null;
    }

    public void visitInnerClass(String arg0,
                                String arg1,
                                String arg2,
                                int arg3) {
    }

    public MethodVisitor visitMethod(int arg0,
                                     String arg1,
                                     String arg2,
                                     String arg3,
                                     String[] arg4) {
        return null;
    }

    public void visitOuterClass(String arg0,
                                String arg1,
                                String arg2) {
    }

    public void visitSource(String arg0,
                            String arg1) {
    }
}
