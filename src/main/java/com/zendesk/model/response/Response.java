package com.zendesk.model.response;

import java.util.LinkedList;
import java.util.List;
import lombok.Getter;

public class Response<T extends ResponseItem> {

  @Getter
  List<T> responseItems = new LinkedList<>();

  public void addResponseItem(T responseItem){
    responseItems.add(responseItem);
  }
}
