package com.zendesk.exception.input;

import lombok.NoArgsConstructor;

/**
 * Thrown when user is searching for a field that is not known
 */
@NoArgsConstructor
public class NotSupportedFieldException extends InvalidInputException {

}
