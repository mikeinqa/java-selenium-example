package com.tideworks.json.objectmodel;

/**
 * Grid Configuration model.
 */
public class GridCongiuration {
  public String id;
  public GridColumnConfiguration[] columns;

  public GridCongiuration() {
  }

  public GridCongiuration(String id, GridColumnConfiguration[] columns) {
    this.id = id;
    this.columns = columns;
  }
}
