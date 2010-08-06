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
import java.net.URI;
import java.util.Date;
import java.util.UUID;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

/**
 *
 * @author imyousuf
 */
@Path("/")
public class SomeDomainResource {

  private static final int DOMAIN_SIZE = 100;
  private static final SomeDomain[] DOMAIN_DATA;

  static {
    DOMAIN_DATA = new SomeDomain[DOMAIN_SIZE];
    for (int i = 0; i < DOMAIN_SIZE; ++i) {
      DOMAIN_DATA[i] = new SomeDomain();
      DOMAIN_DATA[i].setTestName(UUID.randomUUID().toString());
    }
  }
  @Context
  public UriInfo uriInfo;
  protected final Factory abderaFactory = Abdera.getNewFactory();

  protected UriBuilder setBaseUri(final UriBuilder builder) throws IllegalArgumentException {
    final URI baseUri = uriInfo.getBaseUri();
    builder.host(baseUri.getHost());
    builder.port(baseUri.getPort());
    builder.scheme(baseUri.getScheme());
    return builder;
  }

  protected Feed getFeed(String title, Date updated) {
    return getFeed(uriInfo.getRequestUri().toString(), title, updated);
  }

  protected Feed getFeed(String id, String title, Date updated) {
    Feed feed = getFeed();
    feed.setId(id);
    feed.setTitle(title);
    feed.setUpdated(updated);
    return feed;
  }

  protected Feed getFeed() {
    Feed feed = abderaFactory.newFeed();
    feed.addLink(getSelfLink());
    feed.addAuthor("author");     ///error in adding getDefaultAuthor();
    return feed;
  }

  protected Link getSelfLink() {
    Link selfLink = abderaFactory.newLink();
    selfLink.setHref(uriInfo.getRequestUri().toString());
    selfLink.setRel(Link.REL_SELF);
    return selfLink;
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {
    return Response.noContent().build();
  }
}
