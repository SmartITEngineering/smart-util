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
package com.smartitengineering.util.rest.atom.resources;

import com.smartitengineering.util.rest.atom.resources.domain.SomeDomain;
import com.smartitengineering.util.rest.atom.server.AbstractResource;
import java.net.URI;
import java.util.Date;
import java.util.UUID;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import org.apache.abdera.ext.opensearch.OpenSearchConstants;
import org.apache.abdera.ext.opensearch.model.IntegerElement;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

/**
 *
 * @author imyousuf
 */
@Path("/")
public class SomeDomainResource extends AbstractResource {

  public static final String COUNT = "count";
  public static final String STARTINDEX = "startIndex";
  public static final int DOMAIN_SIZE = 100;
  public static final SomeDomain[] DOMAIN_DATA;

  static {
    DOMAIN_DATA = new SomeDomain[DOMAIN_SIZE];
    for (int i = 0; i < DOMAIN_SIZE; ++i) {
      DOMAIN_DATA[i] = new SomeDomain();
      DOMAIN_DATA[i].setTestName(UUID.randomUUID().toString());
    }
  }

  @HEAD
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {
    return Response.noContent().build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/domain/{index}")
  public SomeDomain getDomain(@PathParam("index") int index) {
    return DOMAIN_DATA[index];
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  @Path("feed")
  public Response getFeed(@QueryParam(STARTINDEX) @DefaultValue("0") final int startIndex,
                          @QueryParam(COUNT) @DefaultValue("5") final int count) {
    Feed feed = getDomainFeed(startIndex, count);
    final ResponseBuilder responseBuilder = Response.ok(feed);
    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  @Path("osfeed")
  public Response getOpenSearchFeed(@QueryParam(STARTINDEX) @DefaultValue("0") final int startIndex,
                                    @QueryParam(COUNT) @DefaultValue("5") final int count) {
    final Feed feed = getDomainFeed(startIndex, count);
    IntegerElement itemsPerPageElement = getAbderaFactory().newElement(OpenSearchConstants.ITEMS_PER_PAGE);
    itemsPerPageElement.setValue(count);
    feed.addExtension(itemsPerPageElement);
    IntegerElement totalCountElement = getAbderaFactory().newElement(OpenSearchConstants.TOTAL_RESULTS);
    totalCountElement.setValue(DOMAIN_SIZE);
    feed.addExtension(totalCountElement);
    final ResponseBuilder responseBuilder = Response.ok(feed);
    return responseBuilder.build();
  }

  protected Feed getDomainFeed(final int startIndex,
                               final int count)
      throws IllegalArgumentException,
             UriBuilderException {
    final Feed feed = getFeed("Feed!", new Date());
    final UriBuilder builder = getUriInfo().getAbsolutePathBuilder();
    final int nextIndex = startIndex + count;
    if (nextIndex < DOMAIN_SIZE) {
      builder.queryParam(STARTINDEX, nextIndex);
      builder.queryParam(COUNT, count);
      Link link = getLink(builder.build(), Link.REL_NEXT, MediaType.APPLICATION_ATOM_XML);
      feed.addLink(link);
    }
    final int previousIndex = startIndex - count;
    if (previousIndex > 0) {
      builder.queryParam(STARTINDEX, previousIndex);
      builder.queryParam(COUNT, count);
      Link link = getLink(builder.build(), Link.REL_PREVIOUS, MediaType.APPLICATION_ATOM_XML);
      feed.addLink(link);
    }
    final int toIndex;
    final int probableToIndex = startIndex + count - 1;
    if (probableToIndex >= DOMAIN_SIZE) {
      toIndex = DOMAIN_SIZE - 1;
    }
    else {
      toIndex = probableToIndex;
    }
    for (int i = startIndex; i <= toIndex; ++i) {
      UriBuilder uriBuilder = getAbsoluteURIBuilder().path("/domain/" + i);
      final String id = Integer.toString(i);
      final String name = "Domain " + Integer.toString(i);
      final Date updated = new Date();
      Link link = getLink(uriBuilder.build(), Link.REL_ALTERNATE, MediaType.APPLICATION_JSON);
      Entry entry = getEntry(id, name, updated, link);
      feed.addEntry(entry);
    }
    return feed;
  }

  @Override
  protected String getAuthor() {
    return "author";
  }
}
