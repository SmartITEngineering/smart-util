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
package com.smartitengineering.util.opensearch.io.impl.dom;

import com.smartitengineering.util.opensearch.api.Image;
import com.smartitengineering.util.opensearch.api.OpenSearchDescriptor;
import com.smartitengineering.util.opensearch.api.Query;
import com.smartitengineering.util.opensearch.api.Url;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author imyousuf
 */
class OpenSearchDescriptorWriter implements XmlConstants {

  private OutputStream sink;
  private OpenSearchDescriptor descriptor;
  private boolean closeOnFinish;
  private final Map<String, String> namespaceUris;

  OpenSearchDescriptorWriter(OutputStream sink, OpenSearchDescriptor descriptor) {
    this(sink, descriptor, false);
  }

  OpenSearchDescriptorWriter(OutputStream sink, OpenSearchDescriptor descriptor, boolean closeOnFinish) {
    if (sink == null) {
      throw new IllegalArgumentException("Source stream can not be null!");
    }
    if (descriptor == null) {
      throw new IllegalArgumentException("Descriptor can not be null!");
    }
    this.descriptor = descriptor;
    this.sink = sink;
    this.closeOnFinish = closeOnFinish;
    this.namespaceUris = new HashMap<String, String>();
  }

  public void write() throws IOException {
    try {
      final Document document;
      document = buildDoc();
      IOUtils.write(document.toXML(), sink);
    }
    finally {
      if (closeOnFinish) {
        try {
          sink.close();
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }
  }

  protected Document buildDoc() {
    Element rootElement = getOpenSearchElement(ELEM_OPENSEARCHDESCRIPTION);
    addStringElement(rootElement, ELEM_SHORTNAME, descriptor.getShortName());
    addStringElement(rootElement, ELEM_DESCRIPTION, descriptor.getDescription());
    addCollectionOfStringAsSpaceDelimitedStringTextElement(descriptor.getTags(), ELEM_TAGS, rootElement);
    addStringElement(rootElement, ELEM_CONTACT, descriptor.getContact());
    for (Url url : descriptor.getUrls()) {
      addUrlElement(url, rootElement);
    }
    addStringElement(rootElement, ELEM_LONGNAME, descriptor.getLongName());
    for (Image image : descriptor.getImages()) {
      addImageElement(image, rootElement);
    }
    for (Query query : descriptor.getQueries()) {
      addQueryElement(query, rootElement);
    }
    addStringElement(rootElement, ELEM_DEVELOPER, descriptor.getDeveloper());
    addStringElement(rootElement, ELEM_ATTRIBUTION, descriptor.getAttribution());
    addStringElement(rootElement, ELEM_SYNDICATIONRIGHT, descriptor.getSyndicationRight().getValue());
    addStringElement(rootElement, ELEM_ADULTCONTENT, Boolean.toString(descriptor.containsAdultContent()));
    addStringElement(rootElement, ELEM_LANGUAGE, getLocaleString(descriptor.getLanguage()));
    addStringElement(rootElement, ELEM_OUTPUTENCODING, descriptor.getOutputEncoding());
    addStringElement(rootElement, ELEM_INPUTENCODING, descriptor.getInputEncoding());
    return new Document(rootElement);
  }

  protected String getCollectionOfStringAsSpaceDelimitedString(final Collection<? extends Object> objs) {
    final String singleString;
    if (!objs.isEmpty()) {
      final StringBuilder stringBuilder = new StringBuilder("");
      boolean first = true;
      for (Object obj : objs) {
        if (obj == null) {
          continue;
        }
        String objStr = obj.toString();
        if (StringUtils.isNotBlank(objStr)) {
          if (!first) {
            stringBuilder.append(' ');
          }
          stringBuilder.append(objStr);
          first = false;
        }
      }
      singleString = stringBuilder.toString();
    }
    else {
      singleString = null;
    }
    return singleString;
  }

  protected Element addCollectionOfStringAsSpaceDelimitedStringTextElement(final Collection<? extends Object> objs,
                                                                           String childElementName,
                                                                           Element parentElement) {
    String tagString = getCollectionOfStringAsSpaceDelimitedString(objs);
    return addStringElement(parentElement, childElementName, tagString);
  }

  protected Node addCollectionOfStringAsSpaceDelimitedStringAttributed(final Collection<? extends Object> objs,
                                                                       String childAttrName, Element parentElement) {
    String attrValue = getCollectionOfStringAsSpaceDelimitedString(objs);
    return addStringAttr(parentElement, childAttrName, attrValue);
  }

  protected Element addStringElement(Element parentElement, String childElementName, String value) {
    if (StringUtils.isNotBlank(value)) {
      Element childElement = getOpenSearchElement(childElementName);
      parentElement.appendChild(childElement);
      childElement.appendChild(value);
      return childElement;
    }
    return null;
  }

  protected Node addStringAttr(Element parentElement, String childAttrName, int value, int min) {
    if (value > min) {
      return addStringAttr(parentElement, childAttrName, value);
    }
    else {
      return null;
    }
  }

  protected Node addStringAttr(Element parentElement, String childAttrName, int value) {
    return addStringAttr(parentElement, childAttrName, Integer.toString(value));
  }

  protected Node addStringAttr(Element parentElement, String childAttrName, String value) {
    if (StringUtils.isNotBlank(value)) {
      final int indexOf = childAttrName.indexOf(':');
      final boolean isNamespace;
      final String ns;
      final String prefix;
      if (indexOf > 0) {
        ns = childAttrName.substring(0, indexOf);
        if (StringUtils.equals("xmlns", ns)) {
          isNamespace = true;
        }
        else {
          isNamespace = false;
        }
        prefix = childAttrName.substring(indexOf + 1);
      }
      else {
        ns = "";
        isNamespace = false;
        prefix = "";
      }
      if (isNamespace) {
        namespaceUris.put(prefix, value);
        parentElement.addNamespaceDeclaration(prefix, value);
        return null;
      }
      else {
        final Attribute childAttribute;
        if (indexOf > 0) {
          childAttribute = new Attribute(childAttrName, namespaceUris.get(ns), value);
        }
        else {
          childAttribute = new Attribute(childAttrName, value);
        }
        parentElement.addAttribute(childAttribute);
        return childAttribute;
      }
    }
    return null;
  }

  protected void addMapAsAttributes(Map<String, String> map, final Element element) {
    for (Map.Entry<String, String> entry : map.entrySet()) {
      addStringAttr(element, entry.getKey(), entry.getValue());
    }
  }

  protected Element addUrlElement(Url url, Element rootElement) {
    final Element urlElement;
    if (url != null) {
      urlElement = getOpenSearchElement(ELEM_URL);
      rootElement.appendChild(urlElement);
      Map<String, String> map = url.getCustomAttributes();
      addMapAsAttributes(map, urlElement);
      addStringAttr(urlElement, ATTR_TEMPLATE, url.getTemplate());
      addStringAttr(urlElement, ATTR_TYPE, url.getType());
      addCollectionOfStringAsSpaceDelimitedStringAttributed(url.getRels(), ATTR_REL, urlElement);
      addStringAttr(urlElement, ATTR_INDEXOFFSET, url.getIndexOffset());
      addStringAttr(urlElement, ATTR_PAGEOFFSET, url.getPageOffset());
    }
    else {
      urlElement = null;
    }
    return urlElement;
  }

  protected Element addImageElement(Image image, Element rootElement) {
    if (image != null) {
      Element imageElement = addStringElement(rootElement, ELEM_IMAGE, image.getImageUri().toString());
      if (imageElement != null) {
        final int height = image.getHeight();
        if (height > 0) {
          addStringAttr(imageElement, ATTR_HEIGHT, height);
        }
        final int width = image.getWidth();
        if (width > 0) {
          addStringAttr(imageElement, ATTR_WIDTH, width);
        }
        addStringAttr(imageElement, ATTR_TYPE, image.getMimeType());
      }
      return imageElement;
    }
    return null;
  }

  protected Element addQueryElement(Query query, Element rootElement) {
    if (query != null) {
      Element queryElement = getOpenSearchElement(ELEM_QUERY);
      addMapAsAttributes(query.getCustomAttributes(), queryElement);
      addStringAttr(queryElement, ATTR_ROLE, query.getRole().getRoleString());
      addStringAttr(queryElement, ATTR_TITLE, query.getTitle());
      addStringAttr(queryElement, ATTR_SEARCHTERMS, query.getSearchTerms());
      addStringAttr(queryElement, ATTR_TOTALRESULTS, query.getTotalResults(), -1);
      addStringAttr(queryElement, ATTR_COUNT, query.getCount(), -1);
      addStringAttr(queryElement, ATTR_STARTINDEX, query.getStartIndex(), -1);
      addStringAttr(queryElement, ATTR_STARTPAGE, query.getStartPage(), -1);
      addStringAttr(queryElement, ATTR_LANGUAGE, query.getLanguage());
      addStringAttr(queryElement, ATTR_INPUTENCODING, query.getInputEncoding());
      addStringAttr(queryElement, ATTR_OUTPUTENCODING, query.getOutputEncoding());
      rootElement.appendChild(queryElement);
      return queryElement;
    }
    return null;
  }

  protected String getLocaleString(Locale language) {
    if (language == null) {
      return null;
    }
    else {
      StringBuilder localeString = new StringBuilder("");
      if (StringUtils.isNotBlank(language.getLanguage())) {
        localeString.append(language.getLanguage());
        if (StringUtils.isNotBlank(language.getCountry())) {
          localeString.append('-').append(language.getCountry().toLowerCase());
        }
      }
      return localeString.toString();
    }
  }

  protected Element getOpenSearchElement(String name) {
    return new Element(name, XmlConstants.NS_OPENSEARCH);
  }
}
