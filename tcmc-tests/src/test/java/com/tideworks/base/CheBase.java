package com.tideworks.base;

import static com.tideworks.utilities.api.Che.assignToVesselOperation;
import static com.tideworks.utilities.api.Che.assignToZoneUrl;
import static com.tideworks.utilities.api.Che.containersInRowUrl;
import static com.tideworks.utilities.api.Che.containersToRowUrl;
import static com.tideworks.utilities.api.Che.getBlocksUrl;
import static com.tideworks.utilities.api.Che.getOperationsUrl;
import static com.tideworks.utilities.api.Che.getSelectedContainerUrl;
import static com.tideworks.utilities.api.Che.getUtrsUrl;
import static com.tideworks.utilities.api.Che.getWorklistUrl;
import static com.tideworks.utilities.api.Che.getZonesUrl;
import static com.tideworks.utilities.api.Common.segmentsUrl;
import static com.tideworks.utilities.controllers.enviroment.EnvironmentController.getDriverType;
import static com.tideworks.utilities.services.driver.DriverTypes.Chrome;

import com.tideworks.json.builders.ContainerBuilder;
import com.tideworks.json.objectmodel.BlockKind;
import com.tideworks.json.objectmodel.Che;
import com.tideworks.json.objectmodel.CheGridConfiguration;
import com.tideworks.json.objectmodel.ContainerPlan;
import com.tideworks.json.objectmodel.Equipment;
import com.tideworks.json.objectmodel.EquipmentLocationKind;
import com.tideworks.json.objectmodel.GridColumn;
import com.tideworks.json.objectmodel.IntermodalUnit;
import com.tideworks.json.objectmodel.MoveSegmentStatus;
import com.tideworks.json.objectmodel.ProfileType;
import com.tideworks.json.objectmodel.Segment;
import com.tideworks.json.objectmodel.VesselOperation;
import com.tideworks.json.objectmodel.Zone;
import com.tideworks.json.objectmodel.ZoneKind;
import com.tideworks.json.objectmodel.locations.EquipmentLocation;
import com.tideworks.json.objectmodel.locations.YardLocation;
import com.tideworks.pages.PageFactory;
import com.tideworks.pages.che.WorklistPage;
import com.tideworks.pages.che.ZoneViewPage;

import lombok.val;
import lombok.var;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Request stub information.
 *
 * <p>Page: operation-summary
 *
 * <ul>
 *   <li>getVesselOperations
 *   <li>getZones
 *   <li>getBlocks
 * </ul>
 *
 * <p>Page: worklist
 *
 * <ul>
 *   <li>assignToZone
 *   <li>getWorklist
 * </ul>
 *
 * <p>Page: YardView(ErvView/HeapView)
 *
 * <ul>
 *   <li>selectSegment
 *   <li>deselectSegment
 *   <li>updateContainerLocation
 *   <li>getContainersInYardRow
 *   <li>getUtrs
 *   <li>getSelectedContainer
 *   <li>getContainersInYardRow
 * </ul>
 */
public class CheBase extends Base {

  protected final String block = "Block";
  protected final String heapBlock = "Heap";
  protected final String zoneId = "Zone1";
  protected final String vesselOperationId = "Operation1";
  protected final String equipmentId = "CHE01";
  protected String truck1 = "UTR1";
  protected Che cheProfile;

  @BeforeClass
  public void beforeCheClass() {
    cheProfile = getDefaultCheProfile();
    remoteDriverService.setAutoLoginEnabled(true);
  }

  @BeforeMethod
  public void createTerminal() {
    terminalController.createBlock(
        blockBuilder ->
            blockBuilder
                .ofKind(BlockKind.Grounded)
                .withName(block)
                .withRow("A", 5, Arrays.asList("A", "B", "C", "D", "E"))
                .withRow("B", 5, Arrays.asList("A", "B", "C", "D", "E"))
                .withRow("C", 5, Arrays.asList("A", "B", "C", "D", "E")));

    terminalController.createBlock(
        blockBuilder -> blockBuilder.ofKind(BlockKind.GroundedHeap).withName(heapBlock));
    createUtrEquipment();
  }

  @BeforeMethod
  public void beforeCheMethod() {
    stubProfileData(cheProfile);
  }

  @AfterMethod
  public void resetBuilders() {
    ContainerBuilder.reset();
  }

  /**
   * Creates work-list page.
   *
   * @return Work-list page object.
   */
  protected WorklistPage getWorklistPage() {
    launchApp();

    if (getDriverType() == Chrome && remoteDriverService.isAutoLoginEnabled()) {
      getLoginPage().login(cheProfile, userName, userPassword, apiMockService.getServiceAddress());
    }

    return PageFactory.getPage(WorklistPage.class);
  }

  /**
   * Creates zone view page.
   *
   * @return Zone view page object.
   */
  protected ZoneViewPage getZoneViewPage() {
    launchApp();

    if (getDriverType() == Chrome && remoteDriverService.isAutoLoginEnabled()) {
      getLoginPage().login(cheProfile, userName, userPassword, apiMockService.getServiceAddress());
    }

    return PageFactory.getPage(ZoneViewPage.class);
  }

  /**
   * Creates default profile for Che application.
   *
   * @return Che application profile.
   */
  protected Che getDefaultCheProfile() {
    Che che = new Che();
    che.id = 1;
    che.equipmentId = equipmentId;
    che.deviceId = deviceId;
    che.type = ProfileType.che;
    che.selfAssign = true;
    che.splitWorklist = false;
    che.blockPicker = true;
    che.escalatedMovesFilter = true;
    return che;
  }

  /**
   * Updates default profile for Che application with addition columns at worklist.
   *
   * @param worklistColumns Work list column to be displayed.
   */
  protected void getExtendedProfile(List<GridColumn> worklistColumns) {
    var cheGridConfiguration = new CheGridConfiguration();
    cheGridConfiguration.id = "worklist";
    cheGridConfiguration.columns = worklistColumns;

    cheProfile.gridConfigurations = new ArrayList<>();
    cheProfile.gridConfigurations.add(cheGridConfiguration);
  }

  /**
   * Creates active Utr equipment.
   *
   * @return Equipment.
   */
  public Equipment createUtrEquipment() {
    return createUtrEquipment(truck1);
  }

  /**
   * Creates active Utr equipment with name.
   *
   * @param utrName Utr name.
   * @return Equipment.
   */
  public Equipment createUtrEquipment(String utrName) {
    return terminalController.createEquipment(
        new Equipment(utrName, EquipmentLocationKind.truck, utrName, false, true));
  }

  /**
   * Gets default operation.
   *
   * @return Operation object.
   */
  protected VesselOperation createVesselOperation() {
    return terminalController.createVesselOperation(
        s ->
            s.withId(vesselOperationId)
                .withVessel("VESSEL1")
                .withCrane("CRANE1")
                .withIsActive(true)
                .withGang("GANG1")
                .withShift("SHIFT1")
                .withDateTime(new Date())
                .withAssignedEquipment(new String[] {equipmentId}));
  }

  /**
   * Creates zone.
   *
   * @param name Zone name
   * @param zoneKind Kind of zone
   * @return Zone object.
   */
  protected Zone createZone(String name, ZoneKind zoneKind) {
    return terminalController.createZone(
        s ->
            s.withId(name)
                .withKind(zoneKind)
                .withEquipmentCount(terminalController.getTerminal().getEquipments().size())
                .withYardMoveCount(terminalController.getTerminal().getMoves().size())
                .withUnloadMoveCount(0)
                .withLoadMoveCount(0)
                .withStowMoveCount(0)
                .withSelfAssign(true)
                .withSplitWorklist(true)
                .withAssignedEquipment(Collections.singletonList(equipmentId)));
  }

  /**
   * Stub data for selected container.
   *
   * @param containerPlan Plan for container
   */
  protected void stubTestData(ContainerPlan containerPlan) {
    apiMockService.setJsonResponseForGet(getUtrsUrl(), terminal.getEquipments());
    apiMockService.setJsonResponseForPut(
        segmentsUrl("(.*)\\"), apiMockService.response().withStatus(200).build(), null, null, true);
    apiMockService.setJsonResponseForPut(
        segmentsUrl("(.*)\\"),
        apiMockService.response().withStatus(200).build(),
        equipmentId,
        null,
        true);
    apiMockService.setJsonResponseForPost(
        assignToVesselOperation(vesselOperationId),
        apiMockService.response().withStatus(200).build());
    apiMockService.setJsonResponseForPost(
        assignToZoneUrl("(.*)\\"),
        apiMockService.response().withStatus(200).withBody("OK").build(),
        String.format("{\"equipmentId\":\"%s\",\"context\":\"Deliver\"}", equipmentId),
        null,
        true);
    apiMockService.setJsonResponseForPost(
        assignToZoneUrl("(.*)\\"),
        apiMockService.response().withStatus(200).withBody("OK").build(),
        String.format("{\"equipmentId\":\"%s\",\"context\":\"Receive\"}", equipmentId),
        null,
        true);
    apiMockService.setJsonResponseForPost(
        assignToZoneUrl("(.*)\\"),
        apiMockService.response().withStatus(200).withBody("OK").build(),
        String.format("{\"equipmentId\":\"%s\"}", equipmentId),
        null,
        true);
    apiMockService.setJsonResponseForGet(
        assignToZoneUrl(cheProfile.equipmentId), terminal.getZones());
    stubTerminalTestData();
    apiMockService.setJsonResponseForGet(
        getWorklistUrl(cheProfile.equipmentId),
        terminalController
            .getSegmentsExpandContainerAndMove()
            .stream()
            .filter(
                s -> s.status == MoveSegmentStatus.Selected || s.status == MoveSegmentStatus.Active)
            .toArray());
    apiMockService.setJsonResponseForGet(
        getSelectedContainerUrl(containerPlan.intermodalUnit.id),
        terminalController.getContainerEmbedMovesAndSegments(containerPlan.intermodalUnit));

    if (containerPlan.intermodalUnit.location instanceof YardLocation) {
      stubGetContainersInYardRow((YardLocation) containerPlan.intermodalUnit.location);
    }
    if (containerPlan.intermodalUnit.location instanceof EquipmentLocation) {
      terminalController
          .getContainerEmbedMovesAndSegments(containerPlan.intermodalUnit)
          .segments
          .stream()
          .filter(segment -> segment.to instanceof YardLocation)
          .findFirst()
          .ifPresent(
              toYardLocationSegment ->
                  stubGetSegmentsToYardRow((YardLocation) toYardLocationSegment.to));
    }
  }

  /** Stubs terminal data: zones, operations and blocks. */
  protected void stubTerminalTestData() {
    apiMockService.setJsonResponseForGet(getZonesUrl(), terminal.getZones());
    apiMockService.setJsonResponseForGet(getOperationsUrl(), terminal.getVesselOperations());
    apiMockService.setJsonResponseForGet(getBlocksUrl(), terminal.getBlocks());
  }

  /**
   * Stubs get containers in yard row request.
   *
   * @param yardLocation Yard location to stub for.
   */
  protected void stubGetContainersInYardRow(YardLocation yardLocation) {
    val containersEmbedMovesAndSegments =
        terminalController
            .getContainersEmbedMovesAndSegments()
            .stream()
            .filter(
                container ->
                    container.location instanceof YardLocation
                            && ((YardLocation) container.location).isInSameRow(yardLocation)
                        || container
                            .segments
                            .stream()
                            .anyMatch(
                                segment ->
                                    segment.to instanceof YardLocation
                                        && ((YardLocation) segment.to).isInSameRow(yardLocation)))
            .collect(Collectors.toList());

    if (yardLocation.isHeap()) {
      apiMockService.setJsonResponseForGet(
          containersInRowUrl(yardLocation.block), containersEmbedMovesAndSegments);
      apiMockService.setJsonResponseForGet(
          containersToRowUrl(yardLocation.block), createEmptyArray(Segment.class));
    } else {
      apiMockService.setJsonResponseForGet(
          containersInRowUrl(yardLocation.block, yardLocation.row),
          containersEmbedMovesAndSegments);
      apiMockService.setJsonResponseForGet(
          containersToRowUrl(yardLocation.block, yardLocation.row),
          createEmptyArray(Segment.class));
    }
  }

  /**
   * Stubs get containers in yard row request.
   *
   * @param yardLocation Yard location to stub for.
   */
  protected void stubGetSegmentsToYardRow(YardLocation yardLocation) {
    val segmentsEmbedMovesAndSegments =
        terminalController
            .getContainersEmbedMovesAndSegments()
            .stream()
            .filter(
                container ->
                    container
                        .segments
                        .stream()
                        .anyMatch(
                            segment ->
                                segment.to instanceof YardLocation
                                    && ((YardLocation) segment.to).isInSameRow(yardLocation)))
            .map(container -> container.segments)
            .flatMap(List::stream)
            .map(terminalController::getSegmentExpandContainerAndMove)
            .collect(Collectors.toList());

    if (yardLocation.isHeap()) {
      apiMockService.setJsonResponseForGet(
          containersInRowUrl(yardLocation.block), createEmptyArray(IntermodalUnit.class));
      apiMockService.setJsonResponseForGet(
          containersToRowUrl(yardLocation.block), segmentsEmbedMovesAndSegments);
    } else {
      apiMockService.setJsonResponseForGet(
          containersInRowUrl(yardLocation.block, yardLocation.row),
          createEmptyArray(IntermodalUnit.class));
      apiMockService.setJsonResponseForGet(
          containersToRowUrl(yardLocation.block, yardLocation.row), segmentsEmbedMovesAndSegments);
    }
  }

  /**
   * Formats block and row information to how it should looks in end row view page.
   *
   * @param block Block name.
   * @param row Row name.
   * @return Formatted string.
   */
  protected String formatExpectedErvBlockHeader(String block, String row) {
    return String.format("%s %s (%s)", block, row, row);
  }
}
