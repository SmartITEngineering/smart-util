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
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.commons.lang.IncompleteArgumentException;
import org.apache.commons.lang.StringUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

/**
 * Default implementation of {@link ClassScanner}.
 * @author imyousuf
 * @since 0.2
 */
public class DefaultClassScannerImpl
    implements ClassScanner {

    private ClassLoader classLoader;

    public DefaultClassScannerImpl() {
        classLoader = Thread.currentThread().getContextClassLoader();
    }

    public void scan(String[] packages,
                     ClassVisitor classVisitor)
        throws IllegalArgumentException {
        if (packages == null || packages.length <= 0 || classVisitor == null) {
            throw new IncompleteArgumentException("packages and/or classVisitor");
        }
        for (String pckg : packages) {
            if (StringUtils.isBlank(pckg)) {
                continue;
            }
            try {
                String filePath = pckg.replace('.', '/');
                Enumeration<URL> urls = classLoader.getResources(filePath);
                while (urls.hasMoreElements()) {
                    URL url = urls.nextElement();
                    try {
                        URI uri = getURI(url);
                        scanForVisit(uri, filePath, classVisitor);
                    }
                    catch (URISyntaxException e) {
                    }
                }
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void scan(File[] paths,
                     ClassVisitor classVisitor)
        throws IllegalArgumentException {
        if (paths == null || paths.length <= 0 || classVisitor == null) {
            throw new IncompleteArgumentException("paths and/or classVisitor");
        }
        for (File file : paths) {
            scanForVisit(file, classVisitor);
        }
    }

    private URI getURI(URL url)
        throws URISyntaxException {
        if (url.getProtocol().equalsIgnoreCase("vfsfile")) {
            // Used with JBoss 5.x: trim prefix "vfs"
            // This code learnt and followed from project Jersey
            return new URI(url.toString().substring(3));
        }
        else {
            return url.toURI();
        }
    }

    private void scanForVisit(File file,
                              ClassVisitor classVisitor) {
        if (file.isDirectory()) {
            scanDir(file, true, classVisitor);
        }
        else if (file.getName().endsWith(".jar") ||
            file.getName().endsWith(".zip")) {
            scanJar(file, classVisitor);
        }
        else {
        }
    }

    private void scanForVisit(URI u,
                              String filePackageName,
                              ClassVisitor classVisitor) {
        String scheme = u.getScheme();
        if (scheme.equals("file")) {
            File f = new File(u.getPath());
            if (f.isDirectory()) {
                scanDir(f, false, classVisitor);
            }
            else {
            }
        }
        else if (scheme.equals("jar") || scheme.equals("zip")) {
            URI jarUri = URI.create(u.getRawSchemeSpecificPart());
            String jarFile = jarUri.getPath();
            jarFile = jarFile.substring(0, jarFile.indexOf('!'));
            scanJar(new File(jarFile), filePackageName, classVisitor);
        }
        else {
        }
    }

    private void scanDir(File root,
                         boolean indexJars,
                         ClassVisitor classVisitor) {
        for (File child : root.listFiles()) {
            if (child.isDirectory()) {
                scanDir(child, indexJars, classVisitor);
            }
            else if (indexJars && child.getName().endsWith(".jar")) {
                scanJar(child, classVisitor);
            }
            else if (child.getName().endsWith(".class")) {
                visitClassFile(child.toURI(), classVisitor);
            }
        }
    }

    private void scanJar(File jarFile,
                         ClassVisitor classVisitor) {
        scanJar(jarFile, "", classVisitor);
    }

    private void scanJar(File jarFile,
                         String parentPath,
                         ClassVisitor classVisitor) {
        final JarFile jar = getJarFile(jarFile);
        try {
            final Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry e = entries.nextElement();
                if (!e.isDirectory() && e.getName().startsWith(parentPath) &&
                    e.getName().endsWith(".class")) {
                    visitClassFile(jar, e, classVisitor);
                }
            }
        }
        catch (Exception e) {
        }
        finally {
            try {
                if (jar != null) {
                    jar.close();
                }
            }
            catch (IOException ex) {
            }
        }
    }

    private JarFile getJarFile(File file) {
        if (file == null) {
            return null;
        }
        try {
            return new JarFile(file);
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void visitClassFile(URI classFileUri,
                                ClassVisitor classVisitor) {
        ClassReader reader = getClassReader(classFileUri);
        startVisit(reader, classVisitor);
    }

    private void visitClassFile(JarFile jarFile,
                                JarEntry entry,
                                ClassVisitor classVisitor) {
        ClassReader reader = getClassReader(jarFile, entry);
        startVisit(reader, classVisitor);
    }

    private void startVisit(ClassReader reader,
                            ClassVisitor classVisitor) {
        if (reader != null && classVisitor != null) {
            reader.accept(classVisitor, 0);
        }
    }

    private ClassReader getClassReader(JarFile jarFile,
                                       JarEntry entry) {
        InputStream is = null;
        try {
            is = jarFile.getInputStream(entry);
            ClassReader cr = new ClassReader(is);
            return cr;
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        finally {
            try {
                if (is != null) {
                    is.close();
                }
            }
            catch (IOException ex) {
            }
        }
    }

    private ClassReader getClassReader(URI classFileUri) {
        InputStream is = null;
        try {
            is = classFileUri.toURL().openStream();
            ClassReader cr = new ClassReader(is);
            return cr;
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        finally {
            try {
                if (is != null) {
                    is.close();
                }
            }
            catch (IOException ex) {
            }
        }
    }
}
