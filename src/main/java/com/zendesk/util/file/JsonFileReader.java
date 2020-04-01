package com.zendesk.util.file;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.zendesk.model.User;
import com.zendesk.util.GsonProvider;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import lombok.SneakyThrows;

public class JsonFileReader<T> extends FileLoader<T> {

  private Gson gson;

  public JsonFileReader() {
    this.gson = GsonProvider.getInstance();
  }

  @Override
  public Iterator getObjectIterator(String filePath, Type typeOfT)
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
        return gson.fromJson(reader, typeOfT);
      }
    };

  }
}


