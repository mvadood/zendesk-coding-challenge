package com.zendesk.repository;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import com.zendesk.exception.NoOrgsFoundException;
import com.zendesk.exception.NoTicketsFoundException;
import com.zendesk.fixtures.EntityFixtures;
import com.zendesk.model.Ticket;
import com.zendesk.util.TestConstants;
import com.zendesk.util.file.JsonFileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class ReverseIndexRepositoryTests {

  private static final String ID_FIELD = "id";
  private static final String URL_FIELD = "url";
  private static final String TAGS_FIELD = "tags";
  private static final String SUBJECT_FIELD = "subject";

  private ReverseIndexRepository reverseIndexRepository;

  @Before
  public void instantiateReverseIndex() {
    reverseIndexRepository = new ReverseIndexRepository(new JsonFileReader());
  }

  @Test
  public void entityKeyValuesShouldContainSetKeys() {
    Map<String, Object> entityKeyValues = reverseIndexRepository
        .buildEntityKeyValues(EntityFixtures.user);

    assertTrue(entityKeyValues.containsKey(ID_FIELD));
    assertTrue(entityKeyValues.containsKey(URL_FIELD));
    assertTrue(entityKeyValues.containsKey(TAGS_FIELD));
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
    Map<String, Map<Object, List<String>>> index = new HashMap<>();
    index.put(TAGS_FIELD, new HashMap<>());
    reverseIndexRepository
        .indexList(TAGS_FIELD, EntityFixtures.user.getTags(), EntityFixtures.user, index);

    assertEquals(1,
        index.get(TAGS_FIELD).get(EntityFixtures.user.getTags().get(0).trim().toLowerCase())
            .size());
    assertEquals(1,
        index.get(TAGS_FIELD).get(EntityFixtures.user.getTags().get(1).trim().toLowerCase())
            .size());
  }

  @Test
  public void searchTicketBySubjectShouldReturn1() throws IOException, NoTicketsFoundException {
    loadUpIndex(reverseIndexRepository);

    List<Ticket> tickets = reverseIndexRepository
        .searchTicket(SUBJECT_FIELD, "A Catastrophe in Korea (North)".toLowerCase());

    assertEquals(1, tickets.size());
  }

  @Test
  public void searchTicketByTagShouldReturn14() throws IOException, NoTicketsFoundException {
    loadUpIndex(reverseIndexRepository);

    List<Ticket> tickets = reverseIndexRepository
        .searchTicket(TAGS_FIELD, "Pennsylvania".toLowerCase());

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
