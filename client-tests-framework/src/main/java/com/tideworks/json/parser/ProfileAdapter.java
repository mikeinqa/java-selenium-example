package com.tideworks.json.parser;

import com.tideworks.json.objectmodel.Profile;
import com.tideworks.json.objectmodel.ProfileType;

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
 * Adapter for profiles.
 *
 * @param <T> Type of profile.
 */
public class ProfileAdapter<T extends Profile> implements JsonDeserializer<T>, JsonSerializer<T> {

  private static final String CLASS_NAME = "type";
  private static final String PACKAGE_NAME = "framework.json.objectmodel.";

  @Override
  public T deserialize(
      final JsonElement jsonElement,
      final Type type,
      final JsonDeserializationContext deserializationContext)
      throws JsonParseException {

    final JsonObject jsonObject = jsonElement.getAsJsonObject();
    final JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASS_NAME);
    final ProfileType profileType = ProfileType.valueOf(prim.getAsString().toLowerCase());
    final String className = PACKAGE_NAME + profileType.toString(profileType);
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
