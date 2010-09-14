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
import com.sun.jersey.api.client.filter.GZIPContentEncodingFilter;
import com.sun.jersey.client.apache.config.ApacheHttpClientConfig;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;
import java.io.File;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import junit.framework.Assert;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CustomResolverBasedCacheableClientTest {

  public static final int PORT = 20090;
  private static final ApacheHttpClientConfig DEFAULT_CONFIG = new DefaultApacheHttpClientConfig();
  private static final ApacheHttpClientConfig DEFAULT_CHUNKED_CONFIG = new DefaultApacheHttpClientConfig();
  private static final ApacheHttpClientConfig GZIP_CONFIG = new DefaultApacheHttpClientConfig();
  private static Server jettyServer;
  private static final Map<ApacheHttpClientConfig, Client> CLIENT_CACHE =
                                                           new ConcurrentHashMap<ApacheHttpClientConfig, Client>();
  private static final Map<Client, Map.Entry<HttpClient, MultiThreadedHttpConnectionManager>> HTTP_CLIENTS =
                                                                                              new ConcurrentHashMap<Client, Map.Entry<HttpClient, MultiThreadedHttpConnectionManager>>();

  static {
    DEFAULT_CHUNKED_CONFIG.getProperties().put(ApacheHttpClientConfig.PROPERTY_CHUNKED_ENCODING_SIZE, 512);
  }

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
    return getAuthClient(false);
  }

  protected Client getAuthClient(boolean pre) {
    ApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
    config.getState().setCredentials(null, null, -1, "name", "password");
    config.getProperties().put(ApacheHttpClientConfig.PROPERTY_PREEMPTIVE_AUTHENTICATION, pre);
    return getClient(config);
  }

  protected Client getClient() {
    return getClient(DEFAULT_CONFIG);
  }

  protected Client getGZipClient() {
    final Client client = getClient(GZIP_CONFIG);
    client.addFilter(new GZIPContentEncodingFilter());
    return client;
  }

  protected Client getGZipClient(ApacheHttpClientConfig config) {
    final Client client = getClient(config);
    client.addFilter(new GZIPContentEncodingFilter());
    return client;
  }

  protected Client getClient(ApacheHttpClientConfig clientConfig) {
    if (CLIENT_CACHE.containsKey(clientConfig)) {
      return CLIENT_CACHE.get(clientConfig);
    }
    clientConfig.getClasses().add(Resources.HeaderWriter.class);
    final MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager =
                                             new MultiThreadedHttpConnectionManager();
    HttpClient httpClient = new HttpClient(multiThreadedHttpConnectionManager);
    CacheableClientHandler handler = new CacheableClientHandler(httpClient, clientConfig);
    Client client = CacheableClient.create(clientConfig);
    CLIENT_CACHE.put(clientConfig, client);
    HTTP_CLIENTS.put(client,
                     new SimpleEntry<HttpClient, MultiThreadedHttpConnectionManager>(httpClient,
                                                                                     multiThreadedHttpConnectionManager));
    return client;
  }

  private UriBuilder getUri() {
    return UriBuilder.fromPath("/").host("localhost").port(PORT).scheme("http");
  }

  @Test
  public void testPreemptiveAuth() {
    Client c = getAuthClient(true);

    WebResource r = c.resource(getUri().path(Resources.PREEMPTIVE_PATH).build());
    Assert.assertEquals("GET", r.get(String.class));
  }

  @Test
  public void testPreemptiveAuthPost() {
    Client c = getAuthClient(true);

    WebResource r = c.resource(getUri().path(Resources.PREEMPTIVE_PATH).build());
    Assert.assertEquals("POST", r.post(String.class, "POST"));
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
    ApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
    config.getClasses().add(Resources.HeaderWriter.class);
    config.getProperties().put(ApacheHttpClientConfig.PROPERTY_HANDLE_COOKIES, true);
    Client c = getClient(config);
    WebResource r = c.resource(getUri().path(Resources.COOKIE_PATH).build());
    Assert.assertEquals("NO-COOKIE", r.get(String.class));
    Assert.assertEquals("value", r.get(String.class));
  }

  @Test
  public void testCookieWithState() {
    ApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
    config.getClasses().add(Resources.HeaderWriter.class);
    config.getState();
    config.getProperties().put(ApacheHttpClientConfig.PROPERTY_HANDLE_COOKIES, true);
    Client c = getClient(config);
    WebResource r = c.resource(getUri().path(Resources.COOKIE_PATH).build());
    Assert.assertEquals("NO-COOKIE", r.get(String.class));
    Assert.assertEquals("value", r.get(String.class));
    Assert.assertNotNull(config.getState().getHttpState().getCookies());
    Assert.assertEquals(1, config.getState().getHttpState().getCookies().length);
    Assert.assertEquals("value", config.getState().getHttpState().getCookies()[0].getValue());
  }

  @Test
  public void testGZipPost() {
    Client c = getGZipClient();

    WebResource r = c.resource(getUri().path(Resources.GZIP_PATH).build());
    byte[] content = new byte[1024 * 1024];
    Assert.assertTrue(Arrays.equals(content, r.post(byte[].class, content)));

    ClientResponse cr = r.post(ClientResponse.class, content);
    Assert.assertTrue(cr.hasEntity());
    cr.close();
  }

  @Test
  public void testGZipPostChunked() {
    ApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
    config.getProperties().put(ApacheHttpClientConfig.PROPERTY_CHUNKED_ENCODING_SIZE, 512);
    Client c = getGZipClient(config);

    WebResource r = c.resource(getUri().path(Resources.GZIP_PATH).build());
    byte[] content = new byte[1024 * 1024];
    Assert.assertTrue(Arrays.equals(content, r.post(byte[].class, content)));

    ClientResponse cr = r.post(ClientResponse.class, "POST");
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

  //Using new config due to a bug that the request just after HEAD is hunged up when using Apach HTTP Client
  @Test
  public void testHead() {
    WebResource r =
                getClient(new DefaultApacheHttpClientConfig()).resource(getUri().path(Resources.METHOD_PATH).build());
    ClientResponse cr = r.head();
    Assert.assertFalse(cr.hasEntity());
    cr.close();
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
  public void testDeleteWithEntity() {
    WebResource r = getClient().resource(getUri().path(Resources.METHOD_PATH + "/withentity").build());
    r.addFilter(new com.sun.jersey.api.client.filter.LoggingFilter());
    Assert.assertEquals("DELETE with entity", r.delete(String.class, "DELETE with entity"));

    ClientResponse cr = r.delete(ClientResponse.class, "DELETE with entity");
    Assert.assertTrue(cr.hasEntity());
    cr.close();
  }

  @Test
  public void testPatch() {
    WebResource r = getClient().resource(getUri().path(Resources.METHOD_PATH).build());
    r.addFilter(new com.sun.jersey.api.client.filter.LoggingFilter());
    Assert.assertEquals("PATCH", r.method("PATCH", String.class, "PATCH"));

    ClientResponse cr = r.method("PATCH", ClientResponse.class, "PATCH");
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
  public void testNoEntityGet() {
    final Client client = getClient();
    WebResource r = client.resource(getUri().path(Resources.NO_ENTITY_PATH).build());

    for (int i = 0; i < 5; i++) {
      System.out.println("i " + i);
      ClientResponse cr = r.get(ClientResponse.class);
      //TODO It hangs here need to investigate whether its for Jetty, it hangs with ApacheHttpClient.create() as well
      cr.close();
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

  @Test
  public void testHeaderPostChunked() {
    Client c = getClient(DEFAULT_CHUNKED_CONFIG);

    WebResource r = c.resource(getUri().path(Resources.HEADER_PATH).build());

    ClientResponse cr = r.header("X-CLIENT", "client").post(ClientResponse.class, "POST");
    Assert.assertEquals(200, cr.getStatus());
    Assert.assertTrue(cr.hasEntity());
    cr.close();
  }

  @Test
  public void testMethodPostChunked() {
    Client c = getClient(DEFAULT_CHUNKED_CONFIG);

    WebResource r = c.resource(getUri().path(Resources.METHOD_PATH).build());
    Assert.assertEquals("POST", r.post(String.class, "POST"));

    ClientResponse cr = r.post(ClientResponse.class, "POST");
    Assert.assertTrue(cr.hasEntity());
    cr.close();
  }
}
