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
package com.smartitengineering.util.rest.server;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.core.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public abstract class AbstractResource {

  @Context
  private UriInfo uriInfo;
  @Context
  private HttpContext context;
  @Context
  private ResourceContext resourceContext;

  protected AbstractResource() {
  }

  protected AbstractResource(UriInfo info, HttpContext context, ResourceContext resourceContext) {
    this.uriInfo = info;
    this.context = context;
    this.resourceContext = resourceContext;
  }

  protected AbstractResource(ServerResourceInjectables injectables) {
    this.uriInfo = injectables.getUriInfo();
    this.context = injectables.getContext();
    this.resourceContext = injectables.getResourceContext();
  }

  protected ServerResourceInjectables getInjectables() {
    ServerResourceInjectables injectables = new ServerResourceInjectables();
    injectables.setContext(context);
    injectables.setResourceContext(resourceContext);
    injectables.setUriInfo(uriInfo);
    return injectables;
  }

  protected HttpContext getContext() {
    return context;
  }

  protected ResourceContext getResourceContext() {
    return resourceContext;
  }

  protected UriInfo getUriInfo() {
    return uriInfo;
  }

  protected UriBuilder getAbsoluteURIBuilder() {
    return uriInfo.getBaseUriBuilder();
  }

  protected UriBuilder getRelativeURIBuilder() {
    return UriBuilder.fromPath(uriInfo.getBaseUriBuilder().build().getPath());
  }
}
