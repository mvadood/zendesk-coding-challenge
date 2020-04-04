package com.zendesk.model.request;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TicketField {

  private static Map<String, Type> fieldTypes;

  public static final String ID = "_id";
  public static final String URL = "url";
  public static final String EXTERNAL_ID = "external_id";


  public static final String CREATED_AT = "created_at";
  public static final String TYPE = "type";
  public static final String SUBJECT = "subject";
  public static final String DESCRIPTION = "description";
  public static final String PRIORITY = "priority";
  public static final String STATUS = "status";
  public static final String SUBMITTER_ID = "submitter_id";
  public static final String ASSIGNEE_ID = "assignee_id";
  public static final String ORGANIZATION_ID = "organization_id";

  public static final String TAGS = "tags";
  public static final String HAS_INCIDENTS = "has_incidents";
  public static final String DUE_AT = "due_at";
  public static final String VIA = "via";

  static {
    fieldTypes = new HashMap<>();
    fieldTypes.put(ID, String.class);
    fieldTypes.put(URL, String.class);
    fieldTypes.put(EXTERNAL_ID, String.class);
    fieldTypes.put(CREATED_AT, ZonedDateTime.class);
    fieldTypes.put(TYPE, String.class);
    fieldTypes.put(SUBJECT, String.class);
    fieldTypes.put(DESCRIPTION, String.class);
    fieldTypes.put(PRIORITY, String.class);
    fieldTypes.put(STATUS, String.class);
    fieldTypes.put(SUBMITTER_ID, Long.class);
    fieldTypes.put(ASSIGNEE_ID, Long.class);
    fieldTypes.put(ORGANIZATION_ID, String.class);
    fieldTypes.put(TAGS, String.class);
    fieldTypes.put(HAS_INCIDENTS, Boolean.class);
    fieldTypes.put(DUE_AT, ZonedDateTime.class);
    fieldTypes.put(VIA, String.class);
  }

  public static Type getFieldType(String fieldName) {
    return fieldTypes.get(fieldName);
  }

  public static List<String> getFieldsList() {
    return new LinkedList<>(fieldTypes.keySet());
  }
}
