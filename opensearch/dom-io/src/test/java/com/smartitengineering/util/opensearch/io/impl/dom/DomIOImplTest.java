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

import com.smartitengineering.util.opensearch.api.OpenSearchDescriptor;
import com.smartitengineering.util.opensearch.api.Role.LocalOpenSearchRoles;
import com.smartitengineering.util.opensearch.api.Url.RelEnum;
import com.smartitengineering.util.opensearch.impl.OpenSearchAPIBuilders;
import com.smartitengineering.util.opensearch.impl.OpenSearchDescriptorBuilder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Unit test for {@link DomIOImpl}.
 */
public class DomIOImplTest extends TestCase {

  public static final String MIN = "<?xml version=\"1.0\"?>\n<OpenSearchDescription xmlns=\"http://a9.com/-/spec/opensearch/" +
      "1.1/\"><ShortName>testDesc</ShortName><Description>Some test description</Description><Url template=" +
      "\"http://google.com/\" type=\"text/html\" rel=\"results\" indexOffset=\"1\" pageOffset=\"1\" /><Syndication" +
      "Right>open</SyndicationRight><AdultContent>false</AdultContent><Language>en</Language></OpenSearchDescription>\n";
  public static final String MAX = "<?xml version=\"1.0\"?>\n<OpenSearchDescription xmlns=\"http://a9.com/-/spec/opensearch/" +
      "1.1/\"><ShortName>testDesc</ShortName><Description>Some test description</Description><Tags>tag1 tag2</Tags>" +
      "<Contact>contact</Contact><Url template=\"http://google.com/\" type=\"text/html\" rel=\"results\" indexOffset=" +
      "\"1\" pageOffset=\"1\" /><Url template=\"http://yahoo.com/\" type=\"text/html\" rel=\"self collection\" " +
      "indexOffset=\"1\" pageOffset=\"1\" /><LongName>All combo test</LongName><Image>http://google.com/logo.gif" +
      "</Image><Image height=\"100\" width=\"200\" type=\"image/jpg\">http://yahoo.com/logo.gif</Image><Query role=" +
      "\"superset\" searchTerms=\"some terms\" /><Query xmlns:cns=\"http://google.com/1.1\" role=" +
      "\"cns:arole\" title=\"comprehensive query\" searchTerms=\"any term\" totalResults=\"2\" count=\"3\" startIndex" +
      "=\"0\" startPage=\"3\" language=\"de\" inputEncoding=\"ASCII\" outputEncoding=\"UTF-16\" /><Developer>Smart IT " +
      "Engineering Ltd. - Beta Team</Developer><Attribution>Some attribution</Attribution><SyndicationRight>open" +
      "</SyndicationRight><AdultContent>false</AdultContent><Language>en-us</Language><OutputEncoding>ISO-8859-1" +
      "</OutputEncoding><InputEncoding>UTF-8</InputEncoding></OpenSearchDescription>\n";
  public static final OpenSearchDescriptor MIN_DESC = OpenSearchAPIBuilders.getOpenSearchDescriptorBuilder().shortName(
      "testDesc").description("Some test description").urls(OpenSearchAPIBuilders.getUrlBuilder().template(
      "http://google.com/").type("text/html").build()).language(Locale.ENGLISH).build();
  public static final OpenSearchDescriptorBuilder MAX_BUILDER = OpenSearchAPIBuilders.getOpenSearchDescriptorBuilder().
      shortName("testDesc").description("Some test description").urls(OpenSearchAPIBuilders.getUrlBuilder().template(
      "http://google.com/").type("text/html").build(), OpenSearchAPIBuilders.getUrlBuilder().template(
      "http://yahoo.com/").type("text/html").rel(RelEnum.SELF).rel(RelEnum.COLLECTION).build()).tags("tag1", "tag2").
      contact("contact").developer("Smart IT Engineering Ltd. - Beta Team").longName("All combo test").attribution(
      "Some attribution").language(Locale.US).inputEncoding("UTF-8").outputEncoding("ISO-8859-1").images(OpenSearchAPIBuilders.
      getImageBuilder().imageUri("http://google.com/logo.gif").build(), OpenSearchAPIBuilders.getImageBuilder().imageUri(
      "http://yahoo.com/logo.gif").height(100).width(200).mimeType("image/jpg").build()).queries(OpenSearchAPIBuilders.
      getQueryBuilder().searchTerms("some terms").totalResults(-2).role(OpenSearchAPIBuilders.getRoleBuilder().localRole(
      LocalOpenSearchRoles.SUPERSET).build()).build(), OpenSearchAPIBuilders.getQueryBuilder().searchTerms("any term").
      count(3).startIndex(0).startPage(3).totalResults(2).customAttribute("xmlns:cns", "http://google.com/1.1").role(OpenSearchAPIBuilders.
      getRoleBuilder().extendedRole("cns:arole").build()).inputEncodings("ASCII").outputEncodings("UTF-16").language(Locale.GERMAN.
      getLanguage()).title("comprehensive query").build());
  public static final OpenSearchDescriptor MAX_DESC = MAX_BUILDER.build();

  public void testMinimalWrite() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    OpenSearchDescriptorWriter writer = new OpenSearchDescriptorWriter(outputStream, MIN_DESC, true);
    try {
      final long start = System.currentTimeMillis();
      writer.write();
      final long end = System.currentTimeMillis();
      System.out.println("Time taken to write: " + (end - start));
    }
    catch (IOException ex) {
      ex.printStackTrace();
      Assert.fail(ex.getMessage());
    }
    final String toString = outputStream.toString();
    System.out.println(toString);
    assertEquals(MIN, toString);
  }

  public void testWrite() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    OpenSearchDescriptorWriter writer = new OpenSearchDescriptorWriter(outputStream, MAX_DESC, true);
    try {
      final long start = System.currentTimeMillis();
      writer.write();
      final long end = System.currentTimeMillis();
      System.out.println("Time taken to write: " + (end - start));
    }
    catch (IOException ex) {
      ex.printStackTrace();
      Assert.fail(ex.getMessage());
    }
    final String toString = outputStream.toString();
    System.out.println(toString);
    assertEquals(MAX, toString);

    outputStream = new ByteArrayOutputStream();
    MAX_BUILDER.queries(OpenSearchAPIBuilders.getQueryBuilder().searchTerms("some terms").
        customAttribute("xmlns:cns", "http://google.com/1.1").customAttribute("cns:add", "value").build());
    writer = new OpenSearchDescriptorWriter(outputStream, MAX_BUILDER.build(), true);
    try {
      final long start = System.currentTimeMillis();
      writer.write();
      final long end = System.currentTimeMillis();
      System.out.println("Time taken to write: " + (end - start));
    }
    catch (IOException ex) {
      ex.printStackTrace();
      Assert.fail(ex.getMessage());
    }
    final String extendedString = outputStream.toString();
    System.out.println(extendedString);
    StringBuilder newStringBuilder = new StringBuilder(MAX);
    int insertionPoint = newStringBuilder.indexOf("<Developer>");
    String additionalString = "<Query xmlns:cns=\"http://google.com/1.1\" cns:add=\"value\" role=\"example\" " +
        "searchTerms=\"some terms\" />";
    newStringBuilder.insert(insertionPoint, additionalString);
    assertEquals(newStringBuilder.toString(), extendedString);
  }
}
