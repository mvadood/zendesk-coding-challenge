package com.zendesk.view.presentation;

import com.zendesk.model.entity.Entity;
import com.zendesk.model.entity.Organization;
import com.zendesk.model.entity.Ticket;
import com.zendesk.model.entity.User;
import com.zendesk.model.response.OrgResponseItem;
import com.zendesk.model.response.TicketResponseItem;
import com.zendesk.model.response.UserResponseItem;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class that draws an {@link OrgResponseItem} into the console in terms of tables
 */
@Component
public class OrgResponseItemDrawer implements Drawable<OrgResponseItem> {

  private final
  EntityDrawer entityDrawer;

  @Autowired
  public OrgResponseItemDrawer(EntityDrawer entityDrawer) {
    this.entityDrawer = entityDrawer;
  }

  /**
   * Draws an {@link Entity} into the console in terms of a table
   *
   * @param orgResponseItem {@link OrgResponseItem} to draw
   * @param itemNum index of the response item
   * @return a string representing the {@link UserResponseItem}
   */
  @Override
  public String draw(OrgResponseItem orgResponseItem, String itemNum) {
    Organization org = orgResponseItem.getOrg();
    List<User> orgUsers = orgResponseItem.getOrgUsers();
    List<Ticket> orgTickets = orgResponseItem.getOrgTickets();

    StringBuilder result = new StringBuilder(entityDrawer.draw(org,
        String.format("Organization Number %s -> ID = %s", itemNum, org.getId())) + System
        .lineSeparator());

    if (orgUsers == null || orgUsers.isEmpty()) {
      result.append(String.format("No users were registered for organization %s", org.getId()))
          .append(System
              .lineSeparator());
    } else {
      result.append(String
          .format("%d users are registered for organization %s", orgUsers.size(), org.getId()))
          .append(System.lineSeparator());
    }

    if (orgTickets == null || orgTickets.isEmpty()) {
      result.append(String.format("No tickets were registered for organization %s", org.getId()))
          .append(System
              .lineSeparator());
    } else {
      result.append(String
          .format("%d tickets are registered for organization %s", orgTickets.size(), org.getId()))
          .append(System.lineSeparator());
    }

    if (orgUsers != null && !orgUsers.isEmpty()) {
      int counter = 0;
      for (User user : orgUsers) {
        result.append(entityDrawer.draw(user, String
            .format("User number %s with ID=%s belonging to organization %s",
                ++counter, user.getId(),
                org.getId()))).append(System.lineSeparator());
      }
    }

    if (orgTickets != null && !orgTickets.isEmpty()) {
      int counter = 0;
      for (Ticket ticket : orgTickets) {
        result.append(entityDrawer.draw(ticket, String
            .format("Ticket number %s with ID=%s belonging to organization %s",
                ++counter, ticket.getId(),
                org.getId()))).append(System.lineSeparator());
      }
    }

    return result.toString().trim();
  }
}
