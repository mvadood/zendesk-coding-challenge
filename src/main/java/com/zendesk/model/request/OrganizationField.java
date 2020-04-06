package com.zendesk.model.request;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class listing all the fields supported for an organization
 */
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
    fieldTypes.put(CREATED_AT, ZonedDateTime.class);
    fieldTypes.put(DETAILS, String.class);
    fieldTypes.put(SHARED_TICKETS, Boolean.class);
    fieldTypes.put(TAGS, String.class);
  }

  /**
   * Gets the required data type for a field
   *
   * @param fieldName name of the field to enquire about its data type
   * @return java type for that field
   */
  public static Type getFieldType(String fieldName) {
    return fieldTypes.get(fieldName);
  }

  /**
   * Gets the list of fields supported by the organization entity
   */
  public static List<String> getFieldsList() {
    return new LinkedList<>(fieldTypes.keySet());
  }
}
