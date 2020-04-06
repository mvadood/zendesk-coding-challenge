package com.zendesk.model.entity;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Data class representing an organization
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Organization extends Entity{

  private String url;
  private String externalId;
  private String name;
  private List<String> domainNames;
  private ZonedDateTime createdAt;
  private String details;
  private boolean sharedTickets;
  private List<String> tags;
}
