package com.zendesk.util.file;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Iterator;

/**
 * Class meant to be responsible for loading data from files
 */
public abstract class FileLoader<T> {

  /**
   * Loads content of a file into an iterator of objects of type `typeOfT`
   *
   * @param inputStream input stream pointing to the file
   * @param typeOfT type of object the file content should be translated into an iterator of
   *
   * @return an iterator of type typeOfT which can be used to load an index
   * @throws IOException if there is a problem loading from a file
   */
  public abstract Iterator<T> getIterator(InputStream inputStream, Type typeOfT)
      throws IOException;
}
