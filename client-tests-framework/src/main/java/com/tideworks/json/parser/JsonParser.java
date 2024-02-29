package com.tideworks.json.parser;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/** Json parser for db.json. */
public class JsonParser extends JsonParserBase {
  /**
   * Deserialize json file data.
   *
   * @param path path to db.json file.
   * @param classType destination class type.
   * @param <T> destination type.
   * @return Filled DbJsonModel.
   * @throws IOException exception while reading.
   */
  public <T> T deserialize(String path, Class<T> classType) throws IOException {
    StringBuilder file = new StringBuilder();
    try (FileReader fileReader = new FileReader(path)) {
      try (Scanner scanner = new Scanner(fileReader)) {
        while (scanner.hasNextLine()) {
          file.append(scanner.nextLine());
        }
      }
    }

    return parser.fromJson(file.toString(), classType);
  }
}
