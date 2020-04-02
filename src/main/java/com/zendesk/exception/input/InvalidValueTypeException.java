package com.zendesk.exception.input;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidValueTypeException extends InvalidInputException {

  public InvalidValueTypeException(Throwable cause) {
    super(cause);
  }

}
