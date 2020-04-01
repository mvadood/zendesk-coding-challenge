package com.zendesk.processor;

import com.zendesk.exception.NoOrgsFoundException;
import com.zendesk.exception.NoTicketsFoundException;
import com.zendesk.exception.NoUsersFoundException;
import com.zendesk.model.Organization;
import com.zendesk.model.Ticket;
import com.zendesk.model.User;
import com.zendesk.model.response.OrgResponseItem;
import com.zendesk.model.response.Response;
import com.zendesk.model.response.TicketResponseItem;
import com.zendesk.model.response.UserResponseItem;
import com.zendesk.repository.ReverseIndexRepository;
import com.zendesk.util.file.JsonFileReader;
import java.io.IOException;
import java.util.List;

public class Processor {

  ReverseIndexRepository reverseIndexRepository;
  public static final String DEFAULT_USERS_FILE_PATH = "users.json";
  public static final String DEFAULT_ORGS_FILE_PATH = "organizations.json";
  public static final String DEFAULT_TICKETS_FILE_PATH = "tickets.json";

  public Processor() {
    this.reverseIndexRepository = new ReverseIndexRepository(new JsonFileReader());
  }


  public void loadUpRepo()
      throws IOException {
    loadUpRepo(DEFAULT_USERS_FILE_PATH, DEFAULT_ORGS_FILE_PATH, DEFAULT_TICKETS_FILE_PATH);
  }

  public void loadUpRepo(String usersFilePath, String orgsFilePath, String ticketsFilePath)
      throws IOException {
    reverseIndexRepository.loadIndex(usersFilePath, orgsFilePath, ticketsFilePath);
  }

  public Response<TicketResponseItem> lookupTicket(String field, Object value)
      throws NoTicketsFoundException {
    Response<TicketResponseItem> response = new Response<>();

    List<Ticket> tickets = reverseIndexRepository.searchTicket(field, value);
    for (Ticket ticket : tickets) {
      TicketResponseItem ticketResponseItem = new TicketResponseItem();
      ticketResponseItem.setTicket(ticket);
      try {
        ticketResponseItem.setOrg(reverseIndexRepository.searchOrgById(ticket.getOrganizationId()));
      } catch (NoOrgsFoundException e) {
        ticketResponseItem.setOrg(null);
      }
      try {
        ticketResponseItem
            .setSubmitter(reverseIndexRepository.searchUserById(ticket.getSubmitterId()));
      } catch (NoUsersFoundException e) {
        ticketResponseItem.setSubmitter(null);
      }
      try {
        ticketResponseItem
            .setAssignee(reverseIndexRepository.searchUserById(ticket.getAssigneeId()));
      } catch (NoUsersFoundException e) {
        ticketResponseItem.setAssignee(null);
      }
      response.addResponseItem(ticketResponseItem);
    }
    return response;
  }

  public Response<OrgResponseItem> lookupOrg(String field, Object value)
      throws NoOrgsFoundException {
    Response<OrgResponseItem> response = new Response<>();

    List<Organization> orgs = reverseIndexRepository.searchOrg(field, value);

    for (Organization org : orgs) {
      OrgResponseItem orgResponseItem = new OrgResponseItem();
      orgResponseItem.setOrg(org);
      try {
        orgResponseItem.setOrgUsers(reverseIndexRepository.searchUserByOrgId(org.getId()));
      } catch (NoUsersFoundException e) {
        orgResponseItem.setOrgUsers(null);
      }
      try {
        orgResponseItem.setTickets(reverseIndexRepository.searchTicketByOrgId(org.getId()));
      } catch (NoTicketsFoundException e) {
        orgResponseItem.setTickets(null);
      }
      response.addResponseItem(orgResponseItem);
    }

    return response;
  }

  public Response<UserResponseItem> lookupUser(String field, Object value)
      throws NoUsersFoundException {
    Response<UserResponseItem> response = new Response<>();

    List<User> users = reverseIndexRepository.searchUser(field, value);

    for (User user : users) {
      UserResponseItem userResponseItem = new UserResponseItem();
      userResponseItem.setUser(user);
      try {
        userResponseItem
            .setSubmittedTickets(reverseIndexRepository.searchTicketBySubmitterId(user.getId()));
      } catch (NoTicketsFoundException e) {
        userResponseItem.setSubmittedTickets(null);
      }
      try {
        userResponseItem
            .setAssigneeTickets(reverseIndexRepository.searchTicketByAssigneeId(user.getId()));
      } catch (NoTicketsFoundException e) {
        userResponseItem.setAssigneeTickets(null);
      }
      try {
        userResponseItem
            .setOrganization(reverseIndexRepository.searchOrgById(user.getOrganizationId()));
      } catch (NoOrgsFoundException e) {
        userResponseItem.setOrganization(null);
      }

      response.addResponseItem(userResponseItem);
    }
    return response;
  }

}
