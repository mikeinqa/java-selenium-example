package com.tideworks.json.parser;

import com.tideworks.json.objectmodel.locations.Location;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Adapter for locations.
 *
 * @param <T> Type of location.
 */
public class LocationAdapter<T extends Location> implements JsonDeserializer<T>, JsonSerializer<T> {

  private static final String CLASS_NAME = "type";
  private static final String INTERFACE_TUG = "Location";
  private static final String PACKAGE_NAME = "framework.json.objectmodel.";

  @Override
  public T deserialize(
      final JsonElement jsonElement,
      final Type type,
      final JsonDeserializationContext deserializationContext)
      throws JsonParseException {

    final JsonObject jsonObject = jsonElement.getAsJsonObject();
    final JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASS_NAME);
    final String classNameString = prim.getAsString();
    final String className =
        PACKAGE_NAME
            + classNameString.substring(0, 1).toUpperCase()
            + classNameString.substring(1)
            + INTERFACE_TUG;
    final Class classInstance = getClassInstance(className);
    return deserializationContext.deserialize(jsonObject, classInstance);
  }

  private Class getClassInstance(String className) {
    try {
      return Class.forName(className);
    } catch (ClassNotFoundException classNotFoundException) {
      throw new JsonParseException(classNotFoundException.getMessage());
    }
  }

  @Override
  public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
    return context.serialize(src);
  }
}
