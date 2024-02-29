package com.tideworks.utilities.css;

/** Provided css strings with property. */
public enum CssProperties {
  Background("background-color"),
  Outline("outline"),
  Border("border");

  private final String propertyString;

  CssProperties(String property) {
    propertyString = property;
  }

  /**
   * Get current property as String.
   *
   * @return String representation scc properties name
   */
  public String toString() {
    return this.propertyString;
  }
}
