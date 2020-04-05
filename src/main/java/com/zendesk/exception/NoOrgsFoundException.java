package com.zendesk.exception;


/**
 * Thrown when no organizations were found for a specific search query
 */
public class NoOrgsFoundException extends EntityNotFoundException {

  public NoOrgsFoundException() {
    super();
  }
}
