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
package com.smartitengineering.util.opensearch.api;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;

/**
 * This API represents an open search description document. Description and Examples of an valid XML presentation of the
 * document can be referred to <a href="http://www.opensearch.org/Specifications/OpenSearch/1.1">this document</a>.
 * @author imyousufo
 */
public interface OpenSearchDescriptor {

  public String getShortName();

  public String getDescription();

  public String getContact();

  public String getLongName();

  public String getDeveloper();

  public String getInputEncoding();

  public String getOutputEncoding();

  public String getAttribution();

  public Set<String> getTags();

  public SyndicationRightEnum getSyndicationRight();

  public Collection<Url> getUrls();

  public Collection<Image> getImages();

  public Collection<Query> getQueries();

  public Locale getLanguage();

  public boolean containsAdultContent();
}
