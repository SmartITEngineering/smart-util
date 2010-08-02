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

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import org.apache.commons.lang.StringUtils;

/**
 * A convenient Jersey Client wrapper which intends to create absolute URI from a relative URI
 * @author imyousuf
 */
public class HttpClient {

  private final Client client;
  private final String host;
  private final int port;

  /**
   * Initialize a Jersey based HTTP Client so that relative URI can be handled
   * @param client The client to use to fetch resources
   * @param host The host name or IP address to connect to
   * @param port Port number to connect to
   * @throws IllegalArgumentException If any parameter is null or port is non-positive and greater than 65536
   */
  public HttpClient(Client client, String host, int port) throws IllegalArgumentException {
    if (client == null || StringUtils.isBlank(host) || port < 1 || port > 65536) {
      throw new IllegalArgumentException();
    }
    this.client = client;
    this.host = host;
    this.port = port;
  }

  public WebResource getWebResource(URI resourceUri) {
    if (resourceUri == null) {
      return null;
    }
    final WebResource resource;
    if (StringUtils.isNotBlank(resourceUri.getHost())) {
      resource = client.resource(resourceUri);
    }
    else {
      UriBuilder builder = UriBuilder.fromUri(resourceUri);
      builder.host(host);
      builder.port(port);
      builder.scheme("http");
      resource = client.resource(builder.build());
    }
    return resource;
  }

  public Client getClient() {
    return client;
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }
}
