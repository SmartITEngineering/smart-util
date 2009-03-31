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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author imyousuf
 * @since 0.2
 */
public class PropertiesLocator {

    public static final String DEFAULT_RESOURCE_SUFFIX = ".template";
    public static final String CLASSPATH_RESOURCE_PREFIX = "classpath:";
    private String defaultResourceSuffix =
        PropertiesLocator.DEFAULT_RESOURCE_SUFFIX;
    private String[] smartLocations;
    private boolean defaultSearchEnabled = true;
    private boolean classpathSearchEnabled = true;
    private boolean currentDirSearchEnabled = true;
    private boolean userHomeSearchEnabled = true;
    private String resourceContext;
    private String[] searchLocations;
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

    public boolean loadProperties(Properties props)
        throws IOException {
        boolean resourceFound = false;
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
                        String resourcePath = new StringBuilder(
                            CLASSPATH_RESOURCE_PREFIX).append(resourceName).
                            toString();
                        resource =
                            ResourceFactory.getResource(resourcePath);
                        is = attemptToLoadResource(props, resource);
                        resourceFound = closeInputStream(is) || resourceFound;
                    }
                    if (isClasspathSearchEnabled()) {
                        String resourcePath = new StringBuilder(
                            CLASSPATH_RESOURCE_PREFIX).append(fileName).
                            toString();
                        resource =
                            ResourceFactory.getResource(resourcePath);
                        is = attemptToLoadResource(props, resource);
                        resourceFound = closeInputStream(is) || resourceFound;
                    }
                    if (isCurrentDirSearchEnabled()) {
                        String parent = System.getProperty("user.dir");
                        resourceFound =
                            attempToReadRsrcFromFile(parent, fileName,
                            resourceFound, props) || resourceFound;
                    }
                    if (isUserHomeSearchEnabled()) {
                        String parent = System.getProperty("user.home");
                        resourceFound =
                            attempToReadRsrcFromFile(parent, fileName,
                            resourceFound, props) || resourceFound;
                    }
                    if (getSearchLocations() != null) {
                        for (String searchLocation : getSearchLocations()) {
                            if (StringUtils.isNotEmpty(StringUtils.trim(
                                searchLocation))) {
                                resourceFound =
                                    attempToReadRsrcFromFile(searchLocation,
                                    fileName, resourceFound, props) ||
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
            ResourceFactory.getResource(resourceFile.getAbsolutePath());
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
                props.loadFromXML(is);
            }
            else {
                if (this.fileEncoding != null) {
                    loadFromReader(props, new InputStreamReader(is,
                        this.fileEncoding));
                }
                else {
                    props.load(is);
                }
            }
        }
        catch (Exception ex) {
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

    protected void loadFromReader(Properties props,
                                  Reader reader)
        throws IOException {
        BufferedReader in = new BufferedReader(reader);
        while (true) {
            String line = in.readLine();
            if (line == null) {
                return;
            }
            line = StringUtils.stripStart(line, null);
            if (line.length() > 0) {
                char firstChar = line.charAt(0);
                if (firstChar != '#' && firstChar != '!') {
                    while (endsWithContinuationMarker(line)) {
                        String nextLine = in.readLine();
                        line = line.substring(0, line.length() - 1);
                        if (nextLine != null) {
                            line += StringUtils.stripStart(nextLine, null);
                        }
                    }
                    int separatorIndex = line.indexOf("=");
                    if (separatorIndex == -1) {
                        separatorIndex = line.indexOf(":");
                    }
                    String key = (separatorIndex != -1 ? line.substring(0,
                        separatorIndex) : line);
                    String value = (separatorIndex != -1) ? line.substring(
                        separatorIndex + 1) : "";
                    key = StringUtils.stripEnd(key, null);
                    value = StringUtils.stripStart(value, null);
                    props.put(unescape(key), unescape(value));
                }
            }
        }
    }

    protected boolean endsWithContinuationMarker(String line) {
        boolean evenSlashCount = true;
        int index = line.length() - 1;
        while (index >= 0 && line.charAt(index) == '\\') {
            evenSlashCount = !evenSlashCount;
            index--;
        }
        return !evenSlashCount;
    }

    protected String unescape(String str) {
        String result = StringUtils.replace(str, "\\t", "\t", -1);
        result = StringUtils.replace(result, "\\r", "\r", -1);
        result = StringUtils.replace(result, "\\n", "\n", -1);
        result = StringUtils.replace(result, "\\f", "\f", -1);
        return result;
    }
}
