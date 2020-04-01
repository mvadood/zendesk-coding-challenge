package com.zendesk.model;

import com.google.gson.annotations.SerializedName;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Data;

@Data
public class User {

  @SerializedName("_id")
  private long id;
  private String url;
  private String externalId;
  private String name;
  private String alias;
  private ZonedDateTime createdAt;
  private boolean active;
  private boolean verified;
  private boolean shared;
  private String locale;
  private String timezone;
  private ZonedDateTime lastLoginAt;
  private String email;
  private String phone;
  private String signature;
  private int organizationId;
  private List<String> tags;
  private boolean suspended;
  private String role;
}
