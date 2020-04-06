package com.zendesk.exception.input;

import lombok.NoArgsConstructor;

/**
 * Thrown when user input for a field value is not in the right data format
 */
@NoArgsConstructor
public class InvalidValueTypeException extends InvalidInputException {

  public InvalidValueTypeException(Throwable cause) {
    super(cause);
  }

}
