package com.zendesk.model.entity;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class User extends Entity{

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
  private String organizationId;
  private List<String> tags;
  private boolean suspended;
  private String role;
}
