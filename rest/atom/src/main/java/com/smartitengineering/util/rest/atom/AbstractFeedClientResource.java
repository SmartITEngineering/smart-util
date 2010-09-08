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

import com.smartitengineering.util.rest.client.AbstractClientResource;
import com.smartitengineering.util.rest.client.ClientFactory;
import com.smartitengineering.util.rest.client.ResouceLink;
import com.smartitengineering.util.rest.client.Resource;
import com.sun.jersey.api.client.UniformInterfaceException;
import java.net.URI;
import javax.ws.rs.core.MediaType;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

/**
 *
 * @author imyousuf
 */
public abstract class AbstractFeedClientResource<P extends Resource<? extends Feed>> extends AbstractClientResource<Feed, P> {

  protected AbstractFeedClientResource(Resource referrer, ResouceLink resouceLink) throws
      IllegalArgumentException, UniformInterfaceException {
    this(referrer, resouceLink, true);
  }

  protected AbstractFeedClientResource(Resource referrer, ResouceLink resouceLink, boolean invokeGet) throws
      IllegalArgumentException, UniformInterfaceException {
    this(referrer, resouceLink, invokeGet, null);
  }

  protected AbstractFeedClientResource(Resource referrer, ResouceLink resouceLink, boolean invokeGet,
                                       ClientFactory clientFactory) throws IllegalArgumentException,
                                                                           UniformInterfaceException {
    this(referrer, resouceLink.getUri(), invokeGet, clientFactory);
  }

  protected AbstractFeedClientResource(Resource referrer, URI thisResourceUri) throws IllegalArgumentException,
                                                                                      UniformInterfaceException {
    this(referrer, thisResourceUri, true, null);
  }

  protected AbstractFeedClientResource(Resource referrer, URI thisResourceUri, boolean invokeGet,
                                       ClientFactory clientFactory) throws IllegalArgumentException,
                                                                           UniformInterfaceException {
    super(referrer, thisResourceUri, MediaType.APPLICATION_ATOM_XML, Feed.class, AtomClientUtil.getInstance(), invokeGet,
          clientFactory);
  }

  @Override
  protected ResouceLink getNextUri() {
    getIfFirstTimeRequest();
    return getRelatedResourceUris().getFirst(Link.REL_NEXT);
  }

  @Override
  protected ResouceLink getPreviousUri() {
    getIfFirstTimeRequest();
    return getRelatedResourceUris().getFirst(Link.REL_PREVIOUS);
  }
}
