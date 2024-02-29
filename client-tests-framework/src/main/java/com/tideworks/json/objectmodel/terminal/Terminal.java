package com.tideworks.json.objectmodel.terminal;

import com.tideworks.json.objectmodel.Block;
import com.tideworks.json.objectmodel.ContainerKind;
import com.tideworks.json.objectmodel.Damage;
import com.tideworks.json.objectmodel.Equipment;
import com.tideworks.json.objectmodel.IntermodalUnit;
import com.tideworks.json.objectmodel.Move;
import com.tideworks.json.objectmodel.Oversize;
import com.tideworks.json.objectmodel.Preference;
import com.tideworks.json.objectmodel.Profile;
import com.tideworks.json.objectmodel.Row;
import com.tideworks.json.objectmodel.Seal;
import com.tideworks.json.objectmodel.Segment;
import com.tideworks.json.objectmodel.VesselOperation;
import com.tideworks.json.objectmodel.Zone;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to store test's data information. All actions belong to manipulation with test's objects
 * should be located here.
 */
public class Terminal {
  private final ArrayList<IntermodalUnit> containers = new ArrayList<>();
  @Getter private final ArrayList<Oversize> oversizes = new ArrayList<>();
  @Getter private final ArrayList<Segment> segments = new ArrayList<>();
  @Getter private final ArrayList<Move> moves = new ArrayList<>();
  @Getter private final ArrayList<Equipment> equipments = new ArrayList<>();
  @Getter private final ArrayList<Block> blocks = new ArrayList<>();
  @Getter private final ArrayList<Zone> zones = new ArrayList<>();
  @Getter private final ArrayList<Row> rows = new ArrayList<>();
  @Getter private final ArrayList<Seal> seals = new ArrayList<>();
  @Getter private final ArrayList<Damage> damages = new ArrayList<>();
  @Getter private final ArrayList<Profile> profiles = new ArrayList<>();
  @Getter private final ArrayList<VesselOperation> vesselOperations = new ArrayList<>();
  @Getter private final ArrayList<String> devices = new ArrayList<>();
  @Getter private final ArrayList<Preference> preferences = new ArrayList<>();

  /**
   * Add container to terminal.
   *
   * @param container Container to add.
   */
  public void add(IntermodalUnit container) {
    containers.add(container);
  }

  /**
   * Add oversize to terminal.
   *
   * @param oversize Oversize to add.
   */
  public void add(Oversize oversize) {
    oversizes.add(oversize);
  }

  /**
   * Add segment to terminal.
   *
   * @param segment Segment to add.
   */
  public void add(Segment segment) {
    segments.add(segment);
  }

  /**
   * Add move to terminal.
   *
   * @param move Move to add.
   */
  public void add(Move move) {
    moves.add(move);
  }

  /**
   * Add equipment to terminal.
   *
   * @param equipment Equipment to add.
   */
  public void add(Equipment equipment) {
    equipments.add(equipment);
  }

  /**
   * Add block to terminal.
   *
   * @param block Block to add.
   */
  public void add(Block block) {
    blocks.add(block);
  }

  /**
   * Add zone to terminal.
   *
   * @param zone Zone to add.
   */
  public void add(Zone zone) {
    zones.add(zone);
  }

  /**
   * Add row to terminal.
   *
   * @param row Row to add.
   */
  public void add(Row row) {
    rows.add(row);
  }

  /**
   * Add seal to terminal.
   *
   * @param seal Seal to add.
   */
  public void add(Seal seal) {
    seals.add(seal);
  }

  /**
   * Add damage to terminal.
   *
   * @param damage Damage to add.
   */
  public void add(Damage damage) {
    damages.add(damage);
  }

  /**
   * Add profile to terminal.
   *
   * @param profile Profile to add.
   */
  public void add(Profile profile) {
    profiles.add(profile);
  }

  /**
   * Add device to terminal.
   *
   * @param device Device to add.
   */
  public void add(String device) {
    devices.add(device);
  }

  /**
   * Add preference to terminal.
   *
   * @param preference Preference to add.
   */
  public void add(Preference preference) {
    preferences.add(preference);
  }

  /**
   * Add vessel operation to terminal.
   *
   * @param vesselOperation Vessel operation to add.
   */
  public void add(VesselOperation vesselOperation) {
    vesselOperations.add(vesselOperation);
  }

  /**
   * remove container to terminal.
   *
   * @param container Container to remove.
   */
  public void remove(IntermodalUnit container) {
    containers.remove(container);
  }

  /**
   * remove oversize to terminal.
   *
   * @param oversize Oversize to remove.
   */
  public void remove(Oversize oversize) {
    oversizes.remove(oversize);
  }

  /**
   * remove segment to terminal.
   *
   * @param segment Segment to remove.
   */
  public void remove(Segment segment) {
    segments.remove(segment);
  }

  /**
   * remove move to terminal.
   *
   * @param move Move to remove.
   */
  public void remove(Move move) {
    moves.remove(move);
  }

  /**
   * remove equipment to terminal.
   *
   * @param equipment Equipment to remove.
   */
  public void remove(Equipment equipment) {
    equipments.remove(equipment);
  }

  /**
   * remove block to terminal.
   *
   * @param block Block to remove.
   */
  public void remove(Block block) {
    blocks.remove(block);
  }

  /**
   * remove zone to terminal.
   *
   * @param zone Zone to remove.
   */
  public void remove(Zone zone) {
    zones.remove(zone);
  }

  /**
   * remove row to terminal.
   *
   * @param row Row to remove.
   */
  public void remove(Row row) {
    rows.remove(row);
  }

  /**
   * remove seal to terminal.
   *
   * @param seal Seal to remove.
   */
  public void remove(Seal seal) {
    seals.remove(seal);
  }

  /**
   * remove damage to terminal.
   *
   * @param damage Damage to remove.
   */
  public void remove(Damage damage) {
    damages.remove(damage);
  }

  /**
   * remove profile to terminal.
   *
   * @param profile Profile to remove.
   */
  public void remove(Profile profile) {
    profiles.remove(profile);
  }

  /**
   * remove device to terminal.
   *
   * @param device Device to remove.
   */
  public void remove(String device) {
    devices.remove(device);
  }

  /**
   * remove preference to terminal.
   *
   * @param preference Preference to remove.
   */
  public void remove(Preference preference) {
    preferences.remove(preference);
  }

  /**
   * remove vessel operation to terminal.
   *
   * @param vesselOperation Vessel operation to remove.
   */
  public void remove(VesselOperation vesselOperation) {
    vesselOperations.remove(vesselOperation);
  }

  /**
   * Get all container which existing on terminal.
   *
   * @return Container list
   */
  public List<IntermodalUnit> getContainers() {
    return containers
        .stream()
        .filter(c -> c.kind == ContainerKind.container)
        .collect(Collectors.toList());
  }

  /**
   * Get all chassis which existing on terminal.
   *
   * @return Chassis list
   */
  public List<IntermodalUnit> getChassis() {
    return containers
        .stream()
        .filter(c -> c.kind == ContainerKind.chassis)
        .collect(Collectors.toList());
  }

  /** Clears all collections. */
  public void clear() {
    containers.clear();
    oversizes.clear();
    segments.clear();
    moves.clear();
    equipments.clear();
    blocks.clear();
    zones.clear();
    rows.clear();
    seals.clear();
    damages.clear();
    profiles.clear();
    devices.clear();
    preferences.clear();
    vesselOperations.clear();
  }
}
