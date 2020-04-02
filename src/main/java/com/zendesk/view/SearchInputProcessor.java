package com.zendesk.view;

import com.google.common.base.CaseFormat;
import com.zendesk.exception.input.InvalidValueTypeException;
import com.zendesk.exception.input.NotSupportedEntityType;
import com.zendesk.exception.input.NotSupportedFieldException;
import com.zendesk.model.entity.Organization;
import com.zendesk.model.entity.Ticket;
import com.zendesk.model.entity.User;
import com.zendesk.model.request.OrganizationField;
import com.zendesk.model.request.TicketField;
import com.zendesk.model.request.UserField;
import com.zendesk.util.Constants;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
public class SearchInputProcessor {

  public Object validateAndConvert(Type searchClass, String fieldName, String value)
      throws InvalidValueTypeException {
    try {
      Type fieldType;
      if (searchClass.equals(User.class)) {
        fieldType = UserField.getFieldType(fieldName);
      } else if (searchClass.equals(Organization.class)) {
        fieldType = OrganizationField.getFieldType(fieldName);
      } else {
        fieldType = TicketField.getFieldType(fieldName);
      }
      return convertTo(value, fieldType);
    } catch (Exception ex) {
      throw new InvalidValueTypeException(ex);
    }
  }


  private Object convertTo(String value, Type fieldType) {
    if (fieldType.equals(Long.class)) {
      return Long.parseLong(value);
    } else if (fieldType.equals(Boolean.class)) {
      return Boolean.parseBoolean(value);
    } else if (fieldType.equals(ZonedDateTime.class)) {
      try {
        return Long.parseLong(value);
      } catch (NumberFormatException ex) {
        return ZonedDateTime.parse(value,
            DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)).toEpochSecond();
      }
    } else {
      assert fieldType.equals(String.class);
      return value.trim().toLowerCase();
    }
  }

  public Type getSearchEntity(String entity) throws NotSupportedEntityType {
    entity = entity.trim().toLowerCase();
    Type entityType;
    if (entity.equals(EntityType.USER.toString())) {
      entityType = User.class;
    } else if (entity.equals(EntityType.TICKET.toString())) {
      entityType = Ticket.class;
    } else if (entity.equals(EntityType.ORGANIZATION.toString())) {
      entityType = Organization.class;
    } else {
      throw new NotSupportedEntityType();
    }
    return entityType;
  }

  public void checkIfFieldExists(Type entityType, String fieldName)
      throws NotSupportedFieldException {
    fieldName = fieldName.trim().toLowerCase();

    if ((entityType.equals(User.class) && !UserField.getFieldsList().contains(fieldName)) ||
        (entityType.equals(Organization.class) && !OrganizationField.getFieldsList()
            .contains(fieldName)) ||
        (entityType.equals(Ticket.class) && !TicketField.getFieldsList().contains(fieldName))) {
      throw new NotSupportedFieldException();
    }
  }

  public String convertFieldValueString(String fieldName) {
    if (fieldName.equals("_id")) {
      return "id";
    } else {
      return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, fieldName);
    }
  }

  enum EntityType {
    USER,
    ORGANIZATION,
    TICKET;

    @Override
    public String toString() {
      return super.toString().toLowerCase();
    }
  }

}
