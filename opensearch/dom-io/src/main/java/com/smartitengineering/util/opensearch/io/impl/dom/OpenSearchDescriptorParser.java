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
import com.smartitengineering.util.opensearch.impl.OpenSearchAPIBuilders;
import com.smartitengineering.util.opensearch.impl.OpenSearchDescriptorBuilder;
import com.smartitengineering.util.opensearch.impl.QueryBuilder;
import com.smartitengineering.util.opensearch.impl.UrlBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 * @author imyousuf
 */
class OpenSearchDescriptorParser implements XmlConstants {

  private InputStream source;
  private OpenSearchDescriptor descriptor;
  private boolean closeOnFinish;

  OpenSearchDescriptorParser(InputStream source) {
    this(source, false);
  }

  OpenSearchDescriptorParser(InputStream source, boolean closeOnFinish) {
    if (source == null) {
      throw new IllegalArgumentException("Source stream can not be null!");
    }
    this.descriptor = null;
    this.source = source;
    this.closeOnFinish = closeOnFinish;
  }

  public OpenSearchDescriptor parse() throws IOException {
    if (descriptor == null) {
      try {
        Builder builder = new Builder(false);
        Document document = builder.build(source);
        Element rootElement = document.getRootElement();
        final String shortName = parseMandatoryStringElement(rootElement, ELEM_SHORTNAME);
        final String description = parseMandatoryStringElement(rootElement, ELEM_DESCRIPTION);
        final Url[] urls = parseUrls(rootElement);
        final OpenSearchDescriptor.SyndicationRightEnum rightEnum = parseSyndicationRightEnum(rootElement);
        final boolean adultContent = parseAdultContent(rootElement);
        final Locale language = parseLanguage(rootElement);
        final String longName = parseOptionalStringElement(rootElement, ELEM_LONGNAME);
        final String attribution = parseOptionalStringElement(rootElement, ELEM_ATTRIBUTION);
        final String contact = parseOptionalStringElement(rootElement, ELEM_CONTACT);
        final String developer = parseOptionalStringElement(rootElement, ELEM_DEVELOPER);
        final String tagString = parseOptionalStringElement(rootElement, ELEM_TAGS);
        final String tags[];
        if (StringUtils.isNotBlank(tagString)) {
          tags = tagString.split(" ");
        }
        else {
          tags = new String[0];
        }
        final String inputEnc = parseOptionalStringElement(rootElement, ELEM_INPUTENCODING);
        final String outputEnc = parseOptionalStringElement(rootElement, ELEM_OUTPUTENCODING);
        final Query[] queries = parseQueries(rootElement);
        final Image[] images = parseImages(rootElement);
        // Build the descriptor object!
        final OpenSearchDescriptorBuilder descBuilder = OpenSearchAPIBuilders.getOpenSearchDescriptorBuilder().shortName(
            shortName).description(description).urls(urls).syndicationRight(rightEnum).language(language).attribution(
            attribution).contact(contact).developer(developer).inputEncoding(inputEnc).outputEncoding(outputEnc).tags(
            tags).longName(longName).queries(queries).images(images);
        if (adultContent) {
          descBuilder.setAdultContent();
        }
        else {
          descBuilder.unsetAdultContent();
        }
        descriptor = descBuilder.build();
      }
      catch (ValidityException ex) {
        throw new IOException(ex);
      }
      catch (ParsingException ex) {
        throw new IOException(ex);
      }
      finally {
        if (closeOnFinish) {
          try {
            source.close();
          }
          catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      }
    }
    return descriptor;
  }

  protected String getNamespaceDeclAttrName(final String namespacePrefix) {
    return new StringBuilder("xmlns:").append(namespacePrefix).toString();
  }

  protected String parseMandatoryStringElement(Element rootElement, final String elementName) throws
      IllegalStateException {
    Elements elems = rootElement.getChildElements(elementName, NS_OPENSEARCH);
    if (elems.size() < 1) {
      throw new IllegalStateException("No " + elementName);
    }
    if (elems.size() > 1) {
      throw new IllegalStateException("More than one " + elementName);
    }
    Element elem = elems.get(0);
    return elem.getValue();
  }

  protected String parseOptionalStringElement(Element rootElement, final String elementName) throws
      IllegalStateException {
    Elements elems = rootElement.getChildElements(elementName, NS_OPENSEARCH);
    if (elems.size() > 1) {
      throw new IllegalStateException("More than one " + elementName);
    }
    if (elems.size() > 0) {
      Element elem = elems.get(0);
      return elem.getValue();
    }
    else {
      return null;
    }
  }

  protected OpenSearchDescriptor.SyndicationRightEnum parseSyndicationRightEnum(Element rootElement) throws
      IllegalStateException {
    final String elementName = ELEM_SHORTNAME;
    Elements rightEnumElems = rootElement.getChildElements(elementName, NS_OPENSEARCH);
    if (rightEnumElems.size() > 1) {
      throw new IllegalStateException("More than one " + elementName);
    }
    if (rightEnumElems.size() > 0) {
      Element rightEnumElem = rightEnumElems.get(0);
      return OpenSearchDescriptor.SyndicationRightEnum.getEnumForValue(rightEnumElem.getValue());
    }
    else {
      return OpenSearchDescriptor.SyndicationRightEnum.getDefault();
    }
  }

  protected boolean parseAdultContent(Element rootElement) throws IllegalStateException {
    final String elementName = ELEM_ADULTCONTENT;
    Elements adultContentElems = rootElement.getChildElements(elementName, NS_OPENSEARCH);
    if (adultContentElems.size() > 1) {
      throw new IllegalStateException("More than one " + elementName);
    }
    if (adultContentElems.size() > 0) {
      Element adultContentElem = adultContentElems.get(0);
      return Boolean.valueOf(adultContentElem.getValue());
    }
    else {
      return false;
    }
  }

  protected Locale parseLanguage(Element rootElement) throws IllegalStateException {
    final String elementName = ELEM_LANGUAGE;
    Elements langElems = rootElement.getChildElements(elementName, NS_OPENSEARCH);
    if (langElems.size() > 1) {
      throw new IllegalStateException("More than one " + elementName);
    }
    if (langElems.size() > 0) {
      Element langElem = langElems.get(0);
      final String value = langElem.getValue();
      if (StringUtils.isNotBlank(value)) {
        int langEnd = value.indexOf('-');
        if (langEnd < 0) {
          langEnd = value.length();
        }
        String lang = value.substring(0, langEnd);
        final Locale locale;
        if (langEnd < value.length()) {
          String country = value.substring(langEnd + 1);
          locale = new Locale(lang, country.toUpperCase());
        }
        else {
          locale = new Locale(lang);
        }
        return locale;
      }
      else {
        return Locale.getDefault();
      }
    }
    else {
      return Locale.getDefault();
    }
  }

  protected Url[] parseUrls(Element rootElement) throws IllegalStateException {
    Elements urlElems = rootElement.getChildElements(ELEM_URL, NS_OPENSEARCH);
    if (urlElems.size() < 1) {
      throw new IllegalStateException("No " + ELEM_URL);
    }
    Url[] urls = new Url[urlElems.size()];
    for (int i = 0; i < urls.length; ++i) {
      Element urlElem = urlElems.get(i);
      final Map<String, String> customAttributes = new LinkedHashMap<String, String>();
      final String template = urlElem.getAttributeValue(ATTR_TEMPLATE);
      final String type = urlElem.getAttributeValue(ATTR_TYPE);
      final String relStr = urlElem.getAttributeValue(ATTR_REL);
      final String[] rels;
      if (StringUtils.isNotBlank(relStr)) {
        rels = relStr.split(" ");
      }
      else {
        rels = new String[0];
      }
      final int indexOffset = NumberUtils.toInt(urlElem.getAttributeValue(ATTR_INDEXOFFSET), -1);
      final int pageOffset = NumberUtils.toInt(urlElem.getAttributeValue(ATTR_PAGEOFFSET), -1);
      processElementForCustomAttributes(customAttributes, urlElem);
      int attrs = urlElem.getAttributeCount();
      for (int j = 0; j < attrs; ++j) {
        Attribute attribute = urlElem.getAttribute(j);
        processAttributeForCustomAttributes(customAttributes, attribute, ATTR_TEMPLATE, ATTR_TYPE, ATTR_REL,
                                            ATTR_INDEXOFFSET, ATTR_PAGEOFFSET);

      }
      final UrlBuilder urlBuilder = OpenSearchAPIBuilders.getUrlBuilder();
      for (Map.Entry<String, String> entry : customAttributes.entrySet()) {
        urlBuilder.customAttribute(entry.getKey(), entry.getValue());
      }
      for (String rel : rels) {
        urlBuilder.rel(rel);
      }
      urls[i] = urlBuilder.indexOffset(indexOffset).pageOffset(pageOffset).template(template).type(type).build();
    }
    return urls;
  }

  protected Image[] parseImages(Element rootElement) throws IllegalStateException {
    Elements imageElems = rootElement.getChildElements(ELEM_IMAGE, NS_OPENSEARCH);
    Image[] images = new Image[imageElems.size()];
    for (int i = 0; i < images.length; ++i) {
      Element imageElem = imageElems.get(i);
      final String uri = imageElem.getValue();
      final String type = imageElem.getAttributeValue(ATTR_TYPE);
      final int height = NumberUtils.toInt(imageElem.getAttributeValue(ATTR_HEIGHT), -1);
      final int width = NumberUtils.toInt(imageElem.getAttributeValue(ATTR_WIDTH), -1);
      images[i] =
      OpenSearchAPIBuilders.getImageBuilder().imageUri(uri).height(height).width(width).mimeType(type).build();
    }
    return images;
  }

  protected Query[] parseQueries(Element rootElement) throws IllegalStateException {
    Elements queryElems = rootElement.getChildElements(ELEM_QUERY, NS_OPENSEARCH);
    Query[] queries = new Query[queryElems.size()];
    for (int i = 0; i < queries.length; ++i) {
      Element queryElem = queryElems.get(i);
      final Map<String, String> customAttributes = new LinkedHashMap<String, String>();
      final String title = queryElem.getAttributeValue(ATTR_TITLE);
      final String searchTerms = queryElem.getAttributeValue(ATTR_SEARCHTERMS);
      final String roleStr = queryElem.getAttributeValue(ATTR_ROLE);
      final int totalResults = NumberUtils.toInt(queryElem.getAttributeValue(ATTR_TOTALRESULTS), -1);
      final int count = NumberUtils.toInt(queryElem.getAttributeValue(ATTR_COUNT), -1);
      final int startIndex = NumberUtils.toInt(queryElem.getAttributeValue(ATTR_STARTINDEX), -1);
      final int startPage = NumberUtils.toInt(queryElem.getAttributeValue(ATTR_STARTPAGE), -1);
      final String language = queryElem.getAttributeValue(ATTR_LANGUAGE);
      final String inputEnc = queryElem.getAttributeValue(ATTR_INPUTENCODING);
      final String outputEnc = queryElem.getAttributeValue(ATTR_OUTPUTENCODING);
      processElementForCustomAttributes(customAttributes, queryElem);
      int attrs = queryElem.getAttributeCount();
      for (int j = 0; j < attrs; ++j) {
        Attribute attribute = queryElem.getAttribute(j);
        processAttributeForCustomAttributes(customAttributes, attribute, ATTR_TEMPLATE, ATTR_TYPE, ATTR_REL,
                                            ATTR_INDEXOFFSET, ATTR_PAGEOFFSET);

      }
      final QueryBuilder queryBuilder = OpenSearchAPIBuilders.getQueryBuilder();
      for (Map.Entry<String, String> entry : customAttributes.entrySet()) {
        queryBuilder.customAttribute(entry.getKey(), entry.getValue());
      }
      queries[i] = queryBuilder.count(count).totalResults(totalResults).inputEncodings(inputEnc).outputEncodings(
          outputEnc).language(language).title(title).searchTerms(searchTerms).startIndex(startIndex).startPage(startPage).
          role(OpenSearchAPIBuilders.getRoleBuilder().extendedRole(roleStr).build()).build();
    }
    return queries;
  }

  protected void processAttributeForCustomAttributes(final Map<String, String> customAttributes,
                                                     final Attribute attribute,
                                                     final String... expectedAttribs) {
    if (!Arrays.asList(expectedAttribs).contains(attribute.getQualifiedName())) {
      final String namespacePrefix = attribute.getNamespacePrefix();
      if (StringUtils.isNotBlank(namespacePrefix)) {
        String nsKey = getNamespaceDeclAttrName(namespacePrefix);
        if (!customAttributes.containsKey(nsKey)) {
          customAttributes.put(nsKey, attribute.getNamespaceURI());
        }
      }
      customAttributes.put(attribute.getQualifiedName(), attribute.getValue());
    }
  }

  protected void processElementForCustomAttributes(final Map<String, String> customAttributes, final Element urlElem) {
    int nsCount = urlElem.getNamespaceDeclarationCount();
    for (int i = 0; i < nsCount; ++i) {
      String prefix = urlElem.getNamespacePrefix(i);
      String nsKey = getNamespaceDeclAttrName(prefix);
      if (!customAttributes.containsKey(nsKey)) {
        customAttributes.put(nsKey, urlElem.getNamespaceURI(prefix));
      }
    }
  }
}
