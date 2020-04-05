package com.zendesk.view.presentation;

import com.zendesk.model.entity.Entity;
import com.zendesk.model.response.OrgResponseItem;
import com.zendesk.model.response.Response;
import com.zendesk.model.response.ResponseItem;
import com.zendesk.model.response.TicketResponseItem;
import com.zendesk.model.response.UserResponseItem;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class that draws a {@link Response} into the console in terms of tables
 */
@Component
public class ResponseDrawer implements Drawable<Response> {

  private final
  OrgResponseItemDrawer orgResponseItemDrawer;

  private final
  TicketResponseItemDrawer ticketResponseItemDrawer;

  private final
  UserResponseItemDrawer userResponseItemDrawer;

  @Autowired
  public ResponseDrawer(OrgResponseItemDrawer orgResponseItemDrawer,
      TicketResponseItemDrawer ticketResponseItemDrawer,
      UserResponseItemDrawer userResponseItemDrawer) {
    this.orgResponseItemDrawer = orgResponseItemDrawer;
    this.ticketResponseItemDrawer = ticketResponseItemDrawer;
    this.userResponseItemDrawer = userResponseItemDrawer;
  }

  /**
   * Draws an {@link Response} into the console in terms of a table
   *
   * @param toDraw {@link Response} to draw
   * @param entity being searched
   * @return a string representing the {@link Response}
   */
  @Override
  public String draw(Response toDraw, String entity) {
    List<? extends ResponseItem> responseItems = toDraw.getResponseItems();

    StringBuilder result = new StringBuilder(
        String.format("%d %ss were found for your search query", responseItems.size(), entity))
        .append(System.lineSeparator());

    for (int i = 0; i < responseItems.size(); i++) {
      result.append(
          getProperDrawer(responseItems.get(i)).draw(responseItems.get(i), String.valueOf(i + 1)))
          .append(System.lineSeparator());
      result.append(
          "*********************************************************************************************************************")
          .append(System.lineSeparator());
    }

    return result.toString().trim();
  }

  /**
   * Gets the right drawer for a {@link ResponseItem}
   *
   * @param responseItem {@link ResponseItem} to get the drawer for
   * @return a {@link Drawable} instance
   */
  private Drawable getProperDrawer(ResponseItem responseItem) {
    if (responseItem instanceof TicketResponseItem) {
      return ticketResponseItemDrawer;
    } else if (responseItem instanceof OrgResponseItem) {
      return orgResponseItemDrawer;
    } else {
      assert responseItem instanceof UserResponseItem;
      return userResponseItemDrawer;
    }
  }
}
