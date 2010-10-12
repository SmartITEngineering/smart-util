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
package com.smartitengineering.util.rest.client.jersey.cache;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProviderFactory;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

public class CacheableClient
    extends Client {

  private CacheableClientHandler clientHandler;

  public CacheableClient(CacheableClientHandler handler,
                         ClientConfig config,
                         IoCComponentProviderFactory provider) {
    super(handler, config, provider);
    this.clientHandler = handler;
    if (this.clientHandler.getMethodProcessor() != null) {
      inject(this.clientHandler.getMethodProcessor());
    }
  }

  public CacheableClient(CacheableClientHandler root,
                         ClientConfig config) {
    this(root, config, null);
  }

  public CacheableClient(CacheableClientHandler root) {
    this(root, new DefaultClientConfig());
  }

  public CacheableClient() {
    this(createDefaultClientHander(new DefaultClientConfig()));

  }

  public CacheableClientHandler getClientHandler() {
    return clientHandler;
  }

  private static CacheableClientHandler createDefaultClientHander(ClientConfig clientConfig) {
    final HttpClient client = new HttpClient(new MultiThreadedHttpConnectionManager());
    return new CacheableClientHandler(client, clientConfig);
  }

  public static CacheableClient create() {
    return create(new DefaultClientConfig());
  }

  public static CacheableClient create(ClientConfig cc) {
    return create(cc, null);
  }

  public static CacheableClient create(ClientConfig cc,
                                       IoCComponentProviderFactory provider) {
    return create(cc, createDefaultClientHander(cc), provider);
  }

  public static CacheableClient create(ClientConfig cc,
                                       CacheableClientHandler clientHandler,
                                       IoCComponentProviderFactory provider) {
    return new CacheableClient(clientHandler, cc, provider);
  }
}
