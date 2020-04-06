package com.zendesk.processor;

import com.zendesk.exception.NoOrgsFoundException;
import com.zendesk.exception.NoTicketsFoundException;
import com.zendesk.exception.NoUsersFoundException;
import com.zendesk.model.entity.Organization;
import com.zendesk.model.entity.Ticket;
import com.zendesk.model.entity.User;
import com.zendesk.model.response.OrgResponseItem;
import com.zendesk.model.response.Response;
import com.zendesk.model.response.TicketResponseItem;
import com.zendesk.model.response.UserResponseItem;
import com.zendesk.repository.ReverseIndexRepository;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Processor is responsible for taking the user input and building up the inverted index {@link
 * com.zendesk.repository.ReverseIndexRepository}. It also takes care of the search
 * </p>
 */
@Slf4j
@Component
public class Processor {

  private final ReverseIndexRepository reverseIndexRepository;

  @Autowired
  public Processor(ReverseIndexRepository reverseIndexRepository) {
    this.reverseIndexRepository = reverseIndexRepository;
  }

  /**
   * Clears the repository from any data
   */
  public void clear() {
    reverseIndexRepository.clear();
  }

  /**
   * Loads up the index from input json files
   *
   * @param usersInputStream input stream pointing to the users file
   * @param orgsInputStream input stream pointing to the organizations file
   * @param ticketsInputStream input stream pointing to the tickets file
   */
  public void loadUpRepo(InputStream usersInputStream, InputStream orgsInputStream,
      InputStream ticketsInputStream)
      throws IOException {
    reverseIndexRepository.loadIndex(usersInputStream, orgsInputStream, ticketsInputStream);
  }

  /**
   * Searches among the tickets based on a given key and value and returns its corresponding users
   * an organizations as well
   *
   * @param field name of the field searching for
   * @param value value of the field to search for
   * @return tickets matching the search plus their corresponding users and organizations in the
   * format of a {@link Response}
   * @throws com.zendesk.exception.NoTicketsFoundException in case there were not tickets found
   */
  public Response<TicketResponseItem> lookupTicket(String field, Object value)
      throws NoTicketsFoundException {
    log.trace("Ticket search query was received with (%s,%s)", field, value);
    Response<TicketResponseItem> response = new Response<>();

    List<Ticket> tickets = reverseIndexRepository.searchTicket(field, value);
    for (Ticket ticket : tickets) {
      TicketResponseItem ticketResponseItem = new TicketResponseItem();
      ticketResponseItem.setTicket(ticket);
      try {
        ticketResponseItem.setOrg(reverseIndexRepository.searchOrgById(ticket.getOrganizationId()));
      } catch (NoOrgsFoundException e) {
        log.trace("No organizations with id={} were found for ticket {}",
            ticket.getOrganizationId(), ticket.getId(), e);
        ticketResponseItem.setOrg(null);
      }
      try {
        ticketResponseItem
            .setSubmitter(reverseIndexRepository.searchUserById(ticket.getSubmitterId()));
      } catch (NoUsersFoundException e) {
        log.trace("No submitter users with id=%s were found for ticket {}",
            ticket.getSubmitterId(), ticket.getId(), e);
        ticketResponseItem.setSubmitter(null);
      }
      try {
        ticketResponseItem
            .setAssignee(reverseIndexRepository.searchUserById(ticket.getAssigneeId()));
      } catch (NoUsersFoundException e) {
        log.trace(
            "No assignee users with id={} were found for ticket {}", ticket.getAssigneeId(),
            ticket.getId(), e);
        ticketResponseItem.setAssignee(null);
      }
      response.addResponseItem(ticketResponseItem);
    }
    return response;
  }

  /**
   * Searches among the organizations based on a given key and value
   *
   * @param field name of the field searching for
   * @param value value of the field to search for
   * @return organizations matching the search plus their corresponding users and tickets in the
   * format of a {@link Response}
   * @throws com.zendesk.exception.NoOrgsFoundException in case there were not tickets found
   */
  public Response<OrgResponseItem> lookupOrg(String field, Object value)
      throws NoOrgsFoundException {
    log.trace("Organization search query was received with ({},{})", field, value);
    Response<OrgResponseItem> response = new Response<>();

    List<Organization> orgs = reverseIndexRepository.searchOrg(field, value);

    for (Organization org : orgs) {
      OrgResponseItem orgResponseItem = new OrgResponseItem();
      orgResponseItem.setOrg(org);
      try {
        orgResponseItem.setOrgUsers(reverseIndexRepository.searchUserByOrgId(org.getId()));
      } catch (NoUsersFoundException e) {
        log.trace("No users were found for organization {}", org.getId(), e);
        orgResponseItem.setOrgUsers(null);
      }
      try {
        orgResponseItem.setOrgTickets(reverseIndexRepository.searchTicketByOrgId(org.getId()));
      } catch (NoTicketsFoundException e) {
        log.trace("No tickets were found for organization {}", org.getId(), e);
        orgResponseItem.setOrgTickets(null);
      }
      response.addResponseItem(orgResponseItem);
    }

    return response;
  }

  /**
   * Searches among the users based on a given key and value
   *
   * @param field name of the field searching for
   * @param value value of the field to search for
   * @return users matching the search plus their corresponding organizations and tickets in the
   * format of a {@link Response}
   * @throws com.zendesk.exception.NoUsersFoundException in case there were not tickets found
   */
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
        log.trace("No submitted tickets were found for user {}", user.getId(), e);
        userResponseItem.setSubmittedTickets(null);
      }
      try {
        userResponseItem
            .setAssigneeTickets(reverseIndexRepository.searchTicketByAssigneeId(user.getId()));
      } catch (NoTicketsFoundException e) {
        log.trace("No assigned tickets were found for user {}", user.getId(), e);
        userResponseItem.setAssigneeTickets(null);
      }
      try {
        userResponseItem
            .setOrganization(reverseIndexRepository.searchOrgById(user.getOrganizationId()));
      } catch (NoOrgsFoundException e) {
        log.trace("No organizations were found for user {}", user.getId(), e);
        userResponseItem.setOrganization(null);
      }

      response.addResponseItem(userResponseItem);
    }
    return response;
  }

}
