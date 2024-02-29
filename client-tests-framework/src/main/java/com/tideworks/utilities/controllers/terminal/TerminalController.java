package com.tideworks.utilities.controllers.terminal;

import com.tideworks.json.builders.BlockBuilder;
import com.tideworks.json.builders.ContainerBuilder;
import com.tideworks.json.builders.ContainerPlanBuilder;
import com.tideworks.json.builders.EquipmentBuilder;
import com.tideworks.json.builders.MoveBuilder;
import com.tideworks.json.builders.SegmentBuilder;
import com.tideworks.json.builders.VesselOperationBuilder;
import com.tideworks.json.builders.ZoneBuilder;
import com.tideworks.json.objectmodel.Block;
import com.tideworks.json.objectmodel.BlockKind;
import com.tideworks.json.objectmodel.ContainerKind;
import com.tideworks.json.objectmodel.ContainerPlan;
import com.tideworks.json.objectmodel.Equipment;
import com.tideworks.json.objectmodel.EquipmentLocationKind;
import com.tideworks.json.objectmodel.IntermodalUnit;
import com.tideworks.json.objectmodel.MoveSegmentStatus;
import com.tideworks.json.objectmodel.MoveStatus;
import com.tideworks.json.objectmodel.Segment;
import com.tideworks.json.objectmodel.SegmentKind;
import com.tideworks.json.objectmodel.VesselOperation;
import com.tideworks.json.objectmodel.Zone;
import com.tideworks.json.objectmodel.locations.BackreachLocation;
import com.tideworks.json.objectmodel.locations.EquipmentLocation;
import com.tideworks.json.objectmodel.locations.GateLocation;
import com.tideworks.json.objectmodel.locations.Location;
import com.tideworks.json.objectmodel.locations.LocationType;
import com.tideworks.json.objectmodel.locations.RailLocation;
import com.tideworks.json.objectmodel.locations.RailLocation.RailTier;
import com.tideworks.json.objectmodel.locations.VesselLocation;
import com.tideworks.json.objectmodel.locations.YardLocation;
import com.tideworks.json.objectmodel.terminal.Terminal;

import lombok.Getter;
import lombok.val;
import org.openqa.selenium.NotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Class to store test's data information. All actions belong to manipulation with test's objects
 * should be located here, so it possible to perform manipulation with data from any place where
 * instance of this class is available.
 */
@SuppressWarnings("WeakerAccess")
public class TerminalController {

  @Getter private final Terminal terminal;

  public TerminalController() {
    terminal = new Terminal();
  }

  /** Cleats all data collections. */
  public void clearTerminal() {
    terminal.clear();
  }

  /**
   * Creates block.
   *
   * @param blockBuilderFunction Block builder.
   * @return Block instance.
   */
  public Block createBlock(Function<BlockBuilder, BlockBuilder> blockBuilderFunction) {
    val block = blockBuilderFunction.apply(BlockBuilder.create()).build();

    terminal.add(block);
    terminal.getRows().addAll(block.rows);

    return block;
  }

  /**
   * Creates container in given location.
   *
   * @param location Location for container creation.
   * @return Container.
   */
  public IntermodalUnit createContainer(Location location) {
    return createContainer(
        ContainerBuilder.create()
            .withKind(ContainerKind.container)
            .withLocation(location)
            .withSizeType("40HC")
            .withWeight("2000")
            .build());
  }

  /**
   * Creates chassis in terminal.
   *
   * @return Chassis.
   */
  public IntermodalUnit createChassis() {
    val chassis =
        createContainer(
            ContainerBuilder.create("CHAS")
                .withKind(ContainerKind.chassis)
                .withWeight("800")
                .withSizeType("40")
                .build());

    terminal.add(chassis);

    return chassis;
  }

  /**
   * CreatesIntermodalUnitin terminal.
   *
   * @param containerBuilderFunction IntermodalUnitbuilder.
   * @return Container.
   */
  public IntermodalUnit createContainer(
      Function<ContainerBuilder, ContainerBuilder> containerBuilderFunction) {
    return createContainer(containerBuilderFunction.apply(ContainerBuilder.create()).build());
  }

  /**
   * Creates IntermodalUnit in terminal.
   *
   * @param container IntermodalUnit to create in terminal.
   * @return Container.
   */
  public IntermodalUnit createContainer(IntermodalUnit container) {
    terminal.add(container);

    return container;
  }

  /**
   * Creates container, located in grounded heap block.
   *
   * @return IntermodalUnit in grounded heap.
   */
  public IntermodalUnit createContainerInHeap() {
    return createContainer(createHeapLocation());
  }

  /**
   * Creates container, located in grounded block.
   *
   * @return IntermodalUnit in grounded block
   */
  public IntermodalUnit createContainerInYard() {
    return createContainer(createYardLocation());
  }

  /**
   * Creates IntermodalUnit located on vessel location.
   *
   * @return IntermodalUnit on vessel location.
   */
  public IntermodalUnit createContainerOnVessel() {
    return createContainer(createVesselLocation());
  }

  /**
   * Creates IntermodalUnit located on truck location
   *
   * @return IntermodalUnit on truck location.
   */
  private IntermodalUnit createContainerOnUtr() {
    return createContainer(createTruckLocation());
  }

  /**
   * Creates IntermodalUnit located in grounded heap, with move planed from heap to yard.
   *
   * @return IntermodalUnit in grounded heap.
   */
  public ContainerPlan planContainerInHeapFromHeapToYard() {
    return planContainerInHeapFromHeapToYard(MoveStatus.Active, MoveSegmentStatus.Active, true);
  }

  /**
   * Creates IntermodalUnit located in grounded heap, with move planed from heap to yard.
   *
   * @param moveStatus Move status.
   * @param segmentStatus Segment status.
   * @param selectedByEquipment Indicates if IntermodalUnit selected by equipment.
   * @return IntermodalUnit in grounded heap.
   */
  public ContainerPlan planContainerInHeapFromHeapToYard(
      MoveStatus moveStatus, MoveSegmentStatus segmentStatus, boolean selectedByEquipment) {
    val container = createContainerInHeap();
    val toLocation = createYardLocation();
    val segmentToLocation = createTruckLocation();

    return planContainer(
        container, toLocation, moveStatus, segmentToLocation, segmentStatus, selectedByEquipment);
  }

  /**
   * Creates container located in grounded location, with move planed from yard to heap.
   *
   * @return IntermodalUnit in grounded location.
   */
  public ContainerPlan planContainerInYardFromYardToHeap() {
    return planContainerInYardFromYardToHeap(MoveStatus.Active, MoveSegmentStatus.Active, true);
  }

  /**
   * Creates IntermodalUnit in ground with set aside move.
   *
   * @param moveStatus Move status
   * @param segmentStatus Segment status
   * @param selectedByEquipment Indicates if IntermodalUnit selected by equipment
   * @return IntermodalUnit in ground
   */
  public ContainerPlan planSetAsideContainer(
      final MoveStatus moveStatus,
      final MoveSegmentStatus segmentStatus,
      final boolean selectedByEquipment) {
    val container = createContainerInYard();
    val toLocation = createYardLocation();

    return planContainer(
        container, toLocation, moveStatus, toLocation, segmentStatus, selectedByEquipment);
  }

  /**
   * Creates IntermodalUnit on UTR and moves YUY.
   *
   * @param moveStatus Move status
   * @param segmentStatus Segment status for last segment
   * @param selectedByEquipment Indicates if IntermodalUnit selected by equipment
   * @return Container ou UTR
   */
  public ContainerPlan planContainerOnUtrFromYardToYard(
      final MoveStatus moveStatus,
      final MoveSegmentStatus segmentStatus,
      final boolean selectedByEquipment) {

    val fromLocation = createYardLocation();
    val toLocation = createYardLocation();
    val trackLocation = createTruckLocation();
    val container = createContainer(trackLocation);

    return planContainer(
        container,
        toLocation,
        moveStatus,
        toLocation,
        segmentStatus,
        selectedByEquipment,
        fromLocation);
  }

  /**
   * Creates IntermodalUnit on UTR and moves VUY.
   *
   * @param moveStatus Move status
   * @param segmentStatus Segment status for last segment
   * @param selectedByEquipment Indicates if IntermodalUnit selected by equipment
   * @return IntermodalUnit on UTR
   */
  public ContainerPlan planContainerOnUtrFromVesselToYard(
      final MoveStatus moveStatus,
      final MoveSegmentStatus segmentStatus,
      final boolean selectedByEquipment) {
    val fromLocation = createVesselLocation();
    val toLocation = createYardLocation();
    val trackLocation = createTruckLocation();
    val container = createContainer(trackLocation);

    return planContainer(
        container,
        toLocation,
        moveStatus,
        toLocation,
        segmentStatus,
        selectedByEquipment,
        fromLocation);
  }

  /**
   * Creates IntermodalUnit on UTR and moves RUY.
   *
   * @param moveStatus Move status
   * @param segmentStatus Segment status for last segment
   * @param selectedByEquipment Indicates if IntermodalUnit selected by equipment
   * @return IntermodalUnit on UTR
   */
  public ContainerPlan planContainerOnUtrFromRailToYard(
      final MoveStatus moveStatus,
      final MoveSegmentStatus segmentStatus,
      final boolean selectedByEquipment) {
    val fromLocation = createRailLocation();
    val toLocation = createYardLocation();
    val trackLocation = createTruckLocation();
    val container = createContainer(trackLocation);

    return planContainer(
        container,
        toLocation,
        moveStatus,
        toLocation,
        segmentStatus,
        selectedByEquipment,
        fromLocation);
  }

  /**
   * Creates IntermodalUnit on UTR and moves YUY with active move and segment equipment was not
   * selected.
   *
   * @return IntermodalUnit ou UTR
   */
  public ContainerPlan planContainerOnUtrFromYardToYard() {
    return planContainerOnUtrFromYardToYard(MoveStatus.Active, MoveSegmentStatus.Active, false);
  }

  /**
   * Creates IntermodalUnit on OTR and gate in moves.
   *
   * @param moveStatus Move status
   * @param segmentStatus Segment status for last segment
   * @param selectedByEquipment Indicates if IntermodalUnit selected by equipment
   * @return IntermodalUnit ou OTR
   */
  public ContainerPlan planContainerOnOtrFromGateToYard(
      final MoveStatus moveStatus,
      final MoveSegmentStatus segmentStatus,
      final boolean selectedByEquipment) {
    val currentLocation = createGateLocation();
    val toLocation = createYardLocation();
    val container = createContainer(currentLocation);

    return planContainer(
        container, toLocation, moveStatus, toLocation, segmentStatus, selectedByEquipment);
  }

  /**
   * Creates IntermodalUnit in ground and gate out moves.
   *
   * @param moveStatus Move status
   * @param segmentStatus Segment status for last segment
   * @param selectedByEquipment Indicates if IntermodalUnit selected by equipment
   * @return IntermodalUnit in ground
   */
  public ContainerPlan planContainerInYardFromGateToYard(
      final MoveStatus moveStatus,
      final MoveSegmentStatus segmentStatus,
      final boolean selectedByEquipment) {
    val currentLocation = createYardLocation();
    val toLocation = createGateLocation();
    val container = createContainer(currentLocation);

    return planContainer(
        container, toLocation, moveStatus, toLocation, segmentStatus, selectedByEquipment);
  }

  /**
   * Creates IntermodalUnit located in grounded location, with move planed from yard to heap.
   *
   * @param moveStatus Move status.
   * @param segmentStatus Segment status.
   * @param selectedByEquipment Indicates if IntermodalUnit selected by equipment.
   * @return IntermodalUnit in grounded location.
   */
  public ContainerPlan planContainerInYardFromYardToHeap(
      MoveStatus moveStatus, MoveSegmentStatus segmentStatus, boolean selectedByEquipment) {
    val container = createContainerInYard();
    val toLocation = createHeapLocation();
    val segmentToLocation = createTruckLocation();

    return planContainer(
        container, toLocation, moveStatus, segmentToLocation, segmentStatus, selectedByEquipment);
  }

  /**
   * Creates IntermodalUnit located in grounded location, with move planed from yard to vessel.
   *
   * @param moveStatus Move status.
   * @param segmentStatus Segment status.
   * @param selectedByEquipment Indicates if IntermodalUnit selected by equipment.
   * @return IntermodalUnit in grounded location.
   */
  public ContainerPlan planContainerInYardFromYardToVessel(
      MoveStatus moveStatus, MoveSegmentStatus segmentStatus, boolean selectedByEquipment) {
    val container = createContainerInYard();
    val toLocation = createVesselLocation();
    val segmentToLocation = createTruckLocation();

    return planContainer(
        container, toLocation, moveStatus, segmentToLocation, segmentStatus, selectedByEquipment);
  }

  /**
   * Creates IntermodalUnit located in grounded location, with move planed from yard to gate.
   *
   * @param moveStatus Move status.
   * @param segmentStatus Segment status.
   * @param selectedByEquipment Indicates if IntermodalUnit selected by equipment.
   * @return IntermodalUnit in grounded location.
   */
  public ContainerPlan planContainerInYardFromYardToGate(
      MoveStatus moveStatus, MoveSegmentStatus segmentStatus, boolean selectedByEquipment) {
    val container = createContainerInYard();
    val toLocation = createGateLocation();
    val segmentToLocation = createTruckLocation();

    return planContainer(
        container, toLocation, moveStatus, segmentToLocation, segmentStatus, selectedByEquipment);
  }

  /**
   * Creates IntermodalUnit located in grounded location, with move planed from yard to rail.
   *
   * @param moveStatus Move status.
   * @param segmentStatus Segment status.
   * @param selectedByEquipment Indicates if container selected by equipment.
   * @return Container in grounded location.
   */
  public ContainerPlan planContainerInYardFromYardToRail(
      MoveStatus moveStatus, MoveSegmentStatus segmentStatus, boolean selectedByEquipment) {
    val container = createContainerInYard();
    val toLocation = createRailLocation();
    val segmentToLocation = createTruckLocation();

    return planContainer(
        container, toLocation, moveStatus, segmentToLocation, segmentStatus, selectedByEquipment);
  }

  /**
   * Creates container located on vessel location, with move planed from vessel to yard with segment
   * to bachreach.
   *
   * @return Container on vessel location.
   */
  public ContainerPlan planContainerOnVesselFromVesselToBackreach() {
    return planContainerOnVesselFromVesselToBackreach(
        MoveStatus.Active, MoveSegmentStatus.Active, false);
  }

  /**
   * Creates container located on vessel location, with move planed from vessel to yard with segment
   * to bachreach.
   *
   * @param moveStatus Move status.
   * @param segmentStatus Segment status.
   * @param selectedByEquipment Indicates if container selected by equipment.
   * @return Container on vessel location.
   */
  public ContainerPlan planContainerOnVesselFromVesselToBackreach(
      MoveStatus moveStatus, MoveSegmentStatus segmentStatus, boolean selectedByEquipment) {
    val container = createContainerOnVessel();
    val toLocation = createYardLocation();
    val operation = terminal.getVesselOperations().stream().findFirst().get();
    val segmentToLocation = new BackreachLocation();
    segmentToLocation.crane = operation.crane;

    return planContainer(
        container,
        toLocation,
        moveStatus,
        segmentToLocation,
        segmentStatus,
        selectedByEquipment,
        container.location,
        operation,
        null,
        SegmentKind.V);
  }

  /**
   * Creates container located on vessel location, with move planed from vessel to yard with segment
   * to utr.
   *
   * @return Container on vessel location.
   */
  public ContainerPlan planContainerOnVesselFromVesselToYard() {
    return planContainerOnVesselFromVesselToYard(
        MoveStatus.Active, MoveSegmentStatus.Active, false);
  }

  /**
   * Creates container located on vessel location, with move planed from vessel to yard with segment
   * to utr.
   *
   * @param moveStatus Move status.
   * @param segmentStatus Segment status.
   * @param selectedByEquipment Indicates if container selected by equipment.
   * @return Container on vessel location.
   */
  public ContainerPlan planContainerOnVesselFromVesselToYard(
      MoveStatus moveStatus, MoveSegmentStatus segmentStatus, boolean selectedByEquipment) {
    val container = createContainerOnVessel();
    val toLocation = createYardLocation();
    val segmentToLocation = createTruckLocation();

    return planContainer(
        container,
        toLocation,
        moveStatus,
        segmentToLocation,
        segmentStatus,
        selectedByEquipment,
        container.location,
        terminal.getVesselOperations().stream().findFirst().get(),
        null,
        SegmentKind.V);
  }

  /**
   * Creates container located on vessel location, with move planed from vessel to wheeled with
   * segment to utr.
   *
   * @return Container on vessel location.
   */
  public ContainerPlan planContainerOnVesselFromVesselToWheeled() {
    return planContainerOnVesselFromVesselToWheeled(
        MoveStatus.Active, MoveSegmentStatus.Active, false);
  }

  /**
   * Creates container located on vessel location, with move planed from vessel to yard with segment
   * to utr without associated chassis.
   *
   * @return Container on vessel location.
   */
  public ContainerPlan planContainerOnVesselFromVesselToWheeledWithoutChassis() {
    return planContainerOnVesselFromVesselToWheeled(
        MoveStatus.Active, MoveSegmentStatus.Active, false, true);
  }

  /**
   * Creates container located on vessel location, with move planed from vessel to yard with segment
   * to utr.
   *
   * @param moveStatus Move status.
   * @param segmentStatus Segment status.
   * @param selectedByEquipment Indicates if container selected by equipment.
   * @return Container on vessel location.
   */
  public ContainerPlan planContainerOnVesselFromVesselToWheeled(
      MoveStatus moveStatus, MoveSegmentStatus segmentStatus, boolean selectedByEquipment) {
    return planContainerOnVesselFromVesselToWheeled(
        moveStatus, segmentStatus, selectedByEquipment, false);
  }

  /**
   * Creates container located on vessel location, with move planed from vessel to yard with segment
   * to utr.
   *
   * @param moveStatus Move status.
   * @param segmentStatus Segment status.
   * @param selectedByEquipment Indicates if container selected by equipment.
   * @param withoutChassis If true chassis will not created.
   * @return Container on vessel location.
   */
  public ContainerPlan planContainerOnVesselFromVesselToWheeled(
      MoveStatus moveStatus,
      MoveSegmentStatus segmentStatus,
      boolean selectedByEquipment,
      boolean withoutChassis) {
    val container = createContainerOnVessel();
    val toLocation = createWheeledLocation();
    val segmentToLocation = createTruckLocation();

    return planContainer(
        container,
        toLocation,
        moveStatus,
        segmentToLocation,
        segmentStatus,
        selectedByEquipment,
        container.location,
        terminal.getVesselOperations().stream().findFirst().get(),
        withoutChassis ? null : createChassis().id,
        SegmentKind.V);
  }

  /**
   * Creates container located in yard location, with move planed from yard to yard with segment to
   * utr.
   *
   * @return Container in yard location.
   */
  public ContainerPlan planContainerInYardFromYardToYard() {
    return planContainerInYardFromYardToYard(MoveStatus.Active, MoveSegmentStatus.Active, false);
  }

  /**
   * Creates container located in yard location, with move planed from yard to yard with segment to
   * utr.
   *
   * @param moveStatus Move status.
   * @param segmentStatus Segment status.
   * @param selectedByEquipment Indicates if container selected by equipment.
   * @return Container in yard location.
   */
  public ContainerPlan planContainerInYardFromYardToYard(
      MoveStatus moveStatus, MoveSegmentStatus segmentStatus, boolean selectedByEquipment) {
    val container = createContainerInYard();
    val toLocation = createYardLocation();
    val segmentToLocation = createTruckLocation();

    return planContainer(
        container, toLocation, moveStatus, segmentToLocation, segmentStatus, selectedByEquipment);
  }

  /**
   * Creates container located on truck location, with move planed from yard to vessel with segment
   * to vessel.
   *
   * @param moveStatus Move status.
   * @param segmentStatus Segment status.
   * @param selectedByEquipment Indicates if container selected by equipment.
   * @return Container in yard location.
   */
  public ContainerPlan planContainerOnUtrFromYardToVessel(
      MoveStatus moveStatus, MoveSegmentStatus segmentStatus, boolean selectedByEquipment) {
    val container = createContainerOnUtr();
    val fromLocation = createYardLocation();
    val toLocation = createVesselLocation();

    return planContainer(
        container,
        toLocation,
        moveStatus,
        toLocation,
        segmentStatus,
        selectedByEquipment,
        fromLocation,
        terminal.getVesselOperations().stream().findFirst().get(),
        null,
        SegmentKind.V);
  }

  /**
   * Creates move and move segment for given container.
   *
   * @param container Container to move.
   * @param toLocation Move planed location.
   * @param moveStatus Move status.
   * @param segmentToLocation Equipment location
   * @param segmentStatus Segment status
   * @param selectedByEquipment Indicates if container selected by equipment.
   * @return Container with move and segment information.
   */
  public ContainerPlan planContainer(
      final IntermodalUnit container,
      final Location toLocation,
      final MoveStatus moveStatus,
      final Location segmentToLocation,
      final MoveSegmentStatus segmentStatus,
      final boolean selectedByEquipment) {
    return planContainer(
        container,
        toLocation,
        moveStatus,
        segmentToLocation,
        segmentStatus,
        selectedByEquipment,
        container.location);
  }

  /**
   * Creates move and move segment for given container.
   *
   * @param container Container to move.
   * @param toLocation Move planed location.
   * @param moveStatus Move status.
   * @param segmentToLocation Equipment location
   * @param segmentStatus Segment status
   * @param selectedByEquipment Indicates if container selected by equipment.
   * @param moveFromLocation Move start location.
   * @return Container with move and segment information.
   */
  public ContainerPlan planContainer(
      final IntermodalUnit container,
      final Location toLocation,
      final MoveStatus moveStatus,
      final Location segmentToLocation,
      final MoveSegmentStatus segmentStatus,
      final boolean selectedByEquipment,
      final Location moveFromLocation) {

    return planContainer(
        container,
        toLocation,
        moveStatus,
        segmentToLocation,
        segmentStatus,
        selectedByEquipment,
        container.location,
        null,
        null,
        SegmentKind.Y);
  }

  /**
   * Creates move and move segment for given container.
   *
   * @param container Container to move.
   * @param toLocation Move planed location.
   * @param moveStatus Move status.
   * @param segmentToLocation Equipment location
   * @param segmentStatus Segment status
   * @param selectedByEquipment Indicates if container selected by equipment
   * @param moveFromLocation Move original location
   * @param vesselOperation Vessel operation for vessel moves
   * @param associatedChassisId Associated chassis for move
   * @param segmentKind Segment kind
   * @return Container with move and segment information.
   */
  public ContainerPlan planContainer(
      final IntermodalUnit container,
      final Location toLocation,
      final MoveStatus moveStatus,
      final Location segmentToLocation,
      final MoveSegmentStatus segmentStatus,
      final boolean selectedByEquipment,
      final Location moveFromLocation,
      final VesselOperation vesselOperation,
      final String associatedChassisId,
      final SegmentKind segmentKind) {
    return planContainer(
        container,
        toLocation,
        moveStatus,
        segmentToLocation,
        segmentStatus,
        selectedByEquipment,
        moveFromLocation,
        container.location,
        vesselOperation,
        associatedChassisId,
        segmentKind,
        false);
  }

  /**
   * Creates move and move segment for given container.
   *
   * @param container Container to move.
   * @param toLocation Move planed location.
   * @param moveStatus Move status.
   * @param segmentToLocation Equipment location
   * @param segmentStatus Segment status
   * @param selectedByEquipment Indicates if container selected by equipment
   * @param moveFromLocation Move original location
   * @param segmentFrom Segment original location
   * @param vesselOperation Vessel operation for vessel moves
   * @param associatedChassisId Associated chassis for move
   * @param segmentKind Segment kind
   * @param isSystemMove Indicates if move system or not.
   * @return Container with move and segment information.
   */
  public ContainerPlan planContainer(
      final IntermodalUnit container,
      final Location toLocation,
      final MoveStatus moveStatus,
      final Location segmentToLocation,
      final MoveSegmentStatus segmentStatus,
      final boolean selectedByEquipment,
      final Location moveFromLocation,
      final Location segmentFrom,
      final VesselOperation vesselOperation,
      final String associatedChassisId,
      final SegmentKind segmentKind,
      final Boolean isSystemMove) {

    val move =
        MoveBuilder.create()
            .withContainerId(container.id)
            .withFrom(moveFromLocation)
            .withTo(toLocation)
            .withStatus(moveStatus)
            .withPriority(0)
            .withSequence(0)
            .withZoneIds(terminal.getZones().stream().map(s -> s.id).collect(Collectors.toList()))
            .withOperation(vesselOperation)
            .withAssociatedChassisId(associatedChassisId)
            .withSystemMove(isSystemMove)
            .build();

    val segment =
        SegmentBuilder.create()
            .withMoveId(move.id)
            .withContainerId(container.id)
            .withAssignedEquipment(Collections.singletonList("CHE01"))
            .withKind(segmentKind)
            .withFrom(segmentFrom)
            .withTo(segmentToLocation)
            .withStatus(segmentStatus)
            .withSequence(0)
            .withSelectedEquipment(
                selectedByEquipment
                    ? terminal
                        .getEquipments()
                        .stream()
                        .filter(s -> s.kind == EquipmentLocationKind.truck)
                        .findFirst()
                        .get()
                        .equipmentNo
                    : "")
            .build();

    return planContainer(
        s ->
            s.withContainer(container)
                .withSegment(segment)
                .withMove(move)
                .withOperation(vesselOperation)
                .withChassis(associatedChassisId));
  }

  /**
   * Creates equipment location.
   *
   * @return Equipment location.
   */
  public EquipmentLocation createTruckLocation() {
    return createTruckLocation(terminal.getEquipments().get(0).id);
  }

  /**
   * Creates equipment location.
   *
   * @param truckId Truck id which location should be created.
   * @return Equipment location.
   */
  public EquipmentLocation createTruckLocation(String truckId) {
    val equipmentLocation = new EquipmentLocation();
    equipmentLocation.kind = EquipmentLocationKind.truck;
    equipmentLocation.equipmentId =
        terminal
            .getEquipments()
            .stream()
            .filter(s -> s.id.equals(truckId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Equipment with given id was not found."))
            .id;

    return equipmentLocation;
  }

  /**
   * Creates Heap location.
   *
   * @return Heap location.
   */
  public YardLocation createHeapLocation() {
    val heapBlock =
        terminal
            .getBlocks()
            .stream()
            .filter(s -> s.kind == BlockKind.GroundedHeap)
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Heap block does not exist."));
    val heapLocation = new YardLocation();
    heapLocation.block = heapBlock.id;

    return heapLocation;
  }

  /**
   * Creates Grounded Yard location.
   *
   * @return Grounded Yard location.
   */
  public YardLocation createYardLocation() {
    val block =
        terminal
            .getBlocks()
            .stream()
            .filter(s -> s.kind == BlockKind.Grounded)
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Grounded block does not exist."));

    val allLocations = createGroundedYardLocations(block);
    val usedYardLocations =
        terminal
            .getContainers()
            .stream()
            .map(s -> s.location)
            .filter(s -> s.type == LocationType.yard)
            .collect(Collectors.toList());
    allLocations.removeAll(usedYardLocations);

    return allLocations.get(0);
  }

  private ArrayList<YardLocation> createGroundedYardLocations(Block block) {
    val yardLocations = new ArrayList<YardLocation>();
    block.rows.forEach(
        row ->
            IntStream.range(1, row.maxTiers + 1)
                .forEach(
                    tier ->
                        row.stacks.forEach(
                            stack ->
                                yardLocations.add(
                                    new YardLocation(block.id, row.id, stack, tier)))));

    return yardLocations;
  }

  private ArrayList<YardLocation> createWheeledYardLocations(Block block) {
    val wheeledLocations = new ArrayList<YardLocation>();
    block.rows.forEach(
        row ->
            row.stacks.forEach(
                stack -> wheeledLocations.add(new YardLocation(block.id, row.id, stack, null))));

    return wheeledLocations;
  }

  /**
   * Creates vessel location.
   *
   * @return Vessel location.
   */
  public VesselLocation createVesselLocation() {
    val vesselLocation = new VesselLocation();
    vesselLocation.type = LocationType.vessel;
    vesselLocation.bay = "1";
    vesselLocation.stack = "1";
    vesselLocation.tier = "1";

    return vesselLocation;
  }

  /**
   * Creates gate location.
   *
   * @return Gate location.
   */
  public GateLocation createGateLocation() {
    val gateLocation = new GateLocation();
    gateLocation.driverLicense = "d3";
    gateLocation.placard = "p3";

    return gateLocation;
  }

  /**
   * Creates gate location.
   *
   * @return Heap location.
   */
  public RailLocation createRailLocation() {
    val railLocation = new RailLocation();
    railLocation.railcar = "railCar1";
    railLocation.stack = "railStack";
    railLocation.tier = RailTier.B;
    railLocation.track = "railTrack";
    railLocation.well = "railWell";

    return railLocation;
  }

  /**
   * Creates wheeled location.
   *
   * @return Wheeled location.
   */
  public Location createWheeledLocation() {
    val block =
        terminal
            .getBlocks()
            .stream()
            .filter(s -> s.kind == BlockKind.Wheeled)
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Wheeled block does not exist."));

    val allLocations = createWheeledYardLocations(block);
    val usedYardLocations =
        terminal
            .getContainers()
            .stream()
            .map(s -> s.location)
            .filter(s -> s.type == LocationType.yard)
            .collect(Collectors.toList());
    allLocations.removeAll(usedYardLocations);

    return allLocations.get(0);
  }

  /**
   * Creates move and move segment for given container.
   *
   * @param containerPlanBuilderFunction Builder for container's move.
   * @return Plan for container's move.
   */
  public ContainerPlan planContainer(
      Function<ContainerPlanBuilder, ContainerPlanBuilder> containerPlanBuilderFunction) {
    val containerPlan = containerPlanBuilderFunction.apply(ContainerPlanBuilder.create()).build();
    terminal.getSegments().addAll(containerPlan.segments);
    terminal.add(containerPlan.move);

    val containers = terminal.getContainers();
    if (!containers.contains(containerPlan.intermodalUnit)) {
      terminal.add(containerPlan.intermodalUnit);
    }

    return containerPlan;
  }

  /**
   * Creates zone in terminal.
   *
   * @param zoneBuilderFunction Zone builder.
   * @return Zone
   */
  public Zone createZone(Function<ZoneBuilder, ZoneBuilder> zoneBuilderFunction) {
    return createZone(zoneBuilderFunction.apply(ZoneBuilder.create()).build());
  }

  /**
   * Creates zone in terminal.
   *
   * @param zone Zone to create.
   * @return Zone.
   */
  public Zone createZone(Zone zone) {
    terminal.add(zone);
    return zone;
  }

  /**
   * Creates vessel operation.
   *
   * @param vesselOperationFunction Vessel operation builder.
   * @return Vessel operation.
   */
  public VesselOperation createVesselOperation(
      Function<VesselOperationBuilder, VesselOperationBuilder> vesselOperationFunction) {
    return createVesselOperation(
        vesselOperationFunction.apply(VesselOperationBuilder.create()).build());
  }

  /**
   * Creates vessel operation.
   *
   * @param vesselOperation Vessel operation to add to terminal.
   * @return Vessel operation.
   */
  public VesselOperation createVesselOperation(VesselOperation vesselOperation) {
    terminal.add(vesselOperation);

    return vesselOperation;
  }

  /**
   * Creates equipment.
   *
   * @param equipmentBuilderFunction Equipment builder.
   * @return Equipment.
   */
  public Equipment createEquipment(
      Function<EquipmentBuilder, EquipmentBuilder> equipmentBuilderFunction) {
    return createEquipment(equipmentBuilderFunction.apply(EquipmentBuilder.create()).build());
  }

  /**
   * Creates equipment.
   *
   * @param equipment Equipment to add to terminal.
   * @return Equipment.
   */
  public Equipment createEquipment(Equipment equipment) {
    if (!terminal.getEquipments().contains(equipment)) {
      terminal.add(equipment);
    }

    return equipment;
  }

  /**
   * Set moves escalation for each container from given list.
   *
   * @param containers Containers which moves escalation needs to change.
   * @param escalated Escalation value.
   */
  public void setEscalation(List<IntermodalUnit> containers, boolean escalated) {
    containers.forEach(container -> setEscalation(container, escalated));
  }

  /**
   * Set move escalation for given container.
   *
   * @param container Container, which move escalation needs to change.
   * @param escalated Escalation value.
   */
  public void setEscalation(IntermodalUnit container, boolean escalated) {
    terminal
            .getMoves()
            .stream()
            .filter(move -> move.intermodalUnitId.equals(container.id))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Move not found."))
            .escalated =
        escalated;
  }

  /**
   * Search for move and move segment information for given container if available.
   *
   * @param container IntermodalUnit to search.
   * @return Container with full information.
   */
  public IntermodalUnit getContainerExpandMoveAndSegment(IntermodalUnit container) {
    val containerInformation = new IntermodalUnit(container);
    containerInformation.segment =
        terminal
            .getSegments()
            .stream()
            .filter(s -> s.intermodalUnitId.equals(container.id))
            .findFirst()
            .orElse(null);
    containerInformation.move =
        terminal
            .getMoves()
            .stream()
            .filter(s -> s.intermodalUnitId.equals(container.id))
            .findFirst()
            .orElse(null);

    return containerInformation;
  }

  /**
   * Search for move and move segment information for each given container.
   *
   * @return Containers with full information.
   */
  public List<IntermodalUnit> getContainersExpandMoveAndSegment() {
    return terminal
        .getContainers()
        .stream()
        .map(this::getContainerExpandMoveAndSegment)
        .collect(Collectors.toList());
  }

  /**
   * Gets container with moves and segments information.
   *
   * @param container IntermodalUnit Container to search.
   * @return Containers list.
   */
  public IntermodalUnit getContainerEmbedMovesAndSegments(IntermodalUnit container) {
    val containerInformation = new IntermodalUnit(container);
    containerInformation.segments =
        terminal
            .getSegments()
            .stream()
            .filter(s -> s.intermodalUnitId.equals(container.id))
            .collect(Collectors.toList());
    containerInformation.moves =
        terminal
            .getMoves()
            .stream()
            .filter(s -> s.intermodalUnitId.equals(container.id))
            .collect(Collectors.toList());

    return containerInformation;
  }

  /**
   * Gets container with moves and segments information.
   *
   * @return Containers list.
   */
  public List<IntermodalUnit> getContainersEmbedMovesAndSegments() {
    return terminal
        .getContainers()
        .stream()
        .map(this::getContainerEmbedMovesAndSegments)
        .collect(Collectors.toList());
  }

  /**
   * Gets segments with container and move information.
   *
   * @param segment Segment to search.
   * @return Segments collection.
   */
  public Segment getSegmentExpandContainerAndMove(Segment segment) {
    val segmentInformation = new Segment(segment);
    segmentInformation.move =
        terminal
            .getMoves()
            .stream()
            .filter(s -> s.id.equals(segmentInformation.moveId))
            .findFirst()
            .get();
    segmentInformation.intermodalUnit =
        terminal
            .getContainers()
            .stream()
            .filter(s -> s.id.equals(segmentInformation.intermodalUnitId))
            .findFirst()
            .get();

    return segmentInformation;
  }

  /**
   * Gets segments with container and move information.
   *
   * @return Segments collection.
   */
  public List<Segment> getSegmentsExpandContainerAndMove() {
    return terminal
        .getSegments()
        .stream()
        .map(this::getSegmentExpandContainerAndMove)
        .collect(Collectors.toList());
  }

  /**
   * Gets plan for given container.
   *
   * @param container Container for plan building.
   * @return Container plan with available information.
   */
  public ContainerPlan getContainerPlan(IntermodalUnit container) {
    val containerPlan = new ContainerPlan();
    containerPlan.intermodalUnit = container;
    containerPlan.segments =
        terminal
            .getSegments()
            .stream()
            .filter(s -> s.intermodalUnitId.equals(container.id))
            .collect(Collectors.toList());

    terminal
        .getMoves()
        .stream()
        .filter(move -> move.intermodalUnitId.equals(container.id))
        .findFirst()
        .ifPresent(
            move -> {
              containerPlan.move = move;
              containerPlan.operation =
                  terminal
                      .getVesselOperations()
                      .stream()
                      .filter(vesselOperation -> move.operation.equals(vesselOperation))
                      .findFirst()
                      .orElse(null);
              containerPlan.chassisId =
                  terminal
                      .getChassis()
                      .stream()
                      .filter(chassis -> chassis.move.equals(move))
                      .map(chassis -> chassis.id)
                      .findFirst()
                      .orElse(null);
            });

    return containerPlan;
  }
}
