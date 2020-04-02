package com.zendesk.model.request;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OrganizationField {

  private static Map<String, Type> fieldTypes;

  public static final String ID = "_id";
  public static final String URL = "url";
  public static final String EXTERNAL_ID = "external_id";
  public static final String NAME = "name";
  public static final String DOMAIN_NAMES = "domain_names";
  public static final String CREATED_AT = "created_at";
  public static final String DETAILS = "details";
  public static final String SHARED_TICKETS = "shared_tickets";
  public static final String TAGS = "tags";

  static {
    fieldTypes = new HashMap<>();
    fieldTypes.put(ID, String.class);
    fieldTypes.put(URL, String.class);
    fieldTypes.put(EXTERNAL_ID, String.class);
    fieldTypes.put(NAME, String.class);
    fieldTypes.put(DOMAIN_NAMES, String.class);
    fieldTypes.put(CREATED_AT, String.class);
    fieldTypes.put(DETAILS, String.class);
    fieldTypes.put(SHARED_TICKETS, Boolean.class);
    fieldTypes.put(TAGS, String.class);
  }

  public static Type getFieldType(String fieldName) {
    return fieldTypes.get(fieldName);
  }

  public static List<String> getFieldsList() {
    return new LinkedList<>(fieldTypes.keySet());
  }
}
