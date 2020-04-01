package com.zendesk.model.response;

import com.zendesk.model.Organization;
import com.zendesk.model.Ticket;
import com.zendesk.model.User;
import java.util.List;
import lombok.Data;

@Data
public class OrgResponseItem {

  Organization org;
  List<User> orgUsers;
  List<Ticket> tickets;
}
