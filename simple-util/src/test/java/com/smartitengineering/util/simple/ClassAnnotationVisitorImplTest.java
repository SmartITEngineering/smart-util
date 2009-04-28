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
import com.smartitengineering.util.simple.reflection.AnnotationConfig;
import com.smartitengineering.util.simple.reflection.ClassAnnotationVisitorImpl;
import com.smartitengineering.util.simple.reflection.ClassScanner;
import com.smartitengineering.util.simple.reflection.VisitCallback;
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
public class ClassAnnotationVisitorImplTest
    extends TestCase {

    private Mockery context = new JUnit3Mockery();
    private VisitCallback<AnnotationConfig> mockVisitCallback;

    public ClassAnnotationVisitorImplTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(ClassAnnotationVisitorImplTest.class);
    }

    public void testVisitorCreationWithNullCallback() {
        try {
            new ClassAnnotationVisitorImpl(null);
            fail("Should not succeed!");
        }
        catch (IllegalArgumentException exception) {
        }
        try {
            new ClassAnnotationVisitorImpl(null, "**");
            fail("Should not succeed!");
        }
        catch (IllegalArgumentException exception) {
        }
    }

    public void testVisitorCreationWithNullAnnotationNamePattern() {
        mockVisitCallback = context.mock(VisitCallback.class);
        context.checking(new Expectations() {

            {
                AnnotationConfig config;
                config = new AnnotationConfig();
                String className = TestClass.class.getName().replaceAll("\\.",
                    "/");
                config.setClassName(className);
                config.setAnnotationName("Deprecated");
                exactly(2).of(mockVisitCallback).handle(with(equal(config)));
                config = new AnnotationConfig();
                config.setClassName(className);
                config.setAnnotationName("SuppressWarnings");
                exactly(2).of(mockVisitCallback).handle(with(equal(config)));
            }
        });
        ClassScanner scanner = IOFactory.getDefaultClassScanner();
        ClassVisitor classVisitor = new ClassAnnotationVisitorImpl(
            mockVisitCallback, null);
        scanner.scan(new String[]{"com.smartitengineering.util.simple.data"},
            classVisitor);
        classVisitor = new ClassAnnotationVisitorImpl(mockVisitCallback);
        scanner.scan(new String[]{"com.smartitengineering.util.simple.data"},
            classVisitor);
    }

    public void testVisitorCreationWithAnnotationNamePattern() {
        mockVisitCallback = context.mock(VisitCallback.class);
        context.checking(new Expectations() {

            {
                AnnotationConfig config;
                config = new AnnotationConfig();
                String className = TestClass.class.getName().replaceAll("\\.",
                    "/");
                config.setClassName(className);
                config.setAnnotationName("Deprecated");
                exactly(1).of(mockVisitCallback).handle(with(equal(config)));
            }
        });
        ClassScanner scanner = IOFactory.getDefaultClassScanner();
        ClassVisitor classVisitor = new ClassAnnotationVisitorImpl(
            mockVisitCallback, "Dep*");
        scanner.scan(new String[]{"com.smartitengineering.util.simple.data"},
            classVisitor);
    }
}
