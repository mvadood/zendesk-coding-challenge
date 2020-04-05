package com.zendesk.util.file;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Iterator;

/**
 * Class meant to be responsible for loading data from files
 */
public abstract class FileLoader<T> {

  /**
   * Loads content of a file into an iterator of objects of type `typeOfT`
   *
   * @param filePath path to file
   * @param typeOfT type of object the file content should be translated into an iterator of
   *
   * @return an iterator of type typeOfT which can be used to load an index
   * @throws IOException if there is a problem loading from a file
   */
  public abstract Iterator<T> getIterator(String filePath, Type typeOfT)
      throws IOException;
}
