package com.tideworks.json.objectmodel;

/** ProfileType model. */
public enum ProfileType {
  che,
  microutr,
  vesselclerk;

  /**
   * get camel case profile type.
   *
   * @param className type isn't camel case
   * @return camel case type
   */
  public String toString(ProfileType className) {
    if (className == che) {
      return "Che";
    } else if (className == microutr) {
      return "MicroUtr";
    } else {
      return "VesselClerk";
    }
  }
}
