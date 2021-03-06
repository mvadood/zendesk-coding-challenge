package com.zendesk.stress;

import static org.junit.Assert.assertNotEquals;

import com.zendesk.exception.NoTicketsFoundException;
import com.zendesk.model.entity.Ticket;
import com.zendesk.model.request.TicketField;
import com.zendesk.model.response.Response;
import com.zendesk.model.response.TicketResponseItem;
import com.zendesk.processor.Processor;
import com.zendesk.repository.ReverseIndexRepository;
import com.zendesk.util.Constants;
import com.zendesk.util.GsonProvider;
import com.zendesk.util.TestConstants;
import com.zendesk.util.file.JsonFileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.andreinc.mockneat.MockNeat;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Testing the solution's performance with some mocked data
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ReverseIndexRepository.class, Processor.class, GsonProvider.class,
    JsonFileReader.class})
public class StressTests {

  int numTickets = 100;

  static List<String> orgIdsPool;
  static List<String> userIdsPool;
  static List<String> ticketTypesPool;
  static List<String> ticketPrioritiesPool;
  static List<String> ticketStatusesPool;

  @Autowired
  GsonProvider gsonProvider;

  @Autowired
  private Processor processor;


  private final String TICKETS_FILE = "New_Tickets.json";

  /**
   * Creates the pool of data from which random data can be generated
   */
  @BeforeClass
  public static void initPools() {
    orgIdsPool = IntStream.range(101, 126).mapToObj(String::valueOf)
        .collect(Collectors.toList());

    userIdsPool = IntStream.range(1, 76).mapToObj(String::valueOf)
        .collect(Collectors.toList());

    ticketTypesPool = Arrays.asList("incident", "problem", "question", "task");
    ticketPrioritiesPool = Arrays.asList("low", "normal", "high", "urgent");
    ticketStatusesPool = Arrays.asList("solved", "pending", "closed", "open");
  }

  @Autowired
  ReverseIndexRepository reverseIndexRepository;

  /**
   * Loads up the processor instance building up the default index before each test
   */
  @Before
  public void loadUpProcessor() throws IOException {
    storeNewTicketsFile();

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
  public void clear() {
    processor.clear();
  }

  /**
   * Searching for a ticket by its status in a big dataset should be successful without a major
   * issue
   */
  @Test
  public void searchTicketByStatusShouldSucceed() throws NoTicketsFoundException {
    Response<TicketResponseItem> response = processor.lookupTicket(TicketField.STATUS, "open");

    assertNotEquals(0, response.getResponseItems().size());
  }

  /**
   * Stores the file containing all the mock tickets data
   */
  private void storeNewTicketsFile() throws IOException {
    MockNeat mock = MockNeat.threadLocal();

    List<String> randomDates = mock.localDates().list(1000).val().stream()
        .map(date -> date.atStartOfDay(ZoneOffset.UTC))
        .map(kir -> kir.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT))).collect(
            Collectors.toList());

    String json = mock.constructor(Ticket.class)
        .params(mock.uuids()
            , mock.urls(),
            mock.uuids(),
            mock.fromStrings(randomDates).map(s -> ZonedDateTime.parse(s,
                DateTimeFormatter.ofPattern(Constants.DATE_FORMAT))),
            mock.fromStrings(ticketTypesPool),
            mock.strings(),
            mock.strings(),
            mock.fromStrings(ticketPrioritiesPool),
            mock.fromStrings(ticketStatusesPool),
            mock.fromStrings(userIdsPool),
            mock.fromStrings(userIdsPool),
            mock.fromStrings(orgIdsPool),
            mock.strings().list(2),
            mock.bools(),
            mock.fromStrings(randomDates).map(s -> ZonedDateTime.parse(s,
                DateTimeFormatter.ofPattern(Constants.DATE_FORMAT))),
            mock.strings())
        .list(numTickets).map(gsonProvider.getGson()::toJson).val();
    Files.write(Paths.get(TICKETS_FILE), json.getBytes());
  }


}
