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

import com.smartitengineering.util.rest.client.jersey.cache.CacheableClient;
import com.smartitengineering.util.rest.client.jersey.cache.CacheableClientConfigProps;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.client.apache.config.ApacheHttpClientConfig;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author imyousuf
 */
public class ApplicationWideClientFactoryImpl implements ClientFactory {

  public static final String TRACE = "com.smartitengineering.util.rest.client.ApplicationWideClientFactoryImpl.trace";
  private Client client;
  private ApacheHttpClientConfig clientConfig;
  private HttpClient httpClient;

  private ApplicationWideClientFactoryImpl(ConnectionConfig connectionConfig, ConfigProcessor processor) {
    if (connectionConfig == null) {
      throw new IllegalArgumentException("Connection config can not be null!");
    }
    clientConfig = new DefaultApacheHttpClientConfig();
    if (processor != null) {
      processor.process(clientConfig);
    }
    final Object timeoutProp = clientConfig.getProperty(CacheableClientConfigProps.TIMEOUT);
    if (timeoutProp != null) {
      clientConfig.getProperties().put(ApacheHttpClientConfig.PROPERTY_READ_TIMEOUT, timeoutProp);
    }
    final Object username = clientConfig.getProperty(CacheableClientConfigProps.USERNAME), password = clientConfig.
        getProperty(CacheableClientConfigProps.PASSWORD);
    if (username != null && password != null) {
      clientConfig.getState().setCredentials(null, null, -1, username.toString(), password.toString());
      clientConfig.getProperties().put(ApacheHttpClientConfig.PROPERTY_PREEMPTIVE_AUTHENTICATION, Boolean.TRUE);
    }
    client = CacheableClient.create(clientConfig);
    if (Boolean.parseBoolean(System.getProperty(TRACE))) {
      client.addFilter(new LoggingFilter());
    }
    httpClient = new HttpClient(client, connectionConfig.getHost(), connectionConfig.getPort());
  }
  private static final Map<Entry<ConnectionConfig, ConfigProcessor>, ClientFactory> APPLICATION_CONTEXT;

  static {
    APPLICATION_CONTEXT = new ConcurrentHashMap<Entry<ConnectionConfig, ConfigProcessor>, ClientFactory>();
  }

  public static ClientFactory getClientFactory(ConnectionConfig connectionConfig, ConfigProcessor processor) {
    Entry<ConnectionConfig, ConfigProcessor> entry = new SimpleEntry<ConnectionConfig, ConfigProcessor>(connectionConfig,
                                                                                                        processor);
    if (!APPLICATION_CONTEXT.containsKey(entry)) {
      ApplicationWideClientFactoryImpl clientFactoryImpl = new ApplicationWideClientFactoryImpl(connectionConfig,
                                                                                                processor);
      APPLICATION_CONTEXT.put(entry, clientFactoryImpl);
      return clientFactoryImpl;
    }
    else {
      return APPLICATION_CONTEXT.get(entry);
    }
  }

  @Override
  public Client getClient() {
    return client;
  }

  @Override
  public HttpClient getHttpClient() {
    return httpClient;
  }

  @Override
  public ClientConfig getClientConfig() {
    return clientConfig;
  }
}
