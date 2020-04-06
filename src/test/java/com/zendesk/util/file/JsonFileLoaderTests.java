package com.zendesk.util.file;

import static junit.framework.TestCase.assertEquals;

import com.google.common.io.Resources;
import com.zendesk.model.entity.Organization;
import com.zendesk.model.entity.Ticket;
import com.zendesk.model.entity.User;
import com.zendesk.util.GsonProvider;
import com.zendesk.util.TestConstants;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Tests testing the {@link JsonFileReader} class
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JsonFileReader.class, GsonProvider.class})
public class JsonFileLoaderTests {

  @Autowired
  JsonFileReader<User> usersJsonFileReader;

  @Autowired
  JsonFileReader<Organization> orgsJsonFileReader;

  @Autowired
  JsonFileReader<Ticket> ticketsJsonFileReader;

  /**
   * Number of loaded users should match th real number
   */
  @Test
  public void usersCountShouldMatch75() throws IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    Iterator<User> iterator = usersJsonFileReader
        .getIterator(classLoader.getResourceAsStream(TestConstants.USERS_FILE_PATH), User.class);

    AtomicInteger size = new AtomicInteger();
    iterator.forEachRemaining(o -> size.getAndIncrement());
    assertEquals(75, size.get());
  }


  /**
   * Number of loaded orgs should match th real number
   */
  @Test
  public void orgsCountShouldMatch25() throws IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    Iterator<Organization> iterator = orgsJsonFileReader
        .getIterator(classLoader.getResourceAsStream(TestConstants.ORGS_FILE_PATH),
            Organization.class);

    AtomicInteger size = new AtomicInteger();
    iterator.forEachRemaining(o -> size.getAndIncrement());
    assertEquals(25, size.get());
  }

  /**
   * Number of loaded tickets should match th real number
   */
  @Test
  public void ticketsCountShouldMatch200() throws IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    Iterator<Ticket> iterator = ticketsJsonFileReader
        .getIterator(classLoader.getResourceAsStream(TestConstants.TICKETS_FILE_PATH),
            Ticket.class);

    AtomicInteger size = new AtomicInteger();
    iterator.forEachRemaining(o -> size.getAndIncrement());
    assertEquals(200, size.get());
  }
}
