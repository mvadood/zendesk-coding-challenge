package com.zendesk.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * Class providing a singleton access to a Gson object
 */
@Component
public class GsonProvider {

  @Getter
  private Gson gson;

  /**
   * Initializes a proper {@link Gson} object that can be used for the entity classes
   */
  public GsonProvider() {
    gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .registerTypeAdapter(ZonedDateTime.class,
            (JsonDeserializer<ZonedDateTime>) (json, type, jsonDeserializationContext) ->
                ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString(),
                    DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)))
        .registerTypeAdapter(ZonedDateTime.class,
            (JsonSerializer<ZonedDateTime>) (src, type, jsonSerializationContext) -> new JsonPrimitive(
                src.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)))
        )
        .setPrettyPrinting()
        .create();
  }
}
