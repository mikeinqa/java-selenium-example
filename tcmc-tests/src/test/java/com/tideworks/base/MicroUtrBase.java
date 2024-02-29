package com.tideworks.base;

import static com.tideworks.json.objectmodel.ProfileType.microutr;
import static com.tideworks.utilities.api.MicroUtr.assignedSegmentsUrl;
import static com.tideworks.utilities.api.MicroUtr.containersOnEquipmentUrl;
import static com.tideworks.utilities.api.MicroUtr.equipmentUrl;
import static com.tideworks.utilities.controllers.enviroment.EnvironmentController.getDriverType;
import static com.tideworks.utilities.services.driver.DriverTypes.Chrome;

import com.tideworks.json.objectmodel.ContainerKind;
import com.tideworks.json.objectmodel.Equipment;
import com.tideworks.json.objectmodel.EquipmentLocationKind;
import com.tideworks.json.objectmodel.IntermodalUnit;
import com.tideworks.json.objectmodel.MicroUtr;
import com.tideworks.json.objectmodel.Move;
import com.tideworks.json.objectmodel.MoveSegmentStatus;
import com.tideworks.json.objectmodel.MoveStatus;
import com.tideworks.json.objectmodel.Segment;
import com.tideworks.json.objectmodel.locations.EquipmentLocation;
import com.tideworks.json.objectmodel.locations.Location;
import com.tideworks.json.objectmodel.locations.LocationType;
import com.tideworks.json.objectmodel.locations.VesselLocation;
import com.tideworks.json.objectmodel.locations.YardLocation;
import com.tideworks.pages.PageFactory;
import com.tideworks.pages.microutr.MainPage;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Base class for Micro-Utr application tests. */
public class MicroUtrBase extends Base {

  protected final String containerNo = "CONT000005";
  protected final String secondContainerNo = "CONT000002";
  protected final String thirdContainerNo = "CONT000003";
  protected final String blockAandB = "A&B";
  protected final String row102 = "102";
  protected final String stackA = "A";
  protected final String stackB = "B";
  protected final String unknownLocation = "Unknown";
  protected final String block = "BLOCK";
  protected final String secondBlock = "BLOCK2";
  protected final String row = "001";
  protected final String stack = "S";
  protected final Integer tier = 1;
  protected final String blockCss = "<<css>>";
  protected final String row106 = "106";
  protected final String crane01 = "01";
  protected final String vesselBay1 = "1";
  protected final String vesselBay3 = "3";
  protected final String vesselStack = "9";
  protected final String vesselTier = "78";
  protected final String awaitingInstructionsLocation = "Awaiting instructions...";
  protected final Integer sequence = 0;
  protected String utr01 = "UTR01";
  protected MicroUtr utrProfile;

  @BeforeClass
  public void createUtrProfile() {
    remoteDriverService.setAutoLoginEnabled(true);
  }

  @BeforeMethod
  public void stubUtrProfile() {
    utrProfile = getDefaultUtrProfile();
    stubProfileData(utrProfile);
    stubTestData(createUtrEquipment());
  }

  /** Performs login to Micro-utl application. */
  protected void login() {
    getLoginPage().login(utrProfile, userName, userPassword, apiMockService.getServiceAddress());
  }

  /**
   * Gets container.
   *
   * @param containerId Container Id.
   * @param location Container location.
   * @return Container in given location.
   */
  protected IntermodalUnit getContainer(String containerId, Location location) {
    IntermodalUnit container = new IntermodalUnit();
    container.id = containerId;
    container.kind = ContainerKind.container;
    container.location = location;

    return container;
  }

  /**
   * Gets yard locations.
   *
   * @param block Yard block.
   * @param row Yard row.
   * @param stack Yard stack.
   * @param tier Yard tier.
   * @param failedToDeck indicates if container failed to deck.
   * @return Yard location.
   */
  protected YardLocation getYardLocation(
      String block, String row, String stack, Integer tier, boolean failedToDeck) {
    YardLocation location = new YardLocation();
    location.type = LocationType.yard;
    location.block = block;
    location.row = row;
    location.stack = stack;
    location.tier = tier;
    location.failedToDeck = failedToDeck;

    return location;
  }

  /**
   * Gets yard locations.
   *
   * @param block Yard block.
   * @param row Yard row.
   * @param stack Yard stack.
   * @param tier Yard tier.
   * @return Yard location.
   */
  protected YardLocation getYardLocation(String block, String row, String stack, Integer tier) {
    YardLocation location = new YardLocation();
    location.type = LocationType.yard;
    location.block = block;
    location.row = row;
    location.stack = stack;
    location.tier = tier;

    return location;
  }

  /**
   * Gets equipment location.
   *
   * @param id Equipment id
   * @param kind Equipment kind.
   * @return Equipment location.
   */
  protected EquipmentLocation getEquipmentLocation(String id, EquipmentLocationKind kind) {
    EquipmentLocation location = new EquipmentLocation();
    location.type = LocationType.equipment;
    location.equipmentId = id;
    location.kind = kind;

    return location;
  }

  /**
   * Gets vessel location.
   *
   * @param bay Vessel bay
   * @return Vessel location.
   */
  protected VesselLocation getVesselLocation(String bay) {
    VesselLocation location = new VesselLocation();
    location.bay = bay;
    location.stack = vesselStack;
    location.tier = vesselTier;

    return location;
  }

  /**
   * Gets segment.
   *
   * @param container Container.
   * @param from From location.
   * @param to To location.
   * @param sequence Sequence.
   * @return Segment.
   */
  protected Segment getSegment(IntermodalUnit container, Location from, Location to, int sequence) {
    Segment segment = new Segment();
    segment.intermodalUnit = container;
    segment.from = from;
    segment.to = to;
    segment.sequence = sequence;
    segment.move = new Move(container.id, container.id, from, to);
    return segment;
  }

  /**
   * Gets incomplete twin segments.
   *
   * @param firstContainer First container.
   * @param secondContainer Second container.
   * @param firstStatus First segment status.
   * @param secondStatus Second segment status.
   * @param firstFrom From location for first segment.
   * @param firstTo To location for first segment.
   * @param secondFrom From location for second segment.
   * @param secondTo To location for second segment.
   * @return Incomplete segments.
   */
  protected Segment[] getIncompleteTwinSegments(
      IntermodalUnit firstContainer,
      IntermodalUnit secondContainer,
      MoveSegmentStatus firstStatus,
      MoveSegmentStatus secondStatus,
      Location firstFrom,
      Location firstTo,
      Location secondFrom,
      Location secondTo) {

    List<Segment> notCompleteSegment = new ArrayList<>();
    if (firstStatus != MoveSegmentStatus.Completed) {
      Move firstMove = new Move();
      firstMove.id = "11";
      firstMove.intermodalUnitId = firstContainer.id;
      firstMove.from = firstFrom;
      firstMove.to = firstTo;
      firstMove.status = MoveStatus.Active;
      firstMove.twinMoveId = "22";

      Segment firstSegment = new Segment();
      firstSegment.id = "1";
      firstSegment.moveId = firstMove.id;
      firstSegment.intermodalUnitId = firstContainer.id;
      firstSegment.from = firstMove.from;
      firstSegment.to = firstMove.to;
      firstSegment.status = firstStatus;
      firstSegment.intermodalUnit = firstContainer;
      firstSegment.move = firstMove;

      notCompleteSegment.add(firstSegment);
    }
    if (secondStatus != MoveSegmentStatus.Completed) {
      Move secondMove = new Move();
      secondMove.id = "22";
      secondMove.intermodalUnitId = secondContainer.id;
      secondMove.from = secondFrom;
      secondMove.to = secondTo;
      secondMove.status = MoveStatus.Active;
      secondMove.twinMoveId = "11";

      Segment secondSegment = new Segment();
      secondSegment.id = "2";
      secondSegment.moveId = secondMove.id;
      secondSegment.intermodalUnitId = secondContainer.id;
      secondSegment.from = secondMove.from;
      secondSegment.to = secondMove.to;
      secondSegment.status = secondStatus;
      secondSegment.intermodalUnit = secondContainer;
      secondSegment.move = secondMove;

      notCompleteSegment.add(secondSegment);
    }

    return notCompleteSegment.toArray(new Segment[0]);
  }

  /**
   * Gets incomplete twin segments.
   *
   * @param firstContainer First container.
   * @param secondContainer Second container.
   * @param firstStatus First segment status.
   * @param secondStatus Second segment status.
   * @return Incomplete segments.
   */
  protected Segment[] getIncompleteTwinSegments(
      IntermodalUnit firstContainer,
      IntermodalUnit secondContainer,
      MoveSegmentStatus firstStatus,
      MoveSegmentStatus secondStatus) {

    return getIncompleteTwinSegments(
        firstContainer,
        secondContainer,
        firstStatus,
        secondStatus,
        getYardLocation(block, row, stack, tier),
        getYardLocation(block + "END", row, stack, tier),
        getYardLocation(secondBlock, row, stack, tier),
        getYardLocation(secondBlock + "END", row, stack, tier));
  }

  /**
   * Creates active Utr equipment.
   *
   * @return Equipment.
   */
  public Equipment createUtrEquipment() {
    return terminalController.createEquipment(
        new Equipment(utr01, EquipmentLocationKind.truck, utr01, false, true));
  }

  /**
   * Stubs container and segment.
   *
   * @param container Container to stub.
   * @param segment Segment to stub.
   */
  protected void stubTestData(IntermodalUnit container, Segment segment) {
    stubTestData(container);
    stubTestData(segment);
  }

  /**
   * Stubs containers.
   *
   * @param containers Containers collection to stub.
   */
  protected void stubTestData(IntermodalUnit... containers) {
    apiMockService.setJsonResponseForGet(
        containersOnEquipmentUrl(utr01), Arrays.asList(containers));
  }

  /** Stubs empty instructions. */
  protected void stubTestData() {
    stubTestData(createEmptyArray(IntermodalUnit.class));
    stubTestData(createEmptyArray(Segment.class));
  }

  /**
   * Stubs segments.
   *
   * @param segments Segments collection to stub.
   */
  protected void stubTestData(Segment... segments) {
    apiMockService.setJsonResponseForGet(assignedSegmentsUrl(utr01), Arrays.asList(segments));
  }

  /**
   * Stubs equipment.
   *
   * @param equipment Equipment to stub.
   */
  protected void stubTestData(Equipment equipment) {
    apiMockService.setJsonResponseForGet(equipmentUrl(equipment.id), equipment);
  }

  /**
   * Gets main page of Micro-utr application.
   *
   * @return Main page.
   */
  protected MainPage getMainPage() {
    launchApp();

    if (getDriverType() == Chrome && remoteDriverService.isAutoLoginEnabled()) {
      getLoginPage().login(utrProfile, userName, userPassword, apiMockService.getServiceAddress());
    }

    return PageFactory.getPage(MainPage.class);
  }

  /**
   * Gets default Micro-utr profile.
   *
   * @return Default Micro-utr profile.
   */
  protected MicroUtr getDefaultUtrProfile() {
    MicroUtr microUtr = new MicroUtr();
    microUtr.equipmentId = utr01;
    microUtr.deviceId = deviceId;
    microUtr.id = 111111;
    microUtr.type = microutr;
    return microUtr;
  }
}
