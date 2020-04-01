package com.zendesk.model.response;

import com.zendesk.model.Organization;
import com.zendesk.model.Ticket;
import com.zendesk.model.User;
import java.util.List;
import lombok.Data;

@Data
public class UserResponseItem{
  User user;
  Organization organization;
  List<Ticket> submittedTickets;
  List<Ticket> assigneeTickets;
}
