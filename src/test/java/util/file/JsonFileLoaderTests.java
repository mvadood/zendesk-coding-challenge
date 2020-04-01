package util.file;

import static junit.framework.TestCase.assertEquals;

import com.zendesk.model.Organization;
import com.zendesk.model.Ticket;
import com.zendesk.model.User;
import com.zendesk.util.file.JsonFileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class JsonFileLoaderTests {

  private final String usersFilePath = "test-resources/users.json";

  private final String orgsFilePath = "test-resources/organizations.json";

  private final String ticketsFilePath = "test-resources/tickets.json";


  @Test
  public void usersCountShouldMatch75() throws IOException {
    JsonFileReader<User> jsonFileReader = new JsonFileReader<>();
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL resource = classLoader.getResource(usersFilePath);
    Iterator<User> objectIterator = jsonFileReader.getObjectIterator(resource.getPath(),User.class);

    AtomicInteger size = new AtomicInteger();
    objectIterator.forEachRemaining(o -> size.getAndIncrement());
    assertEquals(75, size.get());
  }


  @Test
  public void orgsCountShouldMatch25() throws IOException {
    JsonFileReader<Organization> jsonFileReader = new JsonFileReader<>();
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL resource = classLoader.getResource(orgsFilePath);
    Iterator<Organization> objectIterator = jsonFileReader.getObjectIterator(resource.getPath(), Organization.class);

    AtomicInteger size = new AtomicInteger();
    objectIterator.forEachRemaining(o -> size.getAndIncrement());
    assertEquals(25, size.get());
  }

  @Test
  public void ticketsCountShouldMatch200() throws IOException {
    JsonFileReader<Ticket> jsonFileReader = new JsonFileReader<>();
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL resource = classLoader.getResource(ticketsFilePath);
    Iterator<Ticket> objectIterator = jsonFileReader.getObjectIterator(resource.getPath(),Ticket.class);

    AtomicInteger size = new AtomicInteger();
    objectIterator.forEachRemaining(o -> size.getAndIncrement());
    assertEquals(200, size.get());
  }
}
