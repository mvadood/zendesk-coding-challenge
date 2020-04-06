package com.zendesk.model.request;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class listing all the fields supported for a user
 */
public class UserField {

  private static Map<String, Type> fieldTypes;

  public static final String ID = "_id";
  public static final String URL = "url";
  public static final String EXTERNAL_ID = "external_id";
  public static final String NAME = "name";
  public static final String ALIAS = "alias";
  public static final String CREATED_AT = "created_at";
  public static final String ACTIVE = "active";
  public static final String VERIFIED = "verified";
  public static final String SHARED = "shared";
  public static final String LOCALE = "locale";
  public static final String TIMEZONE = "timezone";
  public static final String LAST_LOGIN_AT = "last_login_at";
  public static final String EMAIL = "email";
  public static final String PHONE = "phone";
  public static final String SIGNATURE = "signature";
  public static final String ORGANIZATION_ID = "organization_id";
  public static final String TAGS = "tags";
  public static final String SUSPENDED = "suspended";
  public static final String ROLE = "role";

  static {
    fieldTypes = new HashMap<>();
    fieldTypes.put(ID, String.class);
    fieldTypes.put(URL, String.class);
    fieldTypes.put(EXTERNAL_ID, String.class);
    fieldTypes.put(NAME, String.class);
    fieldTypes.put(ALIAS, String.class);
    fieldTypes.put(CREATED_AT, ZonedDateTime.class);
    fieldTypes.put(ACTIVE, Boolean.class);
    fieldTypes.put(VERIFIED, Boolean.class);
    fieldTypes.put(SHARED, Boolean.class);
    fieldTypes.put(LOCALE, String.class);
    fieldTypes.put(TIMEZONE, String.class);
    fieldTypes.put(LAST_LOGIN_AT, ZonedDateTime.class);
    fieldTypes.put(EMAIL, String.class);
    fieldTypes.put(PHONE, String.class);
    fieldTypes.put(SIGNATURE, String.class);
    fieldTypes.put(ORGANIZATION_ID, String.class);
    fieldTypes.put(TAGS, String.class);
    fieldTypes.put(SUSPENDED, Boolean.class);
    fieldTypes.put(ROLE, String.class);
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
   * Gets the list of fields supported by the user entity
   */
  public static List<String> getFieldsList() {
    return new LinkedList<>(fieldTypes.keySet());
  }
}
