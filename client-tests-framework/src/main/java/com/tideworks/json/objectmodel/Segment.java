package com.tideworks.json.objectmodel;

import com.tideworks.json.objectmodel.locations.Location;

import java.util.List;

/** Segment model. */
public class Segment {

  public String id;
  public String moveId;
  public String intermodalUnitId;
  public SegmentKind kind;
  public Location from;
  public Location to;
  public MoveSegmentStatus status;
  public String activationTime;
  public List<String> assignedEquipment;
  public String selectedEquipment;
  public IntermodalUnit intermodalUnit;
  public Move move;
  public int sequence;

  public Segment() {}

  public Segment(
      final String id,
      final String moveId,
      final String intermodalUnitId,
      final SegmentKind kind,
      final Location from,
      final Location to,
      final MoveSegmentStatus status,
      final String activationTime,
      final List<String> assignedEquipment,
      final String selectedEquipment,
      final IntermodalUnit intermodalUnit,
      final Move move,
      final int sequence) {
    this.id = id;
    this.moveId = moveId;
    this.intermodalUnitId = intermodalUnitId;
    this.kind = kind;
    this.from = from;
    this.to = to;
    this.status = status;
    this.activationTime = activationTime;
    this.assignedEquipment = assignedEquipment;
    this.selectedEquipment = selectedEquipment;
    this.intermodalUnit = intermodalUnit;
    this.move = move;
    this.sequence = sequence;
  }

  public Segment(final Segment segment) {
    this(
        segment.id,
        segment.moveId,
        segment.intermodalUnitId,
        segment.kind,
        segment.from,
        segment.to,
        segment.status,
        segment.activationTime,
        segment.assignedEquipment,
        segment.selectedEquipment,
        segment.intermodalUnit,
        segment.move,
        segment.sequence);
  }
}
