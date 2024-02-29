package com.tideworks.json.objectmodel;

import com.tideworks.json.objectmodel.locations.Location;

/** VesselMove model. */
public class VesselMove {

  public String id;
  public String containerNo;
  public String vessel;
  public String crane;
  public MoveType moveType;
  public String plannedLocation;
  public Location nextLocation;
  public boolean plannedToVessel;
  public boolean plannedToWheeled;
  public boolean isHeld;
}
