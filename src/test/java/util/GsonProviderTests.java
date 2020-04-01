package util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import com.google.gson.Gson;
import com.zendesk.model.Organization;
import com.zendesk.model.Ticket;
import com.zendesk.model.User;
import com.zendesk.util.Constants;
import com.zendesk.util.GsonProvider;
import fixtures.Fixtures;
import java.time.format.DateTimeFormatter;
import org.junit.Test;


public class GsonProviderTests {

  @Test
  public void userShouldBeLoaded() {
    Gson gson = GsonProvider.getInstance();
    User user = gson.fromJson(Fixtures.USER, User.class);
    assertNotNull(user);
  }

  @Test
  public void organizationShouldBeLoaded() {
    Gson gson = GsonProvider.getInstance();
    Organization org = gson.fromJson(Fixtures.ORGANIZATION, Organization.class);
    assertNotNull(org);
  }

  @Test
  public void ticketShouldBeLoaded() {
    Gson gson = GsonProvider.getInstance();
    Ticket ticket = gson.fromJson(Fixtures.TICKET, Ticket.class);
    assertNotNull(ticket);
  }

  @Test
  public void userIdShouldNotBeZero() {
    Gson gson = GsonProvider.getInstance();
    User user = gson.fromJson(Fixtures.USER, User.class);
    assertNotEquals(0, user.getId());
  }

  @Test
  public void ticketDateShouldBeLoadedProperly() {
    Gson gson = GsonProvider.getInstance();
    Ticket ticket = gson.fromJson(Fixtures.TICKET, Ticket.class);

    String createdDateString = ticket.getCreatedAt()
        .format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
    assertEquals("2016-04-28T11:19:34 -10:00", createdDateString);
  }
}
