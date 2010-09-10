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
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.client.apache.ApacheHttpMethodProcessor;
import com.sun.jersey.client.apache.DefaultApacheHttpMethodProcessor;
import com.sun.jersey.core.header.InBoundHeaders;
import com.sun.jersey.core.util.ReaderWriter;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.commons.httpclient.HttpClient;
import org.codehaus.httpcache4j.Challenge;
import org.codehaus.httpcache4j.HTTPMethod;
import org.codehaus.httpcache4j.HTTPRequest;
import org.codehaus.httpcache4j.HTTPResponse;
import org.codehaus.httpcache4j.Header;
import org.codehaus.httpcache4j.Headers;
import org.codehaus.httpcache4j.MIMEType;
import org.codehaus.httpcache4j.UsernamePasswordChallenge;
import org.codehaus.httpcache4j.cache.CacheStorage;
import org.codehaus.httpcache4j.cache.HTTPCache;
import org.codehaus.httpcache4j.cache.MemoryCacheStorage;
import org.codehaus.httpcache4j.client.HTTPClientResponseResolver;
import org.codehaus.httpcache4j.payload.InputStreamPayload;
import org.codehaus.httpcache4j.payload.Payload;
import org.codehaus.httpcache4j.resolver.ResponseResolver;

/**
 *
 * @author imyousuf
 */
public class CacheableClientHandler
    extends TerminatingClientHandler {

  private final HTTPCache cache;
  private final HttpClient httpClient;
  private final boolean internalResolver;
  private final ThreadLocal<ClientRequest> requestHolder;
  private final ApacheHttpMethodProcessor methodProcessor;

  public CacheableClientHandler(HttpClient httpClient, ClientConfig clientConfig) {
    this(httpClient, clientConfig, new MemoryCacheStorage());
  }

  public CacheableClientHandler(HttpClient httpClient, ClientConfig clientConfig,
                                CacheStorage storage) {
    this(httpClient, clientConfig, storage, null);
  }

  public CacheableClientHandler(HttpClient httpClient, ClientConfig clientConfig,
                                CacheStorage storage, ResponseResolver responseResolver) {
    this.httpClient = httpClient;
    requestHolder = new ThreadLocal<ClientRequest>();
    if (responseResolver == null) {
      methodProcessor = DefaultApacheHttpMethodProcessor.getProcessorInstance(httpClient, clientConfig, requestHolder);
      responseResolver = new CustomApacheHttpClientResponseResolver(methodProcessor);
    }
    else {
      methodProcessor = null;
    }
    cache = new HTTPCache(storage, responseResolver);
    internalResolver = responseResolver instanceof CustomApacheHttpClientResponseResolver;
  }

  @Override
  public ClientResponse handle(ClientRequest cr)
      throws ClientHandlerException {
    final HTTPMethod method = HTTPMethod.valueOf(cr.getMethod());
    HTTPRequest request = processRequest(cr, method);
    if (internalResolver) {
      requestHolder.set(cr);
    }
    HTTPResponse cachedResponse = cache.doCachedRequest(request);
    if (internalResolver) {
      requestHolder.remove();
    }
    Headers headers = cachedResponse.getHeaders();
    InBoundHeaders inBoundHeaders = getInBoundHeaders(headers);
    final InputStream entity = getEntityStream(cachedResponse);
    ClientResponse response = new ClientResponse(cachedResponse.getStatus().getCode(), inBoundHeaders, entity,
                                                 getMessageBodyWorkers());
    return response;
  }

  public HttpClient getHttpClient() {
    return httpClient;
  }

  public HTTPCache getCache() {
    return cache;
  }

  protected InputStream getEntityStream(HTTPResponse cachedResponse) {
    final InputStream entity;
    if (cachedResponse.hasPayload()) {
      final InputStream inputStream = cachedResponse.getPayload().getInputStream();
      if (inputStream.markSupported()) {
        entity = inputStream;
      }
      else {
        entity = new BufferedInputStream(inputStream, ReaderWriter.BUFFER_SIZE);
      }
    }
    else {
      entity = new ByteArrayInputStream(new byte[0]);
    }
    return entity;
  }

  protected InBoundHeaders getInBoundHeaders(Headers headers) {
    InBoundHeaders inBoundHeaders = new InBoundHeaders();
    for (Header header : headers) {
      List<String> list = inBoundHeaders.get(header.getName());
      if (list == null) {
        list = new ArrayList<String>();
      }
      list.add(header.getValue());
      inBoundHeaders.put(header.getName(), list);
    }
    return inBoundHeaders;
  }

  protected HTTPRequest processRequest(ClientRequest cr,
                                       final HTTPMethod method) {
    HTTPRequest request = new HTTPRequest(cr.getURI(), method);
    if (!internalResolver) {
      final Map<String, Object> props = cr.getProperties();
      /*
       * Add authorization challenge
       */
      if (props.containsKey(CacheableClientConfigProps.USERNAME) &&
          props.containsKey(CacheableClientConfigProps.PASSWORD)) {
        final String username = (String) props.get(CacheableClientConfigProps.USERNAME);
        final String password = (String) props.get(CacheableClientConfigProps.PASSWORD);
        Challenge challenge =
                  new UsernamePasswordChallenge(username, password);
        request = request.challenge(challenge);
      }
      /*
       * Copy headers set by user explicitly
       */
      Headers requestHeaders = new Headers();
      MultivaluedMap<String, Object> map = cr.getHeaders();
      for (String key : map.keySet()) {
        List<Object> values = map.get(key);
        ArrayList<Header> headers = new ArrayList<Header>(values.size());
        for (Object value : values) {
          Header header = new Header(key, ClientRequest.getHeaderValue(value));
          headers.add(header);
        }
        requestHeaders.add(key, headers);
      }
      request = request.headers(requestHeaders);
      /*
       * Copy payload set into the request if any
       */
      if (cr.getEntity() != null) {
        final RequestEntityWriter requestEntityWriter = getRequestEntityWriter(cr);
        final MIMEType mimeType = new MIMEType(requestEntityWriter.getMediaType().getType(), requestEntityWriter.
            getMediaType().getSubtype());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
          requestEntityWriter.writeRequestEntity(outputStream);
        }
        catch (IOException ex) {
          throw new ClientHandlerException(ex);
        }
        Payload payload = new InputStreamPayload(new ByteArrayInputStream(outputStream.toByteArray()), mimeType);
        request = request.payload(payload);
      }
    }
    return request;
  }

  public ApacheHttpMethodProcessor getMethodProcessor() {
    return methodProcessor;
  }
}
