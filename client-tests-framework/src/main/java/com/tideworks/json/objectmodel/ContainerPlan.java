package com.tideworks.json.objectmodel;

import java.util.ArrayList;
import java.util.List;

/** Container's plan model. */
public class ContainerPlan {

  public Move move;
  public List<Segment> segments = new ArrayList<>();
  public IntermodalUnit intermodalUnit;
  public VesselOperation operation;
  public String chassisId;

  public ContainerPlan(
      final Move move,
      final List<Segment> segments,
      final IntermodalUnit container,
      final VesselOperation operation,
      final String chassisId) {
    this.move = move;
    this.segments = segments;
    this.intermodalUnit = container;
    this.operation = operation;
    this.chassisId = chassisId;
  }

  public ContainerPlan() {}

  public Segment getFromSegment() {
    return segments.stream().filter(s -> s.from == move.from).findFirst().get();
  }

  public String getDigitsFromContainerNumber() {
    return intermodalUnit.id.substring(4);
  }
}
