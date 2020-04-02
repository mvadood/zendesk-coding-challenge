package com.zendesk.model.entity;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Ticket extends Entity{

  private String url;
  private String externalId;
  private ZonedDateTime createdAt;
  private String type;
  private String subject;
  private String description;
  private String priority;
  private String status;
  private String submitterId;
  private String assigneeId;
  private String organizationId;
  private List<String> tags;
  private boolean hasIncidents;
  private ZonedDateTime dueAt;
  private String via;
}
