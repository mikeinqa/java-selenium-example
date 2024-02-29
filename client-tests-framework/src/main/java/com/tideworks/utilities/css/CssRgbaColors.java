package com.tideworks.utilities.css;

/** Provided css strings with rgba colors. */
public enum CssRgbaColors {
  White("rgba(255, 255, 255, 1)"),
  Aqua("rgba(0, 255, 255, 1)"),
  Yellow("rgba(255, 255, 0, 1)"),
  Red("rgba(255, 0, 0, 1)"),
  Green("rgba(203, 255, 144, 1)"),
  LightGray("rgba(128, 128, 128, 1)"),
  DarkGray("rgba(211, 211, 211, 1)");

  private final String color;

  CssRgbaColors(String color) {
    this.color = color;
  }

  /**
   * Get current color as String.
   *
   * @return String representation rgba color
   */
  public String toString() {
    return this.color;
  }
}
