package com.zendesk.model;

import com.google.gson.annotations.SerializedName;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Data;

@Data
public class Ticket {

  @SerializedName("_id")
  private String id;
  private String url;
  private String externalId;
  private ZonedDateTime createdAt;
  private String type;
  private String subject;
  private String description;
  private String priority;
  private String status;
  private long submitterId;
  private long assigneeId;
  private long organizationId;
  private List<String> tags;
  private boolean hasIncidents;
  private ZonedDateTime dueAt;
  private String via;
}
