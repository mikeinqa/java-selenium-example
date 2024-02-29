package com.tideworks.json.objectmodel.locations;

import lombok.val;

import java.util.Objects;

/** Yard Location model. */
public class YardLocation extends Location {

  public String block;
  public String row;
  public String stack;
  public Integer tier;
  public Boolean failedToDeck;
  public Integer footmark;

  public YardLocation(
      final String block, final String row, final String stack, final Integer tier) {
    this.type = LocationType.yard;
    this.block = block;
    this.row = row;
    this.stack = stack;
    this.tier = tier;
  }

  public YardLocation() {
    this.type = LocationType.yard;
  }

  @Override
  public String toString() {
    return String.format(
            "%s %s %s%s %s",
            block,
            Objects.toString(row, ""),
            Objects.toString(stack, ""),
            Objects.toString(tier, ""),
            Objects.toString(footmark, ""))
        .trim();
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof YardLocation)) {
      return false;
    }
    final YardLocation that = (YardLocation) other;
    return Objects.equals(block, that.block)
        && Objects.equals(row, that.row)
        && Objects.equals(stack, that.stack)
        && Objects.equals(tier, that.tier)
        && Objects.equals(failedToDeck, that.failedToDeck)
        && Objects.equals(footmark, that.footmark);
  }

  @Override
  public int hashCode() {
    return Objects.hash(block, row, stack, tier, failedToDeck, footmark);
  }

  public boolean isHeap() {
    return !block.isEmpty() && row == null && stack == null && tier == null;
  }

  public boolean isSetAside() {
    return !block.isEmpty() && row != null && stack == null && tier == null;
  }

  /**
   * Checks if given location is Yard location and it located is the same row.
   *
   * @param other Location to compare.
   * @return True if given location is Yard location and it located in the same row otherwise false.
   */
  public boolean isInSameRow(Location other) {
    if (!(other instanceof YardLocation)) {
      return false;
    }
    val otherYardLocation = (YardLocation) other;
    if (isHeap() && otherYardLocation.isHeap()) {
      return block.equals(otherYardLocation.block);
    } else {
      return block.equals(otherYardLocation.block) && row.equals(otherYardLocation.row);
    }
  }
}
