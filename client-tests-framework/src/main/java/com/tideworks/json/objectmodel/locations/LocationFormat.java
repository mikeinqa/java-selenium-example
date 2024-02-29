package com.tideworks.json.objectmodel.locations;

/** LocationFormat model. */
public class LocationFormat {

  public LocationType locationType;
  public String locationFormat;

  public LocationFormat(final LocationType locationType, final String locationFormat) {
    this.locationType = locationType;
    this.locationFormat = locationFormat;
  }

  public LocationFormat() {}
}
