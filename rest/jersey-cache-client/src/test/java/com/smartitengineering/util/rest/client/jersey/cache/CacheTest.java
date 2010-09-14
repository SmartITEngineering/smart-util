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
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.LoggingFilter;
import java.io.File;
import java.net.URI;
import java.util.Date;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author imyousuf
 */
public class CacheTest {

  private static final String CONTENT = "Hello World!";
  private static final String RSRC_PATH = "helloworld.txt";
  private static final String LM_RSRC_PATH = "lm/" + RSRC_PATH;
  private static final String ETAG_RSRC_PATH = "lm/etag/" + RSRC_PATH;
  private static final Client CLIENT = CacheableClient.create();
  private static URI rootUri;
  private static Server jettyServer;

  @BeforeClass
  public static void setupServer()
      throws Exception {
    System.out.println("::: Starting server :::");
    jettyServer = new Server(HttpCache4jResolverBasedCacheableClientTest.PORT);
    final String webapp = "./target/testbed.war";
    if (!new File(webapp).exists()) {
      throw new IllegalStateException("WebApp dir does not exist!");
    }
    Handler webAppHandler = new WebAppContext(webapp, "/");
    jettyServer.setHandler(webAppHandler);
    jettyServer.start();
    setupResource();
  }

  private static void setupResource() {
    CLIENT.addFilter(new LoggingFilter());
    rootUri = UriBuilder.fromUri("/").host("localhost").port(HttpCache4jResolverBasedCacheableClientTest.PORT).scheme(
        "http").build();
    WebResource resource = CLIENT.resource(UriBuilder.fromUri(rootUri).path(RSRC_PATH).build());
    ClientResponse response = resource.entity(CONTENT, MediaType.TEXT_PLAIN).put(ClientResponse.class);
    if (response.getStatus() != ClientResponse.Status.NO_CONTENT.getStatusCode()) {
      throw new RuntimeException("Could not setup resource!");
    }
  }

  @AfterClass
  public static void shutdownServer()
      throws Exception {
    System.out.println("::: Stopping server :::");
    jettyServer.stop();
  }

  @Test
  public void testResourceExistence() {
    WebResource resource = CLIENT.resource(UriBuilder.fromUri(rootUri).path(RSRC_PATH).build());
    ClientResponse response = resource.get(ClientResponse.class);
    Assert.assertEquals(ClientResponse.Status.OK.getStatusCode(), response.getStatus());
    Assert.assertEquals(CONTENT, response.getEntity(String.class));
    resource = CLIENT.resource(UriBuilder.fromUri(rootUri).path(LM_RSRC_PATH).build());
    response = resource.get(ClientResponse.class);
    Assert.assertEquals(ClientResponse.Status.OK.getStatusCode(), response.getStatus());
    Assert.assertEquals(CONTENT, response.getEntity(String.class));
    Assert.assertNotNull(response.getLastModified());
    resource = CLIENT.resource(UriBuilder.fromUri(rootUri).path(ETAG_RSRC_PATH).build());
    response = resource.get(ClientResponse.class);
    Assert.assertEquals(ClientResponse.Status.OK.getStatusCode(), response.getStatus());
    Assert.assertEquals(CONTENT, response.getEntity(String.class));
    Assert.assertNotNull(response.getLastModified());
    Assert.assertNotNull(response.getEntityTag());
  }

  @Test
  public void testLastModified() {
    WebResource resource = CLIENT.resource(UriBuilder.fromUri(rootUri).path(LM_RSRC_PATH).build());
    ClientResponse response = resource.get(ClientResponse.class);
    Assert.assertEquals(ClientResponse.Status.OK.getStatusCode(), response.getStatus());
    Assert.assertEquals(CONTENT, response.getEntity(String.class));
    Assert.assertNotNull(response.getLastModified());
    Date date = response.getLastModified();
    response = resource.get(ClientResponse.class);
    Assert.assertEquals(ClientResponse.Status.NOT_MODIFIED.getStatusCode(), response.getStatus());
    Assert.assertEquals(CONTENT, response.getEntity(String.class));
    Assert.assertNotNull(response.getLastModified());
    Assert.assertEquals(date, response.getLastModified());
  }
}
