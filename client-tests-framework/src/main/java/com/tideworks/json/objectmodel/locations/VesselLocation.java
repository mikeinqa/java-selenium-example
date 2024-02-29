package com.tideworks.json.objectmodel.locations;

import java.util.Objects;

/** Vessel Location model. */
public class VesselLocation extends Location {

  public String vessel;
  public String bay;
  public String stack;
  public String tier;

  public VesselLocation() {
    super.type = LocationType.vessel;
  }

  @Override
  public String toString() {
    return String.format(
            "%s %s%s",
            Objects.toString(bay, ""), Objects.toString(stack, ""), Objects.toString(tier, ""))
        .trim();
  }
}
