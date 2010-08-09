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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/**
 *
 * @author imyousuf
 */
class OpenSearchDescriptorImpl implements OpenSearchDescriptor {

  private String shortName, description, contact, longName, developer, inputEncoding, outputEncoding, attribution;
  private Set<String> tags;
  private SyndicationRightEnum syndicationRight;
  private Collection<Url> urls;
  private Collection<Image> images;
  private Collection<Query> queries;
  private Locale language;
  private boolean adultContent;

  OpenSearchDescriptorImpl() {
    tags = new LinkedHashSet<String>();
    urls = new ArrayList<Url>();
    images = new ArrayList<Image>();
    queries = new ArrayList<Query>();
  }

  public void setAdultContent(boolean adultContent) {
    this.adultContent = adultContent;
  }

  public void setAttribution(String attribution) {
    Utils.checkMaxLength("Long name", 256, longName);
    this.attribution = attribution;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public void setDescription(String description) {
    Utils.checkMinMaxLength("Description", 1, 1024, description);
    this.description = description;
  }

  public void setDeveloper(String developer) {
    Utils.checkMaxLength("Long name", 64, longName);
    this.developer = developer;
  }

  public void addImages(Image... images) {
    this.images.addAll(Arrays.asList(images));
  }

  public void removeImages(Image... images) {
    this.images.removeAll(Arrays.asList(images));
  }

  public void setImages(Collection<Image> images) {
    this.images.clear();
    if (images != null && !images.isEmpty()) {
      this.images.addAll(images);
    }
  }

  public void setInputEncoding(String inputEncoding) {
    this.inputEncoding = inputEncoding;
  }

  public void setLanguage(Locale language) {
    this.language = language;
  }

  public void setLongName(String longName) {
    Utils.checkMaxLength("Long name", 48, longName);
    this.longName = longName;
  }

  public void setOutputEncoding(String outputEncoding) {
    this.outputEncoding = outputEncoding;
  }

  public void addQueries(Query... queries) {
    this.queries.addAll(Arrays.asList(queries));
  }

  public void removeQueries(Query... queries) {
    this.queries.removeAll(Arrays.asList(queries));
  }

  public void setQueries(Collection<Query> queries) {
    this.queries.clear();
    if (queries != null && !queries.isEmpty()) {
      this.queries.addAll(queries);
    }
  }

  public void setShortName(String shortName) {
    Utils.checkMinMaxLength("Short name", 1, 16, shortName);
    this.shortName = shortName;
  }

  public void setSyndicationRight(SyndicationRightEnum syndicationRight) {
    this.syndicationRight = syndicationRight;
  }

  public void addTags(String... tags) {
    this.tags.addAll(Arrays.asList(tags));
  }

  public void removeTags(String... tags) {
    this.tags.removeAll(Arrays.asList(tags));
  }

  public void setTags(Set<String> tags) {
    this.tags.clear();
    if (tags != null && !tags.isEmpty()) {
      this.tags.addAll(tags);
    }
  }

  public void addUrls(Url... urls) {
    this.urls.addAll(Arrays.asList(urls));
  }

  public void removeUrls(Url... urls) {
    this.urls.removeAll(Arrays.asList(urls));
  }

  public void setUrls(Collection<Url> urls) {
    this.urls.clear();
    if (urls != null && !urls.isEmpty()) {
      this.urls.addAll(urls);
    }
  }

  public String getShortName() {
    return shortName;
  }

  public String getDescription() {
    return description;
  }

  public String getContact() {
    return contact;
  }

  public String getLongName() {
    return longName;
  }

  public String getDeveloper() {
    return developer;
  }

  public String getInputEncoding() {
    return inputEncoding;
  }

  public String getOutputEncoding() {
    return outputEncoding;
  }

  public String getAttribution() {
    return attribution;
  }

  public Set<String> getTags() {
    return Collections.unmodifiableSet(tags);
  }

  public SyndicationRightEnum getSyndicationRight() {
    if (syndicationRight == null) {
      return SyndicationRightEnum.getDefault();
    }
    return syndicationRight;
  }

  public Collection<Url> getUrls() {
    return Collections.unmodifiableCollection(urls);
  }

  public Collection<Image> getImages() {
    return Collections.unmodifiableCollection(images);
  }

  public Collection<Query> getQueries() {
    return Collections.unmodifiableCollection(queries);
  }

  public Locale getLanguage() {
    return language;
  }

  public boolean containsAdultContent() {
    return adultContent;
  }
}
