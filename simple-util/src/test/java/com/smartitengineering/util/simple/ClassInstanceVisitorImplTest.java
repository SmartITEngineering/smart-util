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
import com.smartitengineering.util.simple.data.TestClass2;
import com.smartitengineering.util.simple.data.TestClass3;
import com.smartitengineering.util.simple.reflection.ClassInstanceVisitorImpl;
import com.smartitengineering.util.simple.reflection.ClassScanner;
import com.smartitengineering.util.simple.reflection.Config;
import com.smartitengineering.util.simple.reflection.VisitCallback;
import java.util.Comparator;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit3.JUnit3Mockery;
import org.objectweb.asm.ClassVisitor;

/**
 *
 * @author imyousuf
 */
public class ClassInstanceVisitorImplTest
    extends TestCase {

    private Mockery context = new JUnit3Mockery();
    private VisitCallback<Config> mockVisitCallback;

    public ClassInstanceVisitorImplTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(ClassInstanceVisitorImplTest.class);
    }

    public void testVisitorCreationWithNullCallback() {
        try {
            new ClassInstanceVisitorImpl(null);
            fail("Should not succeed!");
        }
        catch (IllegalArgumentException exception) {
        }
        try {
            new ClassInstanceVisitorImpl("**", null);
            fail("Should not succeed!");
        }
        catch (IllegalArgumentException exception) {
        }
    }

    public void testVisitorCreationWithNullParentNamePattern() {
        mockVisitCallback = context.mock(VisitCallback.class);
        context.checking(new Expectations() {

            {
                Config config;
                config = new Config();
                String className = IOFactory.getClassNameForVisitor(
                    TestClass.class);
                config.setClassName(className);
                exactly(2).of(mockVisitCallback).handle(with(equal(config)));
                config = new Config();
                className = IOFactory.getClassNameForVisitor(
                    TestClass2.class);
                config.setClassName(className);
                exactly(2).of(mockVisitCallback).handle(with(equal(config)));
                config = new Config();
                className = IOFactory.getClassNameForVisitor(
                    TestClass3.class);
                config.setClassName(className);
                exactly(2).of(mockVisitCallback).handle(with(equal(config)));
            }
        });
        ClassScanner scanner = IOFactory.getDefaultClassScanner();
        ClassVisitor classVisitor = new ClassInstanceVisitorImpl(
            null, mockVisitCallback);
        scanner.scan(new String[]{"com.smartitengineering.util.simple.data"},
            classVisitor);
        classVisitor = new ClassInstanceVisitorImpl(mockVisitCallback);
        scanner.scan(new String[]{"com.smartitengineering.util.simple.data"},
            classVisitor);
        context.assertIsSatisfied();
    }

    public void testVisitorCreationWithParentNamePattern() {
        mockVisitCallback = context.mock(VisitCallback.class);
        context.checking(new Expectations() {

            {
                Config config;
                config = new Config();
                String className = IOFactory.getClassNameForVisitor(
                    TestClass.class);
                config.setClassName(className);
                exactly(4).of(mockVisitCallback).handle(with(equal(config)));
                config = new Config();
                className = IOFactory.getClassNameForVisitor(
                    TestClass2.class);
                config.setClassName(className);
                exactly(3).of(mockVisitCallback).handle(with(equal(config)));
                config = new Config();
                className = IOFactory.getClassNameForVisitor(
                    TestClass3.class);
                config.setClassName(className);
                exactly(1).of(mockVisitCallback).handle(with(equal(config)));
            }
        });
        ClassScanner scanner = IOFactory.getDefaultClassScanner();
        ClassVisitor classVisitor = new ClassInstanceVisitorImpl(
            "java.lang".replaceAll("\\.", "/").concat(".*"), mockVisitCallback);
        scanner.scan(new String[]{"com.smartitengineering.util.simple.data"},
            classVisitor);
        classVisitor = new ClassInstanceVisitorImpl(
            "java.io".replaceAll("\\.", "/").concat(".*"), mockVisitCallback);
        scanner.scan(new String[]{"com.smartitengineering.util.simple.data"},
            classVisitor);
        classVisitor =
            new ClassInstanceVisitorImpl(
            "javax.swing".replaceAll("\\.", "/").concat(".*"), mockVisitCallback);
        scanner.scan(new String[]{"com.smartitengineering.util.simple.data"},
            classVisitor);
        classVisitor =
            new ClassInstanceVisitorImpl(
            IOFactory.getClassNameForVisitor(Comparator.class),
            mockVisitCallback);
        scanner.scan(new String[]{"com.smartitengineering.util.simple.data"},
            classVisitor);
        classVisitor =
            new ClassInstanceVisitorImpl(
            "com.smartitengineering".replaceAll("\\.", "/").concat(".*"),
            mockVisitCallback);
        scanner.scan(new String[]{"com.smartitengineering.util.simple.data"},
            classVisitor);
        context.assertIsSatisfied();
    }
}
