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

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.TerminatingClientHandler;
import com.sun.jersey.client.apache.ApacheHttpClientHandler;
import org.apache.commons.httpclient.HttpClient;

/**
 *
 * @author imyousuf
 */
public class CacheableClientHandler
    extends TerminatingClientHandler {

  private ApacheHttpClientHandler apacheHttpClientHandler;

  public CacheableClientHandler(HttpClient httpClient) {
    apacheHttpClientHandler = new ApacheHttpClientHandler(httpClient);
  }

  @Override
  public ClientResponse handle(ClientRequest cr)
      throws ClientHandlerException {
    return apacheHttpClientHandler.handle(cr);
  }

  public ApacheHttpClientHandler getApacheHttpClientHandler() {
    return apacheHttpClientHandler;
  }
}
