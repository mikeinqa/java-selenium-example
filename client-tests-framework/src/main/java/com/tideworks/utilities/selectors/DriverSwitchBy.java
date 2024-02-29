package com.tideworks.utilities.selectors;

import com.tideworks.utilities.controllers.enviroment.EnvironmentController;
import com.tideworks.utilities.services.driver.DriverTypes;

import io.appium.java_client.MobileBy.ByAccessibilityId;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;

/** Selector for cases, when needs to define different approach of searching elements at page. */
public class DriverSwitchBy extends By {
  private static DriverTypes driverTypes;
  private static String UNSUPPORTED_DRIVER_TYPE = "Unsupported driver type.";

  static {
    driverTypes = EnvironmentController.getDriverType();
  }

  /**
   * Creates by id selector, depends on target test platform.
   *
   * @param id Element's id.
   * @return By id selector.
   */
  public static By id(String id) {
    switch (driverTypes) {
      case Windows:
        return new ByAccessibilityId(id);
      case Chrome:
        return By.id(id);
      default:
        throw new UnsupportedOperationException(UNSUPPORTED_DRIVER_TYPE);
    }
  }

  /**
   * Creates by xpath selector, depends on target test platform.
   *
   * @param byChromeXPath Xpath for web case.
   * @param byWindowsXpath Xpath for windows case.
   * @return By xpath selector.
   */
  public static ByXPath xpath(String byChromeXPath, String byWindowsXpath) {
    switch (driverTypes) {
      case Windows:
        return new ByXPath(byWindowsXpath);
      case Chrome:
        return new ByXPath(byChromeXPath);
      default:
        throw new UnsupportedOperationException(UNSUPPORTED_DRIVER_TYPE);
    }
  }

  @Override
  public List<WebElement> findElements(final SearchContext context) {
    return null;
  }
}
