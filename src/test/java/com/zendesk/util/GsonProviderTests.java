package com.zendesk.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import com.google.gson.Gson;
import com.zendesk.fixtures.StringFixtures;
import com.zendesk.model.entity.Organization;
import com.zendesk.model.entity.Ticket;
import com.zendesk.model.entity.User;
import java.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Tests testing the {@link GsonProvider} class
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {GsonProvider.class})
public class GsonProviderTests {

  @Autowired
  GsonProvider gsonProvider;

  /**
   * User entity should be loaded fine from a string
   */
  @Test
  public void userShouldBeLoaded() {
    Gson gson = gsonProvider.getGson();
    User user = gson.fromJson(StringFixtures.USER_STR, User.class);
    assertNotNull(user);
  }

  /**
   * Organization entity should be loaded fine from a string
   */
  @Test
  public void organizationShouldBeLoaded() {
    Gson gson = gsonProvider.getGson();
    Organization org = gson.fromJson(StringFixtures.ORG_STR, Organization.class);
    assertNotNull(org);
  }

  /**
   * Ticket entity should be loaded fine from a string
   */
  @Test
  public void ticketShouldBeLoaded() {
    Gson gson = gsonProvider.getGson();
    Ticket ticket = gson.fromJson(StringFixtures.TICKET_STR, Ticket.class);
    assertNotNull(ticket);
  }

  /**
   * User id should not be zero
   */
  @Test
  public void userIdShouldNotBeZero() {
    Gson gson = gsonProvider.getGson();
    User user = gson.fromJson(StringFixtures.USER_STR, User.class);
    assertNotEquals(0, user.getId());
  }

  /**
   * A ticket loaded date should match the real date given
   */
  @Test
  public void ticketDateShouldBeLoadedProperly() {
    Gson gson = gsonProvider.getGson();
    Ticket ticket = gson.fromJson(StringFixtures.TICKET_STR, Ticket.class);

    String createdDateString = ticket.getCreatedAt()
        .format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
    assertEquals("2016-04-28T11:19:34 -10:00", createdDateString);
  }
}
