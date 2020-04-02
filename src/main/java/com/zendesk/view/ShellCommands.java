package com.zendesk.view;

import com.zendesk.processor.Processor;
import com.zendesk.util.Constants;
import java.io.IOException;
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

  @Autowired
  public ShellCommands(Processor processor) {
    this.processor = processor;
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

//  @ShellMethod(value = "Load up entity files. Pass three values pointing to the users, organization and tickets files.", key = "load")
//  public String search(){
//    return "";
//  }


}
