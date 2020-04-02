package com.zendesk.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class GsonProvider {

  @Getter
  private Gson gson;

  public GsonProvider() {
    gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .registerTypeAdapter(ZonedDateTime.class,
            (JsonDeserializer<ZonedDateTime>) (json, type, jsonDeserializationContext) ->
                ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString(),
                    DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)))
        .create();
  }
}
