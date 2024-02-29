package com.tideworks.utilities.css;

/** Provided css strings with rgb colors. */
public enum CssRgbColors {
  Blue("rgb(0, 0, 255)"),
  Orange("rgb(255, 165, 0)"),
  Black("rgb(0, 0, 0)");

  private final String color;

  CssRgbColors(String color) {
    this.color = color;
  }

  /**
   * Get current color as String.
   *
   * @return String representation rgb color
   */
  public String toString() {
    return this.color;
  }
}
