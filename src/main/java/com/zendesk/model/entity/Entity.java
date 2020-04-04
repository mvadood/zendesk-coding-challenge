package com.zendesk.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.zendesk.util.GsonProvider;
import de.vandermeer.asciitable.AT_Row;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import java.util.Map.Entry;
import lombok.Data;

@Data
public abstract class Entity implements Drawable {

  @JsonIgnore
  protected transient GsonProvider gsonProvider = new GsonProvider();

  @SerializedName("_id")
  protected String id;

  @Override
  public String draw(String header) {
    AsciiTable at = new AsciiTable();
    JsonObject jsonObject = gsonProvider.getGson().toJsonTree(this).getAsJsonObject();

    at.addRule();
    AT_Row row = at
        .addRow(null, header);
    row.setTextAlignment(TextAlignment.CENTER);

    at.addRule();
    for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
      at.addRule();
      at.addRow(entry.getKey(), entry.getValue());
    }
    at.addRule();
    return at.render();
  }
}
