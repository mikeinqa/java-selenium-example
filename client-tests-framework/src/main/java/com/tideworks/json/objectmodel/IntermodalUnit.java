package com.tideworks.json.objectmodel;

import com.tideworks.json.objectmodel.locations.Location;

import java.util.List;
import java.util.Objects;

/** Container model. */
public class IntermodalUnit {

  public String id;
  public ContainerKind kind;
  public Location location;
  public String sizeType;
  public String associatedUnitId;
  public String sortCode;
  public String groupCode;
  public Segment segment;
  public Move move;
  public String weight;
  public List<Segment> segments;
  public List<Move> moves;

  public IntermodalUnit(
      final String id,
      final ContainerKind kind,
      final Location location,
      final String sizeType,
      final String associatedUnitId,
      final String sortCode,
      final String groupCode,
      final Segment segment,
      final Move move,
      final String weight,
      final List<Move> moves,
      final List<Segment> segments) {
    this.id = id;
    this.kind = kind;
    this.location = location;
    this.sizeType = sizeType;
    this.associatedUnitId = associatedUnitId;
    this.sortCode = sortCode;
    this.groupCode = groupCode;
    this.segment = segment;
    this.move = move;
    this.weight = weight;
    this.moves = moves;
    this.segments = segments;
  }

  public IntermodalUnit(IntermodalUnit other) {
    this(
        other.id,
        other.kind,
        other.location,
        other.sizeType,
        other.associatedUnitId,
        other.sortCode,
        other.groupCode,
        other.segment,
        other.move,
        other.weight,
        other.moves,
        other.segments);
  }

  public IntermodalUnit() {}

  @Override
  public boolean equals(final Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof IntermodalUnit)) {
      return false;
    }
    final IntermodalUnit container = (IntermodalUnit) other;
    return Objects.equals(id, container.id)
        && kind == container.kind
        && Objects.equals(location, container.location)
        && Objects.equals(sizeType, container.sizeType)
        && Objects.equals(associatedUnitId, container.associatedUnitId)
        && Objects.equals(sortCode, container.sortCode)
        && Objects.equals(groupCode, container.groupCode)
        && Objects.equals(segment, container.segment)
        && Objects.equals(move, container.move)
        && Objects.equals(weight, container.weight)
        && Objects.equals(segments, container.segment)
        && Objects.equals(moves, container.moves);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        id,
        kind,
        location,
        sizeType,
        associatedUnitId,
        sortCode,
        groupCode,
        segment,
        move,
        weight,
        segments,
        moves);
  }
}
