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

  @Override
  public String getShortName() {
    return shortName;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public String getContact() {
    return contact;
  }

  @Override
  public String getLongName() {
    return longName;
  }

  @Override
  public String getDeveloper() {
    return developer;
  }

  @Override
  public String getInputEncoding() {
    return inputEncoding;
  }

  @Override
  public String getOutputEncoding() {
    return outputEncoding;
  }

  @Override
  public String getAttribution() {
    return attribution;
  }

  @Override
  public Set<String> getTags() {
    return Collections.unmodifiableSet(tags);
  }

  @Override
  public SyndicationRightEnum getSyndicationRight() {
    if (syndicationRight == null) {
      return SyndicationRightEnum.getDefault();
    }
    return syndicationRight;
  }

  @Override
  public Collection<Url> getUrls() {
    return Collections.unmodifiableCollection(urls);
  }

  @Override
  public Collection<Image> getImages() {
    return Collections.unmodifiableCollection(images);
  }

  @Override
  public Collection<Query> getQueries() {
    return Collections.unmodifiableCollection(queries);
  }

  @Override
  public Locale getLanguage() {
    return language;
  }

  @Override
  public boolean containsAdultContent() {
    return adultContent;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final OpenSearchDescriptorImpl other = (OpenSearchDescriptorImpl) obj;
    if ((this.shortName == null) ? (other.shortName != null) : !this.shortName.equals(other.shortName)) {
      return false;
    }
    if ((this.description == null) ? (other.description != null) : !this.description.equals(other.description)) {
      return false;
    }
    if ((this.contact == null) ? (other.contact != null) : !this.contact.equals(other.contact)) {
      return false;
    }
    if ((this.longName == null) ? (other.longName != null) : !this.longName.equals(other.longName)) {
      return false;
    }
    if ((this.developer == null) ? (other.developer != null) : !this.developer.equals(other.developer)) {
      return false;
    }
    if ((this.inputEncoding == null) ? (other.inputEncoding != null) : !this.inputEncoding.equals(other.inputEncoding)) {
      return false;
    }
    if ((this.outputEncoding == null) ? (other.outputEncoding != null)
        : !this.outputEncoding.equals(other.outputEncoding)) {
      return false;
    }
    if ((this.attribution == null) ? (other.attribution != null) : !this.attribution.equals(other.attribution)) {
      return false;
    }
    if (this.tags != other.tags && (this.tags == null || !this.tags.equals(other.tags))) {
      return false;
    }
    if (this.syndicationRight != other.syndicationRight) {
      return false;
    }
    if (this.urls != other.urls && (this.urls == null || !this.urls.equals(other.urls))) {
      return false;
    }
    if (this.images != other.images && (this.images == null || !this.images.equals(other.images))) {
      return false;
    }
    if (this.queries != other.queries && (this.queries == null || !this.queries.equals(other.queries))) {
      return false;
    }
    if (this.language != other.language && (this.language == null || !this.language.equals(other.language))) {
      return false;
    }
    if (this.adultContent != other.adultContent) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 53 * hash + (this.shortName != null ? this.shortName.hashCode() : 0);
    hash = 53 * hash + (this.description != null ? this.description.hashCode() : 0);
    hash = 53 * hash + (this.contact != null ? this.contact.hashCode() : 0);
    hash = 53 * hash + (this.longName != null ? this.longName.hashCode() : 0);
    hash = 53 * hash + (this.developer != null ? this.developer.hashCode() : 0);
    hash = 53 * hash + (this.inputEncoding != null ? this.inputEncoding.hashCode() : 0);
    hash = 53 * hash + (this.outputEncoding != null ? this.outputEncoding.hashCode() : 0);
    hash = 53 * hash + (this.attribution != null ? this.attribution.hashCode() : 0);
    hash = 53 * hash + (this.tags != null ? this.tags.hashCode() : 0);
    hash = 53 * hash + (this.syndicationRight != null ? this.syndicationRight.hashCode() : 0);
    hash = 53 * hash + (this.urls != null ? this.urls.hashCode() : 0);
    hash = 53 * hash + (this.images != null ? this.images.hashCode() : 0);
    hash = 53 * hash + (this.queries != null ? this.queries.hashCode() : 0);
    hash = 53 * hash + (this.language != null ? this.language.hashCode() : 0);
    hash = 53 * hash + (this.adultContent ? 1 : 0);
    return hash;
  }
}
