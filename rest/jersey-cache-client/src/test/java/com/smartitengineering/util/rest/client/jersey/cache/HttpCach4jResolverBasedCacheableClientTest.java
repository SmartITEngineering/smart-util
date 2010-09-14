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
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.GZIPContentEncodingFilter;
import java.io.File;
import java.util.Arrays;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import junit.framework.Assert;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.codehaus.httpcache4j.cache.MemoryCacheStorage;
import org.codehaus.httpcache4j.client.HTTPClientResponseResolver;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class HttpCach4jResolverBasedCacheableClientTest {

  public static final int PORT = 20090;
  private static Server jettyServer;

  @BeforeClass
  public static void setupServer()
      throws Exception {
    System.out.println("::: Starting server :::");
    jettyServer = new Server(PORT);
    final String webapp = "./src/test/webapp";
    if (!new File(webapp).exists()) {
      throw new IllegalStateException("WebApp dir does not exist!");
    }
    WebAppContext webAppContext = new WebAppContext(webapp, "/");
    jettyServer.setHandler(webAppContext);
    jettyServer.start();
  }

  @AfterClass
  public static void shutdownServer()
      throws Exception {
    System.out.println("::: Stopping server :::");
    jettyServer.stop();
  }

  protected Client getAuthClient() {
    ClientConfig config = new DefaultClientConfig();
    config.getProperties().
        put(CacheableClientConfigProps.USERNAME, "name");
    config.getProperties().
        put(CacheableClientConfigProps.PASSWORD, "password");
    config.getClasses().add(Resources.HeaderWriter.class);
    HttpClient lClient = new HttpClient(new MultiThreadedHttpConnectionManager());
    HTTPClientResponseResolver responseResolver = new HTTPClientResponseResolver(lClient);
    CacheableClientHandler handler = new CacheableClientHandler(new MemoryCacheStorage(), responseResolver);
    Client c = CacheableClient.create(config, handler, null);
    return c;
  }

  protected Client getClient() {
    ClientConfig config = new DefaultClientConfig();
    config.getClasses().add(Resources.HeaderWriter.class);
    HttpClient lClient = new HttpClient(new MultiThreadedHttpConnectionManager());
    HTTPClientResponseResolver responseResolver = new HTTPClientResponseResolver(lClient);
    CacheableClientHandler handler = new CacheableClientHandler(new MemoryCacheStorage(), responseResolver);
    Client c = CacheableClient.create(config, handler, null);
    return c;
  }

  private UriBuilder getUri() {
    return UriBuilder.fromPath("/").host("localhost").port(PORT).scheme("http");
  }

  @Test
  public void testAuthGet() {
    Client c = getAuthClient();

    WebResource r = c.resource(getUri().path(Resources.AUTH_PATH).build());
    Assert.assertEquals("GET", r.get(String.class));
  }

  @Test
  public void testAuthPost() throws InterruptedException {
    Client c = getAuthClient();

    WebResource r = c.resource(getUri().path(Resources.AUTH_PATH).build());
    Builder builder = r.entity("POST", MediaType.TEXT_PLAIN_TYPE);
    Assert.assertEquals("POST", builder.post(String.class));
  }

  @Test
  public void testCookie() {
    Client c = getClient();
    WebResource r = c.resource(getUri().path(Resources.COOKIE_PATH).build());
    Assert.assertEquals("NO-COOKIE", r.get(String.class));
    Assert.assertEquals("value", r.get(String.class));
  }

  @Test
  public void testGZipPost() {
    Client c = getClient();
    c.addFilter(new GZIPContentEncodingFilter());

    WebResource r = c.resource(getUri().path(Resources.GZIP_PATH).build());
    byte[] content = new byte[1024 * 1024];
    Assert.assertTrue(Arrays.equals(content, r.post(byte[].class, content)));

    ClientResponse cr = r.post(ClientResponse.class, content);
    Assert.assertTrue(cr.hasEntity());
    cr.close();
  }

  @Test
  public void testHeaderPost() {
    Client c = getClient();

    WebResource r = c.resource(getUri().path(Resources.HEADER_PATH).build());

    ClientResponse cr = r.header("X-CLIENT", "client").entity("POST", MediaType.TEXT_PLAIN).post(ClientResponse.class);
    Assert.assertEquals(200, cr.getStatus());
    Assert.assertTrue(cr.hasEntity());
    cr.close();
  }

  @Test
  public void testHead() {

    WebResource r = getClient().resource(getUri().path(Resources.METHOD_PATH).build());
    ClientResponse cr = r.head();
    Assert.assertFalse(cr.hasEntity());
  }

  @Test
  public void testOptions() {
    WebResource r = getClient().resource(getUri().path(Resources.METHOD_PATH).build());
    ClientResponse cr = r.options(ClientResponse.class);
    Assert.assertTrue(cr.hasEntity());
    cr.close();
  }

  @Test
  public void testGet() {
    WebResource r = getClient().resource(getUri().path(Resources.METHOD_PATH).build());
    Assert.assertEquals("GET", r.get(String.class));

    ClientResponse cr = r.get(ClientResponse.class);
    Assert.assertTrue(cr.hasEntity());
    cr.close();
  }

  @Test
  public void testPost() {
    WebResource r = getClient().resource(getUri().path(Resources.METHOD_PATH).build());
    Assert.assertEquals("POST", r.post(String.class, "POST"));

    ClientResponse cr = r.post(ClientResponse.class, "POST");
    Assert.assertTrue(cr.hasEntity());
    cr.close();
  }

  @Test
  public void testPostVoid() {
    WebResource r = getClient().resource(getUri().path(Resources.METHOD_PATH).build());

    // This test will lock up if ClientResponse is not closed by WebResource.
    // TODO need a better way to detect this.
    for (int i = 0; i < 100; i++) {
      r.post("POST");
    }
  }

  @Test
  public void testPostNoProduce() {
    WebResource r = getClient().resource(getUri().path(Resources.METHOD_PATH).build());
    Assert.assertEquals(204, r.path("noproduce").post(ClientResponse.class, "POST").getStatus());

    ClientResponse cr = r.path("noproduce").post(ClientResponse.class, "POST");
    Assert.assertFalse(cr.hasEntity());
    cr.close();
  }

  @Test
  public void testPostNoConsumeProduce() {
    WebResource r = getClient().resource(getUri().path(Resources.METHOD_PATH).build());
    Assert.assertEquals(204, r.path("noconsumeproduce").post(ClientResponse.class).getStatus());

    ClientResponse cr = r.path("noconsumeproduce").post(ClientResponse.class, "POST");
    Assert.assertFalse(cr.hasEntity());
    cr.close();
  }

  @Test
  public void testPut() {
    WebResource r = getClient().resource(getUri().path(Resources.METHOD_PATH).build());
    Assert.assertEquals("PUT", r.put(String.class, "PUT"));

    ClientResponse cr = r.put(ClientResponse.class, "PUT");
    Assert.assertTrue(cr.hasEntity());
    cr.close();
  }

  @Test
  public void testDelete() {
    WebResource r = getClient().resource(getUri().path(Resources.METHOD_PATH).build());
    Assert.assertEquals("DELETE", r.delete(String.class));

    ClientResponse cr = r.delete(ClientResponse.class);
    Assert.assertTrue(cr.hasEntity());
    cr.close();
  }

  @Test
  public void testAll() {
    WebResource r = getClient().resource(getUri().path(Resources.METHOD_PATH).build());

    Assert.assertEquals("GET", r.get(String.class));

    Assert.assertEquals("POST", r.post(String.class, "POST"));

    Assert.assertEquals(204, r.path("noproduce").post(ClientResponse.class, "POST").getStatus());

    Assert.assertEquals(204, r.path("noconsumeproduce").post(ClientResponse.class).getStatus());

    Assert.assertEquals("PUT", r.post(String.class, "PUT"));

    Assert.assertEquals("DELETE", r.delete(String.class));
  }

  @Test
  public void testPostError() {
    WebResource r = getClient().resource(getUri().path(Resources.METHOD_ERROR_PATH).build());

    // This test will lock up if ClientResponse is not closed by WebResource.
    // TODO need a better way to detect this.
    for (int i = 0; i < 100; i++) {
      try {
        r.post("POST");
      }
      catch (UniformInterfaceException ex) {
      }
    }
  }

  @Test
  public void testPostErrorWithEntity() {
    WebResource r = getClient().resource(getUri().path(Resources.METHOD_ERROR_PATH + "/entity").build());

    // This test will lock up if ClientResponse is not closed by WebResource.
    // TODO need a better way to detect this.
    for (int i = 0; i < 100; i++) {
      try {
        r.post("POST");
      }
      catch (UniformInterfaceException ex) {
        String s = ex.getResponse().getEntity(String.class);
        Assert.assertEquals("error", s);
      }
    }
  }

  @Test
  public void testNoEntityGetWithClose() {
    WebResource r = getClient().resource(getUri().path(Resources.NO_ENTITY_PATH).build());

    for (int i = 0; i < 5; i++) {
      ClientResponse cr = r.get(ClientResponse.class);
      cr.close();
    }
  }

  @Test
  public void testNoEntityPost() {
    WebResource r = getClient().resource(getUri().path(Resources.NO_ENTITY_PATH).build());

    for (int i = 0; i < 5; i++) {
      ClientResponse cr = r.post(ClientResponse.class);
    }
  }

  @Test
  public void testNoEntityPostWithClose() {
    WebResource r = getClient().resource(getUri().path(Resources.NO_ENTITY_PATH).build());

    for (int i = 0; i < 5; i++) {
      ClientResponse cr = r.post(ClientResponse.class);
      cr.close();
    }
  }
}
