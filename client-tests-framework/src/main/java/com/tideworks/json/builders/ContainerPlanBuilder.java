package com.tideworks.json.builders;

import com.tideworks.json.objectmodel.ContainerPlan;
import com.tideworks.json.objectmodel.IntermodalUnit;
import com.tideworks.json.objectmodel.Move;
import com.tideworks.json.objectmodel.Segment;
import com.tideworks.json.objectmodel.VesselOperation;

import java.util.function.Function;

/** Builder for container plan model. */
public class ContainerPlanBuilder {
  final ContainerPlan movePlan = new ContainerPlan();

  private ContainerPlanBuilder() {}

  /**
   * Creates builder.
   *
   * @return Builder instance.
   */
  public static ContainerPlanBuilder create() {
    return new ContainerPlanBuilder();
  }

  /**
   * Sets move.
   *
   * @param moveBuilderFunction Move builder.
   * @return ContainerPlanBuilder
   */
  public ContainerPlanBuilder withMove(Function<MoveBuilder, MoveBuilder> moveBuilderFunction) {
    return withMove(moveBuilderFunction.apply(MoveBuilder.create()).build());
  }

  /**
   * Sets move.
   *
   * @param move Move to set.
   * @return ContainerPlanBuilder
   */
  public ContainerPlanBuilder withMove(Move move) {
    movePlan.move = move;
    return this;
  }

  /**
   * Sets container.
   *
   * @param containerBuilderFunction Container builder.
   * @return ContainerPlanBuilder
   */
  public ContainerPlanBuilder withContainer(
      Function<ContainerBuilder, ContainerBuilder> containerBuilderFunction) {
    return withContainer(containerBuilderFunction.apply(ContainerBuilder.create()).build());
  }

  /**
   * Sets container.
   *
   * @param container Container to set.
   * @return ContainerPlanBuilder
   */
  public ContainerPlanBuilder withContainer(IntermodalUnit container) {
    movePlan.intermodalUnit = container;
    return this;
  }

  /**
   * Sets segment.
   *
   * @param segmentBuilderFunction Segment builder.
   * @return ContainerPlanBuilder
   */
  public ContainerPlanBuilder withSegment(
      Function<SegmentBuilder, SegmentBuilder> segmentBuilderFunction) {
    return withSegment(segmentBuilderFunction.apply(SegmentBuilder.create()).build());
  }

  /**
   * Sets segment.
   *
   * @param segment Segment to set.
   * @return ContainerPlanBuilder
   */
  public ContainerPlanBuilder withSegment(Segment segment) {
    movePlan.segments.add(segment);

    return this;
  }

  /**
   * Sets vessel operation.
   *
   * @param operation Operation to set.
   * @return ContainerPlanBuilder
   */
  public ContainerPlanBuilder withOperation(VesselOperation operation) {
    movePlan.operation = operation;

    return this;
  }

  /**
   * Builds container.
   *
   * @return Container instance.
   */
  public ContainerPlan build() {
    movePlan.segments.forEach(
        segment -> {
          segment.moveId = movePlan.move.id;
          segment.intermodalUnitId = movePlan.intermodalUnit.id;
        });
    movePlan.move.intermodalUnitId = movePlan.intermodalUnit.id;

    return movePlan;
  }

  /**
   * Sets chassis.
   *
   * @param associatedChassisId Chassis id to set.
   * @return ContainerPlanBuilder
   */
  public ContainerPlanBuilder withChassis(final String associatedChassisId) {
    movePlan.chassisId = associatedChassisId;

    return this;
  }
}
