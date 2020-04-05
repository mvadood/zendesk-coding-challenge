package com.zendesk.model.entity;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Ticket extends Entity {

  public Ticket(String id, String url, String externalId, ZonedDateTime createdAt, String type,
      String subject, String description, String priority, String status,
      String submitterId, String assigneeId, String organizationId,
      List<String> tags, boolean hasIncidents, ZonedDateTime dueAt, String via) {
    this.id = id;
    this.url = url;
    this.externalId = externalId;
    this.createdAt = createdAt;
    this.type = type;
    this.subject = subject;
    this.description = description;
    this.priority = priority;
    this.status = status;
    this.submitterId = submitterId;
    this.assigneeId = assigneeId;
    this.organizationId = organizationId;
    this.tags = tags;
    this.hasIncidents = hasIncidents;
    this.dueAt = dueAt;
    this.via = via;
  }

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
