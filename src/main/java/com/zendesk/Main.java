package com.zendesk;

import com.zendesk.util.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry to the application
 *
 * @author Milad Vadoodparast
 */
@SpringBootApplication
public class Main {

  public static void main(String[] args) {
    System.out.println(Constants.LOGO);
    SpringApplication.run(Main.class, args);
  }
}
