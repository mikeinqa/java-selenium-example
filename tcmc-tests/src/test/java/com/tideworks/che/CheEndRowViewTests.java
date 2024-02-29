package com.tideworks.che;

import static com.tideworks.utilities.api.Che.getSelectedContainerUrl;
import static com.tideworks.utilities.api.Che.updateContainerLocationUrl;
import static com.tideworks.utilities.api.Common.segmentsUrl;
import static com.tideworks.verifications.che.Verifications.cellContent;
import static com.tideworks.verifications.che.Verifications.cellContentIsBlank;
import static com.tideworks.verifications.che.Verifications.containerBorderColor;
import static com.tideworks.verifications.che.Verifications.containerCellBackgroundColor;
import static com.tideworks.verifications.che.Verifications.containersFields;
import static com.tideworks.verifications.che.Verifications.displayedContainers;
import static com.tideworks.verifications.che.Verifications.elementDisplayedAfterScroll;
import static com.tideworks.verifications.che.Verifications.header;
import static com.tideworks.verifications.che.Verifications.labelAlignToStack;
import static com.tideworks.verifications.che.Verifications.setAsideCellBackgroundColor;
import static com.tideworks.verifications.che.Verifications.setAsideCellBorderColor;
import static com.tideworks.verifications.che.Verifications.setAsideContainersCount;
import static com.tideworks.verifications.che.Verifications.truckPickerButtonColor;
import static com.tideworks.verifications.che.Verifications.truckPickerButtonIsEnable;
import static com.tideworks.verifications.che.Verifications.utrBackgroundColor;
import static com.tideworks.verifications.che.Verifications.utrBorderColor;
import static com.tideworks.verifications.che.Verifications.utrIsPresent;
import static com.tideworks.verifications.elements.Verifications.utrIsPresentInDialog;

import com.tideworks.base.CheBase;
import com.tideworks.json.builders.ContainerBuilder;
import com.tideworks.json.builders.SegmentBuilder;
import com.tideworks.json.objectmodel.BlockKind;
import com.tideworks.json.objectmodel.ContainerPlan;
import com.tideworks.json.objectmodel.Equipment;
import com.tideworks.json.objectmodel.EquipmentLocationKind;
import com.tideworks.json.objectmodel.IntermodalUnit;
import com.tideworks.json.objectmodel.MoveSegmentStatus;
import com.tideworks.json.objectmodel.MoveStatus;
import com.tideworks.json.objectmodel.SegmentKind;
import com.tideworks.json.objectmodel.locations.EquipmentLocation;
import com.tideworks.json.objectmodel.locations.GateLocation;
import com.tideworks.json.objectmodel.locations.Location;
import com.tideworks.json.objectmodel.locations.YardLocation;
import com.tideworks.pages.che.yardview.EndRowViewPage;
import com.tideworks.pages.elements.dialogs.SelectUtrDialog;
import com.tideworks.utilities.annotations.RunForDriver;
import com.tideworks.utilities.css.CssRgbColors;
import com.tideworks.utilities.css.CssRgbaColors;
import com.tideworks.utilities.services.driver.DriverTypes;

import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import lombok.val;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/** Tests for Che heap-view page. */
public class CheEndRowViewTests extends CheBase {

  private final String planedMoveBadge = "P";

  /**
   * Move types data provider.
   *
   * @return set of parametrized data
   */
  @DataProvider(name = "Move types")
  public static Object[][] locationTypes() {
    return new Object[][] {
      {SegmentKind.Y, MoveStatus.Active, MoveSegmentStatus.Active, "8205718"},
      {SegmentKind.Y, MoveStatus.Active, MoveSegmentStatus.Inactive, "8205717"},
      {SegmentKind.Y, MoveStatus.Inactive, MoveSegmentStatus.Inactive, "8205716"},
      {SegmentKind.R, MoveStatus.Active, MoveSegmentStatus.Active, "8205715"},
      {SegmentKind.R, MoveStatus.Active, MoveSegmentStatus.Inactive, "8205712"},
      {SegmentKind.R, MoveStatus.Inactive, MoveSegmentStatus.Inactive, "8205710"},
      {SegmentKind.G, MoveStatus.Active, MoveSegmentStatus.Active, "8205709"},
      {SegmentKind.G, MoveStatus.Active, MoveSegmentStatus.Inactive, "8196089"},
      {SegmentKind.G, MoveStatus.Inactive, MoveSegmentStatus.Inactive, "8205714"},
      {SegmentKind.V, MoveStatus.Active, MoveSegmentStatus.Active, "8196091"},
      {SegmentKind.V, MoveStatus.Active, MoveSegmentStatus.Inactive, "8196090"},
      {SegmentKind.V, MoveStatus.Inactive, MoveSegmentStatus.Inactive, "8196092"}
    };
  }

  /**
   * Track selector button color data provider.
   *
   * @return set of parametrized data
   */
  @DataProvider(name = "Track selector button color")
  public static Object[][] verifyTrackSelectorButtonColorData() {
    return new Object[][] {
      {SegmentType.YAlone, false},
      {SegmentType.YUYFirst, true}
    };
  }

  /**
   * Container third segments data provider.
   *
   * @return set of parametrized data
   */
  @DataProvider(name = "Container third segments data provider")
  public static Object[][] containerThirdSegmentsDataProvider() {
    return new Object[][] {{SegmentType.VUYThird}, {SegmentType.RUYThird}, {SegmentType.YUYThird}};
  }

  /**
   * Container first segments data provider.
   *
   * @return set of parametrized data
   */
  @DataProvider(name = "Container first segments data provider")
  public static Object[][] containerFirstSegmentsDataProvider() {
    return new Object[][] {{SegmentType.YUVFirst}, {SegmentType.YURFirst}, {SegmentType.YUYFirst}};
  }

  enum SegmentType {
    YAlone,
    YUYThird,
    RUYThird,
    VUYThird,
    inGAlone,
    outGAlone,
    YUYFirst,
    YURFirst,
    YUVFirst,
    YUVFirstWithOutUTR,
    YUYFirstWithOutUTR,
    YURFirstWithOutUTR
  }

  @Test(dataProvider = "Move types")
  @Issue("TCL-15907")
  @TmsLink("8205718")
  @TmsLink("8205717")
  @TmsLink("8205716")
  @TmsLink("8205715")
  @TmsLink("8205712")
  @TmsLink("8205710")
  @TmsLink("8205709")
  @TmsLink("8196089")
  @TmsLink("8205714")
  @TmsLink("8196091")
  @TmsLink("8196090")
  @TmsLink("8196092")
  public void cheErvMoveTypeIndicator(
      SegmentKind segmentKind,
      MoveStatus moveStatus,
      MoveSegmentStatus segmentStatus,
      String testCase) {

    val containerPlan = getContainerPlan(segmentKind, moveStatus, segmentStatus);
    val containerActive =
        getContainerPlan(segmentKind, MoveStatus.Active, MoveSegmentStatus.Active);

    stubTestData(containerPlan);

    apiMockService.setJsonResponseForGet(
        getSelectedContainerUrl(containerActive.intermodalUnit.id),
        terminalController.getContainerEmbedMovesAndSegments(containerActive.intermodalUnit));

    getWorklistPage()
        .<EndRowViewPage>selectContainer(containerActive.intermodalUnit)
        .verify(displayedContainers(terminalController.getContainersExpandMoveAndSegment()));
  }

  @Test
  @Issue("TCL-14641")
  @TmsLink("7423641")
  @TmsLink("8764845")
  public void cheErvCompleteContainer() {
    val containerPlan = terminalController.planContainerInYardFromYardToHeap();

    val selectedContainer =
        terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit);

    stubTestData(containerPlan);

    val updateContainerLocationRequest =
        apiMockService.setJsonResponseForPut(
            updateContainerLocationUrl(selectedContainer.id),
            apiMockService.response().withStatus(200).build(),
            selectedContainer.segment.to);

    getWorklistPage().<EndRowViewPage>selectContainer(selectedContainer).completeContainer();

    apiMockService.verify(updateContainerLocationRequest);
  }

  @Test
  @Issue("TCL-14641")
  @TmsLink("7423642")
  public void cheErvDeselectContainer() {
    val containerPlan = terminalController.planContainerInYardFromYardToHeap();
    val selectedContainer =
        terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit);

    stubTestData(containerPlan);

    val deselectContainerRequest =
        apiMockService.setJsonResponseForPut(
            segmentsUrl(String.valueOf(selectedContainer.segment.id)),
            apiMockService.response().withStatus(200).build(),
            "");

    getWorklistPage().<EndRowViewPage>selectContainer(selectedContainer).deselectContainer();

    apiMockService.verify(deselectContainerRequest);
  }

  @Test
  @Issue("TCL-15685")
  @TmsLink("7380985")
  public void cheErvPlannedMoveIndicator() {
    val containerInYard = terminalController.createContainerInYard();
    val toLocation = terminalController.createYardLocation();

    val containerPlan =
        terminalController.planContainer(
            containerInYard,
            toLocation,
            MoveStatus.Active,
            toLocation,
            MoveSegmentStatus.Active,
            true);

    stubTestData(containerPlan);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(toLocation.row, toLocation.row)
        .verify(cellContent(toLocation, planedMoveBadge));
  }

  @Test
  @Issue("TCL-15704")
  @TmsLink("7380976")
  public void cheErvStackLabelsAlignCorrectly() {
    val uniqueStackName = "STACK";
    val blockName = "1000StacksBlock";
    val stacks = new ArrayList<String>();
    IntStream.range(1, 1000).forEach(s -> stacks.add(String.valueOf(s)));
    stacks.add(uniqueStackName);
    terminalController.createBlock(
        s -> s.ofKind(BlockKind.Grounded).withName(blockName).withRow("A", 1, stacks));

    val containerInYard =
        terminalController.createContainer(new YardLocation(blockName, "A", uniqueStackName, 1));
    val toLocation = terminalController.createYardLocation();
    val containerPlan =
        terminalController.planContainer(
            containerInYard,
            toLocation,
            MoveStatus.Active,
            toLocation,
            MoveSegmentStatus.Active,
            true);

    stubTestData(containerPlan);

    getWorklistPage()
        .<EndRowViewPage>selectContainer(containerPlan.intermodalUnit)
        .verify(labelAlignToStack(uniqueStackName));
  }

  @Test
  @Issue("TCL-14643")
  @TmsLink("6585734")
  public void cheErvHeaderForSelectedContainer() {
    val containerPlan = terminalController.planContainerInYardFromYardToHeap();
    val selectedContainer =
        terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit);

    stubTestData(containerPlan);
    val selectedRow = ((YardLocation) selectedContainer.location).row;
    val expectedErvHeader = String.format("%s %s (%s)", block, selectedRow, selectedRow);

    getWorklistPage()
        .<EndRowViewPage>selectContainer(selectedContainer)
        .verify(header(selectedContainer, expectedErvHeader))
        .deselectContainer()
        .verify(header(null, expectedErvHeader))
        .selectContainer(selectedContainer)
        .verify(header(selectedContainer, expectedErvHeader));
  }

  @Test
  @Issue("TCL-14643")
  @TmsLink("6585736")
  public void cheErvGridUpdatesAutomatically() {
    val containerPlan = terminalController.planContainerInYardFromYardToHeap();
    val selectedContainer =
        terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit);

    terminalController.createContainerInYard();
    terminalController.createContainerInYard();
    terminalController.createContainerInYard();
    val containerToRemove = terminalController.createContainerInYard();

    stubTestData(containerPlan);

    val ervPage =
        getWorklistPage()
            .<EndRowViewPage>selectContainer(selectedContainer)
            .verify(displayedContainers(terminalController.getContainersExpandMoveAndSegment()));

    terminalController.getTerminal().remove(containerToRemove);
    stubTestData(containerPlan);

    ervPage.verify(displayedContainers(terminalController.getContainersExpandMoveAndSegment()));
  }

  @Test
  @Issue("TCL-15475")
  @TmsLink("7381041")
  public void cheErv_displaysAllAvailableUtrs() {
    val containerPlan = createContainersWithUtrs();
    val selectedContainer =
        terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit);
    stubTestData(containerPlan);

    getWorklistPage()
        .<EndRowViewPage>selectContainer(selectedContainer)
        .verify(displayedContainers(terminalController.getContainersExpandMoveAndSegment()))
        .verify(utrIsPresent(terminalController.getTerminal().getEquipments()));
  }

  @Test
  @Issue("TCL-15475")
  @TmsLink("7381041")
  public void cheTruckPicker_displaysAllAvailableUtrs() {
    val containerPlan = createContainersWithUtrs();
    val selectedContainer =
        terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit);
    stubTestData(containerPlan);

    getWorklistPage()
        .<EndRowViewPage>selectContainer(selectedContainer)
        .verify(displayedContainers(terminalController.getContainersExpandMoveAndSegment()))
        .clickTruckPickerButton()
        .verify(utrIsPresentInDialog(terminalController.getTerminal().getEquipments()));
  }

  @Test
  @Issue("TCL-15475")
  @TmsLink("7381039")
  public void cheErv_displaysNoUtrsInTruckList() {
    val containerPlan = terminalController.planContainerInYardFromYardToHeap();
    containerPlan.segments.get(0).to = terminalController.createYardLocation();
    val selectedContainer =
        terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit);
    stubTestData(containerPlan);

    getWorklistPage()
        .<EndRowViewPage>selectContainer(selectedContainer)
        .verify(displayedContainers(terminalController.getContainersExpandMoveAndSegment()))
        .verify(utrIsPresent(Collections.emptyList()))
        .verify(truckPickerButtonIsEnable(false));
  }

  @Test
  @Issue("TCL-15555")
  @Issue("TCL-16205")
  @TmsLink("8338722")
  public void cheErv_containerPlannedFromUtrToYard_plannedLocationChangedToHeap() {
    val truckLocation = terminalController.createTruckLocation();
    val fromLocation = new YardLocation(block, "A", "A", 1);
    val toLocation = new YardLocation(block, "B", "A", 1);
    val container = terminalController.createContainer(truckLocation);
    val newPlannedLocation = terminalController.createHeapLocation();

    val containerPlan =
        createContainerPlan(container, fromLocation, toLocation, truckLocation, toLocation);
    val selectedContainer =
        terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit);

    stubTestData(containerPlan);
    stubGetContainersInYardRow(toLocation);
    stubGetContainersInYardRow(newPlannedLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(toLocation)
        .verify(cellContent(toLocation, planedMoveBadge))
        .selectTruck(truck1)
        .verify(header(selectedContainer, formatExpectedErvBlockHeader(block, toLocation.row)))
        .clickBlockPickerButton()
        .selectHeapBlock(heapBlock)
        .verify(containersFields(terminalController.getContainersExpandMoveAndSegment()))
        .verify(
            header(
                ContainerBuilder.create()
                    .withContainer(container)
                    .withSegment(SegmentBuilder.create().withTo(newPlannedLocation).build())
                    .build(),
                heapBlock));
  }

  @Test
  @Issue("TCL-15555")
  @Issue("TCL-16205")
  @TmsLink("8338721")
  public void cheErv_containerPlannedFromYardToUtr_plannedLocationChangedToHeap() {
    val truckLocation = terminalController.createTruckLocation();
    val fromLocation = new YardLocation(block, "A", "A", 1);
    val toLocation = new YardLocation(block, "B", "A", 1);
    val container = terminalController.createContainer(fromLocation);
    val newPlannedLocation = terminalController.createHeapLocation();

    val containerPlan =
        createContainerPlan(container, fromLocation, toLocation, fromLocation, truckLocation);
    val selectedContainer =
        terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit);

    stubTestData(containerPlan);
    stubGetContainersInYardRow(newPlannedLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(fromLocation)
        .verify(cellContent(selectedContainer))
        .selectContainer(selectedContainer)
        .verify(header(selectedContainer, formatExpectedErvBlockHeader(block, fromLocation.row)))
        .clickBlockPickerButton()
        .selectHeapBlock(heapBlock)
        .verify(containersFields(terminalController.getContainersExpandMoveAndSegment()))
        .verify(
            header(
                ContainerBuilder.create()
                    .withContainer(container)
                    .withSegment(SegmentBuilder.create().withTo(newPlannedLocation).build())
                    .build(),
                heapBlock));
  }

  @Test
  @Issue("TCL-15555")
  @Issue("TCL-16205")
  @TmsLink("8338720")
  public void cheErv_containerPlannedWithinCurrentRow_plannedLocationChangedToHeap() {
    val fromLocation = new YardLocation(block, "A", "A", 1);
    val toLocation = new YardLocation(block, "A", "B", 1);
    val container = terminalController.createContainer(fromLocation);
    val newPlannedLocation = terminalController.createHeapLocation();

    val containerPlan =
        createContainerPlan(container, fromLocation, toLocation, fromLocation, toLocation);
    val selectedContainer =
        terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit);

    stubTestData(containerPlan);
    stubGetContainersInYardRow(newPlannedLocation);

    getWorklistPage()
        .<EndRowViewPage>selectContainer(selectedContainer)
        .verify(cellContent(selectedContainer))
        .verify(cellContent(toLocation, planedMoveBadge))
        .verify(header(selectedContainer, formatExpectedErvBlockHeader(block, toLocation.row)))
        .clickBlockPickerButton()
        .selectHeapBlock(heapBlock)
        .verify(containersFields(terminalController.getContainersExpandMoveAndSegment()))
        .verify(
            header(
                ContainerBuilder.create()
                    .withContainer(container)
                    .withSegment(SegmentBuilder.create().withTo(newPlannedLocation).build())
                    .build(),
                heapBlock));
  }

  @Test
  @Issue("TCL-15555")
  @TmsLink("8338719")
  public void cheErv_containerPlannedFromYardToUtr_plannedLocationChangedToGroundedFineSpot() {
    val truckLocation = terminalController.createTruckLocation();
    val fromLocation = new YardLocation(block, "A", "A", 1);
    val toLocation = new YardLocation(block, "B", "B", 1);
    val container = terminalController.createContainer(fromLocation);
    val newPlannedLocation = new YardLocation(block, "C", "A", 1);

    val containerPlan =
        createContainerPlan(container, fromLocation, toLocation, fromLocation, truckLocation);
    val selectedContainer =
        terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit);

    stubTestData(containerPlan);
    stubGetContainersInYardRow(newPlannedLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(fromLocation)
        .verify(cellContent(selectedContainer))
        .selectContainer(selectedContainer)
        .verify(header(selectedContainer, formatExpectedErvBlockHeader(block, fromLocation.row)))
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(newPlannedLocation)
        .selectCell(newPlannedLocation)
        .verify(
            header(
                ContainerBuilder.create()
                    .withContainer(container)
                    .withSegment(SegmentBuilder.create().withTo(newPlannedLocation).build())
                    .build(),
                formatExpectedErvBlockHeader(block, newPlannedLocation.row)));
  }

  @Test
  @Issue("TCL-15555")
  @TmsLink("8338718")
  public void cheErv_containerPlannedFromUtrToYard_plannedLocationChangedToGroundedFineSpot() {
    val truckLocation = terminalController.createTruckLocation();
    val fromLocation = new YardLocation(block, "A", "A", 1);
    val toLocation = new YardLocation(block, "B", "A", 1);
    val container = terminalController.createContainer(truckLocation);
    val newPlannedLocation = new YardLocation(block, "C", "A", 1);

    val containerPlan =
        createContainerPlan(container, fromLocation, toLocation, truckLocation, toLocation);
    val selectedContainer =
        terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit);

    stubTestData(containerPlan);
    stubGetContainersInYardRow(toLocation);
    stubGetContainersInYardRow(newPlannedLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(toLocation)
        .verify(cellContent(toLocation, planedMoveBadge))
        .selectTruck(truck1)
        .verify(header(selectedContainer, formatExpectedErvBlockHeader(block, toLocation.row)))
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(newPlannedLocation)
        .selectCell(newPlannedLocation)
        .verify(
            header(
                ContainerBuilder.create()
                    .withContainer(container)
                    .withSegment(SegmentBuilder.create().withTo(newPlannedLocation).build())
                    .build(),
                formatExpectedErvBlockHeader(block, newPlannedLocation.row)));
  }

  @Test
  @Issue("TCL-15555")
  @TmsLink("8338717")
  public void cheErv_containerPlannedWithinCurrentRow_plannedLocationChangedToGroundedFineSpot() {
    val fromLocation = new YardLocation(block, "A", "A", 1);
    val toLocation = new YardLocation(block, "A", "B", 1);
    val container = terminalController.createContainer(fromLocation);
    val newPlannedLocation = new YardLocation(block, "B", "A", 1);

    val containerPlan =
        createContainerPlan(container, fromLocation, toLocation, fromLocation, toLocation);
    val selectedContainer =
        terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit);

    stubTestData(containerPlan);
    stubGetContainersInYardRow(toLocation);
    stubGetContainersInYardRow(newPlannedLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(toLocation)
        .verify(cellContent(selectedContainer))
        .verify(cellContent(toLocation, planedMoveBadge))
        .selectContainer(selectedContainer)
        .verify(header(selectedContainer, formatExpectedErvBlockHeader(block, toLocation.row)))
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(newPlannedLocation)
        .selectCell(newPlannedLocation)
        .verify(
            header(
                ContainerBuilder.create()
                    .withContainer(container)
                    .withSegment(SegmentBuilder.create().withTo(newPlannedLocation).build())
                    .build(),
                formatExpectedErvBlockHeader(block, "B")));
  }

  @Test
  @Issue("TCL-15555")
  @TmsLink("8338716")
  public void cheErv_containerPlannedFromYardToUtr_plannedLocationChangedWithinCurrentRow() {
    val truckLocation = terminalController.createTruckLocation();
    val fromLocation = new YardLocation(block, "A", "A", 1);
    val toLocation = new YardLocation(block, "B", "A", 1);
    val container = terminalController.createContainer(fromLocation);
    val newPlannedLocation = new YardLocation(block, "A", "C", 1);

    val containerPlan =
        createContainerPlan(container, fromLocation, toLocation, fromLocation, truckLocation);
    val selectedContainer =
        terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit);

    stubTestData(containerPlan);
    stubGetContainersInYardRow(toLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(fromLocation)
        .verify(cellContent(selectedContainer))
        .selectContainer(selectedContainer)
        .verify(header(selectedContainer, formatExpectedErvBlockHeader(block, fromLocation.row)))
        .selectCell(newPlannedLocation)
        .verify(
            header(
                ContainerBuilder.create()
                    .withContainer(container)
                    .withSegment(SegmentBuilder.create().withTo(newPlannedLocation).build())
                    .build(),
                formatExpectedErvBlockHeader(block, newPlannedLocation.row)));
  }

  @Test
  @Issue("TCL-15555")
  @TmsLink("8338715")
  public void cheErv_containerPlannedFromUtrToYard_plannedLocationChangedWithinCurrentRow() {
    val truckLocation = terminalController.createTruckLocation();
    val fromLocation = new YardLocation(block, "A", "A", 1);
    val toLocation = new YardLocation(block, "B", "A", 1);
    val container = terminalController.createContainer(truckLocation);
    val newPlannedLocation = new YardLocation(block, "B", "C", 1);

    val containerPlan =
        createContainerPlan(container, fromLocation, toLocation, truckLocation, toLocation);
    val selectedContainer =
        terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit);

    stubTestData(containerPlan);
    stubGetContainersInYardRow(toLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(toLocation)
        .verify(cellContent(toLocation, planedMoveBadge))
        .selectTruck(truck1)
        .verify(header(selectedContainer, formatExpectedErvBlockHeader(block, toLocation.row)))
        .selectCell(newPlannedLocation)
        .verify(
            header(
                ContainerBuilder.create()
                    .withContainer(container)
                    .withSegment(SegmentBuilder.create().withTo(newPlannedLocation).build())
                    .build(),
                formatExpectedErvBlockHeader(block, newPlannedLocation.row)));
  }

  @Test
  @Issue("TCL-15555")
  @TmsLink("8338714")
  public void cheErv_containerPlannedWithinCurrentRow_plannedLocationChangedWithinCurrentRow() {
    val fromLocation = new YardLocation(block, "A", "A", 1);
    val toLocation = new YardLocation(block, "A", "B", 1);
    val container = terminalController.createContainer(fromLocation);
    val newPlannedLocation = new YardLocation(block, "A", "C", 1);

    val containerPlan =
        createContainerPlan(container, fromLocation, toLocation, fromLocation, toLocation);
    val selectedContainer =
        terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit);

    stubTestData(containerPlan);
    stubGetContainersInYardRow(toLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(toLocation)
        .verify(cellContent(toLocation, planedMoveBadge))
        .selectContainer(selectedContainer)
        .verify(header(selectedContainer, formatExpectedErvBlockHeader(block, toLocation.row)))
        .selectCell(newPlannedLocation)
        .verify(
            header(
                ContainerBuilder.create()
                    .withContainer(container)
                    .withSegment(SegmentBuilder.create().withTo(newPlannedLocation).build())
                    .build(),
                formatExpectedErvBlockHeader(block, newPlannedLocation.row)));
  }

  @Test(dataProvider = "Track selector button color")
  @Issue("TCL-16185")
  @TmsLink("8216615")
  public void cheErv_verifyTrackSelectorButtonColor(
      SegmentType segmentKind, Boolean buttonIsEnable) {

    val containerPlan = getContainerPlan(segmentKind);

    stubTestData(containerPlan);
    if (buttonIsEnable) {
      stubGetContainersInYardRow((YardLocation) containerPlan.intermodalUnit.location);
    }

    getWorklistPage()
        .<EndRowViewPage>selectContainer(containerPlan.intermodalUnit)
        .verify(truckPickerButtonIsEnable(buttonIsEnable))
        .verify(truckPickerButtonColor())
        .deselectContainer()
        .verify(truckPickerButtonIsEnable(false))
        .verify(truckPickerButtonColor());
  }

  @Test(dataProvider = "Container third segments data provider")
  @Issue("TCL-15556")
  @Issue("TCL-16547")
  @TmsLink("8183419")
  public void cheErv_selectedContainerOnUtrPlannedToGrounded_verifyUtrAndContainerColors(
      SegmentType segmentKind) {
    val containerPlan = getContainerPlan(segmentKind);
    YardLocation yardLocation = (YardLocation) containerPlan.move.to;
    String utrName = ((EquipmentLocation) containerPlan.intermodalUnit.location).equipmentId;

    stubTestData(containerPlan);
    stubGetSegmentsToYardRow(yardLocation);

    getWorklistPage()
        .<EndRowViewPage>selectContainer(containerPlan.intermodalUnit)
        .verify(cellContent(yardLocation, planedMoveBadge))
        .verify(containerBorderColor(yardLocation, CssRgbColors.Blue))
        .verify(utrBackgroundColor(utrName, CssRgbaColors.Aqua))
        .verify(utrBorderColor(utrName, CssRgbColors.Orange))
        .verify(truckPickerButtonIsEnable(false))
        .deselectContainer()
        .selectTruck(utrName)
        .verify(cellContent(yardLocation, planedMoveBadge))
        .verify(containerBorderColor(yardLocation, CssRgbColors.Blue))
        .verify(utrBackgroundColor(utrName, CssRgbaColors.Aqua))
        .verify(utrBorderColor(utrName, CssRgbColors.Orange))
        .verify(truckPickerButtonIsEnable(false));
  }

  @Test
  @Issue("TCL-15556")
  @Issue("TCL-16547")
  @Issue("TCL-15475")
  @TmsLink("8183424")
  @TmsLink("7381039")
  public void cheErv_notSelectedContainerOnUtrPlannedToGround_verifyUtrAndContainerColors() {
    val containerPlan = terminalController.planContainerOnUtrFromYardToYard();
    YardLocation yardLocation = (YardLocation) containerPlan.move.to;
    String utrName = ((EquipmentLocation) containerPlan.intermodalUnit.location).equipmentId;

    stubTestData(containerPlan);
    stubGetSegmentsToYardRow(yardLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(yardLocation.block)
        .selectBlockRow(yardLocation)
        .verify(cellContent(yardLocation, planedMoveBadge))
        .verify(containerBorderColor(yardLocation))
        .verify(utrBackgroundColor(utrName, CssRgbaColors.Yellow))
        .verify(utrBorderColor(utrName))
        .verify(truckPickerButtonIsEnable(false));
  }

  @Test(dataProvider = "Container first segments data provider")
  @Issue("TCL-15556")
  @Issue("TCL-16547")
  @Issue("TCL-15475")
  @TmsLink("8183426")
  @TmsLink("7381039")
  public void cheErv_notSelectedContainerPlannedToUtr_verifyUtrAndContainerColors(
      SegmentType segmentKind) {
    val containerPlan = getContainerPlan(segmentKind);
    YardLocation yardLocation = (YardLocation) containerPlan.intermodalUnit.location;
    String utrName = ((EquipmentLocation) containerPlan.segments.get(0).to).equipmentId;
    stubTestData(containerPlan);
    stubGetContainersInYardRow(yardLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(yardLocation.block)
        .selectBlockRow(yardLocation)
        .verify(containerCellBackgroundColor(yardLocation, CssRgbaColors.Green))
        .verify(
            cellContent(
                terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit)))
        .verify(containerBorderColor(yardLocation))
        .verify(utrBackgroundColor(utrName, CssRgbaColors.Green))
        .verify(utrBorderColor(utrName))
        .verify(truckPickerButtonIsEnable(false));
  }

  @Test(dataProvider = "Container first segments data provider")
  @Issue("TCL-15556")
  @Issue("TCL-16547")
  @TmsLink("8183425")
  public void cheErv_selectedContainerPlannedToUtr_verifyUtrAndContainerColors(
      SegmentType segmentKind) {
    val containerPlan = getContainerPlan(segmentKind);
    YardLocation yardLocation = (YardLocation) containerPlan.intermodalUnit.location;
    String utrName = ((EquipmentLocation) containerPlan.segments.get(0).to).equipmentId;

    stubTestData(containerPlan);
    stubGetContainersInYardRow(yardLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(yardLocation.block)
        .selectBlockRow(yardLocation)
        .selectCell(yardLocation)
        .verify(
            cellContent(
                terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit)))
        .verify(containerCellBackgroundColor(yardLocation, CssRgbaColors.Aqua))
        .verify(containerBorderColor(yardLocation, CssRgbColors.Orange))
        .verify(utrBackgroundColor(utrName, CssRgbaColors.Aqua))
        .verify(utrBorderColor(utrName, CssRgbColors.Blue))
        .verify(truckPickerButtonIsEnable(true))
        .deselectContainer()
        .selectTruck(utrName)
        .verify(
            cellContent(
                terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit)))
        .verify(containerCellBackgroundColor(yardLocation, CssRgbaColors.Aqua))
        .verify(containerBorderColor(yardLocation, CssRgbColors.Orange))
        .verify(utrBackgroundColor(utrName, CssRgbaColors.Aqua))
        .verify(utrBorderColor(utrName, CssRgbColors.Blue))
        .verify(truckPickerButtonIsEnable(true));
  }

  /** Story TestCase . */
  @Test(dataProvider = "Container first segments data provider")
  @Issue("TCL-15927")
  @TmsLink("8183430")
  public void cheErv_selectedContainerWithOutAssignedUtr_changeUtr_verifyUtrAndContainerColors(
      SegmentType segmentKind) {
    val utrName = "UTR754";
    val utrNameForOtherMove = "UTR15";

    val containerPlan = getContainerPlan(segmentKind);
    val otherContainerPlan = getContainerPlan(segmentKind);
    ((EquipmentLocation) otherContainerPlan.segments.get(0).to).equipmentId = utrNameForOtherMove;
    YardLocation yardLocation = (YardLocation) containerPlan.intermodalUnit.location;
    ((EquipmentLocation) containerPlan.segments.get(0).to).equipmentId = "";

    createUtrEquipment(utrName);
    createUtrEquipment(utrNameForOtherMove);

    stubTestData(containerPlan);
    stubGetContainersInYardRow(yardLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(yardLocation.block)
        .selectBlockRow(yardLocation)
        .selectCell(yardLocation)
        .verify(
            cellContent(
                terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit)))
        .verify(containerCellBackgroundColor(yardLocation, CssRgbaColors.Aqua))
        .verify(containerBorderColor(yardLocation, CssRgbColors.Orange))
        .verify(truckPickerButtonIsEnable(true))
        .clickTruckPickerButton()
        .selectUtr(utrName)
        .verify(
            cellContent(
                terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit)))
        .verify(containerCellBackgroundColor(yardLocation, CssRgbaColors.Aqua))
        .verify(containerBorderColor(yardLocation, CssRgbColors.Orange))
        .verify(utrBackgroundColor(utrName, CssRgbaColors.Aqua))
        .verify(utrBorderColor(utrName, CssRgbColors.Blue))
        .verify(truckPickerButtonIsEnable(true))
        .clickTruckPickerButton()
        .selectUtr(utrNameForOtherMove)
        .verify(
            cellContent(
                terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit)))
        .verify(containerCellBackgroundColor(yardLocation, CssRgbaColors.Aqua))
        .verify(containerBorderColor(yardLocation, CssRgbColors.Orange))
        .verify(utrBackgroundColor(utrNameForOtherMove, CssRgbaColors.Aqua))
        .verify(utrBorderColor(utrNameForOtherMove, CssRgbColors.Blue))
        .verify(truckPickerButtonIsEnable(true));
  }

  @Test(dataProvider = "Container first segments data provider")
  @Issue("TCL-15927")
  @TmsLink("8183428")
  public void cheErv_selectedContainerWithAssignedUtr_changeUtr_verifyUtrAndContainerColors(
      SegmentType segmentKind) {
    val otherUtrName = "UTR754";
    val utrNameForOtherMove = "UTR15";

    val containerPlan = getContainerPlan(segmentKind);
    val otherContainerPlan = getContainerPlan(segmentKind);
    ((EquipmentLocation) otherContainerPlan.segments.get(0).to).equipmentId = utrNameForOtherMove;
    YardLocation yardLocation = (YardLocation) containerPlan.intermodalUnit.location;
    String utrName = ((EquipmentLocation) containerPlan.segments.get(0).to).equipmentId;

    createUtrEquipment(otherUtrName);
    createUtrEquipment(utrNameForOtherMove);

    stubTestData(containerPlan);
    stubGetContainersInYardRow(yardLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(yardLocation.block)
        .selectBlockRow(yardLocation)
        .selectCell(yardLocation)
        .verify(
            cellContent(
                terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit)))
        .verify(containerCellBackgroundColor(yardLocation, CssRgbaColors.Aqua))
        .verify(containerBorderColor(yardLocation, CssRgbColors.Orange))
        .verify(utrBackgroundColor(utrName, CssRgbaColors.Aqua))
        .verify(utrBorderColor(utrName, CssRgbColors.Blue))
        .verify(truckPickerButtonIsEnable(true))
        .clickTruckPickerButton()
        .selectUtr(otherUtrName)
        .verify(
            cellContent(
                terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit)))
        .verify(containerCellBackgroundColor(yardLocation, CssRgbaColors.Aqua))
        .verify(containerBorderColor(yardLocation, CssRgbColors.Orange))
        .verify(utrBackgroundColor(otherUtrName, CssRgbaColors.Aqua))
        .verify(utrBorderColor(otherUtrName, CssRgbColors.Blue))
        .verify(truckPickerButtonIsEnable(true))
        .clickTruckPickerButton()
        .selectUtr(utrNameForOtherMove)
        .verify(
            cellContent(
                terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit)))
        .verify(containerCellBackgroundColor(yardLocation, CssRgbaColors.Aqua))
        .verify(containerBorderColor(yardLocation, CssRgbColors.Orange))
        .verify(utrBackgroundColor(utrNameForOtherMove, CssRgbaColors.Aqua))
        .verify(utrBorderColor(utrNameForOtherMove, CssRgbColors.Blue))
        .verify(truckPickerButtonIsEnable(true));
  }

  @Test
  @Issue("TCL-15556")
  @Issue("TCL-16547")
  @TmsLink("8325797")
  public void cheErv_inactiveContainerInYard_verifyUtrAndContainerColors() {
    val containerPlan =
        getContainerPlan(
            SegmentType.YUYFirst, MoveStatus.Active, MoveSegmentStatus.Inactive, false);
    YardLocation yardLocation = (YardLocation) containerPlan.intermodalUnit.location;
    val containerActive =
        getContainerPlan(SegmentType.YUYFirst, MoveStatus.Active, MoveSegmentStatus.Active, false);
    stubTestData(containerPlan);
    stubGetContainersInYardRow(yardLocation);
    apiMockService.setJsonResponseForGet(
        getSelectedContainerUrl(containerActive.intermodalUnit.id),
        terminalController.getContainerEmbedMovesAndSegments(containerActive.intermodalUnit));

    getWorklistPage()
        .<EndRowViewPage>selectContainer(containerActive.intermodalUnit)
        .deselectContainer()
        .selectContainer(containerPlan.intermodalUnit)
        .verify(containerCellBackgroundColor(yardLocation, CssRgbaColors.LightGray))
        .verify(truckPickerButtonIsEnable(false));
  }

  @Test
  @Issue("TCL-15556")
  @Issue("TCL-16547")
  @TmsLink("8327960")
  public void cheErv_ContainerWithoutMoveInYard_verifyContainerColors() {
    val yardLocation = terminalController.createYardLocation();
    val containerPlan = terminalController.createContainer(yardLocation);

    stubTestData(new ContainerPlan(null, null, containerPlan, null, null));
    stubGetContainersInYardRow(yardLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(yardLocation.block)
        .selectBlockRow(yardLocation)
        .selectCell(yardLocation)
        .verify(containerCellBackgroundColor(yardLocation, CssRgbaColors.DarkGray))
        .verify(containerBorderColor(yardLocation, CssRgbColors.Orange))
        .verify(truckPickerButtonIsEnable(false));
  }

  @Test
  @Issue("TCL-15556")
  @Issue("TCL-16547")
  @TmsLink("8327974")
  public void cheErv_selectContainerOnOtr_verifyUtrAndContainerColors() {
    val containerPlan = getContainerPlan(SegmentType.inGAlone);
    YardLocation yardLocation = (YardLocation) containerPlan.move.to;
    GateLocation gateLocation = (GateLocation) containerPlan.intermodalUnit.location;

    stubTestData(containerPlan);
    stubGetContainersInYardRow(yardLocation);

    getWorklistPage()
        .<EndRowViewPage>selectContainer(containerPlan.intermodalUnit)
        .verify(cellContent(yardLocation, planedMoveBadge))
        .verify(containerBorderColor(yardLocation, CssRgbColors.Blue))
        .verify(utrBackgroundColor(gateLocation.toString(), CssRgbaColors.Aqua))
        .verify(utrBorderColor(gateLocation.toString(), CssRgbColors.Orange))
        .verify(truckPickerButtonIsEnable(false))
        .clickBackButton()
        .clickBlockPickerButton()
        .selectBlock(yardLocation.block)
        .selectBlockRow(yardLocation)
        .selectCell(yardLocation)
        .verify(cellContent(yardLocation, planedMoveBadge))
        .verify(containerBorderColor(yardLocation))
        .verify(truckPickerButtonIsEnable(false))
        .verify(utrBackgroundColor(gateLocation.toString(), CssRgbaColors.Yellow));
  }

  @Test
  @Issue("TCL-15556")
  @Issue("TCL-16547")
  @TmsLink("8327975")
  public void cheErv_selectContainerToOtr_verifyUtrAndContainerColors() {
    val containerPlan = getContainerPlan(SegmentType.outGAlone);
    YardLocation yardLocation = (YardLocation) containerPlan.intermodalUnit.location;
    GateLocation gateLocation = (GateLocation) containerPlan.move.to;

    stubTestData(containerPlan);
    stubGetContainersInYardRow(yardLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(yardLocation.block)
        .selectBlockRow(yardLocation)
        .verify(
            cellContent(
                terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit)))
        .verify(containerCellBackgroundColor(yardLocation, CssRgbaColors.Green))
        .verify(containerBorderColor(yardLocation))
        .verify(truckPickerButtonIsEnable(false))
        .verify(utrBackgroundColor(gateLocation.toString(), CssRgbaColors.Green))
        .verify(utrBorderColor(gateLocation.toString()))
        .selectCell(yardLocation)
        .verify(
            cellContent(
                terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit)))
        .verify(containerCellBackgroundColor(yardLocation, CssRgbaColors.Aqua))
        .verify(containerBorderColor(yardLocation, CssRgbColors.Orange))
        .verify(utrBackgroundColor(gateLocation.toString(), CssRgbaColors.Aqua))
        .verify(utrBorderColor(gateLocation.toString(), CssRgbColors.Blue))
        .verify(truckPickerButtonIsEnable(false))
        .deselectContainer()
        .verify(
            cellContent(
                terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit)))
        .selectTruck(gateLocation.toString())
        .verify(containerCellBackgroundColor(yardLocation, CssRgbaColors.Aqua))
        .verify(containerBorderColor(yardLocation, CssRgbColors.Orange))
        .verify(utrBackgroundColor(gateLocation.toString(), CssRgbaColors.Aqua))
        .verify(utrBorderColor(gateLocation.toString(), CssRgbColors.Blue))
        .verify(truckPickerButtonIsEnable(false));
  }

  @Test
  @Issue("TCL-15927")
  @TmsLink("8353377")
  @TmsLink("8353376")
  @RunForDriver(DriverTypes.Chrome)
  public void cheErv_selectContainer_elementScrollingCorrectly() {
    terminal.getBlocks().clear();

    val uniqueStackName = "STACK";
    val blockName = "1000StacksBlock";

    val stacks =
        IntStream.range(1, 30)
            .mapToObj(String::valueOf)
            .collect(Collectors.toCollection(ArrayList::new));
    stacks.add(uniqueStackName);
    terminalController.createBlock(
        s -> s.ofKind(BlockKind.Grounded).withName(blockName).withRow("A", 20, stacks));

    IntStream.range(0, 30)
        .forEach(
            index -> {
              val container = terminalController.createContainerInYard();
              createContainerPlan(
                  container,
                  container.location,
                  terminalController.createYardLocation(),
                  container.location,
                  new EquipmentLocation(EquipmentLocationKind.truck, truck1));
            });

    val containerPlan = terminalController.getContainerPlan(terminal.getContainers().get(0));

    stubTestData(containerPlan);

    getWorklistPage()
        .<EndRowViewPage>selectContainer(terminal.getContainers().get(0))
        .verify(elementDisplayedAfterScroll(terminal.getContainers().get(0)))
        .verify(
            elementDisplayedAfterScroll(
                terminal.getContainers().get(terminal.getContainers().size() - 1)))
        .waitUntilPageLoaded();
  }

  @Test
  @Issue("TCL-16553")
  @TmsLink("8851351")
  @TmsLink("8918470")
  @RunForDriver(DriverTypes.Chrome)
  public void cheErv_selectUtr_truckPickerRow_elementScrollingCorrectly() {
    IntStream.range(0, 24)
        .forEach(
            index -> {
              val utrName = "UTR" + String.valueOf(index);

              terminalController.createEquipment(
                  equipmentBuilder ->
                      equipmentBuilder
                          .withEnabled(true)
                          .withEquipmentNo(utrName)
                          .withHeld(false)
                          .withKind(EquipmentLocationKind.truck));

              val container = terminalController.createContainerInYard();
              createContainerPlan(
                  container,
                  container.location,
                  terminalController.createYardLocation(),
                  container.location,
                  new EquipmentLocation(EquipmentLocationKind.truck, utrName));
            });

    val containerPlan = terminalController.getContainerPlan(terminal.getContainers().get(0));
    val equipments = terminal.getEquipments();

    stubTestData(containerPlan);

    getWorklistPage()
        .<EndRowViewPage>selectContainer(containerPlan.intermodalUnit)
        .verify(elementDisplayedAfterScroll(equipments.get(0)))
        .verify(elementDisplayedAfterScroll(equipments.get(equipments.size() - 1)))
        .clickTruckPickerButton()
        .<SelectUtrDialog>verify(utrIsPresentInDialog(equipments))
        .selectUtr(equipments.get(equipments.size() - 1).equipmentNo)
        .verify(
            utrBackgroundColor(
                equipments.get(equipments.size() - 1).equipmentNo, CssRgbaColors.Aqua))
        .waitUntilPageLoaded();
  }

  @Test
  @Issue("TCL-16553")
  @TmsLink("8918468")
  @RunForDriver(DriverTypes.Chrome)
  public void cheErv_setDownView_elementScrollingCorrectly() {
    IntStream.range(0, 30)
        .forEach(
            index -> terminalController.createContainer(new YardLocation(block, "A", null, null)));

    val containers = terminal.getContainers();
    val containerPlan = terminalController.getContainerPlan(terminal.getContainers().get(0));

    stubTestData(containerPlan);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow((YardLocation) containerPlan.intermodalUnit.location)
        .verify(elementDisplayedAfterScroll(containers.get(0)))
        .verify(elementDisplayedAfterScroll(containers.get(containers.size() - 1)));
  }

  @Test
  @Issue("TCL-16512")
  @TmsLink("8325796")
  public void cheErv_inactiveMoveInYard_verifyUtrAndContainerColors() {
    val containerPlan =
        getContainerPlan(SegmentKind.Y, MoveStatus.Inactive, MoveSegmentStatus.Inactive);
    YardLocation yardLocation = (YardLocation) containerPlan.intermodalUnit.location;

    stubTestData(containerPlan);
    stubGetContainersInYardRow(yardLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(yardLocation)
        .selectContainer(containerPlan.intermodalUnit)
        .verify(containerCellBackgroundColor(yardLocation, CssRgbaColors.LightGray))
        .verify(containerBorderColor(yardLocation, CssRgbColors.Orange))
        .verify(truckPickerButtonIsEnable(false))
        .verify(
            header(
                containerPlan.intermodalUnit,
                formatExpectedErvBlockHeader(block, yardLocation.row)));
  }

  @Test
  @Issue("TCL-16512")
  @TmsLink("8766834")
  public void cheErv_inactiveMoveInYard_clickOnEmptyCell_verifyUtrAndContainerColors() {
    val containerPlan =
        getContainerPlan(SegmentKind.Y, MoveStatus.Inactive, MoveSegmentStatus.Inactive);
    YardLocation yardLocation = (YardLocation) containerPlan.intermodalUnit.location;
    YardLocation selectedLocation = terminalController.createYardLocation();

    stubTestData(containerPlan);
    stubGetContainersInYardRow(yardLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(yardLocation)
        .selectContainer(containerPlan.intermodalUnit)
        .selectCell(selectedLocation)
        .verify(containerCellBackgroundColor(yardLocation, CssRgbaColors.LightGray))
        .verify(containerBorderColor(yardLocation, CssRgbColors.Orange))
        .verify(truckPickerButtonIsEnable(false))
        .verify(
            header(
                containerPlan.intermodalUnit.id,
                containerPlan.intermodalUnit.weight,
                containerPlan.intermodalUnit.location.toString(),
                selectedLocation.toString(),
                formatExpectedErvBlockHeader(block, yardLocation.row)));
  }

  @Test
  @Issue("TCL-16512")
  @TmsLink("8183427")
  public void cheErv_containerNotPlannedToUtr_verifyUtrAndContainerColors() {
    val containerPlan = getContainerPlan(SegmentType.YAlone);
    YardLocation yardLocation = (YardLocation) containerPlan.intermodalUnit.location;
    YardLocation toLocation = (YardLocation) containerPlan.move.to;

    stubTestData(containerPlan);
    stubGetContainersInYardRow(yardLocation);
    containerPlan.intermodalUnit.segment = containerPlan.segments.get(0);
    containerPlan.intermodalUnit.move = containerPlan.move;

    getWorklistPage()
        .<EndRowViewPage>selectContainer(containerPlan.intermodalUnit)
        .verify(containerCellBackgroundColor(yardLocation, CssRgbaColors.Aqua))
        .verify(containerBorderColor(yardLocation, CssRgbColors.Orange))
        .verify(cellContent(containerPlan.intermodalUnit))
        .verify(cellContent(toLocation, planedMoveBadge))
        .verify(containerBorderColor(toLocation, CssRgbColors.Blue))
        .verify(truckPickerButtonIsEnable(false))
        .verify(
            header(
                containerPlan.intermodalUnit,
                formatExpectedErvBlockHeader(block, yardLocation.row)));
  }

  @Test
  @Issue("TCL-16501")
  @TmsLink("8766836")
  public void cheErv_inactiveMoveInYard_changeContainerFromLocation() {
    val containerPlan =
        getContainerPlan(SegmentKind.Y, MoveStatus.Inactive, MoveSegmentStatus.Inactive);
    YardLocation fromLocation = (YardLocation) containerPlan.intermodalUnit.location;
    YardLocation selectedLocation = terminalController.createYardLocation();

    stubTestData(containerPlan);
    stubGetContainersInYardRow(fromLocation);
    val updateContainerLocationRequest =
        apiMockService.setJsonResponseForPut(
            updateContainerLocationUrl(containerPlan.intermodalUnit.id),
            apiMockService.response().withStatus(200).build(),
            selectedLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(fromLocation)
        .selectContainer(containerPlan.intermodalUnit)
        .selectCell(selectedLocation)
        .completeContainer();

    apiMockService.verify(updateContainerLocationRequest);
  }

  @Test
  @Issue("TCL-16501")
  @TmsLink("8766836")
  public void cheErv_inactiveMoveInYard_completeMove() {
    val containerPlan =
        terminalController.planContainerInYardFromYardToYard(
            MoveStatus.Inactive, MoveSegmentStatus.Inactive, false);
    YardLocation fromLocation = (YardLocation) containerPlan.intermodalUnit.location;
    YardLocation toLocation = (YardLocation) containerPlan.move.to;

    stubTestData(containerPlan);
    stubGetContainersInYardRow(fromLocation);
    val updateContainerLocationRequest =
        apiMockService.setJsonResponseForPut(
            updateContainerLocationUrl(containerPlan.intermodalUnit.id),
            apiMockService.response().withStatus(200).build(),
            toLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(fromLocation)
        .selectContainer(containerPlan.intermodalUnit)
        .selectCell(toLocation)
        .completeContainer();

    apiMockService.verify(updateContainerLocationRequest);
  }

  @Test
  @Issue("TCL-16501")
  @TmsLink("8766835")
  public void cheErv_containerWithoutMove_changeContainerFromLocation() {
    val container = terminalController.createContainerInYard();
    val containerPlan =
        getContainerPlan(SegmentKind.Y, MoveStatus.Inactive, MoveSegmentStatus.Inactive);
    YardLocation fromLocation = (YardLocation) container.location;
    YardLocation selectedLocation = terminalController.createYardLocation();

    stubTestData(containerPlan);
    stubGetContainersInYardRow(fromLocation);
    val updateContainerLocationRequest =
        apiMockService.setJsonResponseForPut(
            updateContainerLocationUrl(container.id),
            apiMockService.response().withStatus(200).build(),
            selectedLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(fromLocation)
        .selectContainer(container)
        .selectCell(selectedLocation)
        .completeContainer();

    apiMockService.verify(updateContainerLocationRequest);
  }

  @Test
  @Issue("TCL-16501")
  @TmsLink("8764845")
  public void cheErv_changeContainerFromLocation() {
    val containerPlan =
        getContainerPlan(SegmentKind.Y, MoveStatus.Active, MoveSegmentStatus.Active);
    YardLocation fromLocation = (YardLocation) containerPlan.intermodalUnit.location;
    YardLocation selectedLocation = terminalController.createYardLocation();

    stubTestData(containerPlan);
    stubGetContainersInYardRow(fromLocation);
    val updateContainerLocationRequest =
        apiMockService.setJsonResponseForPut(
            updateContainerLocationUrl(containerPlan.intermodalUnit.id),
            apiMockService.response().withStatus(200).build(),
            selectedLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(fromLocation)
        .selectContainer(containerPlan.intermodalUnit)
        .selectCell(selectedLocation)
        .completeContainer();

    apiMockService.verify(updateContainerLocationRequest);
  }

  @Test
  @Issue("TCL-16205")
  @TmsLink("8946122")
  public void cheErv_setAside_blockingContainerCompleted_blockedSelectedAutomatically() {
    val truckLocation = terminalController.createTruckLocation();
    val heapLocation = terminalController.createHeapLocation();

    val blockedContainer = terminalController.createContainerInYard();
    val blockedContainerLocation = (YardLocation) blockedContainer.location;

    val setAsideContainer = terminalController.createContainerInYard();
    val setAsideContainerLocation = (YardLocation) setAsideContainer.location;
    setAsideContainerLocation.stack = "A";
    setAsideContainerLocation.tier = 2;

    val containerPlan =
        createContainerPlan(
            blockedContainer,
            blockedContainer.location,
            heapLocation,
            blockedContainer.location,
            truckLocation);

    val setAsidePlan =
        createContainerPlan(
            setAsideContainer,
            setAsideContainer.location,
            heapLocation,
            setAsideContainer.location,
            truckLocation,
            true);

    stubTestData(containerPlan);
    stubTestData(setAsidePlan);

    apiMockService.setJsonResponseForPut(
        updateContainerLocationUrl(setAsidePlan.intermodalUnit.id),
        apiMockService.response().withStatus(200).build(),
        setAsidePlan.segments.get(0).to);

    val endRowViewPage =
        getWorklistPage()
            .<EndRowViewPage>selectContainer(containerPlan.intermodalUnit)
            .completeContainer();

    setAsidePlan.intermodalUnit.location = truckLocation;
    setAsidePlan.segments.get(0).from = truckLocation;
    apiMockService.resetService();
    stubTestData(containerPlan);
    stubTestData(setAsidePlan);

    endRowViewPage
        .verify(cellContentIsBlank(setAsideContainerLocation))
        .verify(cellContent(terminalController.getContainerExpandMoveAndSegment(blockedContainer)))
        .verify(cellContent(terminalController.getContainerExpandMoveAndSegment(blockedContainer)))
        .verify(containerCellBackgroundColor(blockedContainerLocation, CssRgbaColors.Aqua))
        .verify(utrBorderColor(truck1, CssRgbColors.Blue));
  }

  @Test
  @Issue("TCL-16205")
  @TmsLink("8946120")
  public void cheErv_setAside_blockedContainerSelected_blockingContainerSelectedAutomatically() {
    val truckLocation = terminalController.createTruckLocation();
    val heapLocation = terminalController.createHeapLocation();

    val blockedContainer = terminalController.createContainerInYard();
    val blockedContainerLocation = (YardLocation) blockedContainer.location;

    val setAsideContainer = terminalController.createContainerInYard();
    val setAsideContainerLocation = (YardLocation) setAsideContainer.location;
    setAsideContainerLocation.stack = "A";
    setAsideContainerLocation.tier = 2;

    val containerPlan =
        createContainerPlan(
            blockedContainer,
            blockedContainer.location,
            heapLocation,
            blockedContainer.location,
            truckLocation);

    val setAsidePlan =
        createContainerPlan(
            setAsideContainer,
            setAsideContainer.location,
            heapLocation,
            setAsideContainer.location,
            truckLocation,
            true);

    stubTestData(containerPlan);
    stubTestData(setAsidePlan);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(setAsideContainerLocation)
        .selectContainer(blockedContainer)
        .verify(containerCellBackgroundColor(setAsideContainerLocation, CssRgbaColors.Aqua))
        .verify(cellContent(terminalController.getContainerExpandMoveAndSegment(setAsideContainer)))
        .verify(containerCellBackgroundColor(blockedContainerLocation, CssRgbaColors.Green))
        .verify(cellContent(terminalController.getContainerExpandMoveAndSegment(blockedContainer)));
  }

  @Test
  @Issue("TCL-15738")
  @TmsLink("8327968")
  public void cheErv_setDown_tapOnEmptySpot() {
    val containerPlan =
        getContainerPlan(SegmentKind.Y, MoveStatus.Active, MoveSegmentStatus.Active);
    YardLocation fromLocation = (YardLocation) containerPlan.intermodalUnit.location;
    YardLocation toLocation = new YardLocation(block, fromLocation.row, null, null);

    stubTestData(containerPlan);
    stubGetContainersInYardRow(fromLocation);
    val updateContainerLocationRequest =
        apiMockService.setJsonResponseForPut(
            updateContainerLocationUrl(containerPlan.intermodalUnit.id),
            apiMockService.response().withStatus(200).build(),
            toLocation);

    getWorklistPage()
        .<EndRowViewPage>selectContainer(containerPlan.intermodalUnit)
        .selectEmptyCell()
        .verify(
            containerBorderColor(
                (YardLocation) containerPlan.intermodalUnit.location, CssRgbColors.Orange))
        .verify(setAsideCellBorderColor(containerPlan.intermodalUnit.id, CssRgbColors.Blue))
        .verify(
            header(
                containerPlan.intermodalUnit.id,
                containerPlan.intermodalUnit.weight,
                fromLocation.toString(),
                toLocation.toString(),
                formatExpectedErvBlockHeader(block, fromLocation.row)))
        .completeContainer();

    apiMockService.verify(updateContainerLocationRequest);
  }

  @Test
  @Issue("TCL-15738")
  @TmsLink("8327969")
  public void cheErv_setDown_noEmptySpots() {
    val containerPlan =
        getContainerPlan(SegmentKind.Y, MoveStatus.Active, MoveSegmentStatus.Active);
    IntStream.range(0, 6)
        .forEach(
            index -> terminalController.createContainer(new YardLocation(block, "A", null, null)));
    YardLocation fromLocation = (YardLocation) containerPlan.intermodalUnit.location;
    YardLocation toLocation = new YardLocation(block, fromLocation.row, null, null);
    val containers = terminal.getContainers();

    stubTestData(containerPlan);
    stubGetContainersInYardRow(fromLocation);
    val updateContainerLocationRequest =
        apiMockService.setJsonResponseForPut(
            updateContainerLocationUrl(containerPlan.intermodalUnit.id),
            apiMockService.response().withStatus(200).build(),
            toLocation);

    getWorklistPage()
        .<EndRowViewPage>selectContainer(containerPlan.intermodalUnit)
        .selectSetAsideCell(containers.get(6).id)
        .verify(
            containerBorderColor(
                (YardLocation) containerPlan.intermodalUnit.location, CssRgbColors.Orange))
        .verify(setAsideCellBorderColor(containerPlan.intermodalUnit.id, CssRgbColors.Blue))
        .verify(
            header(
                containerPlan.intermodalUnit.id,
                containerPlan.intermodalUnit.weight,
                fromLocation.toString(),
                toLocation.toString(),
                formatExpectedErvBlockHeader(block, fromLocation.row)))
        .completeContainer();

    apiMockService.verify(updateContainerLocationRequest);
  }

  @Test
  @Issue("TCL-15738")
  @TmsLink("8327972")
  public void cheErv_setDown_completeFromSetDown() {
    val containerPlan =
        getContainerPlan(SegmentKind.Y, MoveStatus.Active, MoveSegmentStatus.Active);
    val fromLocation = new YardLocation(block, "A", null, null);
    val container = terminalController.createContainer(fromLocation);
    YardLocation toLocation = terminalController.createYardLocation();

    stubTestData(containerPlan);
    stubGetContainersInYardRow(fromLocation);
    val updateContainerLocationRequest =
        apiMockService.setJsonResponseForPut(
            updateContainerLocationUrl(container.id),
            apiMockService.response().withStatus(200).build(),
            toLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(fromLocation)
        .selectSetAsideCell(container.id)
        .verify(setAsideCellBorderColor(container.id, CssRgbColors.Orange))
        .verify(
            header(
                container.id,
                container.weight,
                fromLocation.toString(),
                null,
                formatExpectedErvBlockHeader(block, fromLocation.row)))
        .selectCell(toLocation)
        .verify(containerBorderColor(toLocation, CssRgbColors.Blue))
        .completeContainer();

    apiMockService.verify(updateContainerLocationRequest);
  }

  @Test
  @Issue("TCL-15733")
  @TmsLink("8194145")
  public void cheErv_setDown_containerWithSelectedMove() {
    val fromLocation = new YardLocation(block, "A", null, null);
    val toLocation = terminalController.createYardLocation();
    val container = terminalController.createContainer(fromLocation);
    val containerPlan =
        terminalController.planContainer(
            container, toLocation, MoveStatus.Active, toLocation, MoveSegmentStatus.Active, false);

    stubTestData(containerPlan);
    stubGetContainersInYardRow(fromLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(fromLocation)
        .selectSetAsideCell(containerPlan.intermodalUnit.id)
        .verify(setAsideCellBorderColor(containerPlan.intermodalUnit.id, CssRgbColors.Orange))
        .verify(setAsideCellBackgroundColor(containerPlan.intermodalUnit.id, CssRgbaColors.Aqua))
        .verify(
            header(
                containerPlan.intermodalUnit.id,
                containerPlan.intermodalUnit.weight,
                fromLocation.toString(),
                containerPlan.move.to.toString(),
                formatExpectedErvBlockHeader(block, fromLocation.row)));
  }

  @Test
  @Issue("TCL-15733")
  @TmsLink("8194144")
  public void cheErv_setDown_containerWithActiveMove() {
    val fromLocation = new YardLocation(block, "A", null, null);
    val toLocation = terminalController.createYardLocation();
    val container = terminalController.createContainer(fromLocation);
    val containerPlan =
        terminalController.planContainer(
            container, toLocation, MoveStatus.Active, toLocation, MoveSegmentStatus.Active, false);

    stubTestData(containerPlan);
    stubGetContainersInYardRow(fromLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(fromLocation)
        .verify(setAsideContainersCount(containerPlan.intermodalUnit.id))
        .verify(setAsideCellBackgroundColor(containerPlan.intermodalUnit.id, CssRgbaColors.Green));
  }

  @Test
  @Issue("TCL-15733")
  @TmsLink("8194143")
  public void cheErv_setDown_containerWithoutMove() {
    val fromLocation = new YardLocation(block, "A", null, null);
    val container = terminalController.createContainer(fromLocation);
    val containerPlan =
        getContainerPlan(SegmentKind.Y, MoveStatus.Active, MoveSegmentStatus.Active);

    stubTestData(containerPlan);
    stubGetContainersInYardRow(fromLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(fromLocation)
        .verify(setAsideCellBackgroundColor(container.id, CssRgbaColors.DarkGray));
  }

  @Test
  @Issue("TCL-16523")
  @TmsLink("8946025")
  public void cheErv_containerFromYardToGate_truckShown() {
    val containerPlan = getContainerPlan(SegmentType.outGAlone);
    val toLocation = (GateLocation) containerPlan.move.to;
    val containerLocation = (YardLocation) containerPlan.intermodalUnit.location;

    stubTestData(containerPlan);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(containerLocation)
        .verify(
            cellContent(
                terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit)))
        .verify(containerCellBackgroundColor(containerLocation, CssRgbaColors.Green))
        .verify(utrBackgroundColor(toLocation.toString(), CssRgbaColors.Green))
        .selectContainer(containerPlan.intermodalUnit)
        .verify(containerCellBackgroundColor(containerLocation, CssRgbaColors.Aqua))
        .verify(utrBackgroundColor(toLocation.toString(), CssRgbaColors.Aqua))
        .verify(
            header(
                containerPlan.intermodalUnit.id,
                containerPlan.intermodalUnit.weight,
                containerLocation.toString(),
                toLocation.toString(),
                formatExpectedErvBlockHeader(block, containerLocation.row)));
  }

  @Test
  @Issue("TCL-16523")
  @TmsLink("8946024")
  public void cheErv_containerFromGateToYard_truckShown() {
    val containerPlan = getContainerPlan(SegmentType.inGAlone);
    val toLocation = (YardLocation) containerPlan.move.to;
    val containerLocation = containerPlan.intermodalUnit.location.toString();

    stubTestData(containerPlan);
    stubGetContainersInYardRow(toLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(toLocation)
        .verify(cellContent(toLocation, planedMoveBadge))
        .verify(containerCellBackgroundColor(toLocation, CssRgbaColors.White))
        .verify(utrBackgroundColor(containerLocation, CssRgbaColors.Yellow))
        .selectTruck(containerLocation)
        .verify(utrBackgroundColor(containerLocation, CssRgbaColors.Aqua))
        .verify(containerBorderColor(toLocation, CssRgbColors.Blue))
        .verify(
            header(
                containerPlan.intermodalUnit.id,
                containerPlan.intermodalUnit.weight,
                containerLocation,
                toLocation.toString(),
                formatExpectedErvBlockHeader(block, toLocation.row)));
  }

  private ContainerPlan getContainerPlan(final SegmentType segmentKind) {
    return getContainerPlan(segmentKind, MoveStatus.Active, MoveSegmentStatus.Active, false);
  }

  private ContainerPlan getContainerPlan(
      final SegmentType segmentKind,
      final MoveStatus moveStatus,
      final MoveSegmentStatus segmentStatus,
      final boolean selectedByEquipment) {
    switch (segmentKind) {
      case YAlone:
        return terminalController.planSetAsideContainer(
            moveStatus, segmentStatus, selectedByEquipment);
      case YUYThird:
        return terminalController.planContainerOnUtrFromYardToYard(
            moveStatus, segmentStatus, selectedByEquipment);
      case RUYThird:
        return terminalController.planContainerOnUtrFromRailToYard(
            moveStatus, segmentStatus, selectedByEquipment);
      case VUYThird:
        return terminalController.planContainerOnUtrFromVesselToYard(
            moveStatus, segmentStatus, selectedByEquipment);
      case inGAlone:
        return terminalController.planContainerOnOtrFromGateToYard(
            moveStatus, segmentStatus, selectedByEquipment);
      case outGAlone:
        return terminalController.planContainerInYardFromGateToYard(
            moveStatus, segmentStatus, selectedByEquipment);
      case YUYFirst:
        return terminalController.planContainerInYardFromYardToHeap(
            moveStatus, segmentStatus, selectedByEquipment);
      case YURFirst:
        return terminalController.planContainerInYardFromYardToRail(
            moveStatus, segmentStatus, selectedByEquipment);
      case YUVFirstWithOutUTR:
        return terminalController.planContainerInYardFromYardToVessel(
            moveStatus, segmentStatus, selectedByEquipment);
      case YUYFirstWithOutUTR:
        return terminalController.planContainerInYardFromYardToHeap(
            moveStatus, segmentStatus, selectedByEquipment);
      case YURFirstWithOutUTR:
        return terminalController.planContainerInYardFromYardToRail(
            moveStatus, segmentStatus, selectedByEquipment);
      case YUVFirst:
        return terminalController.planContainerInYardFromYardToVessel(
            moveStatus, segmentStatus, selectedByEquipment);
      default:
        return null;
    }
  }

  private ContainerPlan getContainerPlan(
      final SegmentKind segmentKind,
      final MoveStatus moveStatus,
      final MoveSegmentStatus segmentStatus) {
    switch (segmentKind) {
      case Y:
        return terminalController.planContainerInYardFromYardToHeap(
            moveStatus, segmentStatus, false);
      case R:
        return terminalController.planContainerInYardFromYardToRail(
            moveStatus, segmentStatus, false);
      case G:
        return terminalController.planContainerInYardFromYardToGate(
            moveStatus, segmentStatus, false);
      case V:
        return terminalController.planContainerInYardFromYardToVessel(
            moveStatus, segmentStatus, false);
      default:
        return null;
    }
  }

  private ContainerPlan createContainersWithUtrs() {
    val container1 = terminalController.planContainerInYardFromYardToHeap();
    val container2 = terminalController.planContainerInYardFromYardToHeap();
    val container3 = terminalController.planContainerInYardFromYardToHeap();
    val container4 = terminalController.planContainerInYardFromYardToHeap();

    terminalController.createEquipment(
        new Equipment("UTR2", EquipmentLocationKind.truck, "UTR2", false, true));
    terminalController.createEquipment(
        new Equipment("UTR3", EquipmentLocationKind.truck, "UTR3", false, true));

    container1.segments.get(0).to = terminalController.createTruckLocation("UTR2");
    container2.segments.get(0).to = terminalController.createTruckLocation("UTR3");
    container3.segments.get(0).to = terminalController.createTruckLocation("UTR1");
    container4.segments.get(0).to = terminalController.createTruckLocation("UTR1");

    return container1;
  }

  private ContainerPlan createContainerPlan(
      IntermodalUnit container,
      Location moveFrom,
      Location moveTo,
      Location segmentFrom,
      Location segmentTo) {
    return createContainerPlan(container, moveFrom, moveTo, segmentFrom, segmentTo, null);
  }

  private ContainerPlan createContainerPlan(
      IntermodalUnit container,
      Location moveFrom,
      Location moveTo,
      Location segmentFrom,
      Location segmentTo,
      Boolean isSystemMove) {

    return terminalController.planContainer(
        container,
        moveTo,
        MoveStatus.Active,
        segmentTo,
        MoveSegmentStatus.Active,
        true,
        moveFrom,
        segmentFrom,
        null,
        null,
        SegmentKind.Y,
        isSystemMove);
  }
}
