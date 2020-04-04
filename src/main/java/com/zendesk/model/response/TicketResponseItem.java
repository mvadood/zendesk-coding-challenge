package com.zendesk.model.response;

import com.zendesk.model.entity.Organization;
import com.zendesk.model.entity.Ticket;
import com.zendesk.model.entity.User;
import lombok.Data;

@Data
public class TicketResponseItem extends ResponseItem {

  Ticket ticket;
  User assignee;
  User submitter;
  Organization org;

  @Override
  public String draw(String itemNum) {
    String result = ticket.draw(
        String.format("Ticket Number %s -> ID = %s", itemNum, ticket.getId())) + System
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
      result += org.draw(String
          .format("Ticket %s was registered in organization %s", ticket.getId(), org.getId()))+ System.lineSeparator();
    }

    if (assignee != null) {
      result += assignee.draw(String
          .format("Assignee user with ID=%s for ticket %s", assignee.getId(), ticket.getId()))+ System.lineSeparator();
    }

    if (submitter != null) {
      result += submitter.draw(String
          .format("Submitter user with ID=%s for ticket %s", submitter.getId(), ticket.getId()))+ System.lineSeparator();
    }

    return result.trim();
  }

}
