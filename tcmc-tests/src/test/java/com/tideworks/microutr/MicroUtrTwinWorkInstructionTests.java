package com.tideworks.microutr;

import static com.tideworks.verifications.microutr.Verifications.nextLocationLabel;
import static com.tideworks.verifications.microutr.Verifications.noTitleToolBar;
import static com.tideworks.verifications.microutr.Verifications.twinDeliverTitleToolBar;
import static com.tideworks.verifications.microutr.Verifications.twinPickupTitleToolBar;

import com.tideworks.base.MicroUtrBase;
import com.tideworks.json.objectmodel.EquipmentLocationKind;
import com.tideworks.json.objectmodel.IntermodalUnit;
import com.tideworks.json.objectmodel.MoveSegmentStatus;
import com.tideworks.json.objectmodel.Segment;
import com.tideworks.json.objectmodel.locations.Location;
import com.tideworks.json.objectmodel.locations.YardLocation;

import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import lombok.val;
import org.apache.commons.lang3.ArrayUtils;
import org.testng.annotations.Test;

/** Tests for micro utr client works twin instruction. */
public class MicroUtrTwinWorkInstructionTests extends MicroUtrBase {

  @Test
  @Issue("TCL-14430")
  @TmsLink("6463745")
  public void twinPickupUtrAssignToTwinMoveFirstMoveFromLocationTest() {
    YardLocation firstFrom = getYardLocation(block, row, stack, tier);
    YardLocation secondFrom = getYardLocation(secondBlock, row, stack, tier);
    IntermodalUnit firstContainer = getContainer(containerNo, firstFrom);
    IntermodalUnit secondContainer = getContainer(secondContainerNo, secondFrom);

    Segment[] segments =
        getIncompleteTwinSegments(
            firstContainer, secondContainer, MoveSegmentStatus.Active, MoveSegmentStatus.Active);

    stubTestData(segments);

    getMainPage().verify(nextLocationLabel(firstFrom.toString())).verify(twinPickupTitleToolBar());
  }

  @Test
  @Issue("TCL-14430")
  @TmsLink("6518700")
  public void twinPickUpFirstContainerLoadedOntoUtrSecondMoveFromLocationTest() {
    Location firstFrom = getEquipmentLocation(utrProfile.equipmentId, EquipmentLocationKind.truck);
    YardLocation secondFrom = getYardLocation(secondBlock, row, stack, tier);
    IntermodalUnit firstContainer = getContainer(containerNo, firstFrom);
    IntermodalUnit secondContainer = getContainer(secondContainerNo, secondFrom);
    Segment[] segments =
        getIncompleteTwinSegments(
            firstContainer, secondContainer, MoveSegmentStatus.Active, MoveSegmentStatus.Active);

    stubTestData(segments);

    getMainPage().verify(nextLocationLabel(secondFrom.toString())).verify(twinPickupTitleToolBar());
  }

  @Test
  @Issue("TCL-14430")
  @TmsLink("6518701")
  public void twinDeliverBothContainerLoadedFirstContainerToLocationTest() {
    Location firstFrom = getEquipmentLocation(utrProfile.equipmentId, EquipmentLocationKind.truck);
    Location secondFrom = getEquipmentLocation(utrProfile.equipmentId, EquipmentLocationKind.truck);
    IntermodalUnit firstContainer = getContainer(containerNo, firstFrom);
    IntermodalUnit secondContainer = getContainer(secondContainerNo, secondFrom);
    Segment[] segments =
        getIncompleteTwinSegments(
            firstContainer, secondContainer, MoveSegmentStatus.Active, MoveSegmentStatus.Active);

    stubTestData(segments);

    YardLocation deliverLocation = (YardLocation) segments[0].to;

    getMainPage()
        .verify(nextLocationLabel(deliverLocation.toString()))
        .verify(twinDeliverTitleToolBar());
  }

  @Test
  @Issue("TCL-TCL-14430")
  @TmsLink("6518702")
  public void twinDeliverParentSegmentCompleteSecondContainerToLocationTest() {
    Location firstFrom = getYardLocation(block + "END", row, stack, tier);
    Location secondFrom = getEquipmentLocation(utrProfile.equipmentId, EquipmentLocationKind.truck);
    IntermodalUnit firstContainer = getContainer(containerNo, firstFrom);
    IntermodalUnit secondContainer = getContainer(secondContainerNo, secondFrom);
    Segment[] segments =
        getIncompleteTwinSegments(
            firstContainer, secondContainer, MoveSegmentStatus.Completed, MoveSegmentStatus.Active);

    stubTestData(segments);

    YardLocation deliverLocation = (YardLocation) segments[0].to;

    getMainPage()
        .verify(nextLocationLabel(deliverLocation.toString()))
        .verify(twinDeliverTitleToolBar());
  }

  @Test
  @Issue("TCL-14430")
  @TmsLink("6518702")
  public void awaitingInstructionsBothSegmentCompleteEmptyLocationTest() {
    YardLocation firstFrom = getYardLocation(block, row, stack, tier);
    YardLocation secondFrom = getYardLocation(secondBlock, row, stack, tier);
    IntermodalUnit firstContainer = getContainer(containerNo, firstFrom);
    IntermodalUnit secondContainer = getContainer(secondContainerNo, secondFrom);

    Segment[] segments =
        getIncompleteTwinSegments(
            firstContainer, secondContainer, MoveSegmentStatus.Active, MoveSegmentStatus.Active);

    stubTestData(createEmptyArray(Segment.class));

    val mainPage =
        getMainPage()
            .verify(nextLocationLabel(awaitingInstructionsLocation))
            .verify(noTitleToolBar());

    stubTestData(segments);
    mainPage.verify(nextLocationLabel(firstFrom.toString())).verify(twinPickupTitleToolBar());
  }

  @Test
  @Issue("TCL-19141")
  @TmsLink("9334178")
  public void twinPickupParentSegmentCompletedSecondContainerSelectedFromLocationNextTwinsTest() {
    Location container1Location = getVesselLocation(vesselBay1);
    Location container2Location = getVesselLocation(vesselBay3);
    Location container3Location =
        getEquipmentLocation(utrProfile.equipmentId, EquipmentLocationKind.truck);
    Location segmentsFromLocation = getEquipmentLocation(crane01, EquipmentLocationKind.crane);
    Location segment1ToLocation = getYardLocation(block, row102, stackB, tier);
    Location segment2ToLocation = getYardLocation(block, row106, stackB, tier);
    Location segment3ToLocation = getYardLocation(block, row106, stackA, tier);
    IntermodalUnit firstContainer = getContainer(containerNo, container1Location);
    IntermodalUnit secondContainer = getContainer(secondContainerNo, container2Location);
    IntermodalUnit thirdContainer = getContainer(thirdContainerNo, container3Location);
    Segment[] segments =
        getIncompleteTwinSegments(
            firstContainer,
            secondContainer,
            MoveSegmentStatus.Active,
            MoveSegmentStatus.Active,
            segmentsFromLocation,
            segment1ToLocation,
            segmentsFromLocation,
            segment2ToLocation);
    Segment[] segments2 =
        getIncompleteTwinSegments(
            thirdContainer,
            thirdContainer,
            MoveSegmentStatus.Completed,
            MoveSegmentStatus.Active,
            segmentsFromLocation,
            segment3ToLocation,
            segmentsFromLocation,
            segment3ToLocation);

    stubTestData(segments2);

    getMainPage()
        .verify(nextLocationLabel(segment3ToLocation.toString()))
        .verify(twinDeliverTitleToolBar());

    stubTestData(ArrayUtils.addAll(segments, segments2));

    getMainPage().verify(nextLocationLabel("Crane 01")).verify(twinPickupTitleToolBar());
  }
}
