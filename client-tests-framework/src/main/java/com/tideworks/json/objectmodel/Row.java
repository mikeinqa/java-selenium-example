package com.tideworks.json.objectmodel;

import java.util.List;

/** Row model. */
public class Row {

  public String id;
  public String blockId;
  public String name40;
  public String name40Extension;
  public int maxTiers;
  public List<String> stacks;

  public Row(
      final String id,
      final String blockId,
      final String name40,
      final String name40Extension,
      final int maxTiers,
      final List<String> stacks) {
    this.id = id;
    this.blockId = blockId;
    this.name40 = name40;
    this.name40Extension = name40Extension;
    this.maxTiers = maxTiers;
    this.stacks = stacks;
  }

  public Row() {}
}
