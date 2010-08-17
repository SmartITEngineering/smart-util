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
package com.smartitengineering.util.rest.atom;

import java.util.Collection;
import javax.activation.MimeTypeParseException;
import javax.ws.rs.core.MediaType;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

/**
 * A wrapper that allows clients to walk across a paginated collection of homogeneous entries.
 * @author imyousuf
 */
public class PaginatedEntitiesWrapper<T> {

  private Feed rootFeed;
  private HttpClient client;
  private FeedEntryReader<T> feedEntryReader;

  /**
   * Initialize a paginated entities wrapper based on a feed entry reader, client and the root feed to start walking.
   * @param rootFeed The feed to start walking from
   * @param client The client to use to fetch the next/previous wrapper
   * @param feedEntryReader Reader to read the collection entries
   * @throws IllegalArgumentException If any parameter is null
   */
  public PaginatedEntitiesWrapper(Feed rootFeed, HttpClient client, FeedEntryReader<T> feedEntryReader) throws
      IllegalArgumentException {
    if (rootFeed == null || client == null || feedEntryReader == null) {
      throw new IllegalArgumentException("No parameter can be null!");
    }
    this.rootFeed = rootFeed;
    this.client = client;
    this.feedEntryReader = feedEntryReader;
  }

  public PaginatedEntitiesWrapper<T> next() {
    Link link = rootFeed.getLink(Link.REL_NEXT);
    return getPaginatedWrapperFromFeedLink(link);
  }

  public PaginatedEntitiesWrapper<T> previous() {
    Link link = rootFeed.getLink(Link.REL_PREVIOUS);
    return getPaginatedWrapperFromFeedLink(link);
  }

  protected PaginatedEntitiesWrapper<T> getPaginatedWrapperFromFeedLink(Link link) {
    try {
      if (link != null && link.getMimeType().match(MediaType.APPLICATION_ATOM_XML)) {
        Feed newFeed = ClientUtil.readEntity(link, client, MediaType.APPLICATION_ATOM_XML, Feed.class);
        return new PaginatedEntitiesWrapper<T>(newFeed, client, feedEntryReader);
      }
    }
    catch (MimeTypeParseException ex) {
      ex.printStackTrace();
    }
    return null;

  }

  public Collection<Resource<T>> getEntitiesForCurrentPage() {
    return feedEntryReader.readEntriesFromRooFeed(rootFeed);
  }

  public HttpClient getClient() {
    return client;
  }

  public FeedEntryReader<T> getFeedEntryReader() {
    return feedEntryReader;
  }

  public Feed getRootFeed() {
    return rootFeed;
  }
}
