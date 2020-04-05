package com.zendesk.exception.input;

import lombok.NoArgsConstructor;

/**
 * Thrown when user is searching for something other than a user, an organization or a ticket
 */
@NoArgsConstructor
public class NotSupportedEntityTypeException extends InvalidInputException {
}
