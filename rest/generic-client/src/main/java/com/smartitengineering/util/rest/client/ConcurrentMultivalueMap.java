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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.ws.rs.core.MultivaluedMap;

/**
 *
 * @author imyousuf
 */
public class ConcurrentMultivalueMap<K, V>
    implements MultivaluedMap<K, V> {

  private final ConcurrentMap<K, List<V>> backupMap;

  public ConcurrentMultivalueMap() {
    this(10);
  }

  public ConcurrentMultivalueMap(int initialCapacity) {
    this(new ConcurrentHashMap<K, List<V>>(initialCapacity));
  }

  public ConcurrentMultivalueMap(ConcurrentMap<K, List<V>> backupMap) {
    if (backupMap == null) {
      throw new IllegalArgumentException("Backup map can not be null!");
    }
    this.backupMap = backupMap;
  }

  @Override
  public void putSingle(K key,
                        V value) {
    final List<V> valueList;
    valueList = Collections.singletonList(value);
    backupMap.put(key, valueList);
    valueList.add(value);
  }

  @Override
  public void add(K key,
                  V value) {
    List<V> valueList;
    if (backupMap.containsKey(key)) {
      valueList = backupMap.get(key);
      try {
        valueList.add(value);
      }
      catch (Exception ex) {
        valueList = Collections.synchronizedList(new ArrayList<V>(valueList));
        backupMap.put(key, valueList);
        valueList.add(value);
      }
    }
    else {
      valueList = Collections.synchronizedList(new ArrayList<V>());
      valueList.add(value);
      backupMap.put(key, valueList);
    }
  }

  @Override
  public V getFirst(K key) {
    final List<V> vals = backupMap.get(key);
    if (backupMap.containsKey(key) && !vals.isEmpty()) {
      return vals.get(0);
    }
    else {
      return null;
    }
  }

  @Override
  public int size() {
    return backupMap.size();
  }

  @Override
  public boolean isEmpty() {
    return backupMap.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return backupMap.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return backupMap.containsKey(value);
  }

  @Override
  public List<V> get(Object key) {
    return backupMap.get(key);
  }

  @Override
  public List<V> put(K key,
                     List<V> value) {
    return backupMap.put(key, value);
  }

  @Override
  public List<V> remove(Object key) {
    return backupMap.remove(key);
  }

  @Override
  public void putAll(Map<? extends K, ? extends List<V>> m) {
    backupMap.putAll(m);
  }

  @Override
  public void clear() {
    backupMap.clear();
  }

  @Override
  public Set<K> keySet() {
    return backupMap.keySet();
  }

  @Override
  public Collection<List<V>> values() {
    return backupMap.values();
  }

  @Override
  public Set<Entry<K, List<V>>> entrySet() {
    return backupMap.entrySet();
  }
}
