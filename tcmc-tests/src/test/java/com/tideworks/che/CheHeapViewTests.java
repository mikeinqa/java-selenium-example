package com.tideworks.che;

import static com.tideworks.utilities.api.Che.updateContainerLocationUrl;
import static com.tideworks.verifications.che.Verifications.containerBorderColor;
import static com.tideworks.verifications.che.Verifications.containerCellBackgroundColor;
import static com.tideworks.verifications.che.Verifications.containerStatusIcon;
import static com.tideworks.verifications.che.Verifications.containersFields;
import static com.tideworks.verifications.che.Verifications.containersOrder;
import static com.tideworks.verifications.che.Verifications.header;
import static com.tideworks.verifications.che.Verifications.truckPickerButtonColor;
import static com.tideworks.verifications.che.Verifications.truckPickerButtonIsEnable;
import static com.tideworks.verifications.che.Verifications.utrBackgroundColor;
import static com.tideworks.verifications.che.Verifications.utrBorderColor;
import static com.tideworks.verifications.che.Verifications.utrIsPresent;
import static com.tideworks.verifications.elements.Verifications.utrIsPresentInDialog;

import static java.util.Collections.singletonList;

import com.tideworks.base.CheBase;
import com.tideworks.json.builders.MoveBuilder;
import com.tideworks.json.builders.SegmentBuilder;
import com.tideworks.json.objectmodel.BlockKind;
import com.tideworks.json.objectmodel.ContainerPlan;
import com.tideworks.json.objectmodel.Equipment;
import com.tideworks.json.objectmodel.EquipmentLocationKind;
import com.tideworks.json.objectmodel.IntermodalUnit;
import com.tideworks.json.objectmodel.MoveSegmentStatus;
import com.tideworks.json.objectmodel.MoveStatus;
import com.tideworks.json.objectmodel.SegmentKind;
import com.tideworks.json.objectmodel.locations.Location;
import com.tideworks.json.objectmodel.locations.YardLocation;
import com.tideworks.pages.che.yardview.EndRowViewPage;
import com.tideworks.pages.che.yardview.HeapViewPage;
import com.tideworks.utilities.css.CssRgbColors;
import com.tideworks.utilities.css.CssRgbaColors;
import com.tideworks.verifications.che.Verifications;

import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import lombok.val;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

/** Tests for Che heap-view page. */
public class CheHeapViewTests extends CheBase {

  @Test
  @Issue("TCL-14654")
  @TmsLink("7380987")
  @TmsLink("7380986")
  @TmsLink("7380987")
  @TmsLink("7381014")
  @TmsLink("7381018")
  @TmsLink("7381021")
  @TmsLink("7381019")
  @TmsLink("7380988")
  public void cheHeapViewContainerListShownInCorrectOrderWithValidContentAndHeader() {
    val expectedContainerOrder =
        Arrays.asList("CONT0", "CONT2", "CONT1", "CONT3", "CONT5", "CONT6");

    val containerPlan = terminalController.planContainerInHeapFromHeapToYard();
    val selectedContainer =
        terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit);
    terminalController.planContainerInHeapFromHeapToYard(
        MoveStatus.Held, MoveSegmentStatus.Selected, false);
    terminalController.planContainerInHeapFromHeapToYard(
        MoveStatus.Active, MoveSegmentStatus.Active, false);
    terminalController.planContainerInHeapFromHeapToYard(
        MoveStatus.Active, MoveSegmentStatus.Inactive, false);
    terminalController.planContainerInYardFromYardToHeap(
        MoveStatus.Active, MoveSegmentStatus.Active, false);
    terminalController.createContainerInHeap();
    terminalController.createContainerInHeap();

    stubTestData(containerPlan);

    val expectedContainers =
        terminalController
            .getTerminal()
            .getContainers()
            .stream()
            .filter(s -> ((YardLocation) s.location).isHeap())
            .collect(Collectors.toList());

    getWorklistPage()
        .<HeapViewPage>selectContainer(selectedContainer)
        .verify(containersOrder(expectedContainerOrder))
        .verify(containersFields(expectedContainers))
        .verify(header(selectedContainer, heapBlock))
        .clickBlockPickerButton()
        .selectHeapBlock(heapBlock)
        .verify(containersOrder(expectedContainerOrder))
        .verify(containersFields(expectedContainers))
        .verify(header(selectedContainer, heapBlock));
  }

  @Test
  @Issue("TCL-14654")
  @TmsLink("8183415")
  @TmsLink("8183437")
  public void cheHeapViewContainerSearch() {
    val containerPlan = terminalController.planContainerInHeapFromHeapToYard();
    val searchPattern = "100";
    terminalController.planContainerInHeapFromHeapToYard(
        MoveStatus.Held, MoveSegmentStatus.Selected, false);
    terminalController.planContainerInHeapFromHeapToYard(
        MoveStatus.Active, MoveSegmentStatus.Active, false);
    terminalController.planContainerInHeapFromHeapToYard(
        MoveStatus.Active, MoveSegmentStatus.Inactive, false);
    terminalController.createContainerInHeap();
    terminalController.createContainerInHeap();

    stubTestData(containerPlan);

    getWorklistPage()
        .<HeapViewPage>selectContainer(containerPlan.intermodalUnit)
        .searchContainers(searchPattern)
        .verify(
            containersFields(
                terminalController
                    .getTerminal()
                    .getContainers()
                    .stream()
                    .filter(container -> container.id.contains(searchPattern))
                    .collect(Collectors.toList())))
        .searchContainers(searchPattern + "666")
        .verify(truckPickerButtonColor())
        .verify(containersFields(new ArrayList<>()));
  }

  @Test
  @Issue("TCL-14654")
  @TmsLink("7380989")
  public void cheHeapViewUpdatedOnAdditionsOrRemovalOfContainer() {
    val containerPlan = terminalController.planContainerInHeapFromHeapToYard();
    terminalController.planContainerInHeapFromHeapToYard(
        MoveStatus.Held, MoveSegmentStatus.Selected, false);
    terminalController.planContainerInHeapFromHeapToYard(
        MoveStatus.Active, MoveSegmentStatus.Active, false);
    terminalController.planContainerInHeapFromHeapToYard(
        MoveStatus.Active, MoveSegmentStatus.Inactive, false);
    terminalController.createContainerInHeap();
    val containerToRemove = terminalController.createContainerInHeap();

    stubTestData(containerPlan);

    val heapViewPage =
        getWorklistPage()
            .<HeapViewPage>selectContainer(containerPlan.intermodalUnit)
            .verify(containersFields(terminalController.getTerminal().getContainers()));

    terminalController.getTerminal().remove(containerToRemove);
    stubTestData(containerPlan);

    heapViewPage.verify(containersFields(terminalController.getTerminal().getContainers()));

    terminalController.createContainerInHeap();
    stubTestData(containerPlan);

    heapViewPage.verify(containersFields(terminalController.getTerminal().getContainers()));
  }

  @Test
  @Issue("TCL-14654")
  @TmsLink("8327959")
  public void cheHeapViewSelectAndComplete() {
    val containerPlan = terminalController.planContainerInHeapFromHeapToYard();
    val selectedContainer =
        terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit);

    stubTestData(containerPlan);

    val updateContainerLocationRequest =
        apiMockService.setJsonResponseForPut(
            updateContainerLocationUrl(containerPlan.intermodalUnit.id),
            apiMockService.response().withStatus(200).build(),
            containerPlan.getFromSegment().to);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectHeapBlock(heapBlock)
        .verify(containersFields(terminalController.getTerminal().getContainers()))
        .verify(header(null, heapBlock))
        .selectContainer(selectedContainer.id)
        .verify(header(selectedContainer, heapBlock))
        .deselectContainer()
        .verify(header(null, heapBlock))
        .selectContainer(selectedContainer.id)
        .completeContainer();

    apiMockService.verify(updateContainerLocationRequest);
  }

  @Test
  @Issue("TCL-15475")
  @TmsLink("7381041")
  public void cheHeapView_displaysAllAvailableUtrs() {
    val containerPlan = createContainersWithUtrs();
    val selectedContainer =
        terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit);
    stubTestData(containerPlan);

    getWorklistPage()
        .<HeapViewPage>selectContainer(selectedContainer)
        .verify(containersFields(terminalController.getTerminal().getContainers()))
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
        .<HeapViewPage>selectContainer(selectedContainer)
        .verify(containersFields(terminalController.getTerminal().getContainers()))
        .clickTruckPickerButton()
        .verify(utrIsPresentInDialog(terminalController.getTerminal().getEquipments()));
  }

  @Test
  @Issue("TCL-15475")
  @TmsLink("7381040")
  public void cheHeapView_displaysNoUtrsInTruckList() {
    val containerPlan = terminalController.planContainerInHeapFromHeapToYard();
    containerPlan.segments.get(0).to = terminalController.createYardLocation();
    val selectedContainer =
        terminalController.getContainerExpandMoveAndSegment(containerPlan.intermodalUnit);
    stubTestData(containerPlan);

    getWorklistPage()
        .<HeapViewPage>selectContainer(selectedContainer)
        .verify(containersFields(terminalController.getTerminal().getContainers()))
        .verify(utrIsPresent(Collections.emptyList()))
        .verify(truckPickerButtonIsEnable(false));
  }

  @Test
  @Issue("TCL-15475")
  @TmsLink("7381040")
  public void cheHeapView_containerPlannedToUtr_greenUtrOnTruckList() {
    val selectedPlan = terminalController.planContainerInHeapFromHeapToYard();
    terminalController.planContainerInHeapFromHeapToYard();
    selectedPlan.segments.get(0).to = terminalController.createYardLocation();
    val selectedContainer =
        terminalController.getContainerExpandMoveAndSegment(selectedPlan.intermodalUnit);
    stubTestData(selectedPlan);

    getWorklistPage()
        .<HeapViewPage>selectContainer(selectedContainer)
        .verify(containersFields(terminalController.getTerminal().getContainers()))
        .verify(utrIsPresent(terminalController.getTerminal().getEquipments()))
        .verify(utrBackgroundColor(truck1, CssRgbaColors.Green));
  }

  @Test
  @Issue("TCL-15475")
  @TmsLink("7381040")
  public void cheHeapView_containerPlannedFromUtr_yellowUtrOnTruckList() {
    val selectedPlan = terminalController.planContainerInHeapFromHeapToYard();
    val containerPlan = terminalController.planContainerInHeapFromHeapToYard();
    selectedPlan.segments.get(0).to = terminalController.createYardLocation();
    containerPlan.segments.get(0).to = terminalController.createYardLocation();
    containerPlan.segments.get(0).from = terminalController.createTruckLocation();
    val selectedContainer =
        terminalController.getContainerExpandMoveAndSegment(selectedPlan.intermodalUnit);
    stubTestData(selectedPlan);

    getWorklistPage()
        .<HeapViewPage>selectContainer(selectedContainer)
        .verify(containersFields(terminalController.getTerminal().getContainers()))
        .verify(utrIsPresent(terminalController.getTerminal().getEquipments()))
        .verify(utrBackgroundColor(truck1, CssRgbaColors.Yellow));
  }

  private ContainerPlan createContainersWithUtrs() {
    val container1 = terminalController.planContainerInHeapFromHeapToYard();
    val container2 = terminalController.planContainerInHeapFromHeapToYard();
    val container3 = terminalController.planContainerInHeapFromHeapToYard();

    terminalController.createEquipment(
        new Equipment("UTR2", EquipmentLocationKind.truck, "UTR2", false, true));
    terminalController.createEquipment(
        new Equipment("UTR3", EquipmentLocationKind.truck, "UTR3", false, true));

    container1.segments.get(0).to = terminalController.createTruckLocation("UTR2");
    container2.segments.get(0).to = terminalController.createTruckLocation("UTR3");
    container3.segments.get(0).to = terminalController.createTruckLocation("UTR1");

    return container1;
  }

  @Test
  @Issue("TCL-15748")
  @TmsLink("8621572")
  public void cheHeapView_containerToUtr_toLocationChangedToCurrentHeap_nothingChanged() {
    val container = terminalController.createContainerInHeap();
    val toLocation = terminalController.createTruckLocation();

    val move =
        MoveBuilder.create()
            .withFrom(container.location)
            .withTo(toLocation)
            .withStatus(MoveStatus.Active)
            .build();
    val segment =
        SegmentBuilder.create()
            .withAssignedEquipment(singletonList(equipmentId))
            .withKind(SegmentKind.Y)
            .withStatus(MoveSegmentStatus.Active)
            .withFrom(container.location)
            .withTo(toLocation)
            .build();

    val containerPlan =
        terminalController.planContainer(
            s -> s.withContainer(container).withSegment(segment).withMove(move));
    stubTestData(containerPlan);

    val expandedContainer = terminalController.getContainerExpandMoveAndSegment(container);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectHeapBlock(heapBlock)
        .verify(header(null, heapBlock))
        .selectContainer(container.id)
        .verify(header(expandedContainer, heapBlock))
        .clickBlockPickerButton()
        .selectHeapBlock(heapBlock)
        .verify(header(expandedContainer, heapBlock));
  }

  @Test
  @Issue("TCL-15748")
  @TmsLink("8621561")
  public void
      cheHeapView_containerFromSpotToHeap_navToUnplannedHeap_loadedUtrSelected_toLocationUpdated() {
    val firstContainer = terminalController.createContainerInYard();
    val secondContainer = terminalController.createContainerInHeap();
    val truckLocation = terminalController.createTruckLocation(truck1);

    val containerPlan =
        createContainerPlan(
            firstContainer,
            firstContainer.location,
            truckLocation,
            MoveStatus.Active,
            firstContainer.location,
            truckLocation,
            MoveSegmentStatus.Active);

    createContainerPlan(
        secondContainer,
        secondContainer.location,
        truckLocation,
        MoveStatus.Active,
        secondContainer.location,
        truckLocation,
        MoveSegmentStatus.Active);

    val expandedContainer = terminalController.getContainerExpandMoveAndSegment(firstContainer);
    stubTestData(containerPlan);
    stubGetContainersInYardRow((YardLocation) secondContainer.location);

    getWorklistPage()
        .<EndRowViewPage>selectContainer(expandedContainer)
        .verify(
            header(
                expandedContainer,
                formatExpectedErvBlockHeader(
                    block, ((YardLocation) expandedContainer.location).row)))
        .clickBlockPickerButton()
        .selectHeapBlock(heapBlock)
        .verify(
            header(
                expandedContainer.id,
                expandedContainer.weight,
                expandedContainer.location.toString(),
                heapBlock,
                heapBlock))
        .selectTruck(truck1)
        .verify(utrBackgroundColor(truck1, CssRgbaColors.Aqua))
        .verify(utrBorderColor(truck1, CssRgbColors.Blue))
        .verify(
            header(
                expandedContainer.id,
                expandedContainer.weight,
                expandedContainer.location.toString(),
                truck1,
                heapBlock))
        .selectContainer(secondContainer.id)
        .verify(
            header(
                expandedContainer.id,
                expandedContainer.weight,
                expandedContainer.location.toString(),
                heapBlock,
                heapBlock));
  }

  @Test
  @Issue("TCL-15748")
  @TmsLink("8621560")
  @TmsLink("8621559")
  public void
      cheHeapView_containerToUtr_toLocationChangedToLoadedUtrViaTruckPicker_toLocationUpdated() {
    val firstContainer = terminalController.createContainerInHeap();
    val secondContainer = terminalController.createContainerInHeap();
    val secondTruck = "UTR02";
    val thirdTruck = "UTR03";
    terminalController.createEquipment(
        s ->
            s.withEnabled(true)
                .withEquipmentNo(secondTruck)
                .withHeld(false)
                .withKind(EquipmentLocationKind.truck)
                .withId(secondTruck));
    terminalController.createEquipment(
        s ->
            s.withEnabled(true)
                .withEquipmentNo(thirdTruck)
                .withHeld(false)
                .withKind(EquipmentLocationKind.truck)
                .withId(thirdTruck));
    val firstToLocation = terminalController.createTruckLocation(truck1);
    val secondToLocation = terminalController.createTruckLocation(secondTruck);
    val containerOnUtr =
        terminalController.createContainer(terminalController.createTruckLocation(thirdTruck));

    val containerPlan =
        createContainerPlan(
            firstContainer,
            firstContainer.location,
            firstToLocation,
            MoveStatus.Active,
            secondContainer.location,
            firstToLocation,
            MoveSegmentStatus.Active);

    createContainerPlan(
        secondContainer,
        secondContainer.location,
        secondToLocation,
        MoveStatus.Active,
        secondContainer.location,
        secondToLocation,
        MoveSegmentStatus.Active);

    createContainerPlan(
        containerOnUtr,
        containerOnUtr.location,
        firstContainer.location,
        MoveStatus.Active,
        containerOnUtr.location,
        firstContainer.location,
        MoveSegmentStatus.Active);

    val expandedContainer = terminalController.getContainerExpandMoveAndSegment(firstContainer);
    stubTestData(containerPlan);

    getWorklistPage()
        .<HeapViewPage>selectContainer(expandedContainer)
        .verify(header(expandedContainer, heapBlock))
        .verify(containerStatusIcon(expandedContainer, true))
        .clickTruckPickerButton()
        .selectUtr(thirdTruck)
        .verify(
            header(
                expandedContainer.id,
                expandedContainer.weight,
                expandedContainer.location.toString(),
                truck1,
                heapBlock))
        .selectTruck(thirdTruck)
        .verify(
            header(
                expandedContainer.id,
                expandedContainer.weight,
                expandedContainer.location.toString(),
                truck1,
                heapBlock))
        .clickTruckPickerButton()
        .selectUtr(secondTruck)
        .verify(utrBackgroundColor(secondTruck, CssRgbaColors.Aqua))
        .verify(utrBorderColor(secondTruck, CssRgbColors.Blue))
        .verify(
            header(
                expandedContainer.id,
                expandedContainer.weight,
                expandedContainer.location.toString(),
                secondTruck,
                heapBlock));
  }

  @Test
  @Issue("TCL-15748")
  @TmsLink("8621557")
  @TmsLink("8578299")
  public void cheHeapView_containerToUtr_toLocationChangedToUtrViaTruckLane_toLocationUpdated() {
    val firstContainer = terminalController.createContainerInHeap();
    val secondContainer = terminalController.createContainerInHeap();
    val secondTruck = "UTR02";

    terminalController.createEquipment(
        s ->
            s.withEnabled(true)
                .withEquipmentNo(secondTruck)
                .withHeld(false)
                .withKind(EquipmentLocationKind.truck)
                .withId(secondTruck));

    val firstToLocation = terminalController.createTruckLocation(truck1);
    val secondToLocation = terminalController.createTruckLocation(secondTruck);

    val containerPlan =
        createContainerPlan(
            firstContainer,
            firstContainer.location,
            firstToLocation,
            MoveStatus.Active,
            firstContainer.location,
            firstToLocation,
            MoveSegmentStatus.Active);

    createContainerPlan(
        secondContainer,
        secondContainer.location,
        secondToLocation,
        MoveStatus.Active,
        secondContainer.location,
        secondToLocation,
        MoveSegmentStatus.Active);

    val expandedContainer = terminalController.getContainerExpandMoveAndSegment(firstContainer);
    stubTestData(containerPlan);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectHeapBlock(heapBlock)
        .verify(
            containerStatusIcon(
                terminalController.getContainerExpandMoveAndSegment(firstContainer), false))
        .verify(utrBackgroundColor(truck1, CssRgbaColors.Green))
        .selectContainer(expandedContainer.id)
        .verify(header(expandedContainer, heapBlock))
        .verify(containerStatusIcon(expandedContainer, true))
        .selectTruck(secondTruck)
        .verify(utrBackgroundColor(secondTruck, CssRgbaColors.Aqua))
        .verify(
            header(
                expandedContainer.id,
                expandedContainer.weight,
                expandedContainer.location.toString(),
                secondTruck,
                heapBlock));
  }

  @Test
  @Issue("TCL-15748")
  @TmsLink("8589111")
  public void cheHeapView_containerFromHeapToHeap_navToPlanned_plannedContainerDisplayed() {
    val plannedHeap = "plannedHeap";
    val container = terminalController.createContainerInHeap();
    terminalController.createBlock(
        blockBuilder -> blockBuilder.ofKind(BlockKind.GroundedHeap).withName(plannedHeap));

    val toLocation = new YardLocation(plannedHeap, null, null, null);
    val containerPlan =
        createContainerPlan(
            container,
            container.location,
            toLocation,
            MoveStatus.Active,
            container.location,
            toLocation,
            MoveSegmentStatus.Active);

    val expandedContainer = terminalController.getContainerExpandMoveAndSegment(container);
    stubTestData(containerPlan);
    stubGetContainersInYardRow(toLocation);

    getWorklistPage()
        .<HeapViewPage>selectContainer(expandedContainer)
        .verify(header(expandedContainer, heapBlock))
        .verify(containerStatusIcon(expandedContainer, true))
        .verify(containersFields(Collections.singletonList(expandedContainer)))
        .clickBlockPickerButton()
        .selectHeapBlock(plannedHeap)
        .verify(header(expandedContainer, plannedHeap))
        .verify(containerStatusIcon(expandedContainer, true))
        .verify(containersFields(Collections.singletonList(expandedContainer)));
  }

  @Test
  @Issue("TCL-15748")
  @TmsLink("8589108")
  public void cheHeapView_containerFromSpotToHeap_navToPlanned_plannedContainerDisplayed() {
    val container = terminalController.createContainerInYard();
    val toLocation = terminalController.createHeapLocation();
    val containerPlan =
        createContainerPlan(
            container,
            container.location,
            toLocation,
            MoveStatus.Active,
            container.location,
            toLocation,
            MoveSegmentStatus.Active);

    val expandedContainer = terminalController.getContainerExpandMoveAndSegment(container);
    stubTestData(containerPlan);
    stubGetContainersInYardRow(toLocation);
    stubGetContainersInYardRow(toLocation);

    getWorklistPage()
        .<EndRowViewPage>selectContainer(expandedContainer)
        .verify(
            header(
                expandedContainer,
                formatExpectedErvBlockHeader(
                    block, ((YardLocation) expandedContainer.location).row)))
        .verify(
            containerCellBackgroundColor(
                (YardLocation) expandedContainer.location, CssRgbaColors.Aqua))
        .clickBlockPickerButton()
        .selectHeapBlock(heapBlock)
        .verify(header(expandedContainer, heapBlock))
        .verify(containerStatusIcon(expandedContainer, true))
        .verify(containersFields(Collections.singletonList(expandedContainer)));
  }

  @Test
  @Issue("TCL-15748")
  @TmsLink("8589105")
  public void cheHeapView_containerFromHeapToHeap_navToUnplannedHeap_toLocationUpdated() {
    val plannedHeap = "plannedHeap";
    val unplannedHeap = "unplannedHeap";
    val container = terminalController.createContainerInHeap();
    terminalController.createBlock(
        blockBuilder -> blockBuilder.ofKind(BlockKind.GroundedHeap).withName(plannedHeap));
    terminalController.createBlock(
        blockBuilder -> blockBuilder.ofKind(BlockKind.GroundedHeap).withName(unplannedHeap));

    val toLocation = new YardLocation(plannedHeap, null, null, null);
    val unplannedLocation = new YardLocation(unplannedHeap, null, null, null);
    val containerPlan =
        createContainerPlan(
            container,
            container.location,
            toLocation,
            MoveStatus.Active,
            container.location,
            toLocation,
            MoveSegmentStatus.Active);

    val expandedContainer = terminalController.getContainerExpandMoveAndSegment(container);
    stubTestData(containerPlan);
    stubGetContainersInYardRow(unplannedLocation);

    getWorklistPage()
        .<HeapViewPage>selectContainer(expandedContainer)
        .verify(header(expandedContainer, heapBlock))
        .verify(containerStatusIcon(expandedContainer, true))
        .verify(containersFields(Collections.singletonList(expandedContainer)))
        .clickBlockPickerButton()
        .selectHeapBlock(unplannedHeap)
        .verify(
            header(
                expandedContainer.id,
                expandedContainer.weight,
                expandedContainer.location.toString(),
                unplannedLocation.toString(),
                unplannedHeap))
        .verify(containerStatusIcon(expandedContainer, true))
        .verify(containersFields(Collections.singletonList(expandedContainer)));
  }

  @Test
  @Issue("TCL-15748")
  @TmsLink("8589104")
  public void cheHeapView_containerFromSpotToHeap_navToUnplannedHeap_toLocationUpdated() {
    val plannedHeap = "plannedHeap";
    val unplannedHeap = "unplannedHeap";
    val container = terminalController.createContainerInYard();
    val containerLocation = (YardLocation) container.location;

    terminalController.createBlock(
        blockBuilder -> blockBuilder.ofKind(BlockKind.GroundedHeap).withName(plannedHeap));
    terminalController.createBlock(
        blockBuilder -> blockBuilder.ofKind(BlockKind.GroundedHeap).withName(unplannedHeap));

    val toLocation = new YardLocation(plannedHeap, null, null, null);
    val unplannedLocation = new YardLocation(unplannedHeap, null, null, null);
    val containerPlan =
        createContainerPlan(
            container,
            container.location,
            toLocation,
            MoveStatus.Active,
            container.location,
            toLocation,
            MoveSegmentStatus.Active);

    val expandedContainer = terminalController.getContainerExpandMoveAndSegment(container);
    stubTestData(containerPlan);
    stubGetContainersInYardRow(unplannedLocation);

    getWorklistPage()
        .<EndRowViewPage>selectContainer(expandedContainer)
        .verify(
            header(expandedContainer, formatExpectedErvBlockHeader(block, containerLocation.row)))
        .verify(containerCellBackgroundColor(containerLocation, CssRgbaColors.Aqua))
        .verify(Verifications.cellContent(expandedContainer))
        .clickBlockPickerButton()
        .selectHeapBlock(unplannedHeap)
        .verify(
            header(
                expandedContainer.id,
                expandedContainer.weight,
                expandedContainer.location.toString(),
                unplannedLocation.toString(),
                unplannedHeap))
        .verify(containerStatusIcon(expandedContainer, true))
        .verify(containersFields(Collections.singletonList(expandedContainer)));
  }

  @Test
  @Issue("TCL-15748")
  @TmsLink("8589103")
  @TmsLink("8589100")
  @TmsLink("8578302")
  public void cheHeapView_containerAtHeapWithoutMove_navToHeap_toLocationUpdated() {
    val unplannedHeap = "unplannedHeap";
    val unplannedLocation = new YardLocation(unplannedHeap, null, null, null);
    val container = terminalController.createContainerInHeap();
    terminalController.createBlock(
        blockBuilder -> blockBuilder.ofKind(BlockKind.GroundedHeap).withName(unplannedHeap));

    val containerPlan = new ContainerPlan();
    val expandedContainer = terminalController.getContainerExpandMoveAndSegment(container);
    containerPlan.intermodalUnit = container;

    stubTestData(containerPlan);
    stubGetContainersInYardRow(unplannedLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectHeapBlock(heapBlock)
        .verify(containersFields(Collections.singletonList(expandedContainer)))
        .verify(containerStatusIcon(expandedContainer, false))
        .selectContainer(expandedContainer.id)
        .verify(containerBorderColor(expandedContainer, CssRgbColors.Orange))
        .verify(header(expandedContainer, heapBlock))
        .clickBlockPickerButton()
        .selectHeapBlock(unplannedHeap)
        .verify(containerStatusIcon(expandedContainer, true))
        .verify(containerBorderColor(expandedContainer, CssRgbColors.Blue))
        .verify(
            header(
                expandedContainer.id,
                expandedContainer.weight,
                expandedContainer.location.toString(),
                unplannedLocation.toString(),
                unplannedHeap));
  }

  @Test
  @Issue("TCL-15748")
  @TmsLink("8589102")
  public void cheHeapView_containerAtSpotWithoutMove_navToHeap_toLocationUpdated() {
    val unplannedHeap = "unplannedHeap";
    val unplannedLocation = new YardLocation(unplannedHeap, null, null, null);
    val container = terminalController.createContainerInYard();
    val containerLocation = (YardLocation) container.location;
    terminalController.createBlock(
        blockBuilder -> blockBuilder.ofKind(BlockKind.GroundedHeap).withName(unplannedHeap));

    val containerPlan = new ContainerPlan();
    val expandedContainer = terminalController.getContainerExpandMoveAndSegment(container);
    containerPlan.intermodalUnit = container;

    stubTestData(containerPlan);
    stubGetContainersInYardRow(unplannedLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectBlock(block)
        .selectBlockRow(containerLocation)
        .verify(Verifications.cellContent(container))
        .selectContainer(expandedContainer)
        .verify(
            header(expandedContainer, formatExpectedErvBlockHeader(block, containerLocation.row)))
        .clickBlockPickerButton()
        .selectHeapBlock(unplannedHeap)
        .verify(containerStatusIcon(expandedContainer, true))
        .verify(containerBorderColor(expandedContainer, CssRgbColors.Blue))
        .verify(
            header(
                expandedContainer.id,
                expandedContainer.weight,
                expandedContainer.location.toString(),
                unplannedLocation.toString(),
                unplannedHeap));
  }

  @Test
  @Issue("TCL-15748")
  @TmsLink("8589099")
  public void cheHeapView_containerOnUtrToHeap_containerSelected() {
    val truckLocation = terminalController.createTruckLocation();
    val container = terminalController.createContainer(truckLocation);
    val toLocation = terminalController.createHeapLocation();

    val containerPlan =
        createContainerPlan(
            container,
            container.location,
            toLocation,
            MoveStatus.Active,
            container.location,
            toLocation,
            MoveSegmentStatus.Active);
    val expandedContainer = terminalController.getContainerExpandMoveAndSegment(container);

    stubTestData(containerPlan);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectHeapBlock(heapBlock)
        .selectContainer(expandedContainer.id)
        .verify(header(expandedContainer, heapBlock))
        .verify(containerStatusIcon(expandedContainer, true))
        .verify(containerBorderColor(expandedContainer, CssRgbColors.Blue))
        .verify(containersFields(Collections.singletonList(expandedContainer)))
        .verify(utrBackgroundColor(truck1, CssRgbaColors.Aqua));
  }

  @Test
  @Issue("TCL-15748")
  @TmsLink("8589098")
  public void cheHeapView_containerOnUtrToHeap_moveActive_containerNotSelected() {
    val truckLocation = terminalController.createTruckLocation();
    val container = terminalController.createContainer(truckLocation);
    val toLocation = terminalController.createHeapLocation();

    val containerPlan =
        createContainerPlan(
            container,
            container.location,
            toLocation,
            MoveStatus.Active,
            container.location,
            toLocation,
            MoveSegmentStatus.Active);
    val expandedContainer = terminalController.getContainerExpandMoveAndSegment(container);

    stubTestData(containerPlan);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectHeapBlock(heapBlock)
        .verify(header(null, heapBlock))
        .verify(containerStatusIcon(expandedContainer, false))
        .verify(containersFields(Collections.singletonList(expandedContainer)))
        .verify(utrBackgroundColor(truck1, CssRgbaColors.Yellow));
  }

  @Test
  @Issue("TCL-15748")
  @TmsLink("8589097")
  @TmsLink("8578301")
  public void cheHeapView_containerFromHeapToUtr_moveHeld_containerSelected() {
    val container = terminalController.createContainerInHeap();
    val toLocation = terminalController.createTruckLocation();

    val containerPlan =
        createContainerPlan(
            container,
            container.location,
            toLocation,
            MoveStatus.Held,
            container.location,
            toLocation,
            MoveSegmentStatus.Inactive);
    val expandedContainer = terminalController.getContainerExpandMoveAndSegment(container);

    stubTestData(containerPlan);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectHeapBlock(heapBlock)
        .verify(header(null, heapBlock))
        .verify(containerStatusIcon(expandedContainer, false))
        .verify(containersFields(Collections.singletonList(expandedContainer)))
        .verify(truckPickerButtonIsEnable(false));
  }

  @Test
  @Issue("TCL-15748")
  @TmsLink("8589096")
  @TmsLink("8578300")
  public void cheHeapView_containerFromHeapToUtr_moveInactive_containerSelected() {
    val container = terminalController.createContainerInHeap();
    val toLocation = terminalController.createTruckLocation();

    val containerPlan =
        createContainerPlan(
            container,
            container.location,
            toLocation,
            MoveStatus.Inactive,
            container.location,
            toLocation,
            MoveSegmentStatus.Inactive);
    val expandedContainer = terminalController.getContainerExpandMoveAndSegment(container);

    stubTestData(containerPlan);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectHeapBlock(heapBlock)
        .verify(header(null, heapBlock))
        .verify(containerStatusIcon(expandedContainer, false))
        .verify(containersFields(Collections.singletonList(expandedContainer)))
        .verify(truckPickerButtonIsEnable(false));
  }

  @Test
  @Issue("TCL-16523")
  @TmsLink("8946021")
  public void cheHeapView_containerFromHeapToGate_truckShown() {
    val container = terminalController.createContainerInHeap();
    val toLocation = terminalController.createGateLocation();
    val containerPlan =
        createContainerPlan(
            container,
            container.location,
            toLocation,
            MoveStatus.Active,
            container.location,
            toLocation,
            MoveSegmentStatus.Active);
    val expandedContainer = terminalController.getContainerExpandMoveAndSegment(container);

    stubTestData(containerPlan);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectHeapBlock(heapBlock)
        .verify(containerStatusIcon(expandedContainer, false))
        .verify(utrBackgroundColor(toLocation.toString(), CssRgbaColors.Green))
        .selectContainer(container.id)
        .verify(containerStatusIcon(expandedContainer, true))
        .verify(utrBackgroundColor(toLocation.toString(), CssRgbaColors.Aqua))
        .verify(containerBorderColor(expandedContainer, CssRgbColors.Orange))
        .verify(
            header(
                container.id,
                container.weight,
                container.location.toString(),
                toLocation.toString(),
                heapBlock));
  }

  @Test
  @Issue("TCL-16523")
  @TmsLink("8946020")
  public void cheHeapView_containerFromGateToHeap_truckShown() {
    val containerLocation = terminalController.createGateLocation();
    val container = terminalController.createContainer(containerLocation);
    val toLocation = terminalController.createHeapLocation();
    val containerPlan =
        createContainerPlan(
            container,
            container.location,
            toLocation,
            MoveStatus.Active,
            container.location,
            toLocation,
            MoveSegmentStatus.Active);
    val expandedContainer = terminalController.getContainerExpandMoveAndSegment(container);

    stubTestData(containerPlan);
    stubGetContainersInYardRow(toLocation);

    getWorklistPage()
        .clickBlockPickerButton()
        .selectHeapBlock(heapBlock)
        .verify(containerStatusIcon(expandedContainer, false))
        .verify(utrBackgroundColor(containerLocation.toString(), CssRgbaColors.Yellow))
        .selectContainer(container.id)
        .verify(containerStatusIcon(expandedContainer, true))
        .verify(utrBackgroundColor(containerLocation.toString(), CssRgbaColors.Aqua))
        .verify(containerBorderColor(expandedContainer, CssRgbColors.Blue))
        .verify(
            header(
                container.id,
                container.weight,
                containerLocation.toString(),
                toLocation.toString(),
                heapBlock));
  }

  private ContainerPlan createContainerPlan(
      IntermodalUnit container,
      Location moveFrom,
      Location moveTo,
      MoveStatus moveStatus,
      Location segmentFrom,
      Location segmentTo,
      MoveSegmentStatus segmentStatus) {
    return terminalController.planContainer(
        planBuilder ->
            planBuilder
                .withContainer(container)
                .withMove(s -> s.withFrom(moveFrom).withTo(moveTo).withStatus(moveStatus))
                .withSegment(
                    s ->
                        s.withAssignedEquipment(singletonList(equipmentId))
                            .withKind(SegmentKind.Y)
                            .withStatus(segmentStatus)
                            .withFrom(segmentFrom)
                            .withTo(segmentTo)));
  }
}
