package com.tideworks.microutr;

import static com.tideworks.verifications.microutr.Verifications.deliverTitleToolBar;
import static com.tideworks.verifications.microutr.Verifications.nextLocationLabel;
import static com.tideworks.verifications.microutr.Verifications.noTitleToolBar;
import static com.tideworks.verifications.microutr.Verifications.pickupTitleToolBar;

import com.tideworks.base.MicroUtrBase;
import com.tideworks.json.objectmodel.EquipmentLocationKind;
import com.tideworks.json.objectmodel.IntermodalUnit;
import com.tideworks.json.objectmodel.Segment;
import com.tideworks.json.objectmodel.locations.EquipmentLocation;
import com.tideworks.json.objectmodel.locations.YardLocation;

import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import lombok.val;
import org.testng.annotations.Test;

/** Tests for micro utr client works instruction. */
public class MicroUtrWorkInstructionTests extends MicroUtrBase {

  @Test
  @Issue("TCL-14429")
  @TmsLink("6518725")
  public void utrMicroClientHasDeliverWorkInstruction() {
    EquipmentLocation currentTruckLocation =
        getEquipmentLocation(utrProfile.equipmentId, EquipmentLocationKind.truck);
    IntermodalUnit containerOnTruck = getContainer(containerNo, currentTruckLocation);
    YardLocation fromLocation = getYardLocation(blockAandB, row102, stackA, tier);
    YardLocation toLocation = getYardLocation(blockCss, row106, stackB, tier);
    Segment segment = getSegment(containerOnTruck, fromLocation, toLocation, sequence);

    stubTestData(containerOnTruck, segment);

    getMainPage().verify(nextLocationLabel(toLocation.toString())).verify(deliverTitleToolBar());
  }

  @Test
  @Issue("TCL-14429")
  @TmsLink("6537255")
  public void utrMicroClientHasPickupWorkInstruction() {
    YardLocation fromLocation = getYardLocation(blockAandB, row102, stackA, tier);
    IntermodalUnit containerOnTruck = getContainer(containerNo, fromLocation);
    YardLocation toLocation = getYardLocation(blockCss, row106, stackB, tier);
    Segment segment = getSegment(containerOnTruck, fromLocation, toLocation, sequence);

    stubTestData(containerOnTruck, segment);

    getMainPage().verify(nextLocationLabel(fromLocation.toString())).verify(pickupTitleToolBar());
  }

  @Test
  @Issue("TCL-14429")
  @TmsLink("6537286")
  public void utrMicroClientGetsFailedToDeckWorkInstruction() {
    EquipmentLocation currentTruckLocation =
        getEquipmentLocation(utrProfile.equipmentId, EquipmentLocationKind.truck);
    IntermodalUnit containerOnTruck = getContainer(containerNo, currentTruckLocation);
    YardLocation fromLocation = getYardLocation(blockAandB, row102, stackA, tier);
    YardLocation toLocation = getYardLocation(blockCss, row106, stackB, tier, true);
    Segment segment = getSegment(containerOnTruck, fromLocation, toLocation, sequence);

    stubTestData(containerOnTruck, segment);

    getMainPage().verify(nextLocationLabel("Failed To Deck")).verify(deliverTitleToolBar());
  }

  @Test
  @Issue("TCL-14429")
  @TmsLink("6537314")
  public void utrMicroClientGetsWorkInstructionForHeap() {
    EquipmentLocation currentTruckLocation =
        getEquipmentLocation(utrProfile.equipmentId, EquipmentLocationKind.truck);
    YardLocation toLocation = getYardLocation(blockCss, "", "", null);
    IntermodalUnit containerOnTruck = getContainer(containerNo, currentTruckLocation);
    EquipmentLocation fromLocation = getEquipmentLocation(crane01, EquipmentLocationKind.crane);
    Segment segment = getSegment(containerOnTruck, fromLocation, toLocation, sequence);

    stubTestData(containerOnTruck, segment);

    getMainPage().verify(nextLocationLabel("<<css>>")).verify(deliverTitleToolBar());
  }

  @Test
  @Issue("TCL-14429")
  @TmsLink("7381023")
  public void utrMicroClientInstructionChangingPickupToUnknown() {
    EquipmentLocation fromLocation = getEquipmentLocation(crane01, EquipmentLocationKind.crane);
    YardLocation toLocation = getYardLocation(blockAandB, row102, stackA, tier);
    IntermodalUnit containerOnTruck = getContainer(containerNo, fromLocation);
    Segment segment = getSegment(containerOnTruck, fromLocation, toLocation, sequence);

    stubTestData(containerOnTruck, segment);

    val mainPage =
        getMainPage().verify(nextLocationLabel("Crane " + crane01)).verify(pickupTitleToolBar());

    stubTestData(createEmptyArray(Segment.class));

    mainPage.verify(nextLocationLabel(unknownLocation)).verify(deliverTitleToolBar());
  }

  @Test
  @Issue("TCL-14429")
  @TmsLink("7381008")
  public void utrMicroClientInstructionChangingDeliverToUnknown() {
    EquipmentLocation currentTruckLocation =
        getEquipmentLocation(utrProfile.equipmentId, EquipmentLocationKind.truck);
    YardLocation fromLocation = getYardLocation(blockAandB, row102, stackA, tier);
    IntermodalUnit containerOnTruck = getContainer(containerNo, currentTruckLocation);
    EquipmentLocation toLocation = getEquipmentLocation(crane01, EquipmentLocationKind.crane);
    Segment segment = getSegment(containerOnTruck, fromLocation, toLocation, sequence);

    stubTestData(containerOnTruck, segment);

    val mainPage =
        getMainPage().verify(nextLocationLabel("Crane " + crane01)).verify(deliverTitleToolBar());

    stubTestData(createEmptyArray(Segment.class));

    mainPage.verify(nextLocationLabel(unknownLocation)).verify(deliverTitleToolBar());
  }

  @Test
  @Issue("TCL-14429")
  @TmsLink("7381025")
  public void utrMicroClientInstructionChangingDeliverToAwaitingInstructions() {
    EquipmentLocation currentTruckLocation =
        getEquipmentLocation(utrProfile.equipmentId, EquipmentLocationKind.truck);
    YardLocation fromLocation = getYardLocation(blockAandB, row102, stackA, tier);
    IntermodalUnit containerOnTruck = getContainer(containerNo, currentTruckLocation);
    EquipmentLocation toLocation = getEquipmentLocation(crane01, EquipmentLocationKind.crane);
    Segment segment = getSegment(containerOnTruck, fromLocation, toLocation, sequence);

    stubTestData(containerOnTruck, segment);

    val mainPage =
        getMainPage().verify(nextLocationLabel("Crane " + crane01)).verify(deliverTitleToolBar());

    stubTestData(createEmptyArray(Segment.class));
    stubTestData(createEmptyArray(IntermodalUnit.class));

    mainPage.verify(nextLocationLabel(awaitingInstructionsLocation)).verify(noTitleToolBar());
  }

  @Test
  @Issue("TCL-14429")
  @TmsLink("7381024")
  public void utrMicroClientInstructionChangingPickupToAwaitingInstructions() {
    EquipmentLocation fromLocation = getEquipmentLocation(crane01, EquipmentLocationKind.crane);
    YardLocation toLocation = getYardLocation(blockAandB, row102, stackA, tier);
    IntermodalUnit containerOnTruck = getContainer(containerNo, fromLocation);
    Segment segment = getSegment(containerOnTruck, fromLocation, toLocation, sequence);

    stubTestData(containerOnTruck, segment);

    val mainPage =
        getMainPage().verify(nextLocationLabel("Crane " + crane01)).verify(pickupTitleToolBar());

    stubTestData(createEmptyArray(Segment.class));
    stubTestData(createEmptyArray(IntermodalUnit.class));

    mainPage.verify(nextLocationLabel(awaitingInstructionsLocation)).verify(noTitleToolBar());
  }

  @Test
  @Issue("TCL-14429")
  @TmsLink("7381027")
  public void utrMicroClientInstructionChangingAwaitingInstructionsToDeliver() {
    EquipmentLocation currentTruckLocation =
        getEquipmentLocation(utrProfile.equipmentId, EquipmentLocationKind.truck);
    YardLocation fromLocation = getYardLocation(blockAandB, row102, stackA, tier);
    IntermodalUnit containerOnTruck = getContainer(containerNo, currentTruckLocation);
    EquipmentLocation toLocation = getEquipmentLocation(crane01, EquipmentLocationKind.crane);
    Segment segment = getSegment(containerOnTruck, fromLocation, toLocation, sequence);

    stubTestData(createEmptyArray(Segment.class));
    stubTestData(createEmptyArray(IntermodalUnit.class));

    val mainPage =
        getMainPage()
            .verify(nextLocationLabel(awaitingInstructionsLocation))
            .verify(noTitleToolBar());

    stubTestData(containerOnTruck, segment);

    mainPage.verify(nextLocationLabel("Crane " + crane01)).verify(deliverTitleToolBar());
  }

  @Test
  @Issue("TCL-14429")
  @TmsLink("7381026")
  public void utrMicroClientInstructionChangingAwaitingInstructionsToPickup() {
    EquipmentLocation fromLocation = getEquipmentLocation(crane01, EquipmentLocationKind.crane);
    YardLocation toLocation = getYardLocation(blockAandB, row102, stackA, tier);
    IntermodalUnit containerOnTruck = getContainer(containerNo, fromLocation);
    Segment segment = getSegment(containerOnTruck, fromLocation, toLocation, sequence);

    stubTestData(createEmptyArray(Segment.class));
    stubTestData(createEmptyArray(IntermodalUnit.class));

    val mainPage =
        getMainPage()
            .verify(nextLocationLabel(awaitingInstructionsLocation))
            .verify(noTitleToolBar());

    stubTestData(containerOnTruck, segment);

    mainPage.verify(nextLocationLabel("Crane " + crane01)).verify(pickupTitleToolBar());
  }

  @Test
  @Issue("TCL-14429")
  @TmsLink("7381028")
  public void utrMicroClientInstructionChangingPickupToAwaitingInstructionsToDelivery() {
    EquipmentLocation fromLocation = getEquipmentLocation(crane01, EquipmentLocationKind.crane);
    YardLocation toLocation = getYardLocation(blockAandB, row102, stackA, tier);
    IntermodalUnit container = getContainer(containerNo, fromLocation);
    Segment segment = getSegment(container, fromLocation, toLocation, sequence);

    stubTestData(container, segment);

    val mainPage =
        getMainPage().verify(nextLocationLabel("Crane " + crane01)).verify(pickupTitleToolBar());

    stubTestData(createEmptyArray(Segment.class));
    stubTestData(createEmptyArray(IntermodalUnit.class));
    mainPage.verify(nextLocationLabel(awaitingInstructionsLocation)).verify(noTitleToolBar());

    EquipmentLocation currentTruckLocation =
        getEquipmentLocation(utrProfile.equipmentId, EquipmentLocationKind.truck);
    EquipmentLocation fromLocationDeliver =
        getEquipmentLocation(crane01, EquipmentLocationKind.crane);
    IntermodalUnit containerOnTruck = getContainer(containerNo, currentTruckLocation);
    YardLocation toLocationDeliver = getYardLocation(blockAandB, row102, stackA, tier);
    segment = getSegment(containerOnTruck, fromLocationDeliver, toLocationDeliver, sequence);
    stubTestData(containerOnTruck, segment);

    mainPage.verify(nextLocationLabel(toLocation.toString())).verify(deliverTitleToolBar());
  }

  @Test
  @Issue("TCL-16357")
  @TmsLink("8196088")
  public void utrMicroClientInstructionCycledMovesDeliverToCranePickupFromCrane() {
    EquipmentLocation craneLocation = getEquipmentLocation(crane01, EquipmentLocationKind.crane);
    EquipmentLocation utrLocation =
        getEquipmentLocation(utrProfile.equipmentId, EquipmentLocationKind.truck);
    YardLocation yardLocation = getYardLocation(blockAandB, row102, stackA, tier);
    IntermodalUnit container1 = getContainer(containerNo, utrLocation);
    IntermodalUnit container2 = getContainer("Cont000002", craneLocation);
    Segment segment1 = getSegment(container1, yardLocation, craneLocation, 0);
    Segment segment2 = getSegment(container2, craneLocation, yardLocation, 1);

    stubTestData(container1);
    stubTestData(segment1, segment2);

    val mainPage =
        getMainPage().verify(nextLocationLabel("Crane " + crane01)).verify(deliverTitleToolBar());

    stubTestData(createEmptyArray(IntermodalUnit.class));
    stubTestData(segment2);
    mainPage.verify(nextLocationLabel("Crane " + crane01)).verify(pickupTitleToolBar());

    container2.location = utrLocation;
    stubTestData(container2);
    stubTestData(segment2);

    mainPage.verify(nextLocationLabel(yardLocation.toString())).verify(deliverTitleToolBar());
  }

  @Test
  @Issue("TCL-16357")
  @TmsLink("8196088")
  public void utrMicroClientInstructionCycledMovesDeliverToYardPickupFromCrane() {
    EquipmentLocation craneLocation = getEquipmentLocation(crane01, EquipmentLocationKind.crane);
    EquipmentLocation utrLocation =
        getEquipmentLocation(utrProfile.equipmentId, EquipmentLocationKind.truck);
    YardLocation yardLocation = getYardLocation(blockAandB, row102, stackA, tier);
    IntermodalUnit container1 = getContainer("Cont000002", utrLocation);
    IntermodalUnit container2 = getContainer(containerNo, yardLocation);
    Segment segment1 = getSegment(container1, craneLocation, yardLocation, 0);
    Segment segment2 = getSegment(container2, craneLocation, yardLocation, 1);

    stubTestData(container1);
    stubTestData(segment1, segment2);

    getMainPage().verify(nextLocationLabel(yardLocation.toString())).verify(deliverTitleToolBar());
  }

  @Test
  @Issue("TCL-19141")
  @TmsLink("9334179")
  public void utrMicroClientInstructionFirstMoveSelectedDeliverToPickupChanged() {
    EquipmentLocation utrLocation =
        getEquipmentLocation(utrProfile.equipmentId, EquipmentLocationKind.truck);
    YardLocation formLocation1 = getYardLocation(block, row102, stackA, tier);
    YardLocation toLocation1 = getYardLocation(secondBlock, row102, stackA, tier);
    YardLocation formLocation2 = getYardLocation(block, row102, stackB, tier);
    YardLocation toLocation2 = getYardLocation(secondBlock, row102, stackB, tier);
    IntermodalUnit container1 = getContainer(containerNo, utrLocation);
    IntermodalUnit container2 = getContainer(secondContainerNo, formLocation2);
    Segment segment1 = getSegment(container1, formLocation1, toLocation1, 1);
    Segment segment2 = getSegment(container2, formLocation2, toLocation2, 1);

    stubTestData(segment1);

    val mainPage =
        getMainPage()
            .verify(nextLocationLabel(segment1.to.toString()))
            .verify(deliverTitleToolBar());

    stubTestData(segment2, segment1);

    mainPage.verify(nextLocationLabel(segment2.from.toString())).verify(pickupTitleToolBar());
  }
}
