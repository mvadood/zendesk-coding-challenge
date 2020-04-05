package com.zendesk.util.file;

import com.google.gson.stream.JsonReader;
import com.zendesk.util.GsonProvider;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Class responsible for loading data from json files
 */
@Component
@Scope("prototype")
public class JsonFileReader<T> extends FileLoader<T> {

  private final GsonProvider gsonProvider;

  @Autowired
  public JsonFileReader(GsonProvider gsonProvider) {
    this.gsonProvider = gsonProvider;
  }

  /**
   * Loads content of a json file into an iterator of objects of type `typeOfT`
   *
   * @param filePath path to the json file
   * @param typeOfT type of object the file content should be translated into an iterator of
   * @return an iterator of type typeOfT which can be used to load an index
   * @throws IOException if there is a problem loading from a file
   */
  @Override
  public Iterator<T> getIterator(String filePath, Type typeOfT)
      throws IOException {
    InputStream inputStream = new FileInputStream(filePath);
    JsonReader reader = new JsonReader(new InputStreamReader(inputStream,
        StandardCharsets.UTF_8));

    reader.beginArray();

    return new Iterator<T>() {
      @SneakyThrows
      @Override
      public boolean hasNext() {
        if (reader.hasNext()) {
          return true;
        } else {
          reader.endArray();
          reader.close();
          inputStream.close();
          return false;
        }
      }

      @Override
      public T next() {
        return gsonProvider.getGson().fromJson(reader, typeOfT);
      }
    };

  }
}


