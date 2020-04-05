package com.zendesk.model.response;

import com.zendesk.model.entity.Organization;
import com.zendesk.model.entity.Ticket;
import com.zendesk.model.entity.User;
import java.util.List;
import lombok.Data;

/**
 * Searching for user results in a list containing of elements of this class
 */
@Data
public class UserResponseItem extends ResponseItem {

  User user;
  Organization organization;
  List<Ticket> submittedTickets;
  List<Ticket> assigneeTickets;
}
