package com.zendesk.model.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public abstract class Entity {

  @SerializedName("_id")
  protected String id;
}
