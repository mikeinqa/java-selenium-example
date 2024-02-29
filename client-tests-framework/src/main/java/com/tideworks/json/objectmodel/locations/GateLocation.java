package com.tideworks.json.objectmodel.locations;

/** Gate Location model. */
public class GateLocation extends Location {

  public String driverLicense;
  public String truckLicense;
  public String placard;

  public GateLocation() {
    super.type = LocationType.gate;
  }

  @Override
  public String toString() {
    return ((this.truckLicense == null || this.truckLicense.isEmpty())
        ? "TRUCK"
        : this.truckLicense);
  }
}
