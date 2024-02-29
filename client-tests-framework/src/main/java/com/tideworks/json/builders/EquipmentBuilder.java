package com.tideworks.json.builders;

import com.tideworks.json.objectmodel.Equipment;
import com.tideworks.json.objectmodel.EquipmentLocationKind;

/** Builder for equipment model. */
public final class EquipmentBuilder {

  private static final String UTR_NAME_PATTERN = "UTR";
  private static int identification = 0;
  private final Equipment equipment;

  private EquipmentBuilder() {
    equipment = new Equipment();
    equipment.id = UTR_NAME_PATTERN + String.valueOf(identification++);
  }

  /**
   * Creates builder.
   *
   * @return Builder instance.
   */
  public static EquipmentBuilder create() {
    return new EquipmentBuilder();
  }

  /**
   * Sets id for equipment.
   *
   * @param id Id to set
   * @return EquipmentBuilder
   */
  public EquipmentBuilder withId(String id) {
    equipment.id = id;
    return this;
  }

  /**
   * Sets kind of equipment.
   *
   * @param kind Kind ot set.
   * @return EquipmentBuilder
   */
  public EquipmentBuilder withKind(EquipmentLocationKind kind) {
    equipment.kind = kind;
    return this;
  }

  /**
   * Equipment number to set.
   *
   * @param equipmentNo Equipment number to set.
   * @return EquipmentBuilder
   */
  public EquipmentBuilder withEquipmentNo(String equipmentNo) {
    equipment.equipmentNo = equipmentNo;
    return this;
  }

  /**
   * Sets held of equipment.
   *
   * @param held Held status to set.
   * @return EquipmentBuilder
   */
  public EquipmentBuilder withHeld(boolean held) {
    equipment.held = held;
    return this;
  }

  /**
   * Sets activation status of equipment.
   *
   * @param enabled Status to set.
   * @return EquipmentBuilder
   */
  public EquipmentBuilder withEnabled(boolean enabled) {
    equipment.enabled = enabled;
    return this;
  }

  /**
   * Builds equipment.
   *
   * @return Equipment instance.
   */
  public Equipment build() {
    return equipment;
  }
}
