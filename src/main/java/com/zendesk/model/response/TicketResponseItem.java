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
}
