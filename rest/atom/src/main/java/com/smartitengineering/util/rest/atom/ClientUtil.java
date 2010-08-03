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

import com.sun.jersey.api.client.WebResource;
import java.net.URI;
import org.apache.abdera.model.Link;
import org.apache.commons.lang.StringUtils;

/**
 * A utility method to read entities
 * @author imyousuf
 */
public class ClientUtil {

  public static <T> T readEntity(Link link, HttpClient client, String acceptType, Class<? extends T> clazz) {
    if (link != null && client != null && clazz != null) {
      try {
        final URI uri = link.getHref().toURI();
        return readEntity(uri, client, acceptType, clazz);
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return null;
  }

  public static <T> T readEntity(final URI uri, HttpClient client, String acceptType, Class<? extends T> clazz) {
    if (uri != null && client != null && clazz != null) {
      try {
        WebResource resource = client.getWebResource(uri);
        if (StringUtils.isNotBlank(acceptType)) {
          resource.accept(acceptType);
        }
        T newEntity = resource.get(clazz);
        return newEntity;
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return null;
  }
}
