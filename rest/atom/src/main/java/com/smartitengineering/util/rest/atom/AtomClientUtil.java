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

import com.smartitengineering.util.rest.client.ClientUtil;
import com.smartitengineering.util.rest.client.DefaultResouceLinkImpl;
import com.smartitengineering.util.rest.client.HttpClient;
import com.smartitengineering.util.rest.client.ResouceLink;
import com.sun.jersey.api.client.ClientResponse;
import java.net.URI;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.namespace.QName;
import org.apache.abdera.ext.opensearch.OpenSearchConstants;
import org.apache.abdera.ext.opensearch.model.IntegerElement;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

/**
 * A utility method to read entities
 * @author imyousuf
 */
public class AtomClientUtil extends ClientUtil {

  public static Feed getFeed(ClientResponse response) {
    return response.getEntity(Feed.class);
  }

  public static ClientResponse readClientResponse(Link link, HttpClient client, String acceptType) {
    return readEntity(link, client, acceptType, ClientResponse.class);
  }

  public static <T> T readEntity(Link link, HttpClient client, String acceptType, Class<? extends T> clazz) {
    if (link != null && client != null && clazz != null) {
      try {
        final URI uri = link.getHref().toURI();
        return ClientUtil.readEntity(uri, client, acceptType, clazz);
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return null;
  }

  public static boolean isOpenSearchTotalResultPresent(Feed feed) {
    return hasOpenSearchElement(feed, OpenSearchConstants.TOTAL_RESULTS);
  }

  public static boolean isOpenSearchItemsPerPagePresent(Feed feed) {
    return hasOpenSearchElement(feed, OpenSearchConstants.ITEMS_PER_PAGE);
  }

  public static boolean hasOpenSearchElement(Feed feed, QName qName) {
    return feed.getExtension(qName) != null;
  }

  public static int getOpenSearchTotalResult(Feed feed) {
    return getIntFromOpenSearchIntegerElement(feed, OpenSearchConstants.TOTAL_RESULTS);
  }

  public static int getOpenSearchItemsPerPage(Feed feed) {
    return getIntFromOpenSearchIntegerElement(feed, OpenSearchConstants.ITEMS_PER_PAGE);
  }

  public static int getIntFromOpenSearchIntegerElement(Feed feed, QName qName) {
    IntegerElement element = feed.getExtension(qName);
    return element.getValue();
  }
  private static final AtomClientUtil ATOM_CLIENT_UTIL;

  static {
    ATOM_CLIENT_UTIL = new AtomClientUtil();
  }

  protected AtomClientUtil() {
  }

  public static AtomClientUtil getInstance() {
    return ATOM_CLIENT_UTIL;
  }

  @Override
  public <T> void parseLinks(T entity,
                             MultivaluedMap<String, ResouceLink> uris)
      throws Exception {
    if (entity instanceof Feed) {
      Feed feed = (Feed) entity;
      for (Link link : feed.getLinks()) {
        DefaultResouceLinkImpl uri = new DefaultResouceLinkImpl();
        uri.setMimeType(link.getMimeType().toString());
        uri.setRel(link.getRel());
        uri.setUri(link.getHref().toURI());
        uris.add(link.getRel(), uri);
      }
    }
  }
}
