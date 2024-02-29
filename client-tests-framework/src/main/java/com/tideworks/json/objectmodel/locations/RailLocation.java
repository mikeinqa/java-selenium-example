package com.tideworks.json.objectmodel.locations;

import java.util.Objects;

/** Rail Location model. */
public class RailLocation extends Location {
  public String track;
  public String railcar;
  public String well;
  public RailTier tier;
  public String stack;
  public Boolean isConvention;

  public RailLocation() {
    super.type = LocationType.rail;
  }

  @Override
  public String toString() {
    return String.format(
            "%s %s %s %s",
            railcar,
            Objects.toString(well, ""),
            Objects.toString(tier, ""),
            Objects.toString(stack, ""))
        .trim();
  }

  /** Rail tiers. */
  public enum RailTier {
    T,
    B
  }
}
