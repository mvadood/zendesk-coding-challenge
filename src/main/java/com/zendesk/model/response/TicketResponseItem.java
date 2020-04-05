package com.zendesk.model.response;

import com.zendesk.model.entity.Organization;
import com.zendesk.model.entity.Ticket;
import com.zendesk.model.entity.User;
import lombok.Data;

/**
 * Searching for a ticket results in a list containing of elements of this class
 */
@Data
public class TicketResponseItem extends ResponseItem {

  Ticket ticket;
  User assignee;
  User submitter;
  Organization org;
}
