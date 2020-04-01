package com.zendesk.model.response;

import com.zendesk.model.Organization;
import com.zendesk.model.Ticket;
import com.zendesk.model.User;
import lombok.Data;

@Data
public class TicketResponseItem{
  Ticket ticket;
  User assignee;
  User submitter;
  Organization org;
}
