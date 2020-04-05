package com.zendesk.view.presentation;

/**
 * Interface providing a draw method which can be used to draw an object into the console
 */
public interface Drawable<T> {

  /**
   * Draws an object into the console
   *
   * @param toDraw object to draw
   * @param header a string passed by the method caller that can be used in the header of the
   * figure
   * @return a string representing that object
   */
  String draw(T toDraw, String header);
}
