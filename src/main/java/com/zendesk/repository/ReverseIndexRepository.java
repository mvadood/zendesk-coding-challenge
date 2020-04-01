package com.zendesk.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zendesk.model.Entity;
import com.zendesk.model.Organization;
import com.zendesk.model.Ticket;
import com.zendesk.model.User;
import com.zendesk.util.file.FileLoader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ReverseIndexRepository {

  private Map<Long, User> users;
  private Map<Long, Ticket> tickets;
  private Map<Long, Organization> organizations;

  private Map<String, Map<Object, List<String>>> usersIndex;
  private Map<String, Map<Object, List<String>>> ticketsIndex;
  private Map<String, Map<Object, List<String>>> orgsIndex;

  private FileLoader fileLoader;
  ObjectMapper objectMapper = new ObjectMapper();

  public ReverseIndexRepository(FileLoader fileLoader) {
    this.fileLoader = fileLoader;

    users = new HashMap<>();
    tickets = new HashMap<>();
    organizations = new HashMap<>();

    usersIndex = new HashMap<>();
    orgsIndex = new HashMap<>();
    ticketsIndex = new HashMap<>();

    objectMapper.registerModule(new JavaTimeModule());
  }

  public void loadIndex(String usersFilePath, String orgsFilePath,
      String ticketsFilePath) throws IOException {
    Iterator<User> usersIterator = fileLoader.getIterator(usersFilePath, User.class);
    Iterator<Organization> orgsIterator = fileLoader
        .getIterator(orgsFilePath, Organization.class);
    Iterator<Ticket> ticketsIterator = fileLoader.getIterator(ticketsFilePath, Ticket.class);

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
}
