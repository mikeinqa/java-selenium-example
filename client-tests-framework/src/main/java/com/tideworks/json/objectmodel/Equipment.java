package com.tideworks.json.objectmodel;

import java.util.Objects;

/** Equipment model. */
public class Equipment {

  public String id;
  public EquipmentLocationKind kind;
  public String equipmentNo;
  public boolean held;
  public boolean enabled;

  public Equipment(
      final String id,
      final EquipmentLocationKind kind,
      final String equipmentNo,
      final boolean held,
      final boolean enabled) {
    this.id = id;
    this.kind = kind;
    this.equipmentNo = equipmentNo;
    this.held = held;
    this.enabled = enabled;
  }

  public Equipment() {}

  @Override
  public boolean equals(final Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof Equipment)) {
      return false;
    }
    final Equipment equipment = (Equipment) other;
    return held == equipment.held
        && enabled == equipment.enabled
        && Objects.equals(id, equipment.id)
        && kind == equipment.kind
        && Objects.equals(equipmentNo, equipment.equipmentNo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, kind, equipmentNo, held, enabled);
  }
}
