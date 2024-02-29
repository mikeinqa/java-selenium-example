package com.tideworks.json.objectmodel;

import java.util.List;

/** Che Application profile model. */
public class Che extends Profile {

  public String equipmentId;
  public Boolean selfAssign;
  public Boolean selfAssignZones;
  public Boolean selfAssignVesselOperations;
  public Boolean selfAssignWorkQueues;
  public Boolean splitWorklist;
  public Boolean blockPicker;
  public Boolean escalatedMovesFilter;

  public List<CheGridConfiguration> gridConfigurations;
}
