package com.zendesk.view.presentation;

import com.zendesk.model.entity.Entity;
import com.zendesk.model.entity.Organization;
import com.zendesk.model.entity.Ticket;
import com.zendesk.model.entity.User;
import com.zendesk.model.response.TicketResponseItem;
import com.zendesk.model.response.UserResponseItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class that draws an {@link TicketResponseItem} into the console in terms of tables
 */
@Component
public class TicketResponseItemDrawer implements Drawable<TicketResponseItem> {

  private final
  EntityDrawer entityDrawer;

  @Autowired
  public TicketResponseItemDrawer(EntityDrawer entityDrawer) {
    this.entityDrawer = entityDrawer;
  }

  /**
   * Draws an {@link Entity} into the console in terms of a table
   *
   * @param ticketResponseItem {@link TicketResponseItem} to draw
   * @param itemNum index of the response item
   * @return a string representing the {@link UserResponseItem}
   */
  @Override
  public String draw(TicketResponseItem ticketResponseItem, String itemNum) {
    Ticket ticket = ticketResponseItem.getTicket();
    Organization org = ticketResponseItem.getOrg();
    User assignee = ticketResponseItem.getAssignee();
    User submitter = ticketResponseItem.getSubmitter();

    String result = entityDrawer.draw(ticket,
        String
            .format("Ticket Number %s -> ID = %s", itemNum, ticket.getId()))
        + System
        .lineSeparator();

    if (assignee == null) {
      result += "The assignee user entry was not found in the index." + System.lineSeparator();
    }

    if (submitter == null) {
      result += "The submitter user entry was not found in the index." + System.lineSeparator();
    }

    if (org == null) {
      result += "The organization entry was not found in the index." + System.lineSeparator();
    }

    if (org != null) {
      result += entityDrawer.draw(org, String
          .format("Ticket %s was registered in organization %s", ticket.getId(), org.getId()))
          + System.lineSeparator();
    }

    if (assignee != null) {
      result += entityDrawer.draw(assignee, String
          .format("Assignee user with ID=%s for ticket %s", assignee.getId(), ticket.getId()))
          + System.lineSeparator();
    }

    if (submitter != null) {
      result += entityDrawer.draw(submitter, String
          .format("Submitter user with ID=%s for ticket %s", submitter.getId(), ticket.getId()))
          + System.lineSeparator();
    }

    return result.trim();
  }
}
