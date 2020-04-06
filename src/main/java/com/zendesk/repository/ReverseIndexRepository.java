package com.zendesk.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.CaseFormat;
import com.zendesk.exception.NoOrgsFoundException;
import com.zendesk.exception.NoTicketsFoundException;
import com.zendesk.exception.NoUsersFoundException;
import com.zendesk.model.entity.Entity;
import com.zendesk.model.entity.Organization;
import com.zendesk.model.entity.Ticket;
import com.zendesk.model.entity.User;
import com.zendesk.model.request.TicketField;
import com.zendesk.model.request.UserField;
import com.zendesk.util.Util;
import com.zendesk.util.file.JsonFileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * ReverseIndexRepository is a simple implementation of a reverse/inverted index.
 *
 * @see <a href="https://www.geeksforgeeks.org/inverted-index</a>
 * </p>
 */
@Slf4j
@Repository
public class ReverseIndexRepository {

  private final String ID_FIELD = "id";

  private Map<String, User> users;
  private Map<String, Ticket> tickets;
  private Map<String, Organization> organizations;

  private Map<String, Map<Object, List<String>>> usersIndex;
  private Map<String, Map<Object, List<String>>> ticketsIndex;
  private Map<String, Map<Object, List<String>>> orgsIndex;


  private final JsonFileReader<User> usersfileLoader;
  private final JsonFileReader<Organization> orgsfileLoader;
  private final JsonFileReader<Ticket> ticketsfileLoader;

  private ObjectMapper objectMapper;

  @Autowired
  public ReverseIndexRepository(
      JsonFileReader<User> usersfileLoader,
      JsonFileReader<Organization> orgsfileLoader,
      JsonFileReader<Ticket> ticketsfileLoader) {
    this.usersfileLoader = usersfileLoader;
    this.orgsfileLoader = orgsfileLoader;
    this.ticketsfileLoader = ticketsfileLoader;

    clear();
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
  }

  /**
   * Clears the repository from any data
   */
  public void clear() {
    users = new HashMap<>();
    tickets = new HashMap<>();
    organizations = new HashMap<>();

    usersIndex = new HashMap<>();
    orgsIndex = new HashMap<>();
    ticketsIndex = new HashMap<>();
    log.info("Inverted index repository was cleared");
  }

  /**
   * Loads up the index from input json files
   *
   * @param usersInputStream input stream pointing to the users file
   * @param orgsInputStream input stream pointing to the organizations file
   * @param ticketsInputStream input stream pointing to the tickets file
   * @throws IOException in case there was an issue loading the files
   */
  public void loadIndex(InputStream usersInputStream, InputStream orgsInputStream,
      InputStream ticketsInputStream) throws IOException {
    Iterator<User> usersIterator = usersfileLoader.getIterator(usersInputStream, User.class);
    Iterator<Organization> orgsIterator = orgsfileLoader
        .getIterator(orgsInputStream, Organization.class);
    Iterator<Ticket> ticketsIterator = ticketsfileLoader
        .getIterator(ticketsInputStream, Ticket.class);

    populateMaps(usersIterator, users, usersIndex);
    populateMaps(orgsIterator, organizations, orgsIndex);
    populateMaps(ticketsIterator, tickets, ticketsIndex);
    log.info("Inverted index repository was loaded successfully");
  }

  /**
   * Populates the internal data structures required for building the index
   *
   * @param iterator an iterator to the file being read
   * @param entities map containing data for that specific {@link com.zendesk.model.entity.Entity}
   * whose maps are being populated
   * @param index data structure being built
   */
  private void populateMaps(Iterator iterator, Map entities,
      Map<String, Map<Object, List<String>>> index) {
    while (iterator.hasNext()) {
      Entity entity = (Entity) iterator.next();
      entities.put(entity.getId(), entity);
      Map<String, Object> entityKeyValues = buildEntityKeyValues(entity);
      for (String key : entityKeyValues.keySet()) {
        if (!index.containsKey(key)) {
          index.put(key, new HashMap<>());
        }
        Object value = entityKeyValues.get(key);

        if (value == null) {
          value = "";
        }

        if (value instanceof List) {
          indexList(key, (List) value, entity, index);
        } else {
          indexSingleValue(key, value, entity, index);
        }
      }
    }
  }

  /**
   * indexes a single values item for an entity into the index
   *
   * @param key field name
   * @param value field value
   * @param entity entity to be indexed
   */
  void indexSingleValue(String key, Object value, Entity entity,
      Map<String, Map<Object, List<String>>> index) {
    if (value instanceof String) {
      value = value.toString().trim().toLowerCase();
    }
    if (value instanceof BigDecimal) {
      value = ((BigDecimal) value).longValue();
    }
    index.get(key).computeIfAbsent(value, k -> new LinkedList<>());
    index.get(key).get(value).add(entity.getId());
  }

  void indexList(String key, List value, Entity entity,
      Map<String, Map<Object, List<String>>> index) {
    for (Object item : value) {
      String lowerCaseItem = item.toString().trim().toLowerCase();
      index.get(key).computeIfAbsent(lowerCaseItem, k -> new LinkedList<>());
      index.get(key).get(lowerCaseItem).add(entity.getId());
    }
  }

  /**
   * Creates a map out of an entity
   *
   * @param entity the entity that is going to be converted
   */
  Map<String, Object> buildEntityKeyValues(Entity entity) {
    Map<String, Object> keyValues = objectMapper.convertValue(entity, new TypeReference<>() {
    });

    UserField.getFieldsList().stream().map(Util::convertFieldKeyString).forEach(field ->
        {
          if (!keyValues.containsKey(field)) {
            keyValues.put(field, null);
          }
        }
    );
    return keyValues;
  }

  /**
   * Searches among the tickets based on a given key and value
   *
   * @param field name of the field searching for
   * @param value value of the field to search for
   * @return list of tickets matching the search query
   * @throws com.zendesk.exception.NoTicketsFoundException in case there were not tickets found
   */
  public List<Ticket> searchTicket(String field, Object value) throws NoTicketsFoundException {
    log.trace(String.format("Ticket search was issued for (%s,%s)", field, value));
    List<String> ticketIds = ticketsIndex.get(field).get(value);
    if (ticketIds == null) {
      throw new NoTicketsFoundException();
    } else {
      return ticketIds.stream().map(ticketId -> tickets.get(ticketId)).collect(Collectors.toList());
    }
  }

  /**
   * Searches among the organizations based on a given key and value
   *
   * @param field name of the field searching for
   * @param value value of the field to search for
   * @return list of organizations matching the search query
   * @throws com.zendesk.exception.NoOrgsFoundException case there were not organizations found
   */
  public List<Organization> searchOrg(String field, Object value) throws NoOrgsFoundException {
    log.trace(String.format("Organization search was issued for (%s,%s)", field, value));
    List<String> orgIds = orgsIndex.get(field).get(value);
    if (orgIds == null) {
      throw new NoOrgsFoundException();
    } else {
      return orgIds.stream().map(orgId -> organizations.get(orgId)).collect(Collectors.toList());
    }
  }

  /**
   * Searches among the users based on a given key and value
   *
   * @param field name of the field searching for
   * @param value value of the field to search for
   * @return list of users matching the search query
   * @throws com.zendesk.exception.NoUsersFoundException case there were not users found
   */
  public List<User> searchUser(String field, Object value) throws NoUsersFoundException {
    log.trace(String.format("User search was issued for (%s,%s)", field, value));
    List<String> userIds = usersIndex.get(field).get(value);
    if (userIds == null) {
      throw new NoUsersFoundException();
    } else {
      return userIds.stream().map(userId -> users.get(userId)).collect(Collectors.toList());
    }
  }

  /**
   * searches for an organization based on its id
   *
   * @return {@link Organization} matching the id
   * @throws NoOrgsFoundException in case no organizations were found to have that id
   */
  public Organization searchOrgById(String id) throws NoOrgsFoundException {
    return searchOrg(ID_FIELD, id).get(0);
  }

  /**
   * searches for a user based on its id
   *
   * @return {@link User} matching the id
   * @throws NoUsersFoundException in case no organizations were found to have that id
   */
  public User searchUserById(String id) throws NoUsersFoundException {
    return searchUser(ID_FIELD, id).get(0);
  }

  /**
   * searches for a user based on its organization id
   *
   * @return list of {@link User}s matching the id
   * @throws NoUsersFoundException in case no users were found to have that organization id
   */
  public List<User> searchUserByOrgId(String orgId) throws NoUsersFoundException {
    return searchUser(
        CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, TicketField.ORGANIZATION_ID), orgId);
  }

  /**
   * searches for an ticket based on its organization id
   *
   * @return {@link Ticket} matching having the organization id specified
   * @throws NoTicketsFoundException in case no tickets were found for that organization
   */
  public List<Ticket> searchTicketByOrgId(String orgId)
      throws NoTicketsFoundException {
    return searchTicket(
        CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, TicketField.ORGANIZATION_ID), orgId);
  }

  /**
   * searches for a ticket based on the id of its submitter
   *
   * @return ticket matching submitter with id = submitterId
   * @throws NoTicketsFoundException in case no tickets were submitted by that user
   */
  public List<Ticket> searchTicketBySubmitterId(String submitterId) throws NoTicketsFoundException {
    return searchTicket(
        CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, TicketField.SUBMITTER_ID),
        submitterId);
  }

  /**
   * searches for a ticket based on the id of its assigned user
   *
   * @return ticket matching assignee with id = assigneeId
   * @throws NoTicketsFoundException in case no tickets were assigned to that user
   */
  public List<Ticket> searchTicketByAssigneeId(String assigneeId) throws NoTicketsFoundException {
    return searchTicket(
        CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, TicketField.ASSIGNEE_ID),
        assigneeId);
  }
}
