package com.zendesk.model.response;

import java.util.LinkedList;
import java.util.List;
import lombok.Getter;

/**
 * Main object returned for a search query. Contains a list of {@link ResponseItem}s
 */
public class Response<T extends ResponseItem> {

  @Getter
  List<T> responseItems = new LinkedList<>();

  public void addResponseItem(T responseItem) {
    responseItems.add(responseItem);
  }
}
