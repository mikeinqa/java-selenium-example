package com.tideworks.json.objectmodel;

import com.tideworks.json.objectmodel.locations.Location;

import java.util.List;

/** Move model. */
public class Move {

  public String id;
  public String intermodalUnitId;
  public Location from;
  public Location to;
  public MoveStatus status;
  public String goal;
  public Boolean escalated;
  public Integer priority;
  public Integer sequence;
  public String associatedChassisId;
  public String twinMoveId;
  public List<String> zoneIds;
  public Boolean systemMove;
  public String activationTime;
  public VesselOperation operation;
  public String operationId;

  public Move() {}

  public Move(
      final String id, final String intermodalUnitId, final Location from, final Location to) {
    this.id = id;
    this.intermodalUnitId = intermodalUnitId;
    this.from = from;
    this.to = to;
  }

  public Move(
      final String id,
      final String intermodalUnitId,
      final Location from,
      final Location to,
      final MoveStatus status,
      final String goal,
      final Boolean escalated,
      final int priority,
      final int sequence,
      final String associatedChassisId,
      final String twinMoveId,
      final List<String> zoneIds,
      final Boolean systemMove,
      final VesselOperation operation,
      final String operationId,
      final String activationTime) {
    this.id = id;
    this.intermodalUnitId = intermodalUnitId;
    this.from = from;
    this.to = to;
    this.status = status;
    this.goal = goal;
    this.escalated = escalated;
    this.priority = priority;
    this.sequence = sequence;
    this.associatedChassisId = associatedChassisId;
    this.twinMoveId = twinMoveId;
    this.zoneIds = zoneIds;
    this.systemMove = systemMove;
    this.activationTime = activationTime;
    this.operation = operation;
    this.operationId = operationId;
  }
}
