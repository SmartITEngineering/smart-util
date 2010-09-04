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

import com.smartitengineering.util.rest.client.EntityResource;
import com.smartitengineering.util.rest.client.HttpClient;
import com.smartitengineering.util.rest.atom.resources.SomeDomainResource;
import com.smartitengineering.util.rest.atom.resources.domain.SomeDomain;
import com.smartitengineering.util.rest.client.ClientUtil;
import com.smartitengineering.util.rest.client.jersey.cache.CacheableClient;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.atom.abdera.impl.provider.entity.FeedProvider;
import com.sun.jersey.json.impl.provider.entity.JSONRootElementProvider;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import javax.ws.rs.core.MediaType;
import junit.framework.Assert;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

  private static Server jettyServer;
  private Client client;
  private HttpClient httpClient;

  /**
   * Create the test case
   * @param testName name of the test case
   */
  public AppTest() {
  }

  @BeforeClass
  public static void setupServer()
      throws Exception {
    System.out.println("::: Starting server :::");
    jettyServer = new Server(9090);
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

  @Before
  public void setup() {
    DefaultClientConfig config = new DefaultClientConfig();
    config.getClasses().add(FeedProvider.class);
    config.getClasses().add(JSONRootElementProvider.App.class);
    client = CacheableClient.create(config);
    httpClient = new HttpClient(client, "localhost", 9090);
  }

  @Test
  public void testSimpleGet() {
    System.out.println("::: testSimpleGet :::");
    WebResource resource = client.resource("http://localhost:9090/");
    Assert.assertEquals(204, resource.head().getStatus());
  }

  @Test
  public void testFeed() {
    try {
      System.out.println("::: testFeed :::");
      URI uri = new URI("http://localhost:9090/feed");
      ClientResponse response =
                     ClientUtil.readClientResponse(uri, httpClient, MediaType.APPLICATION_ATOM_XML);
      Feed feed = AtomClientUtil.getFeed(response);
      Assert.assertNotNull(feed);
    }
    catch (URISyntaxException ex) {
      Assert.fail(ex.getMessage());
    }
  }

  @Test
  public void testJson() {
    System.out.println("::: testJson :::");
    WebResource resource = client.resource("http://localhost:9090/domain/0");
    SomeDomain domain = resource.get(SomeDomain.class);
    Assert.assertNotNull(domain);
  }

  @Test
  public void testFeedReader() {
    System.out.println("::: testFeedReader :::");
    final String rootFeedUriStr = "http://localhost:9090/feed";
    WebResource resource = client.resource(rootFeedUriStr);
    Feed feed = resource.get(Feed.class);
    FeedEntryReader<SomeDomain> reader = new FeedEntryReader<SomeDomain>(httpClient, Arrays.<Entry<String, String>>
        asList(new AbstractMap.SimpleEntry<String, String>(Link.REL_ALTERNATE, MediaType.APPLICATION_JSON)),
                                                                         SomeDomain.class);
    Collection<EntityResource<SomeDomain>> collection = reader.readEntriesFromRooFeed(feed);
    Assert.assertNotNull(collection);
    Assert.assertEquals(5, collection.size());
    try {
      int newCount = 20;
      URI uri = new URI(rootFeedUriStr + "?count=" + newCount);
      feed = ClientUtil.readEntity(uri, httpClient, MediaType.APPLICATION_ATOM_XML, Feed.class);
      collection = reader.readEntriesFromRooFeed(feed);
      Assert.assertNotNull(collection);
      Assert.assertEquals(newCount, collection.size());
    }
    catch (Exception ex) {
      ex.printStackTrace();
      Assert.fail(ex.getMessage());
    }
  }

  @Test
  public void testPaginatedWrapper() {
    System.out.println("::: testPaginatedWrapper :::");
    final String rootFeedUriStr = "http://localhost:9090/feed";
    FeedEntryReader<SomeDomain> reader = new FeedEntryReader<SomeDomain>(httpClient, Arrays.<Entry<String, String>>
        asList(new AbstractMap.SimpleEntry<String, String>(Link.REL_ALTERNATE, MediaType.APPLICATION_JSON)),
                                                                         SomeDomain.class);
    try {
      int newCount = 20;
      URI uri = new URI(rootFeedUriStr + "?" + SomeDomainResource.COUNT + "=" + newCount);

      Feed feed = ClientUtil.readEntity(uri, httpClient, MediaType.APPLICATION_ATOM_XML, Feed.class);
      //Thread.sleep(10000);
      PaginatedEntitiesWrapper<SomeDomain> domains = new PaginatedEntitiesWrapper<SomeDomain>(feed, httpClient, reader);
      List<EntityResource<SomeDomain>> domainList = new ArrayList<EntityResource<SomeDomain>>(SomeDomainResource.DOMAIN_SIZE);
      for (int i = 0; i < (SomeDomainResource.DOMAIN_SIZE / newCount); ++i) {
        domainList.addAll(domains.getEntitiesForCurrentPage());
        domains = domains.next();
      }
      Assert.assertNull(domains);
      Assert.assertEquals(SomeDomainResource.DOMAIN_SIZE, domainList.size());
    }
    catch (Exception ex) {
      ex.printStackTrace();
      Assert.fail(ex.getMessage());
    }
  }

  @Test
  public void testPaginatedList() {
    System.out.println("::: testPaginatedList :::");
    final String rootFeedUriStr = "http://localhost:9090/feed";
    FeedEntryReader<SomeDomain> reader = new FeedEntryReader<SomeDomain>(httpClient, Arrays.<Entry<String, String>>
        asList(new AbstractMap.SimpleEntry<String, String>(Link.REL_ALTERNATE, MediaType.APPLICATION_JSON)),
                                                                         SomeDomain.class);
    try {
      int newCount = 20;
      URI uri = new URI(rootFeedUriStr + "?" + SomeDomainResource.COUNT + "=" + newCount);
      Feed feed = ClientUtil.readEntity(uri, httpClient, MediaType.APPLICATION_ATOM_XML, Feed.class);
      PaginatedEntitiesWrapper<SomeDomain> domains = new PaginatedEntitiesWrapper<SomeDomain>(feed, httpClient, reader);
      List<EntityResource<SomeDomain>> domainList = new PaginatedFeedEntitiesList<SomeDomain>(domains);
      Assert.assertEquals(SomeDomainResource.DOMAIN_SIZE, domainList.size());
      final int midSize = SomeDomainResource.DOMAIN_SIZE / 2;
      domainList = new PaginatedFeedEntitiesList<SomeDomain>(domains, midSize);
      Assert.assertEquals(new Double(Math.ceil(midSize / (double) newCount)).intValue() * newCount, domainList.size());
    }
    catch (Exception ex) {
      ex.printStackTrace();
      Assert.fail(ex.getMessage());
    }
  }

  @Test
  public void testDynaPaginatedList() {
    System.out.println("::: testDynaPaginatedList :::");
    final String rootFeedUriStr = "http://localhost:9090/osfeed";
    FeedEntryReader<SomeDomain> reader = new FeedEntryReader<SomeDomain>(httpClient, Arrays.<Entry<String, String>>
        asList(new AbstractMap.SimpleEntry<String, String>(Link.REL_ALTERNATE, MediaType.APPLICATION_JSON)),
                                                                         SomeDomain.class);
    try {
      int newCount = 20;
      URI uri = new URI(rootFeedUriStr + "?" + SomeDomainResource.COUNT + "=" + newCount);
      Feed feed = ClientUtil.readEntity(uri, httpClient, MediaType.APPLICATION_ATOM_XML, Feed.class);
      PaginatedEntitiesWrapper<SomeDomain> domains = new PaginatedEntitiesWrapper<SomeDomain>(feed, httpClient, reader);
      DynamicPaginatedEntitiesList<SomeDomain> dynaDomainList = new DynamicPaginatedEntitiesList<SomeDomain>(domains);
      List<EntityResource<SomeDomain>> domainList = dynaDomainList;
      Assert.assertEquals(SomeDomainResource.DOMAIN_SIZE, domainList.size());
      Assert.assertEquals(0, dynaDomainList.getBackedupList().size());
      Assert.assertNotNull(domainList.get(0));
      Assert.assertEquals(newCount, dynaDomainList.getBackedupList().size());
      final int midSize = SomeDomainResource.DOMAIN_SIZE / 2;
      Assert.assertNotNull(domainList.get(midSize));
      Assert.assertEquals(new Double(Math.ceil(midSize / (double) newCount)).intValue() * newCount, dynaDomainList.
          getBackedupList().size());
      Assert.assertNotNull(domainList.get(SomeDomainResource.DOMAIN_SIZE - 1));
      Assert.assertEquals(SomeDomainResource.DOMAIN_SIZE, dynaDomainList.getBackedupList().size());
    }
    catch (Exception ex) {
      ex.printStackTrace();
      Assert.fail(ex.getMessage());
    }
  }
}
