package com.tideworks.json.objectmodel;

/** Column names for grid. */
public enum ColumnNames {
  containerNo("Container Number"),
  departBy("Depart By"),
  destination("Destination"),
  from("From Location"),
  goal("Goal"),
  groupCode("Group Code"),
  hazard("Hazard"),
  lastFreeDay("Last Free Day"),
  origin("Origin"),
  plan("Plan"),
  shipper("Shipper"),
  sortCode("Sort Code"),
  status("Status"),
  size("Size/Type"),
  activationTime("Activation Time"),
  to("Next Location"),
  weight("Weight");

  private final String propertyString;

  ColumnNames(final String property) {
    propertyString = property;
  }

  @Override
  public String toString() {
    return this.propertyString;
  }
}
