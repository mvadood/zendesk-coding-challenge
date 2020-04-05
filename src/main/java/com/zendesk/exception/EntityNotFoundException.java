package com.zendesk.exception;

/**
 * Thrown when no entity were found for a specific search query
 */
public abstract class EntityNotFoundException extends Exception {
  public EntityNotFoundException(){
    super();
  }
}
