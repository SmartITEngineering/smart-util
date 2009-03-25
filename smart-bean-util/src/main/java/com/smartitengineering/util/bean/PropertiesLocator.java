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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

/**
 *
 * @author imyousuf
 */
public class PropertiesLocator {

    public static final String DEFAULT_RESOURCE_SUFFIX = ".template";
    private String defaultResourceSuffix =
        PropertiesLocator.DEFAULT_RESOURCE_SUFFIX;
    private String[] smartLocations;
    private boolean defaultSearchEnabled = true;
    private boolean classpathSearchEnabled = true;
    private boolean currentDirSearchEnabled = true;
    private boolean userHomeSearchEnabled = true;
    private String resourceContext;
    private String[] searchLocations;
    private PropertiesPersister myPropertiesPersister =
        new DefaultPropertiesPersister();
    private String fileEncoding = null;

    public boolean isClasspathSearchEnabled() {
        return classpathSearchEnabled;
    }

    public void setClasspathSearchEnabled(boolean classpathSearchEnabled) {
        this.classpathSearchEnabled = classpathSearchEnabled;
    }

    public boolean isCurrentDirSearchEnabled() {
        return currentDirSearchEnabled;
    }

    public void setCurrentDirSearchEnabled(boolean currentDirSearchEnabled) {
        this.currentDirSearchEnabled = currentDirSearchEnabled;
    }

    public String getDefaultResourceSuffix() {
        return defaultResourceSuffix;
    }

    public void setDefaultResourceSuffix(String defaultResourceSuffix) {
        this.defaultResourceSuffix = defaultResourceSuffix;
    }

    public boolean isDefaultSearchEnabled() {
        return defaultSearchEnabled;
    }

    public void setDefaultSearchEnabled(boolean defaultSearchEnabled) {
        this.defaultSearchEnabled = defaultSearchEnabled;
    }

    public String getResourceContext() {
        if (resourceContext == null) {
            return "";
        }
        return resourceContext;
    }

    public void setResourceContext(String resourceContext) {
        this.resourceContext = resourceContext;
    }

    public String[] getSearchLocations() {
        return searchLocations;
    }

    public void setSearchLocations(String[] searchLocations) {
        this.searchLocations = searchLocations;
    }

    public String[] getSmartLocations() {
        return smartLocations;
    }

    public void setSmartLocations(String[] smartLocations) {
        this.smartLocations = smartLocations;
    }

    public boolean isUserHomeSearchEnabled() {
        return userHomeSearchEnabled;
    }

    public void setUserHomeSearchEnabled(boolean userHomeSearchEnabled) {
        this.userHomeSearchEnabled = userHomeSearchEnabled;
    }

    public String getFileEncoding() {
        return fileEncoding;
    }

    public void setFileEncoding(String fileEncoding) {
        this.fileEncoding = fileEncoding;
    }

    public PropertiesPersister getMyPropertiesPersister() {
        return myPropertiesPersister;
    }

    public boolean loadProperties(Properties props)
        throws IOException {
        boolean resourceFound = true;
        if (getSmartLocations() != null) {
            for (int i = 0; i < getSmartLocations().length;
                i++) {
                final String location = getSmartLocations()[i];
                InputStream is = null;
                String context = getResourceContext();
                if (StringUtils.isNotEmpty(context)) {
                    if (!context.endsWith("/")) {
                        context =
                            new StringBuilder(context).append('/').toString();
                    }
                }
                String fileName =
                    new StringBuilder(context).append(location).toString();
                if (StringUtils.isEmpty(fileName)) {
                    continue;
                }
                try {
                    Resource resource;
                    if (isDefaultSearchEnabled()) {
                        String resourceName =
                            new StringBuilder(fileName).append(
                            getDefaultResourceSuffix()).
                            toString();
                        resource =
                            new ClassPathResource(resourceName);
                        is = attemptToLoadResource(props, resource);
                        resourceFound = closeInputStream(is) && resourceFound;
                    }
                    if (isClasspathSearchEnabled()) {
                        resource =
                            new ClassPathResource(fileName);
                        is = attemptToLoadResource(props, resource);
                        resourceFound = closeInputStream(is) && resourceFound;
                    }
                    if (isCurrentDirSearchEnabled()) {
                        String parent = System.getProperty("user.dir");
                        resourceFound =
                            attempToReadRsrcFromFile(parent, fileName,
                            resourceFound, props) && resourceFound;
                    }
                    if (isUserHomeSearchEnabled()) {
                        String parent = System.getProperty("user.home");
                        resourceFound =
                            attempToReadRsrcFromFile(parent, fileName,
                            resourceFound, props) && resourceFound;
                    }
                    if (getSearchLocations() != null) {
                        for (String searchLocation : getSearchLocations()) {
                            if (StringUtils.isNotEmpty(StringUtils.trim(
                                searchLocation))) {
                                resourceFound =
                                    attempToReadRsrcFromFile(searchLocation,
                                    fileName, resourceFound, props) &&
                                    resourceFound;
                            }
                        }
                    }
                }
                catch (Exception ex) {
                    IOException exception = new IOException();
                    exception.setStackTrace(ex.getStackTrace());
                    throw exception;
                }
                finally {
                    closeInputStream(is);
                }
            }
        }
        return resourceFound;
    }

    /**
     * 
     * @param parent The parent folder of the fileName
     * @param fileName The file to read
     * @param resourceFound True if resource was earlier found
     * @param props The properties object to fill the properties with
     * @return True if either resourceFound is true or resource was read from
     *         fileName
     * @throws java.io.IOException If error in reading the file
     */
    protected boolean attempToReadRsrcFromFile(String parent,
                                               String fileName,
                                               boolean resourceFound,
                                               Properties props)
        throws IOException {
        File resourceFile = new File(parent, fileName);
        Resource resource =
            new FileSystemResource(resourceFile);
        InputStream is = attemptToLoadResource(props, resource);
        return closeInputStream(is) || resourceFound;
    }

    /**
     * Return suffix for default resource file.
     * @return Suffix for the classpath default resource
     */
    protected InputStream attemptToLoadResource(Properties props,
                                                Resource resource) {
        InputStream is = null;
        try {
            is = resource.getInputStream();
            if (resource.getFilename().endsWith(".xml")) {
                this.myPropertiesPersister.loadFromXml(props, is);
            }
            else {
                if (this.fileEncoding != null) {
                    this.myPropertiesPersister.load(props,
                        new InputStreamReader(is, this.fileEncoding));
                }
                else {
                    this.myPropertiesPersister.load(props, is);
                }
            }
        }
        catch (IOException ex) {
        }
        return is;
    }

    protected boolean closeInputStream(InputStream is)
        throws IOException {
        if (is != null) {
            is.close();
            return true;
        }
        return false;
    }
}
