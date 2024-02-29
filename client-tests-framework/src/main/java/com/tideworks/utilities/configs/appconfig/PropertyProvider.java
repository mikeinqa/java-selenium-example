package com.tideworks.utilities.configs.appconfig;

import org.testng.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Framework properties provider. */
public class PropertyProvider {

  private static final String PROPERTY_FILE_NAME = "config.properties";
  private static final Properties PROPERTIES = new Properties();
  private static PropertyProvider instance;
  private static boolean isLoaded;

  static {
    load();
  }

  private PropertyProvider() {
    final InputStream inputStream =
        getClass().getClassLoader().getResourceAsStream(PROPERTY_FILE_NAME);
    try {
      PROPERTIES.load(inputStream);
      isLoaded = true;
    } catch (IOException exc) {
      exc.printStackTrace();
      Assert.fail(exc.getMessage());
    }
  }

  /** Loads all properties from configuration file. */
  public static void load() {
    if (instance == null) {
      instance = new PropertyProvider();
    }
  }

  public static Boolean isLoaded() {
    return isLoaded;
  }

  /**
   * Gets property value.
   *
   * @param propertyName Property value name.
   * @return Value of given property.
   */
  public static String getValue(String propertyName) {
    return PROPERTIES.getProperty(propertyName);
  }
}
