package com.tideworks.json.builders;

import com.tideworks.json.objectmodel.Zone;
import com.tideworks.json.objectmodel.ZoneKind;

import java.util.List;

/** Builder for move model. */
public final class ZoneBuilder {

  private static final String ZONE_NAME_PATTERN = "ZONE";
  private static int identification = 0;
  private final Zone zone;

  private ZoneBuilder() {
    zone = new Zone();
    zone.id = ZONE_NAME_PATTERN + String.valueOf(identification++);
  }

  /**
   * Creates builder.
   *
   * @return Builder instance.
   */
  public static ZoneBuilder create() {
    return new ZoneBuilder();
  }

  /**
   * Sets zone id.
   *
   * @param id Id to set.
   * @return ZoneBuilder
   */
  public ZoneBuilder withId(String id) {
    zone.id = id;
    return this;
  }

  /**
   * Sets zone kind.
   *
   * @param kind Kind to set.
   * @return ZoneBuilder
   */
  public ZoneBuilder withKind(ZoneKind kind) {
    zone.kind = kind;
    return this;
  }

  /**
   * Sets equipment count.
   *
   * @param equipmentCount Count to set.
   * @return ZoneBuilder
   */
  public ZoneBuilder withEquipmentCount(int equipmentCount) {
    zone.equipmentCount = equipmentCount;
    return this;
  }

  /**
   * Sets yard move count.
   *
   * @param yardMoveCount Count to set.
   * @return ZoneBuilder
   */
  public ZoneBuilder withYardMoveCount(int yardMoveCount) {
    zone.yardMoveCount = yardMoveCount;
    return this;
  }

  /**
   * Sets unload move count.
   *
   * @param unloadMoveCount Count to set.
   * @return ZoneBuilder
   */
  public ZoneBuilder withUnloadMoveCount(int unloadMoveCount) {
    zone.unloadMoveCount = unloadMoveCount;
    return this;
  }

  /**
   * Sets load move count.
   *
   * @param loadMoveCount Count to set.
   * @return ZoneBuilder
   */
  public ZoneBuilder withLoadMoveCount(int loadMoveCount) {
    zone.loadMoveCount = loadMoveCount;
    return this;
  }

  /**
   * Sets stow move count.
   *
   * @param stowMoveCount Count to set.
   * @return ZoneBuilder
   */
  public ZoneBuilder withStowMoveCount(int stowMoveCount) {
    zone.stowMoveCount = stowMoveCount;
    return this;
  }

  /**
   * Sets discharge move count.
   *
   * @param dischargeMoveCount Count to set.
   * @return ZoneBuilder
   */
  public ZoneBuilder withDischargeMoveCount(int dischargeMoveCount) {
    zone.dischargeMoveCount = dischargeMoveCount;
    return this;
  }

  /**
   * Sets self assign.
   *
   * @param selfAssign Assign status to set.
   * @return ZoneBuilder
   */
  public ZoneBuilder withSelfAssign(boolean selfAssign) {
    zone.selfAssign = selfAssign;
    return this;
  }

  /**
   * Sets split work list.
   *
   * @param splitWorklist Worklist status to set.
   * @return ZoneBuilder
   */
  public ZoneBuilder withSplitWorklist(boolean splitWorklist) {
    zone.splitWorklist = splitWorklist;
    return this;
  }

  /**
   * Sets assigned equipments.
   *
   * @param assignedEquipment equipments to set.
   * @return ZoneBuilder
   */
  public ZoneBuilder withAssignedEquipment(List<String> assignedEquipment) {
    zone.assignedEquipment = assignedEquipment;
    return this;
  }

  /**
   * Builds zone.
   *
   * @return Zone instance.
   */
  public Zone build() {
    return zone;
  }
}
