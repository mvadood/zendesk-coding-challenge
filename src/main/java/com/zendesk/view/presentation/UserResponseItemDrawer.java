package com.zendesk.view.presentation;

import com.zendesk.model.entity.Organization;
import com.zendesk.model.entity.Ticket;
import com.zendesk.model.entity.User;
import com.zendesk.model.response.UserResponseItem;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserResponseItemDrawer implements Drawable<UserResponseItem> {

  private final
  EntityDrawer entityDrawer;

  @Autowired
  public UserResponseItemDrawer(EntityDrawer entityDrawer) {
    this.entityDrawer = entityDrawer;
  }

  @Override
  public String draw(UserResponseItem userResponseItem, String itemNum) {
    User user = userResponseItem.getUser();
    Organization organization = userResponseItem.getOrganization();
    List<Ticket> assigneeTickets = userResponseItem.getAssigneeTickets();
    List<Ticket> submittedTickets = userResponseItem.getSubmittedTickets();

    StringBuilder result = new StringBuilder(entityDrawer.draw(user,
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
        result.append(entityDrawer.draw(ticket, String
            .format("Ticket number %s with ID=%s submitted by user %s",
                ++counter, ticket.getId(),
                user.getId()))).append(System.lineSeparator());
      }
    }

    if (assigneeTickets != null && !assigneeTickets.isEmpty()) {
      int counter = 0;
      for (Ticket ticket : assigneeTickets) {
        result.append(entityDrawer.draw(ticket, String
            .format("Ticket number %s with ID=%s assigned to user %s",
                ++counter, ticket.getId(),
                user.getId()))).append(System.lineSeparator());
      }
    }

    return result.toString().trim();
  }
}
