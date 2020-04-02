package com.zendesk.exception.input;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidInputException extends Exception {

  public InvalidInputException(Throwable cause) {
    super(cause);
  }

}
