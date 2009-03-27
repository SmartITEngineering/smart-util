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
package com.smartitengineering.util.simple;

import com.smartitengineering.util.simple.data.TestClass;
import com.smartitengineering.util.simple.reflection.ClassScanner;
import java.io.File;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Unit test for simple App.
 */
public class DefaultClassScannerImplTest
    extends TestCase {

    private ClassScanner classScanner;
    private ClassVisitor classVisitor;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DefaultClassScannerImplTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp()
        throws Exception {
        super.setUp();
        classScanner = IOFactory.getDefaultClassScanner();
        classVisitor = new ClassVisitor() {

            public void visit(int verison,
                              int scope,
                              String name,
                              String signature,
                              String supername,
                              String[] interfaces) {
                assertEquals((scope & Opcodes.ACC_PUBLIC), Opcodes.ACC_PUBLIC);
                assertEquals(name, TestClass.class.getName().replaceAll("\\.",
                    "/"));
                System.out.println();
            }

            public void visitSource(String arg0,
                                    String arg1) {
            }

            public void visitOuterClass(String arg0,
                                        String arg1,
                                        String arg2) {
            }

            public AnnotationVisitor visitAnnotation(String desc,
                                                     boolean visible) {
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
        };
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(DefaultClassScannerImplTest.class);
    }

    public void testScanPackage() {
        classScanner.scan(
            new String[]{"com.smartitengineering.util.simple.data"},
            classVisitor);
    }

    public void testScanLocation() {
        classScanner.scan(
            new File[]{new File(
                "target/test-classes/com/smartitengineering/util/simple/data")
            }, classVisitor);
    }
}
