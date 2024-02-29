package com.tideworks.json.parser;

import com.tideworks.json.objectmodel.Profile;
import com.tideworks.json.objectmodel.locations.Location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;

/** Base class for json parsers. */
public abstract class JsonParserBase {

  Gson parser;

  JsonParserBase() {
    GsonBuilder builder =
        new GsonBuilder()
            .registerTypeAdapter(Profile.class, new ProfileAdapter<>())
            .registerTypeAdapter(Location.class, new LocationAdapter<>())
            .setPrettyPrinting();
    parser = builder.create();
  }

  /**
   * serialize object model into json file.
   *
   * @param serializeObject object to serialize.
   * @param path full file path with name.
   * @param <T> object type.
   * @throws IOException exception while file writing.
   */
  public <T> void serialize(T serializeObject, String path) throws IOException {
    String jsonFile = parser.toJson(serializeObject);
    try (FileWriter fileWriter = new FileWriter(path, false)) {
      fileWriter.write(jsonFile);
      fileWriter.flush();
    }
  }

  /**
   * serialize object model into json file.
   *
   * @param serializeObject object to serialize.
   * @param <T> object type.
   * @return Json representation of serializeObject.
   */
  public <T> String serialize(T serializeObject) {
    return parser.toJson(serializeObject);
  }
}
