package com.zendesk.model.response;

import com.zendesk.model.entity.Organization;
import com.zendesk.model.entity.Ticket;
import com.zendesk.model.entity.User;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Data;

@Data
public class OrgResponseItem extends ResponseItem {

  Organization org;
  List<User> orgUsers;
  List<Ticket> orgTickets;

  @Override
  public String draw(String itemNum) {
    StringBuilder result = new StringBuilder(org.draw(
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
        result.append(user.draw(String
            .format("User number %s with ID=%s belonging to organization %s",
                ++counter, user.getId(),
                org.getId()))).append(System.lineSeparator());
      }
    }

    if (orgTickets != null && !orgTickets.isEmpty()) {
      int counter = 0;
      for (Ticket ticket : orgTickets) {
        result.append(ticket.draw(String
            .format("Ticket number %s with ID=%s belonging to organization %s",
                ++counter, ticket.getId(),
                org.getId()))).append(System.lineSeparator());
      }
    }

    return result.toString().trim();
  }
}
