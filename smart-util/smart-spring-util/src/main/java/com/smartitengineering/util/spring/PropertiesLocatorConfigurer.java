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
package com.smartitengineering.util.spring;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

/**
 * This class will mainly search for a designated properties file at locations
 * predefined by system (that is this module) and user through app context. <p />
 * Preconfigured locations according to ascending priority is -
 * <ul>
 *  <li>System properties (if enabled)</li>
 *  <li>Classpath for default resource (defaultResourceSuffix appended to the path)</li>
 *  <li>Current working directory</li>
 *  <li>Home directory</li>
 *  <li>User specified directory in order specified</li>
 * </ul>
 * 
 * @author imyousuf
 */
public class PropertiesLocatorConfigurer
    extends PropertyPlaceholderConfigurer
    implements BeanFactoryPostProcessor,
               PriorityOrdered,
               BeanNameAware,
               BeanFactoryAware {

    public static final String DEFAULT_RESOURCE_SUFFIX = ".template";
    private String defaultResourceSuffix =
        PropertiesLocatorConfigurer.DEFAULT_RESOURCE_SUFFIX;
    private String[] smartLocations;
    private PropertiesPersister myPropertiesPersister =
        new DefaultPropertiesPersister();
    private boolean ignoreResourceNotFound = false;
    private String fileEncoding = null;
    private boolean defaultSearchEnabled = true;
    private boolean classpathSearchEnabled = true;
    private boolean currentDirSearchEnabled = true;
    private boolean userHomeSearchEnabled = true;
    private String fileContext;
    private String[] searchLocations;

    @Override
    protected void loadProperties(Properties props)
        throws IOException {
        if (this.smartLocations != null) {
            for (int i = 0; i < this.smartLocations.length; i++) {
                String location = this.smartLocations[i];
                if (logger.isInfoEnabled()) {
                    logger.info("Loading properties file from " + location);
                }
                InputStream is = null;
                String fileName =
                    new StringBuilder(getFileContext()).append(location).
                    toString();
                System.out.println(fileName);
                if (fileName == null) {
                    continue;
                }
                try {
                    boolean resourceFound = false;
                    Resource resource;
                    if (isDefaultSearchEnabled()) {
                        String resourceName = new StringBuilder(fileName).append(
                            getDefaultResourceSuffix()).toString();
                        resource =
                            new ClassPathResource(resourceName);
                        is = attemptToLoadResource(props, resource);
                        resourceFound = closeInputStream(is);
                    }
                    if (isClasspathSearchEnabled()) {
                        resource =
                            new ClassPathResource(fileName);
                        is = attemptToLoadResource(props, resource);
                        resourceFound = closeInputStream(is) || resourceFound;
                    }
                    if (isCurrentDirSearchEnabled()) {
                        String parent = System.getProperty("user.dir");
                        resourceFound = attempToReadRsrcFromFile(parent,
                            fileName, resourceFound, props);
                    }
                    if (isUserHomeSearchEnabled()) {
                        String parent = System.getProperty("user.home");
                        resourceFound = attempToReadRsrcFromFile(parent,
                            fileName, resourceFound, props);
                    }
                    if (searchLocations != null) {
                        for (String searchLocation : searchLocations) {
                            if (StringUtils.isNotEmpty(StringUtils.trim(
                                searchLocation))) {
                                attempToReadRsrcFromFile(searchLocation,
                                    fileName,
                                    resourceFound, props);
                            }
                        }
                    }
                    if (!resourceFound) {
                        throw new RuntimeException(fileName + " not found!");
                    }
                }
                catch (Exception ex) {
                    if (this.ignoreResourceNotFound) {
                        if (logger.isWarnEnabled()) {
                            logger.warn("Could not load properties from " +
                                location + ": " + ex.getMessage());
                        }
                    }
                    else {
                        throw new IOException(ex);
                    }
                }
                finally {
                    closeInputStream(is);
                }
            }
        }
    }

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

    public String getDefaultResourceSuffix() {
        return defaultResourceSuffix;
    }

    public void setDefaultResourceSuffix(String defaultResourceSuffix) {
        this.defaultResourceSuffix = defaultResourceSuffix;
    }

    public String getFileContext() {
        if (fileContext == null) {
            return "";
        }
        return fileContext;
    }

    public void setFileContext(String fileContext) {
        this.fileContext = fileContext;
    }

    private InputStream attemptToLoadResource(Properties props,
                                              Resource resource) {
        InputStream is = null;
        try {
            is = resource.getInputStream();
            if (resource.getFilename().endsWith(XML_FILE_EXTENSION)) {
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

    private boolean closeInputStream(InputStream is)
        throws IOException {
        if (is != null) {
            is.close();
            return true;
        }
        return false;
    }

    @Override
    public void setLocation(Resource location) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLocations(Resource[] locations) {
        throw new UnsupportedOperationException();
    }

    public void setSmartLocation(String smartLocation) {
        this.smartLocations = new String[]{smartLocation};
        super.setLocation(new ClassPathResource(smartLocation));
    }

    public void setSmartLocationsAsCsv(String smartLocationsAsCsv) {
        setSearchLocations(smartLocationsAsCsv.split(","));
    }

    public void setSmartLocations(String[] smartLocations) {
        this.smartLocations = smartLocations;
        Resource[] resources = new Resource[smartLocations.length];
        for (int i = 0; i < smartLocations.length; ++i) {
            String smartLocation = StringUtils.trim(smartLocations[i]);
            if (StringUtils.isNotEmpty(smartLocation)) {
                resources[i] = new ClassPathResource(smartLocation);
            }
        }
        super.setLocations(resources);
    }

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

    public boolean isDefaultSearchEnabled() {
        return defaultSearchEnabled;
    }

    public void setDefaultSearchEnabled(boolean defaultSearchEnabled) {
        this.defaultSearchEnabled = defaultSearchEnabled;
    }

    public boolean isUserHomeSearchEnabled() {
        return userHomeSearchEnabled;
    }

    public void setUserHomeSearchEnabled(boolean userHomeSearchEnabled) {
        this.userHomeSearchEnabled = userHomeSearchEnabled;
    }

    public String[] getSearchLocations() {
        return searchLocations;
    }

    public void setSearchLocation(String searchLocation) {
        setSearchLocations(new String[]{searchLocation});
    }

    public void setSearchLocations(String[] searchLocations) {
        this.searchLocations = searchLocations;
    }
}
