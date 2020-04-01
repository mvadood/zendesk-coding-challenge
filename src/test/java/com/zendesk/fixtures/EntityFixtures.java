package com.zendesk.fixtures;

import com.zendesk.model.Organization;
import com.zendesk.model.Ticket;
import com.zendesk.model.User;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

public class EntityFixtures {

  public static User user;
  public static Organization org;
  public static Ticket ticket;

  static {
    user = new User();
    user.setId("1");
    user.setUrl("http://initech.zendesk.com/api/v2/users/1.json");
    user.setCreatedAt(ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")));
    List<String> tags = new LinkedList<>();
    tags.add("Springville");
    tags.add("Sutton");
    user.setTags(tags);
    user.setActive(true);

    org = new Organization();
    org.setId("12");
    org.setName("Enthaze");
    List<String> domainNames = new LinkedList<>();
    domainNames.add("kage.com");
    domainNames.add("ecratic.com");
    org.setDomainNames(domainNames);
    org.setCreatedAt(ZonedDateTime.of(2004, 3, 10, 0, 0, 0, 0, ZoneId.of("Australia/Sydney")));

    ticket = new Ticket();
    ticket.setId("14");
    ticket.setAssigneeId("1");
    ticket.setCreatedAt(ZonedDateTime.of(2018, 1, 11, 12, 0, 0, 0, ZoneId.of("Australia/Darwin")));
    ticket.setHasIncidents(true);
  }

}
