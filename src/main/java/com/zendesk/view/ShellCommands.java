package com.zendesk.view;

import static java.lang.Math.*;

import com.zendesk.exception.NoOrgsFoundException;
import com.zendesk.exception.NoTicketsFoundException;
import com.zendesk.exception.NoUsersFoundException;
import com.zendesk.exception.input.InvalidValueTypeException;
import com.zendesk.exception.input.NotSupportedEntityTypeException;
import com.zendesk.exception.input.NotSupportedFieldException;
import com.zendesk.model.entity.Organization;
import com.zendesk.model.entity.Ticket;
import com.zendesk.model.entity.User;
import com.zendesk.model.request.OrganizationField;
import com.zendesk.model.request.TicketField;
import com.zendesk.model.request.UserField;
import com.zendesk.model.response.Response;
import com.zendesk.model.response.ResponseItem;
import com.zendesk.processor.Processor;
import com.zendesk.util.Util;
import com.zendesk.view.presentation.ResponseDrawer;
import de.vandermeer.asciitable.AsciiTable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * Class containing the shell commands that can be run
 */
@Slf4j
@ShellComponent
public class ShellCommands {

  public static final String DEFAULT_USERS_FILE_PATH = "users_default.json";
  public static final String DEFAULT_ORGS_FILE_PATH = "organizations_default.json";
  public static final String DEFAULT_TICKETS_FILE_PATH = "tickets_default.json";

  private boolean indexLoaded = false;
  private Processor processor;
  private final SearchInputProcessor searchInputProcessor;
  private final ResponseDrawer responseDrawer;

  @Autowired
  public ShellCommands(Processor processor,
      SearchInputProcessor searchInputProcessor,
      ResponseDrawer responseDrawer) {
    this.processor = processor;
    this.searchInputProcessor = searchInputProcessor;
    this.responseDrawer = responseDrawer;
  }

  /**
   * Loads json files into the index
   *
   * @param users path to the users json file
   * @param organizations path to the organizations json file
   * @param tickets path to the tickets json file
   * @return message showing the load up was successful or not
   * @throws IOException in case there was a problem loading the files
   */
  @ShellMethod(value = "Load up entity files. Pass three values pointing to the users, organization and tickets files.", key = "load")
  public String loadFiles(
      @ShellOption(defaultValue = DEFAULT_USERS_FILE_PATH, help = "Path to the users file", value = {
          "--u", "--users"}) String users,
      @ShellOption(defaultValue = DEFAULT_ORGS_FILE_PATH, help = "Path to the organizations file", value = {
          "--o", "--organizations"}) String organizations,
      @ShellOption(defaultValue = DEFAULT_TICKETS_FILE_PATH, help = "Path to the tickets file", value = {
          "--t", "--tickets"}) String tickets)
      throws IOException {
    processor.clear();
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream usersInputStream, orgsInputStream, ticketsInputStream;

    if (users.equals(DEFAULT_USERS_FILE_PATH)) {
      usersInputStream = classLoader.getResourceAsStream(DEFAULT_USERS_FILE_PATH);
    } else {
      usersInputStream = new FileInputStream(new File(users));
    }
    if (organizations.equals(DEFAULT_ORGS_FILE_PATH)) {
      orgsInputStream = classLoader.getResourceAsStream(DEFAULT_ORGS_FILE_PATH);
    } else {
      orgsInputStream = new FileInputStream(new File(organizations));
    }
    if (tickets.equals(DEFAULT_TICKETS_FILE_PATH)) {
      ticketsInputStream = classLoader.getResourceAsStream(DEFAULT_TICKETS_FILE_PATH);
    } else {
      ticketsInputStream = new FileInputStream(new File(tickets));
    }

    processor.loadUpRepo(usersInputStream, orgsInputStream, ticketsInputStream);
    log.trace("{} and {} and {} files were loaded into the index", users, organizations, tickets);
    indexLoaded = true;
    return "Great! Index was loaded. Now you can start searching by using the search command";
  }

  /**
   * Lists the fields on which search can be conducted
   *
   * @return the list of the fields on which search can be conducted
   */
  @ShellMethod(value = "Show the fields on which search is supported.", key = "fields")
  public String fields() {
    AsciiTable at = new AsciiTable();
    at.addRule();
    at.addRow("User", "Organization", "Ticket");
    at.addRule();
    List<String> userFields = UserField.getFieldsList();
    List<String> orgFields = OrganizationField.getFieldsList();
    List<String> ticketFields = TicketField.getFieldsList();

    int maxSize = max(max(userFields.size(), orgFields.size()), ticketFields.size());

    for (int i = 0; i < maxSize; i++) {
      String userField = i < userFields.size() ? userFields.get(i) : null;
      String orgField = i < orgFields.size() ? orgFields.get(i) : null;
      String ticketField = i < ticketFields.size() ? ticketFields.get(i) : null;

      if (userField != null) {
        String userType = UserField.getFieldType(userField).getTypeName();
        userType = userType.substring(userType.lastIndexOf('.') + 1);
        userField += ": " + userType;
      } else {
        userField = "";
      }
      if (orgField != null) {
        String orgType = OrganizationField.getFieldType(orgField).getTypeName();
        orgType = orgType.substring(orgType.lastIndexOf('.') + 1);
        orgField += ": " + orgType;
      } else {
        orgField = "";
      }
      if (ticketField != null) {
        String ticketType = TicketField.getFieldType(ticketField).getTypeName();
        ticketType = ticketType.substring(ticketType.lastIndexOf('.') + 1);
        ticketField += ": " + ticketType;
      } else {
        ticketField = "";
      }

      at.addRow(userField, orgField, ticketField);
      at.addRow(System.lineSeparator(), System.lineSeparator(), System.lineSeparator());
    }
    at.addRule();

    return at.render() + System.lineSeparator();
  }

  /**
   * Searches for entities
   *
   * @param entity string of the entity to search for. user|organization|ticket
   * @param field field to search on
   * @param value value of the field to search for
   * @return the search results in a string table representation or the error message if there is
   * any
   */
  @ShellMethod(value = "Search for entities. Do 'search ticket[|user|organization] field value' to search for entities.", key = "search")
  public String search(
      @ShellOption(defaultValue = "ticket", help = "Entity to search. Choose 'user','organization' or 'ticket'", value = {
          "--e", "--entity"}) String entity,
      @ShellOption(defaultValue = "_id", help = "Field to search. For a full list of fields run the 'fields' command", value = {
          "--f", "--field"}) String field,
      @ShellOption(defaultValue = "", help = "Value to search for", value = {
          "--v", "--value"}) String value) {

    if (!indexLoaded) {
      return "Please load up the files by issuing a 'load' command before doing a search";
    }

    try {
      Type entityType = searchInputProcessor.getSearchEntity(entity);
      searchInputProcessor.checkIfFieldExists(entityType, field);
      Object convertedValue = searchInputProcessor.validateAndConvert(entityType, field, value);
      String convertedField = Util.convertFieldKeyString(field);

      Response<? extends ResponseItem> response;
      if (entityType.equals(User.class)) {
        response = processor.lookupUser(convertedField, convertedValue);
      } else if (entityType.equals(Organization.class)) {
        response = processor.lookupOrg(convertedField, convertedValue);
      } else {
        assert entityType.equals(Ticket.class);
        response = processor.lookupTicket(convertedField, convertedValue);
      }

      log.trace("{} search for field {} and value {} yielded {} results",
          entity, field, value, response.getResponseItems().size());
      return responseDrawer.draw(response, entity);


    } catch (NotSupportedEntityTypeException e) {
      log.warn("User input for entity {} can not be processed", entity, e);
      return String.format(
          "'%s' is not a supported entity. Please choose among 'user', 'organization' and 'ticket'",
          entity);
    } catch (NotSupportedFieldException e) {
      log.warn("User input for field {} and entity can not be processed", field, entity, e);
      return String.format(
          "'%s' is not a supported field for '%s'. Please see the list of full supported fields by running the 'fields' command",
          field, entity);
    } catch (InvalidValueTypeException e) {
      log.warn(
          "User input for entity {}, field {} and value {} can not be processed since the value is not in the right format",
          entity, field, value, e);
      return String.format(
          "'%s' is not a supported value for field '%s'. Please see the list of full supported fields and their types by running the 'fields' command",
          value, field);
    } catch (NoUsersFoundException e) {
      log.trace("User search for field {} and value {} yielded no results",
          field, value);
      return "No users were found!";
    } catch (NoTicketsFoundException e) {
      log.trace("Ticket search for field {} and value {} yielded no results",
          field, value);
      return "No tickets were found!";
    } catch (NoOrgsFoundException e) {
      log.trace("Organizations search field {} and value {} yielded no results",
          field, value);
      return "No organizations were found!";
    }
  }
}
