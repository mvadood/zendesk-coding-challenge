package com.zendesk.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zendesk.exception.NoOrgsFoundException;
import com.zendesk.exception.NoTicketsFoundException;
import com.zendesk.exception.NoUsersFoundException;
import com.zendesk.model.entity.Entity;
import com.zendesk.model.entity.Organization;
import com.zendesk.model.entity.Ticket;
import com.zendesk.model.entity.User;
import com.zendesk.util.file.JsonFileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReverseIndexRepository {

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

  private static final String ID_FIELD_NAME = "id";
  private static final String ORGANIZATION_ID_FIELD_NAME = "organizationId";
  private static final String SUBMITTER_ID_FIELD_NAME = "submitterId";
  private static final String ASSIGNEE_ID_FIELD_NAME = "assigneeId";

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

  public void clear() {
    users = new HashMap<>();
    tickets = new HashMap<>();
    organizations = new HashMap<>();

    usersIndex = new HashMap<>();
    orgsIndex = new HashMap<>();
    ticketsIndex = new HashMap<>();
  }

  public void loadIndex(String usersFilePath, String orgsFilePath,
      String ticketsFilePath) throws IOException {
    Iterator<User> usersIterator = usersfileLoader.getIterator(usersFilePath, User.class);
    Iterator<Organization> orgsIterator = orgsfileLoader
        .getIterator(orgsFilePath, Organization.class);
    Iterator<Ticket> ticketsIterator = ticketsfileLoader.getIterator(ticketsFilePath, Ticket.class);

    populateMaps(usersIterator, users, usersIndex);
    populateMaps(orgsIterator, organizations, orgsIndex);
    populateMaps(ticketsIterator, tickets, ticketsIndex);
  }


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

  Map<String, Object> buildEntityKeyValues(Entity entity) {
    return objectMapper.convertValue(entity, new TypeReference<>() {
    });
  }

  public List<Ticket> searchTicket(String field, Object value) throws NoTicketsFoundException {
    List<String> ticketIds = ticketsIndex.get(field).get(value);
    if (ticketIds == null) {
      throw new NoTicketsFoundException();
    } else {
      return ticketIds.stream().map(ticketId -> tickets.get(ticketId)).collect(Collectors.toList());
    }
  }

  public List<Organization> searchOrg(String field, Object value) throws NoOrgsFoundException {
    List<String> orgIds = orgsIndex.get(field).get(value);
    if (orgIds == null) {
      throw new NoOrgsFoundException();
    } else {
      return orgIds.stream().map(orgId -> organizations.get(orgId)).collect(Collectors.toList());
    }
  }

  public List<User> searchUser(String field, Object value) throws NoUsersFoundException {
    List<String> userIds = usersIndex.get(field).get(value);
    if (userIds == null) {
      throw new NoUsersFoundException();
    } else {
      return userIds.stream().map(userId -> users.get(userId)).collect(Collectors.toList());
    }
  }

  public Organization searchOrgById(String id) throws NoOrgsFoundException {
    return searchOrg(ID_FIELD_NAME, id).get(0);
  }

  public User searchUserById(String id) throws NoUsersFoundException {
    return searchUser(ID_FIELD_NAME, id).get(0);
  }

  public List<User> searchUserByOrgId(String orgId) throws NoUsersFoundException {
    return searchUser(ORGANIZATION_ID_FIELD_NAME, orgId);
  }

  public List<Ticket> searchTicketByOrgId(String orgId)
      throws NoTicketsFoundException {
    return searchTicket(ORGANIZATION_ID_FIELD_NAME, orgId);
  }

  public List<Ticket> searchTicketBySubmitterId(String submitterId) throws NoTicketsFoundException {
    return searchTicket(SUBMITTER_ID_FIELD_NAME, submitterId);
  }

  public List<Ticket> searchTicketByAssigneeId(String assigneeId) throws NoTicketsFoundException {
    return searchTicket(ASSIGNEE_ID_FIELD_NAME, assigneeId);
  }
}
