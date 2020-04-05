package com.zendesk.exception;

/**
 * Thrown when no users were found for a specific search query
 */
public class NoUsersFoundException extends EntityNotFoundException {
  public NoUsersFoundException(){
    super();
  }
}
