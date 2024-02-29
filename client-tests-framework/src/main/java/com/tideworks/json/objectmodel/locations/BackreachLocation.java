package com.tideworks.json.objectmodel.locations;

/** Backreach Location model. */
public class BackreachLocation extends Location {

  public String crane;

  public BackreachLocation() {
    this.type = LocationType.backreach;
  }
}
