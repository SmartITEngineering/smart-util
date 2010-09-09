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
package com.smartitengineering.util.rest.client;

import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.config.ClientConfig;
import java.net.URI;

/**
 *
 * @author imyousuf
 */
public class SimpleResourceImpl<T> extends AbstractClientResource<T, Resource> {

  private ConfigProcessor processor;

  public SimpleResourceImpl(Resource referrer, URI thisResourceUri, String representationType,
                            Class<? extends T> entityClass, ClientUtil clientUtil, boolean invokeGet,
                            ClientFactory clientFactory, ConfigProcessor processor) throws IllegalArgumentException,
                                                                                           UniformInterfaceException {
    this(referrer, thisResourceUri, representationType, entityClass, clientUtil, invokeGet, clientFactory, true,
         processor);
  }

  public SimpleResourceImpl(Resource referrer, URI thisResourceUri, String representationType,
                            Class<? extends T> entityClass, ClientUtil clientUtil, boolean invokeGet,
                            ClientFactory clientFactory, boolean followRedirection, ConfigProcessor processor) throws
      IllegalArgumentException, UniformInterfaceException {
    super(referrer, thisResourceUri, representationType, entityClass, clientUtil, invokeGet, clientFactory, true);
    this.processor = processor;
  }

  @Override
  protected void processClientConfig(ClientConfig clientConfig) {
    if (this.processor != null) {
      this.processor.process(clientConfig);
    }
  }

  @Override
  protected ResourceLink getNextUri() {
    return null;
  }

  @Override
  protected ResourceLink getPreviousUri() {
    return null;
  }

  @Override
  protected Resource instantiatePageableResource(ResourceLink link) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
