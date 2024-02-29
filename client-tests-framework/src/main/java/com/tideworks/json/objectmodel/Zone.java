package com.tideworks.json.objectmodel;

import java.util.List;

/** Zone model. */
public class Zone {

  public String id;
  public ZoneKind kind;
  public int equipmentCount;
  public int yardMoveCount;
  public int unloadMoveCount;
  public int loadMoveCount;
  public int stowMoveCount;
  public int dischargeMoveCount;
  public boolean selfAssign;
  public boolean splitWorklist;
  public List<String> assignedEquipment;

  public Zone(
      final String id,
      final ZoneKind kind,
      final int equipmentCount,
      final int yardMoveCount,
      final int unloadMoveCount,
      final int loadMoveCount,
      final int stowMoveCount,
      final int dischargeMoveCount,
      final boolean selfAssign,
      final boolean splitWorklist,
      final List<String> assignedEquipment) {
    this.id = id;
    this.kind = kind;
    this.equipmentCount = equipmentCount;
    this.yardMoveCount = yardMoveCount;
    this.unloadMoveCount = unloadMoveCount;
    this.loadMoveCount = loadMoveCount;
    this.stowMoveCount = stowMoveCount;
    this.dischargeMoveCount = dischargeMoveCount;
    this.selfAssign = selfAssign;
    this.splitWorklist = splitWorklist;
    this.assignedEquipment = assignedEquipment;
  }

  public Zone() {}
}
