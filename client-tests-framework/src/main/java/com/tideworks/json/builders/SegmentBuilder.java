package com.tideworks.json.builders;

import com.tideworks.json.objectmodel.IntermodalUnit;
import com.tideworks.json.objectmodel.Move;
import com.tideworks.json.objectmodel.MoveSegmentStatus;
import com.tideworks.json.objectmodel.Segment;
import com.tideworks.json.objectmodel.SegmentKind;
import com.tideworks.json.objectmodel.locations.Location;

import java.util.List;

/** Builder for segment model. */
public class SegmentBuilder {

  private static int identification = 0;
  private final Segment segment;

  private SegmentBuilder() {
    segment = new Segment();
    segment.id = Integer.toString(identification++);
  }

  /**
   * Creates builder.
   *
   * @return Builder instance.
   */
  public static SegmentBuilder create() {
    return new SegmentBuilder();
  }

  /**
   * Sets move id.
   *
   * @param moveId Move id to set.
   * @return SegmentBuilder
   */
  public SegmentBuilder withMoveId(String moveId) {
    segment.moveId = moveId;
    return this;
  }

  /**
   * Sets container id.
   *
   * @param containerId Container id to set.
   * @return SegmentBuilder
   */
  public SegmentBuilder withContainerId(String containerId) {
    segment.intermodalUnitId = containerId;
    return this;
  }

  /**
   * Sets segment kind.
   *
   * @param kind Segment kind to set.
   * @return SegmentBuilder
   */
  public SegmentBuilder withKind(SegmentKind kind) {
    segment.kind = kind;
    return this;
  }

  /**
   * Sets segment from location.
   *
   * @param from From location to set.
   * @return SegmentBuilder
   */
  public SegmentBuilder withFrom(Location from) {
    segment.from = from;
    return this;
  }

  /**
   * Sets segment to location.
   *
   * @param to To location to set.
   * @return SegmentBuilder
   */
  public SegmentBuilder withTo(Location to) {
    segment.to = to;
    return this;
  }

  /**
   * Sets move status.
   *
   * @param status Status to set.
   * @return SegmentBuilder
   */
  public SegmentBuilder withStatus(MoveSegmentStatus status) {
    segment.status = status;
    return this;
  }

  /**
   * Sets activation time.
   *
   * @param activationTime Activation time to set.
   * @return SegmentBuilder
   */
  public SegmentBuilder withActivationTime(String activationTime) {
    segment.activationTime = activationTime;
    return this;
  }

  /**
   * Sets assigned equipments.
   *
   * @param assignedEquipment Equipments to set.
   * @return SegmentBuilder
   */
  public SegmentBuilder withAssignedEquipment(List<String> assignedEquipment) {
    segment.assignedEquipment = assignedEquipment;
    return this;
  }

  /**
   * Sets selected equipment.
   *
   * @param selectedEquipment Equipment to set.
   * @return SegmentBuilder
   */
  public SegmentBuilder withSelectedEquipment(String selectedEquipment) {
    segment.selectedEquipment = selectedEquipment;
    return this;
  }

  /**
   * Sets container and container's id.
   *
   * @param container Container to set
   * @return SegmentBuilder
   */
  public SegmentBuilder withContainer(IntermodalUnit container) {
    segment.intermodalUnitId = container.id;
    segment.intermodalUnit = container;
    return this;
  }

  /**
   * Sets move and move's id.
   *
   * @param move Move to set.
   * @return SegmentBuilder
   */
  public SegmentBuilder withMove(Move move) {
    segment.moveId = move.id;
    segment.move = move;
    return this;
  }

  /**
   * Sets sequence.
   *
   * @param sequence Sequence to set.
   * @return SegmentBuilder
   */
  public SegmentBuilder withSequence(int sequence) {
    segment.sequence = sequence;
    return this;
  }

  /**
   * Builds segment.
   *
   * @return Segment instance.
   */
  public Segment build() {
    return segment;
  }
}
