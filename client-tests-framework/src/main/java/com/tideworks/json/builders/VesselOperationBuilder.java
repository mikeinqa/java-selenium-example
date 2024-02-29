package com.tideworks.json.builders;

import com.tideworks.json.objectmodel.VesselOperation;

import java.util.Date;

/** Builder for vessel operation model. */
public final class VesselOperationBuilder {

  private final VesselOperation vesselOperation;

  private VesselOperationBuilder() {
    vesselOperation = new VesselOperation();
  }

  /**
   * Creates builder.
   *
   * @return Builder instance.
   */
  public static VesselOperationBuilder create() {
    return new VesselOperationBuilder();
  }

  /**
   * Sets vessel operation id.
   *
   * @param id Id to set.
   * @return VesselOperationBuilder
   */
  public VesselOperationBuilder withId(String id) {
    vesselOperation.id = id;
    return this;
  }

  /**
   * Sets vessel.
   *
   * @param vessel Vessel to set.
   * @return VesselOperationBuilder
   */
  public VesselOperationBuilder withVessel(String vessel) {
    vesselOperation.vessel = vessel;
    return this;
  }

  /**
   * Sets vessel visit.
   *
   * @param vesselVisit Vessel visit to set.
   * @return VesselOperationBuilder
   */
  public VesselOperationBuilder withVesselVisit(String vesselVisit) {
    vesselOperation.vesselVisit = vesselVisit;
    return this;
  }

  /**
   * Sets crane.
   *
   * @param crane Crane to set.
   * @return VesselOperationBuilder
   */
  public VesselOperationBuilder withCrane(String crane) {
    vesselOperation.crane = crane;
    return this;
  }

  /**
   * Sets activation status.
   *
   * @param isActive Status to set.
   * @return VesselOperationBuilder
   */
  public VesselOperationBuilder withIsActive(boolean isActive) {
    vesselOperation.isActive = isActive;
    return this;
  }

  /**
   * Sets gang.
   *
   * @param gang Gang to set.
   * @return VesselOperationBuilder
   */
  public VesselOperationBuilder withGang(String gang) {
    vesselOperation.gang = gang;
    return this;
  }

  /**
   * Sets shift.
   *
   * @param shift Shift to set.
   * @return VesselOperationBuilder
   */
  public VesselOperationBuilder withShift(String shift) {
    vesselOperation.shift = shift;
    return this;
  }

  /**
   * Sets date time.
   *
   * @param dateTime Date time to set.
   * @return VesselOperationBuilder
   */
  public VesselOperationBuilder withDateTime(Date dateTime) {
    vesselOperation.dateTime = dateTime;
    return this;
  }

  /**
   * Sets assigned equipment.
   *
   * @param assignedEquipment Assigned equipments to set.
   * @return VesselOperationBuilder
   */
  public VesselOperationBuilder withAssignedEquipment(String[] assignedEquipment) {
    vesselOperation.assignedEquipment = assignedEquipment;
    return this;
  }

  /**
   * Builds move.
   *
   * @return Move instance.
   */
  public VesselOperation build() {
    return vesselOperation;
  }
}
