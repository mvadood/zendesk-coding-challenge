package com.zendesk.view;

import com.zendesk.exception.NoOrgsFoundException;
import com.zendesk.exception.NoTicketsFoundException;
import com.zendesk.exception.NoUsersFoundException;
import com.zendesk.exception.input.InvalidValueTypeException;
import com.zendesk.exception.input.NotSupportedEntityType;
import com.zendesk.exception.input.NotSupportedFieldException;
import com.zendesk.model.entity.Entity;
import com.zendesk.model.entity.Organization;
import com.zendesk.model.entity.Ticket;
import com.zendesk.model.entity.User;
import com.zendesk.model.response.Response;
import com.zendesk.model.response.ResponseItem;
import com.zendesk.processor.Processor;
import java.io.IOException;
import java.lang.reflect.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class ShellCommands {

  public static final String DEFAULT_USERS_FILE_PATH = "users.json";
  public static final String DEFAULT_ORGS_FILE_PATH = "organizations.json";
  public static final String DEFAULT_TICKETS_FILE_PATH = "tickets.json";

  private boolean indexLoaded = false;
  private Processor processor;
  private final SearchInputProcessor searchInputProcessor;

  @Autowired
  public ShellCommands(Processor processor,
      SearchInputProcessor searchInputProcessor) {
    this.processor = processor;
    this.searchInputProcessor = searchInputProcessor;
  }

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

    if (users.equals(DEFAULT_USERS_FILE_PATH)) {
      users = classLoader.getResource(DEFAULT_USERS_FILE_PATH).getPath();
    }
    if (organizations.equals(DEFAULT_ORGS_FILE_PATH)) {
      organizations = classLoader.getResource(DEFAULT_ORGS_FILE_PATH).getPath();
    }
    if (tickets.equals(DEFAULT_TICKETS_FILE_PATH)) {
      tickets = classLoader.getResource(DEFAULT_TICKETS_FILE_PATH).getPath();
    }

    processor.loadUpRepo(users, organizations, tickets);
    indexLoaded = true;
    return "Great! Index was loaded. Now you can start searching by using the search command";
  }

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
      String convertedField = searchInputProcessor.convertFieldValueString(field);
      Response<? extends ResponseItem> response;
      if (entityType.equals(User.class)) {
        response = processor.lookupUser(convertedField, convertedValue);
      } else if (entityType.equals(Organization.class)) {
        response = processor.lookupOrg(convertedField, convertedValue);
      } else {
        assert entityType.equals(Ticket.class);
        response = processor.lookupTicket(convertedField, convertedValue);
      }


    } catch (NotSupportedEntityType notSupportedEntityType) {
      return String.format(
          "'%s' is not a supported entity. Please choose among 'user', 'organization' and 'ticket'",
          entity);
    } catch (NotSupportedFieldException e) {
      return String.format(
          "'%s' is not a supported field for '%s'. Please see the list of full supported fields by running the 'fields' command",
          field, entity);
    } catch (InvalidValueTypeException e) {
      return String.format(
          "'%s' is not a supported value for field '%s'. Please see the list of full supported fields and their types by running the 'fields' command",
          value, field);
    } catch (NoUsersFoundException e) {
      return "No users were found!";
    } catch (NoTicketsFoundException e) {
      return "No tickets were found!";
    } catch (NoOrgsFoundException e) {
      return "No organizations were found!";
    }

    return "";
  }


}
