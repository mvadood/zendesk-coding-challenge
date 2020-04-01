package com.zendesk.util.file;

import static junit.framework.TestCase.assertEquals;

import com.zendesk.model.Organization;
import com.zendesk.model.Ticket;
import com.zendesk.model.User;
import com.zendesk.util.TestConstants;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class JsonFileLoaderTests {

  @Test
  public void usersCountShouldMatch75() throws IOException {
    JsonFileReader<User> jsonFileReader = new JsonFileReader<>();
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL resource = classLoader.getResource(TestConstants.USERS_FILE_PATH);
    Iterator<User> iterator = jsonFileReader.getIterator(resource.getPath(), User.class);

    AtomicInteger size = new AtomicInteger();
    iterator.forEachRemaining(o -> size.getAndIncrement());
    assertEquals(75, size.get());
  }


  @Test
  public void orgsCountShouldMatch25() throws IOException {
    JsonFileReader<Organization> jsonFileReader = new JsonFileReader<>();
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL resource = classLoader.getResource(TestConstants.ORGS_FILE_PATH);
    Iterator<Organization> iterator = jsonFileReader
        .getIterator(resource.getPath(), Organization.class);

    AtomicInteger size = new AtomicInteger();
    iterator.forEachRemaining(o -> size.getAndIncrement());
    assertEquals(25, size.get());
  }

  @Test
  public void ticketsCountShouldMatch200() throws IOException {
    JsonFileReader<Ticket> jsonFileReader = new JsonFileReader<>();
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL resource = classLoader.getResource(TestConstants.TICKETS_FILE_PATH);
    Iterator<Ticket> iterator = jsonFileReader.getIterator(resource.getPath(), Ticket.class);

    AtomicInteger size = new AtomicInteger();
    iterator.forEachRemaining(o -> size.getAndIncrement());
    assertEquals(200, size.get());
  }
}
