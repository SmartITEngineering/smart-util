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

import com.sun.jersey.spi.resource.Singleton;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import junit.framework.Assert;

/**
 *
 * @author imyousuf
 */
public class Resources {

  public static final String PREEMPTIVE_PATH = "test-preemptive-auth";
  public static final String AUTH_PATH = "test-auth";
  public static final String COOKIE_PATH = "cookie";
  public static final String GZIP_PATH = "gzip";
  public static final String METHOD_PATH = "method";
  public static final String METHOD_ERROR_PATH = "method-error";
  public static final String HEADER_PATH = "header";
  public static final String NO_ENTITY_PATH = "no-entity";

  @Path(NO_ENTITY_PATH)
  public static class NoEntityResource {

    @GET
    public Response get() {
      return Response.status(Status.CONFLICT).build();
    }

    @POST
    public void post(String entity) {
    }
  }

  @Target({ElementType.METHOD})
  @Retention(RetentionPolicy.RUNTIME)
  @HttpMethod("PATCH")
  public @interface PATCH {
  }

  @Path(METHOD_ERROR_PATH)
  public static class ErrorResource {

    @POST
    public Response post(String entity) {
      return Response.serverError().build();
    }

    @Path("entity")
    @POST
    public Response postWithEntity(String entity) {
      return Response.serverError().entity("error").build();
    }
  }

  @Path(METHOD_PATH)
  public static class HttpMethodResource {

    @GET
    public String get() {
      return "GET";
    }

    @POST
    public String post(String entity) {
      return entity;
    }

    @PUT
    public String put(String entity) {
      return entity;
    }

    @DELETE
    public String delete() {
      return "DELETE";
    }

    @DELETE
    @Path("withentity")
    public String delete(String entity) {
      return entity;
    }

    @POST
    @Path("noproduce")
    public void postNoProduce(String entity) {
    }

    @POST
    @Path("noconsumeproduce")
    public void postNoConsumeProduce() {
    }

    @PATCH
    public String patch(String entity) {
      return entity;
    }
  }

  @Path(HEADER_PATH)
  public static class HttpHeaderResource {

    @POST
    public String post(
        @HeaderParam("Transfer-Encoding") String transferEncoding,
        @HeaderParam("X-CLIENT") String xClient,
        @HeaderParam("X-WRITER") String xWriter,
        String entity) {
      Assert.assertEquals("client", xClient);
      if (transferEncoding == null || !transferEncoding.equals("chunked")) {
        Assert.assertEquals("writer", xWriter);
      }
      return entity;
    }
  }

  @Provider
  @Produces("text/plain")
  public static class HeaderWriter implements MessageBodyWriter<String> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
      return type == String.class;
    }

    @Override
    public long getSize(String t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
      return t != null ? t.length() : -1;
    }

    @Override
    public void writeTo(String t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException,
                                                                                                      WebApplicationException {
      httpHeaders.add("X-WRITER", "writer");
      entityStream.write(t.getBytes());
    }
  }

  @Path(GZIP_PATH)
  public static class Resource {

    @POST
    public byte[] post(byte[] content) {
      return content;
    }
  }

  @Path(COOKIE_PATH)
  public static class CookieResource {

    @GET
    public Response get(@Context HttpHeaders h) {
      Cookie c = h.getCookies().get("name");
      String e = (c == null) ? "NO-COOKIE" : c.getValue();
      return Response.ok(e).
          cookie(new NewCookie("name", "value")).build();
    }
  }

  @Path(PREEMPTIVE_PATH)
  public static class PreemptiveAuthResource {

    @GET
    public String get(@Context HttpHeaders h) {
      String value = h.getRequestHeaders().getFirst("Authorization");
      Assert.assertNotNull(value);
      return "GET";
    }

    @POST
    public String post(@Context HttpHeaders h, String e) {
      String value = h.getRequestHeaders().getFirst("Authorization");
      Assert.assertNotNull(value);
      return e;
    }
  }

  @Path(AUTH_PATH)
  @Singleton
  public static class AuthResource {

    int requestCount = 0;

    @GET
    public String get(@Context HttpHeaders h) {
      requestCount++;
      String value = h.getRequestHeaders().getFirst("Authorization");
      if (value == null) {
        Assert.assertEquals(1, requestCount);
        throw new WebApplicationException(Response.status(401).header("WWW-Authenticate", "Basic realm=\"WallyWorld\"").
            build());
      }
      else {
        Assert.assertTrue(requestCount > 1);
      }

      return "GET";
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String post(@Context HttpHeaders h, @Context HttpServletRequest request, String e) {
      requestCount++;
      String value = h.getRequestHeaders().getFirst("Authorization");
      if (value == null) {
        Assert.assertEquals(3, requestCount);
        throw new WebApplicationException(Response.status(401).header("WWW-Authenticate", "Basic realm=\"WallyWorld\"").
            build());
      }
      else {
        Assert.assertTrue(requestCount > 3);
      }
      return e;
    }
  }
}
