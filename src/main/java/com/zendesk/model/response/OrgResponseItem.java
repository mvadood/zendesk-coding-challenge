package com.zendesk.model.response;

import com.zendesk.model.entity.Organization;
import com.zendesk.model.entity.Ticket;
import com.zendesk.model.entity.User;
import java.util.List;
import lombok.Data;

/**
 * Searching for an organization results in a list containing of elements of this class
 */
@Data
public class OrgResponseItem extends ResponseItem {

  Organization org;
  List<User> orgUsers;
  List<Ticket> orgTickets;
}
