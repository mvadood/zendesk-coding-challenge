package com.zendesk.model;

import com.google.gson.annotations.SerializedName;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Data;

@Data
public class Organization {

  @SerializedName("_id")
  private String id;
  private String url;
  private String externalId;
  private String name;
  private List<String> domainNames;
  private ZonedDateTime createdAt;
  private String details;
  private boolean sharedTickets;
  private List<String> tags;
}
