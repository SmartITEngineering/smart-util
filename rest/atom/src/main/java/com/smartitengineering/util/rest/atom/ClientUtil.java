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

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import javax.xml.namespace.QName;
import org.apache.abdera.Abdera;
import org.apache.abdera.ext.opensearch.OpenSearchConstants;
import org.apache.abdera.ext.opensearch.model.IntegerElement;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;
import org.apache.commons.lang.StringUtils;

/**
 * A utility method to read entities
 * @author imyousuf
 */
public class ClientUtil {

  public static Feed getFeed(ClientResponse response) {
    return response.getEntity(Feed.class);
  }

  public static <T> T getResponseEntity(ClientResponse response, Class<? extends T> clazz) {
    return response.getEntity(clazz);
  }

  public static ClientResponse readClientResponse(URI uri, HttpClient client, String acceptType) {
    return readEntity(uri, client, acceptType, ClientResponse.class);
  }

  public static ClientResponse readClientResponse(Link link, HttpClient client, String acceptType) {
    return readEntity(link, client, acceptType, ClientResponse.class);
  }

  public static <T> T readEntity(Link link, HttpClient client, String acceptType, Class<? extends T> clazz) {
    if (link != null && client != null && clazz != null) {
      try {
        final URI uri = link.getHref().toURI();
        return readEntity(uri, client, acceptType, clazz);
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return null;
  }

  public static <T> T readEntity(final URI uri, HttpClient client, String acceptType, Class<? extends T> clazz) {
    if (uri != null && client != null && clazz != null) {
      try {
        WebResource resource = client.getWebResource(uri);
        if (StringUtils.isNotBlank(acceptType)) {
          resource.accept(acceptType);
        }
        T newEntity = resource.get(clazz);
        return newEntity;
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
}
