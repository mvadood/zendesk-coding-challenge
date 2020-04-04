package com.zendesk.view.presentation;


public interface Drawable<T> {
  String draw(T toDraw, String header);
}
