package com.tideworks.json.objectmodel.locations;

import com.tideworks.json.objectmodel.EquipmentLocationKind;

/** Equipment Location model. */
public class EquipmentLocation extends Location {

  public EquipmentLocationKind kind;
  public String equipmentId;

  public EquipmentLocation() {
    type = LocationType.equipment;
  }

  public EquipmentLocation(final EquipmentLocationKind kind, final String equipmentId) {
    this();
    this.kind = kind;
    this.equipmentId = equipmentId;
  }

  @Override
  public String toString() {
    return equipmentId;
  }
}
