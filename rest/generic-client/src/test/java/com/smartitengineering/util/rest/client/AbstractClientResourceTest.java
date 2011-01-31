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
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MediaType;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractClientResourceTest
    extends TestCase {

  public AbstractClientResourceTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(AbstractClientResourceTest.class);
  }

  public void testApp() throws UniformInterfaceException, URISyntaxException {
    SimpleResource resource = new SimpleResource();
    assertEquals(ArrayList.class, resource.getEntityClass());
    ComplexResource complexResource = new ComplexResource();
    assertEquals(List.class, complexResource.getEntityClass());
  }

  static class SimpleResource extends AbstractClientResource<ArrayList, Resource> {

    public SimpleResource() throws IllegalArgumentException, UniformInterfaceException, URISyntaxException {
      super(null, new URI("http://localhost:9090/"), MediaType.APPLICATION_ATOM_XML, null, null, false, null, true);
    }

    @Override
    protected void processClientConfig(ClientConfig clientConfig) {
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
      return null;
    }
  }

  static class ComplexResource extends AbstractClientResource<List<URI>, Resource> {

    public ComplexResource() throws IllegalArgumentException, UniformInterfaceException, URISyntaxException {
      super(null, new URI("http://localhost:9090/"), MediaType.APPLICATION_ATOM_XML, null, null, false, null, true);
    }

    @Override
    protected void processClientConfig(ClientConfig clientConfig) {
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
      return null;
    }
  }
}
