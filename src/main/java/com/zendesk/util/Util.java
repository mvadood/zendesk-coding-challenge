package com.zendesk.util;

import com.google.common.base.CaseFormat;

public class Util {

  public static String convertFieldKeyString(String fieldName) {
    if (fieldName.equals("_id")) {
      return "id";
    } else {
      return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, fieldName);
    }
  }
}
