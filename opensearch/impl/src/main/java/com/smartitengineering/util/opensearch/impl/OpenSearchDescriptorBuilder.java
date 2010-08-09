/*
 * This is a utility project for wide range of applications
 *
 * Copyright (C) 2010  Imran M Yousuf (imyousuf@smartitengineering.com)
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
package com.smartitengineering.util.opensearch.impl;

import com.smartitengineering.util.opensearch.api.Image;
import com.smartitengineering.util.opensearch.api.OpenSearchDescriptor;
import com.smartitengineering.util.opensearch.api.Query;
import com.smartitengineering.util.opensearch.api.Url;
import java.util.Locale;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author imyousuf
 */
public class OpenSearchDescriptorBuilder {

  private final OpenSearchDescriptorImpl descriptorImpl;

  private OpenSearchDescriptorBuilder() {
    descriptorImpl = new OpenSearchDescriptorImpl();
  }

  public static OpenSearchDescriptorBuilder getBuilder() {
    return new OpenSearchDescriptorBuilder();
  }

  public OpenSearchDescriptor build() {
    if (StringUtils.isBlank(descriptorImpl.getShortName()) || StringUtils.isBlank(descriptorImpl.getDescription()) ||
        descriptorImpl.getUrls().isEmpty()) {
      throw new IllegalStateException("Short name, description and a single Url is a must! All these conditions are " +
          "not met");
    }
    OpenSearchDescriptorImpl descriptor = new OpenSearchDescriptorImpl();
    descriptor.setAdultContent(descriptorImpl.containsAdultContent());
    descriptor.setAttribution(descriptorImpl.getAttribution());
    descriptor.setContact(descriptorImpl.getContact());
    descriptor.setDescription(descriptorImpl.getDescription());
    descriptor.setDeveloper(descriptorImpl.getDeveloper());
    descriptor.setImages(descriptorImpl.getImages());
    descriptor.setInputEncoding(descriptorImpl.getInputEncoding());
    descriptor.setLanguage(descriptorImpl.getLanguage());
    descriptor.setLongName(descriptorImpl.getLongName());
    descriptor.setOutputEncoding(descriptorImpl.getOutputEncoding());
    descriptor.setQueries(descriptorImpl.getQueries());
    descriptor.setShortName(descriptorImpl.getShortName());
    descriptor.setSyndicationRight(descriptorImpl.getSyndicationRight());
    descriptor.setTags(descriptorImpl.getTags());
    descriptor.setUrls(descriptorImpl.getUrls());
    return descriptor;
  }

  public OpenSearchDescriptorBuilder setAdultContent() {
    descriptorImpl.setAdultContent(true);
    return this;
  }

  public OpenSearchDescriptorBuilder unsetAdultContent() {
    descriptorImpl.setAdultContent(false);
    return this;
  }

  public OpenSearchDescriptorBuilder contact(String contact) {
    descriptorImpl.setContact(contact);
    return this;
  }

  public OpenSearchDescriptorBuilder description(String description) {
    descriptorImpl.setDescription(description);
    return this;
  }

  public OpenSearchDescriptorBuilder developer(String developer) {
    descriptorImpl.setDeveloper(developer);
    return this;
  }

  public OpenSearchDescriptorBuilder images(Image... images) {
    descriptorImpl.addImages(images);
    return this;
  }

  public OpenSearchDescriptorBuilder inputEncoding(String inputEncoding) {
    descriptorImpl.setInputEncoding(inputEncoding);
    return this;
  }

  public OpenSearchDescriptorBuilder language(Locale language) {
    descriptorImpl.setLanguage(language);
    return this;
  }

  public OpenSearchDescriptorBuilder longName(String longName) {
    descriptorImpl.setLongName(longName);
    return this;
  }

  public OpenSearchDescriptorBuilder outputEncoding(String outputEncoding) {
    descriptorImpl.setOutputEncoding(outputEncoding);
    return this;
  }

  public OpenSearchDescriptorBuilder queries(Query... queries) {
    descriptorImpl.addQueries(queries);
    return this;
  }

  public OpenSearchDescriptorBuilder shortName(String shortName) {
    descriptorImpl.setShortName(shortName);
    return this;
  }

  public OpenSearchDescriptorBuilder syndicationRight(OpenSearchDescriptor.SyndicationRightEnum rightEnum) {
    descriptorImpl.setSyndicationRight(rightEnum);
    return this;
  }

  public OpenSearchDescriptorBuilder tags(String... tags) {
    descriptorImpl.addTags(tags);
    return this;
  }

  public OpenSearchDescriptorBuilder urls(Url... urls) {
    descriptorImpl.addUrls(urls);
    return this;
  }
}
