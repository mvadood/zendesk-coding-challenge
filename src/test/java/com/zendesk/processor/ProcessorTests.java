package com.zendesk.processor;

import static junit.framework.TestCase.assertEquals;

import com.google.common.base.CaseFormat;
import com.zendesk.exception.NoOrgsFoundException;
import com.zendesk.exception.NoTicketsFoundException;
import com.zendesk.exception.NoUsersFoundException;
import com.zendesk.model.entity.Ticket;
import com.zendesk.model.request.OrganizationField;
import com.zendesk.model.request.TicketField;
import com.zendesk.model.request.UserField;
import com.zendesk.model.response.OrgResponseItem;
import com.zendesk.model.response.Response;
import com.zendesk.model.response.TicketResponseItem;
import com.zendesk.model.response.UserResponseItem;
import com.zendesk.repository.ReverseIndexRepository;
import com.zendesk.util.Constants;
import com.zendesk.util.GsonProvider;
import com.zendesk.util.TestConstants;
import com.zendesk.util.file.JsonFileReader;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Tests testing the {@link Processor}
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ReverseIndexRepository.class, Processor.class, GsonProvider.class,
    JsonFileReader.class})
public class ProcessorTests {

  @Autowired
  private Processor processor;

  @Autowired
  ReverseIndexRepository reverseIndexRepository;

  /**
   * Loads up the repo from the default files
   */
  @Before
  public void loadUpProcessor() throws IOException {
    processor = new Processor(reverseIndexRepository);
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    processor.loadUpRepo(classLoader.getResourceAsStream(TestConstants.USERS_FILE_PATH),
        classLoader.getResourceAsStream(TestConstants.ORGS_FILE_PATH),
        classLoader.getResourceAsStream(TestConstants.TICKETS_FILE_PATH));
  }

  /**
   * Cleans up the repo after each test
   */
  @After
  public void cleanRepository() {
    processor.clear();
  }


  /**
   * Searching for users by role. Should pass if all 24 admins are found
   */
  @Test
  public void lookupUsersByRoleShouldReturn24ResponseItems() throws NoUsersFoundException {
    Response<UserResponseItem> usersResponse = processor.lookupUser(UserField.ROLE, "admin");

    assertEquals(24, usersResponse.getResponseItems().size());
  }

  /**
   * Searching for tickets by created time of their organization. Should pass if all 8 tickets are
   * found
   */
  @Test
  public void lookupOrgsByCreatedAtShouldReturn8RelatedTickets() throws NoOrgsFoundException {
    Response<OrgResponseItem> orgsResponse = processor.lookupOrg(
        CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, OrganizationField.CREATED_AT),
        ZonedDateTime.parse("2016-04-07T08:21:44 -10:00",
            DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)).toEpochSecond());

    assertEquals(1, orgsResponse.getResponseItems().size());
    assertEquals(8, orgsResponse.getResponseItems().get(0).getOrgTickets().size());
  }

  /**
   * Searching for users based on their tickets' external id. Should pass if all 47/54 users are
   * found
   */
  @Test
  public void lookupTicketByExternalIdShouldReturnTheRightUsers() throws NoTicketsFoundException {
    Response<TicketResponseItem> ticketsResponse = processor
        .lookupTicket(
            CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, TicketField.EXTERNAL_ID),
            "c330acd5-26e0-4e99-b946-48b741225828");
    assertEquals(1, ticketsResponse.getResponseItems().size());
    assertEquals("47", ticketsResponse.getResponseItems().get(0).getAssignee().getId());
    assertEquals("54", ticketsResponse.getResponseItems().get(0).getSubmitter().getId());
  }

}
