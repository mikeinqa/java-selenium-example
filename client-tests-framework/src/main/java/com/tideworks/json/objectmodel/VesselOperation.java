package com.tideworks.json.objectmodel;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/** VesselOperation model. */
public class VesselOperation {

  public String id;
  public String vessel;
  public String vesselVisit;
  public String crane;
  public boolean isActive;
  public String gang;
  public String shift;
  public Date dateTime;
  public String[] assignedEquipment;

  public VesselOperation() {}

  public VesselOperation(
      final String id,
      final String vessel,
      final String vesselVisit,
      final String crane,
      final boolean isActive,
      final String gang,
      final String shift,
      final Date dateTime,
      final String[] assignedEquipment) {
    this.id = id;
    this.vessel = vessel;
    this.vesselVisit = vesselVisit;
    this.crane = crane;
    this.isActive = isActive;
    this.gang = gang;
    this.shift = shift;
    this.dateTime = dateTime;
    this.assignedEquipment = assignedEquipment;
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof VesselOperation)) {
      return false;
    }
    final VesselOperation that = (VesselOperation) other;
    return isActive == that.isActive
        && Objects.equals(id, that.id)
        && Objects.equals(vessel, that.vessel)
        && Objects.equals(vesselVisit, that.vesselVisit)
        && Objects.equals(crane, that.crane)
        && Objects.equals(gang, that.gang)
        && Objects.equals(shift, that.shift)
        && Objects.equals(dateTime, that.dateTime)
        && Arrays.equals(assignedEquipment, that.assignedEquipment);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(id, vessel, vesselVisit, crane, isActive, gang, shift, dateTime);
    result = 31 * result + Arrays.hashCode(assignedEquipment);
    return result;
  }
}
