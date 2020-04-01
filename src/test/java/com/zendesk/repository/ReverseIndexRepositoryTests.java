package com.zendesk.repository;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import com.zendesk.fixtures.EntityFixtures;
import com.zendesk.util.file.JsonFileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class ReverseIndexRepositoryTests {

  private static final String ID_FIELD = "id";
  private static final String TAGS_FIELD = "tags";

  private ReverseIndexRepository reverseIndexRepository;

  @Before
  public void instantiateReverseIndex() {
    reverseIndexRepository = new ReverseIndexRepository(new JsonFileReader());
  }

  @Test
  public void entityKeyValuesShouldContainSetKeys() {
    Map<String, Object> entityKeyValues = reverseIndexRepository
        .buildEntityKeyValues(EntityFixtures.user);

    assertTrue(entityKeyValues.containsKey("id"));
    assertTrue(entityKeyValues.containsKey("url"));
    assertTrue(entityKeyValues.containsKey("tags"));
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

}
