package com.zendesk.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class GsonProvider {

  private static Gson instance;

  private GsonProvider() {
  }

  public static Gson getInstance() {
    synchronized (GsonProvider.class) {
      if (instance == null) {
        instance = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(ZonedDateTime.class,
                (JsonDeserializer<ZonedDateTime>) (json, type, jsonDeserializationContext) ->
                    ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString(),
                        DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)))
            .create();
      }
      return instance;
    }
  }
}
