package com.zendesk.repository;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import com.google.common.base.CaseFormat;
import com.zendesk.exception.NoOrgsFoundException;
import com.zendesk.exception.NoTicketsFoundException;
import com.zendesk.fixtures.EntityFixtures;
import com.zendesk.model.entity.Organization;
import com.zendesk.model.entity.Ticket;
import com.zendesk.model.entity.User;
import com.zendesk.model.request.TicketField;
import com.zendesk.model.request.UserField;
import com.zendesk.util.GsonProvider;
import com.zendesk.util.TestConstants;
import com.zendesk.util.file.JsonFileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JsonFileReader.class, ReverseIndexRepository.class, GsonProvider.class})
public class ReverseIndexRepositoryTests {

  private final String ID_FIELD = "id";

  @Autowired
  JsonFileReader<User> usersJsonFileReader;

  @Autowired
  JsonFileReader<Organization> orgsJsonFileReader;

  @Autowired
  JsonFileReader<Ticket> ticketsJsonFileReader;

  @Autowired
  ReverseIndexRepository reverseIndexRepository;

  @After
  public void cleanRepository() {
    reverseIndexRepository.clear();
  }

  @Test
  public void entityKeyValuesShouldContainSetKeys() {
    Map<String, Object> entityKeyValues = reverseIndexRepository
        .buildEntityKeyValues(EntityFixtures.user);

    assertTrue(entityKeyValues.containsKey(ID_FIELD));
    assertTrue(entityKeyValues.containsKey(
        CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, UserField.URL)));
    assertTrue(entityKeyValues
        .containsKey(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, UserField.TAGS)));
  }


  @Test
  public void indexShouldContainIndexedString() {
    Map<String, Map<Object, List<String>>> index = new HashMap<>();
    index.put(ID_FIELD, new HashMap<>());
    reverseIndexRepository
        .indexSingleValue(ID_FIELD, EntityFixtures.org.getId(), EntityFixtures.org, index);

    assertEquals(1,
        index.get(ID_FIELD).get(EntityFixtures.org.getId().trim().toLowerCase()).size());
  }

  @Test
  public void indexShouldContainIndexedList() {
    String tagsField = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, UserField.TAGS);
    Map<String, Map<Object, List<String>>> index = new HashMap<>();
    index.put(tagsField, new HashMap<>());
    reverseIndexRepository
        .indexList(tagsField, EntityFixtures.user.getTags(), EntityFixtures.user, index);

    assertEquals(1,
        index.get(tagsField).get(EntityFixtures.user.getTags().get(0).trim().toLowerCase())
            .size());
    assertEquals(1,
        index.get(tagsField).get(EntityFixtures.user.getTags().get(1).trim().toLowerCase())
            .size());
  }

  @Test
  public void searchTicketBySubjectShouldReturn1() throws IOException, NoTicketsFoundException {
    loadUpIndex(reverseIndexRepository);

    List<Ticket> tickets = reverseIndexRepository
        .searchTicket(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, TicketField.SUBJECT), "A Catastrophe in Korea (North)".toLowerCase());

    assertEquals(1, tickets.size());
  }

  @Test
  public void searchTicketByTagShouldReturn14() throws IOException, NoTicketsFoundException {
    loadUpIndex(reverseIndexRepository);

    List<Ticket> tickets = reverseIndexRepository
        .searchTicket(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, TicketField.TAGS), "Pennsylvania".toLowerCase());

    assertEquals(14, tickets.size());
  }

  @Test(expected = NoOrgsFoundException.class)
  public void shouldThrowNoOrgsFoundException() throws IOException, NoOrgsFoundException {
    loadUpIndex(reverseIndexRepository);
    reverseIndexRepository.searchOrgById("imaginary_id");
  }

  private void loadUpIndex(ReverseIndexRepository reverseIndexRepository) throws IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    reverseIndexRepository
        .loadIndex(classLoader.getResource(TestConstants.USERS_FILE_PATH).getPath(),
            classLoader.getResource(TestConstants.ORGS_FILE_PATH).getPath(),
            classLoader.getResource(TestConstants.TICKETS_FILE_PATH).getPath());

  }

}
