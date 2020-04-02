package com.zendesk.processor;

import static junit.framework.TestCase.assertEquals;

import com.zendesk.exception.NoOrgsFoundException;
import com.zendesk.exception.NoTicketsFoundException;
import com.zendesk.exception.NoUsersFoundException;
import com.zendesk.model.response.OrgResponseItem;
import com.zendesk.model.response.Response;
import com.zendesk.model.response.TicketResponseItem;
import com.zendesk.model.response.UserResponseItem;
import com.zendesk.util.Constants;
import com.zendesk.util.TestConstants;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

public class ProcessorTests {

  private Processor processor;

  public static final String ROLE_FIELD = "role";
  public static final String CREATED_AT_FIELD = "createdAt";
  public static final String EXTERNAL_ID_FIELD = "externalId";

  @Before
  public void loadUpProcessor() throws IOException {
    processor = new Processor();
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    processor.loadUpRepo(classLoader.getResource(TestConstants.USERS_FILE_PATH).getPath(),
        classLoader.getResource(TestConstants.ORGS_FILE_PATH).getPath(),
        classLoader.getResource(TestConstants.TICKETS_FILE_PATH).getPath());
  }


  @Test
  public void lookupUsersByRoleMustReturn24ResponseItems() throws NoUsersFoundException {
    Response<UserResponseItem> usersResponse = processor.lookupUser(ROLE_FIELD, "admin");

    assertEquals(24, usersResponse.getResponseItems().size());
  }

  @Test
  public void lookupOrgsByCreatedAtMustReturn8RelatedTickets() throws NoOrgsFoundException {
    Response<OrgResponseItem> orgsResponse = processor.lookupOrg(CREATED_AT_FIELD,
        ZonedDateTime.parse("2016-04-07T08:21:44 -10:00",
            DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)).toEpochSecond());

    assertEquals(1, orgsResponse.getResponseItems().size());
    assertEquals(8, orgsResponse.getResponseItems().get(0).getTickets().size());
  }

  @Test
  public void lookupTicketByExternalIdMustReturnTheRightUsers() throws NoTicketsFoundException {
    Response<TicketResponseItem> ticketsResponse = processor
        .lookupTicket(EXTERNAL_ID_FIELD, "c330acd5-26e0-4e99-b946-48b741225828");
    assertEquals(1, ticketsResponse.getResponseItems().size());
    assertEquals("47", ticketsResponse.getResponseItems().get(0).getAssignee().getId());
    assertEquals("54", ticketsResponse.getResponseItems().get(0).getSubmitter().getId());
  }

}
