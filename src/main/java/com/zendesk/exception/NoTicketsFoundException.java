package com.zendesk.exception;

/**
 * Thrown when no tickets were found for a specific search query
 */
public class NoTicketsFoundException extends EntityNotFoundException {
  public NoTicketsFoundException(){
    super();
  }
}
