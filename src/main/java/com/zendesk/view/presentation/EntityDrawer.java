package com.zendesk.view.presentation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zendesk.model.entity.Entity;
import com.zendesk.util.GsonProvider;
import de.vandermeer.asciitable.AT_Row;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import java.util.Map.Entry;
import org.springframework.stereotype.Component;

/**
 * Class that draws an {@link Entity} into the console in terms of a table
 */
@Component
public class EntityDrawer implements Drawable<Entity> {

  protected transient GsonProvider gsonProvider = new GsonProvider();

  /**
   * Draws an {@link Entity} into the console in terms of a table
   *
   * @param toDraw entity to draw
   * @param header a string passed by the method caller that can be used in the header of the
   * figure
   * @return a string representing that entity
   */
  @Override
  public String draw(Entity toDraw, String header) {
    AsciiTable at = new AsciiTable();
    JsonObject jsonObject = gsonProvider.getGson().toJsonTree(toDraw).getAsJsonObject();

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
