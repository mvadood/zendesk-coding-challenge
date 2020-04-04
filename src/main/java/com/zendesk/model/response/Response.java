package com.zendesk.model.response;

import com.zendesk.model.entity.Drawable;
import java.util.LinkedList;
import java.util.List;
import lombok.Getter;

public class Response<T extends ResponseItem> implements Drawable {

  @Getter
  List<T> responseItems = new LinkedList<>();

  public void addResponseItem(T responseItem) {
    responseItems.add(responseItem);
  }

  @Override
  public String draw(String entity) {
    StringBuilder result = new StringBuilder(
        String.format("%d %ss were found for your search query", responseItems.size(), entity))
        .append(System.lineSeparator());

    for (int i = 0; i < responseItems.size(); i++) {
      result.append(responseItems.get(i).draw(String.valueOf(i + 1)))
          .append(System.lineSeparator());
      result.append(
          "*********************************************************************************************************************")
          .append(System.lineSeparator());
    }

    return result.toString().trim();
  }
}
