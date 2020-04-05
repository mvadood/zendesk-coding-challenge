package com.zendesk.model.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Users, organizations and tickets are all considered to be a sort of Entity
 */
@Data
public abstract class Entity {

  @SerializedName("_id")
  protected String id;
}
