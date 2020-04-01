package com.zendesk.util.file;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Iterator;

public abstract class FileLoader<T> {

  public abstract Iterator<T> getIterator(String filePath, Type typeOfT)
      throws IOException;
}
