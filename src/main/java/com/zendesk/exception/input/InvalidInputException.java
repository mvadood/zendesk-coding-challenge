package com.zendesk.exception.input;

import lombok.NoArgsConstructor;

/**
 * Thrown when user input is not valid
 */
@NoArgsConstructor
public class InvalidInputException extends Exception {

  public InvalidInputException(Throwable cause) {
    super(cause);
  }

}
