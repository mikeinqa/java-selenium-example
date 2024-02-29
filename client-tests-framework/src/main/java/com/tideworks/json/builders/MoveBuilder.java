package com.tideworks.json.builders;

import com.tideworks.json.objectmodel.Move;
import com.tideworks.json.objectmodel.MoveStatus;
import com.tideworks.json.objectmodel.VesselOperation;
import com.tideworks.json.objectmodel.locations.Location;

import java.util.List;

/** Builder for move model. */
public class MoveBuilder {

  private static int identification = 0;
  private final Move move;

  private MoveBuilder() {
    move = new Move();
    move.id = String.valueOf(identification++);
  }

  /**
   * Creates builder.
   *
   * @return Builder instance.
   */
  public static MoveBuilder create() {
    return new MoveBuilder();
  }

  /**
   * Sets containerId.
   *
   * @param containerId ContainerId to set.
   * @return MoveBuilder
   */
  public MoveBuilder withContainerId(String containerId) {
    move.intermodalUnitId = containerId;
    return this;
  }

  /**
   * Sets from location.
   *
   * @param from From location to set.
   * @return MoveBuilder
   */
  public MoveBuilder withFrom(Location from) {
    move.from = from;
    return this;
  }

  /**
   * Sets to location.
   *
   * @param to To location
   * @return MoveBuilder
   */
  public MoveBuilder withTo(Location to) {
    move.to = to;
    return this;
  }

  /**
   * Sets move status.
   *
   * @param status Move status.
   * @return MoveBuilder
   */
  public MoveBuilder withStatus(MoveStatus status) {
    move.status = status;
    return this;
  }

  /**
   * Sets move goal.
   *
   * @param goal Move goal to set.
   * @return MoveBuilder
   */
  public MoveBuilder withGoal(String goal) {
    move.goal = goal;
    return this;
  }

  /**
   * Sets move escalation.
   *
   * @param escalated Move escalation.
   * @return MoveBuilder
   */
  public MoveBuilder withEscalated(Boolean escalated) {
    move.escalated = escalated;
    return this;
  }

  /**
   * Sets move priority.
   *
   * @param priority Move priority.
   * @return MoveBuilder
   */
  public MoveBuilder withPriority(int priority) {
    move.priority = priority;
    return this;
  }

  /**
   * Sets move sequence.
   *
   * @param sequence Move sequence.
   * @return MoveBuilder
   */
  public MoveBuilder withSequence(int sequence) {
    move.sequence = sequence;
    return this;
  }

  /**
   * Sets associated chassis id.
   *
   * @param associatedChassisId Associated chassis id
   * @return MoveBuilder
   */
  public MoveBuilder withAssociatedChassisId(String associatedChassisId) {
    move.associatedChassisId = associatedChassisId;
    return this;
  }

  /**
   * Sets twin move id.
   *
   * @param twinMoveId Twin move id.
   * @return MoveBuilder
   */
  public MoveBuilder withTwinMoveId(String twinMoveId) {
    move.twinMoveId = twinMoveId;
    return this;
  }

  /**
   * Sets zone ids for move.
   *
   * @param zoneIds Zone ids to set
   * @return MoveBuilder
   */
  public MoveBuilder withZoneIds(List<String> zoneIds) {
    move.zoneIds = zoneIds;
    return this;
  }

  /**
   * Sets vessel operation for move.
   *
   * @param operation Operation to set
   * @return MoveBuilder
   */
  public MoveBuilder withOperation(VesselOperation operation) {
    move.operation = operation;
    return this;
  }

  /**
   * Sets indicator for system move property.
   *
   * @param isMoveSystem Parameter to set.
   * @return MoveBuilder.
   */
  public MoveBuilder withSystemMove(Boolean isMoveSystem) {
    move.systemMove = isMoveSystem;
    return this;
  }

  /**
   * Builds move.
   *
   * @return Move instance.
   */
  public Move build() {
    return move;
  }
}
