package com.zendesk.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Entity {
  @SerializedName("_id")
  protected String id;
}
