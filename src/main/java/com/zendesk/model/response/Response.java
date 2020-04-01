package com.zendesk.model.response;

import java.util.LinkedList;
import java.util.List;
import lombok.Data;

public class Response<T> {

  List<T> responseItems = new LinkedList<>();

  public void addResponseItem(T responseItem){
    responseItems.add(responseItem);
  }
}
