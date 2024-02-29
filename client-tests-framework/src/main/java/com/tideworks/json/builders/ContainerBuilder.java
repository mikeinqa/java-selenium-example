package com.tideworks.json.builders;

import com.tideworks.json.objectmodel.ContainerKind;
import com.tideworks.json.objectmodel.IntermodalUnit;
import com.tideworks.json.objectmodel.Move;
import com.tideworks.json.objectmodel.Segment;
import com.tideworks.json.objectmodel.locations.Location;

/** Builder for container model. */
public final class ContainerBuilder {

  private static final String CONTAINER_NAME_PATTERN = "CONT";
  private static int identification = 0;
  private IntermodalUnit container;

  private ContainerBuilder() {
    this(CONTAINER_NAME_PATTERN);
  }

  private ContainerBuilder(String namePattern) {
    container = new IntermodalUnit();
    container.id = namePattern + String.valueOf(identification++);
  }

  /** Reset identification counter to initial state. */
  public static void reset() {
    identification = 0;
  }

  /**
   * Creates builder.
   *
   * @return Builder instance.
   */
  public static ContainerBuilder create() {
    return new ContainerBuilder();
  }

  /**
   * Creates builder with custom container prefix.
   *
   * @param containerPrefix custom prefix for create container
   * @return Builder instance.
   */
  public static ContainerBuilder create(String containerPrefix) {
    return new ContainerBuilder(containerPrefix);
  }

  /**
   * Copies given container to builder with all fields.
   *
   * @param container Container to copy.
   * @return ContainerBuilder.
   */
  public ContainerBuilder withContainer(IntermodalUnit container) {
    this.container = new IntermodalUnit(container);

    return this;
  }

  /**
   * Sets container kind.
   *
   * @param kind ContainerKind to set.
   * @return ContainerBuilder.
   */
  public ContainerBuilder withKind(ContainerKind kind) {
    container.kind = kind;
    return this;
  }

  /**
   * Sets location.
   *
   * @param location Location to set.
   * @return ContainerBuilder
   */
  public ContainerBuilder withLocation(Location location) {
    container.location = location;
    return this;
  }

  /**
   * Sets sizeType.
   *
   * @param sizeType SizeType to set.
   * @return ContainerBuilder
   */
  public ContainerBuilder withSizeType(String sizeType) {
    container.sizeType = sizeType;
    return this;
  }

  /**
   * Sets associatedUnitId.
   *
   * @param associatedUnitId AssociatedUnitId to set.
   * @return ContainerBuilder
   */
  public ContainerBuilder withAssociatedUnitId(String associatedUnitId) {
    container.associatedUnitId = associatedUnitId;
    return this;
  }

  /**
   * Sets sortCode.
   *
   * @param sortCode SortCode to set.
   * @return ContainerBuilder
   */
  public ContainerBuilder withSortCode(String sortCode) {
    container.sortCode = sortCode;
    return this;
  }

  /**
   * Sets groupCode.
   *
   * @param groupCode Group code to set.
   * @return ContainerBuilder
   */
  public ContainerBuilder withGroupCode(String groupCode) {
    container.groupCode = groupCode;
    return this;
  }

  /**
   * Sets segment.
   *
   * @param segment Segment code to set.
   * @return ContainerBuilder
   */
  public ContainerBuilder withSegment(Segment segment) {
    container.segment = segment;
    return this;
  }

  /**
   * Sets move.
   *
   * @param move Move code to set.
   * @return ContainerBuilder
   */
  public ContainerBuilder withMove(Move move) {
    container.move = move;
    return this;
  }

  /**
   * Sets weight.
   *
   * @param weight Weight code to set.
   * @return ContainerBuilder
   */
  public ContainerBuilder withWeight(String weight) {
    container.weight = weight;
    return this;
  }

  /**
   * Sets id.
   *
   * @param id Container number to set.
   * @return ContainerBuilder
   */
  public ContainerBuilder withId(String id) {
    container.id = id;
    return this;
  }

  /**
   * Builds container.
   *
   * @return Container instance.
   */
  public IntermodalUnit build() {
    return container;
  }
}
