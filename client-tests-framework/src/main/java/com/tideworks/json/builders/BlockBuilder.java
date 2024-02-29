package com.tideworks.json.builders;

import com.tideworks.json.objectmodel.Block;
import com.tideworks.json.objectmodel.BlockKind;
import com.tideworks.json.objectmodel.Row;

import lombok.val;

import java.util.ArrayList;
import java.util.List;

/** Builder for block model. */
public class BlockBuilder {
  private final Block block = new Block();

  private BlockBuilder() {
    block.rows = new ArrayList<>();
  }

  /**
   * Creates builder.
   *
   * @return Builder instance.
   */
  public static BlockBuilder create() {
    return new BlockBuilder();
  }

  /** Sets blockKind.
   * @param blockKind BlockKind to set.
   * @return BlockBuilder
   */
  public BlockBuilder ofKind(BlockKind blockKind) {
    block.kind = blockKind;
    return this;
  }

  /**
   * Sets block's name.
   *
   * @param blockName Name to set
   * @return BlockBuilder
   */
  public BlockBuilder withName(String blockName) {
    block.id = blockName;
    return this;
  }

  /**
   * Adds row for block.
   *
   * @param rowName Row name.
   * @param maxTiers Max tiers in row.
   * @param stackNames Row's stacks.
   * @return BlockBuilder
   */
  public BlockBuilder withRow(String rowName, int maxTiers, List<String> stackNames) {
    val row = new Row();
    row.id = rowName;
    row.maxTiers = maxTiers;
    row.blockId = block.id;
    row.name40Extension = rowName;
    row.name40 = rowName;
    row.stacks = stackNames;

    block.rows.add(row);

    return this;
  }

  /**
   * Builds block.
   *
   * @return Block instance.
   */
  public Block build() {
    return block;
  }
}
