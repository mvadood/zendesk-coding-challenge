package com.zendesk.model.response;

import com.zendesk.model.entity.Organization;
import com.zendesk.model.entity.Ticket;
import com.zendesk.model.entity.User;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Data;

@Data
public class UserResponseItem extends ResponseItem {

  User user;
  Organization organization;
  List<Ticket> submittedTickets;
  List<Ticket> assigneeTickets;

  @Override
  public String draw(String itemNum) {
    StringBuilder result = new StringBuilder(user.draw(
        String.format("User Number %s -> ID = %s", itemNum, user.getId())) + System
        .lineSeparator());

    if (organization == null) {
      result.append("The organization entry was not found in the index.")
          .append(System.lineSeparator());
    }

    if (submittedTickets == null || submittedTickets.isEmpty()) {
      result.append(String.format("No tickets were submitted by user %s", user.getId()))
          .append(System
              .lineSeparator());
    } else {
      result.append(String
          .format("%d tickets are submitter by user %s", submittedTickets.size(), user.getId()))
          .append(System.lineSeparator());
    }

    if (assigneeTickets == null || assigneeTickets.isEmpty()) {
      result.append(String.format("No tickets were assigned to user %s", user.getId()))
          .append(System
              .lineSeparator());
    } else {
      result.append(String
          .format("%d tickets are assigned to user %s", assigneeTickets.size(), user.getId()))
          .append(System.lineSeparator());
    }

    if (submittedTickets != null && !submittedTickets.isEmpty()) {
      int counter = 0;
      for (Ticket ticket : submittedTickets) {
        result.append(ticket.draw(String
            .format("Ticket number %s with ID=%s submitted by user %s",
                ++counter, ticket.getId(),
                user.getId()))).append(System.lineSeparator());
      }
    }

    if (assigneeTickets != null && !assigneeTickets.isEmpty()) {
      int counter = 0;
      for (Ticket ticket : assigneeTickets) {
        result.append(ticket.draw(String
            .format("Ticket number %s with ID=%s assigned to user %s",
                ++counter, ticket.getId(),
                user.getId()))).append(System.lineSeparator());
      }
    }

    return result.toString().trim();
  }
}
