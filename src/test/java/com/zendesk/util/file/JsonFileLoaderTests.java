package com.zendesk.util.file;

import static junit.framework.TestCase.assertEquals;

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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JsonFileReader.class, GsonProvider.class})
public class JsonFileLoaderTests {

  @Autowired
  JsonFileReader<User> usersJsonFileReader;

  @Autowired
  JsonFileReader<Organization> orgsJsonFileReader;

  @Autowired
  JsonFileReader<Ticket> ticketsJsonFileReader;

  @Test
  public void usersCountShouldMatch75() throws IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL resource = classLoader.getResource(TestConstants.USERS_FILE_PATH);
    Iterator<User> iterator = usersJsonFileReader.getIterator(resource.getPath(), User.class);

    AtomicInteger size = new AtomicInteger();
    iterator.forEachRemaining(o -> size.getAndIncrement());
    assertEquals(75, size.get());
  }


  @Test
  public void orgsCountShouldMatch25() throws IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL resource = classLoader.getResource(TestConstants.ORGS_FILE_PATH);
    Iterator<Organization> iterator = orgsJsonFileReader
        .getIterator(resource.getPath(), Organization.class);

    AtomicInteger size = new AtomicInteger();
    iterator.forEachRemaining(o -> size.getAndIncrement());
    assertEquals(25, size.get());
  }

  @Test
  public void ticketsCountShouldMatch200() throws IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL resource = classLoader.getResource(TestConstants.TICKETS_FILE_PATH);
    Iterator<Ticket> iterator = ticketsJsonFileReader.getIterator(resource.getPath(), Ticket.class);

    AtomicInteger size = new AtomicInteger();
    iterator.forEachRemaining(o -> size.getAndIncrement());
    assertEquals(200, size.get());
  }
}
