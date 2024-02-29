package com.tideworks.vesselclerk;

import static com.tideworks.verifications.vesselclerk.Verifications.loadHeldMoveDialog;
import static com.tideworks.verifications.vesselclerk.Verifications.moveOnHoldError;
import static com.tideworks.verifications.vesselclerk.Verifications.moveSelectionWarning;
import static com.tideworks.verifications.vesselclerk.Verifications.searchTextFieldIsEmpty;

import com.tideworks.base.VesselClerkBase;
import com.tideworks.json.objectmodel.MoveSegmentStatus;
import com.tideworks.json.objectmodel.MoveStatus;
import com.tideworks.pages.vesselclerk.ContainerSearchPage;

import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import lombok.val;
import org.testng.annotations.Test;

/** Tests for vessel load moves. */
public class VesselClerkLoadTests extends VesselClerkBase {

  @Test
  @Issue("TCL-14694")
  @TmsLink("7699079")
  public void errorIfContainerSelectedWithHoldMove() {
    val containerPlan =
        terminalController.planContainerOnUtrFromYardToVessel(
            MoveStatus.Held, MoveSegmentStatus.Inactive, false);

    stubTestData(
        terminalController.getContainerEmbedMovesAndSegments(containerPlan.intermodalUnit));
    val request = stubSelectSegmentRequest(containerPlan.segments.get(0).id);

    getOperationsPage()
        .selectVessel(containerPlan.operation.vessel)
        .selectCrane(containerPlan.operation.crane)
        .clickSelectOperationButton()
        .searchContainer(containerPlan.getDigitsFromContainerNumber())
        .<ContainerSearchPage>selectContainer(containerPlan.intermodalUnit.id)
        .verify(moveOnHoldError())
        .clickCloseErrorDialog();

    apiMockService.verify(request, 0);
  }

  @Test
  @Issue("TCL-14694")
  @TmsLink("7699078")
  public void loadHeldMoveRightContinueConfirmDialogIfContainerSelectedWithHoldMove() {
    val containerPlan =
        terminalController.planContainerOnUtrFromYardToVessel(
            MoveStatus.Held, MoveSegmentStatus.Inactive, false);

    vesselClerkProfile.canLoadHeldMove = true;

    stubProfileData(vesselClerkProfile);
    stubTestData(
        terminalController.getContainerEmbedMovesAndSegments(containerPlan.intermodalUnit));
    val request = stubSelectSegmentRequest(containerPlan.segments.get(0).id);

    getOperationsPage()
        .selectVessel(containerPlan.operation.vessel)
        .selectCrane(containerPlan.operation.crane)
        .clickSelectOperationButton()
        .searchContainer(containerPlan.getDigitsFromContainerNumber())
        .<ContainerSearchPage>selectContainer(containerPlan.intermodalUnit.id)
        .verify(loadHeldMoveDialog())
        .clickConfirmLoadHeldMove();

    apiMockService.verify(request);
  }

  @Test
  @Issue("TCL-14694")
  @TmsLink("7699081")
  public void loadHeldMoveRightConfirmDialogIfContainerSelectedWithHoldMoveAndAnotherVessel() {
    val containerPlan =
        terminalController.planContainerOnUtrFromYardToVessel(
            MoveStatus.Held, MoveSegmentStatus.Inactive, false);

    val operation =
        terminal
            .getVesselOperations()
            .stream()
            .filter(
                vesselOperation ->
                    !vesselOperation.crane.equals(containerPlan.move.operation.crane))
            .findFirst()
            .get();

    vesselClerkProfile.canLoadHeldMove = true;

    stubProfileData(vesselClerkProfile);
    stubTestData(
        terminalController.getContainerEmbedMovesAndSegments(containerPlan.intermodalUnit));
    val request = stubSelectSegmentRequest(containerPlan.segments.get(0).id);

    getOperationsPage()
        .selectVessel(operation.vessel)
        .selectCrane(operation.crane)
        .clickSelectOperationButton()
        .searchContainer(containerPlan.getDigitsFromContainerNumber())
        .<ContainerSearchPage>selectContainer(containerPlan.intermodalUnit.id)
        .verify(loadHeldMoveDialog())
        .<ContainerSearchPage>clickConfirmLoadHeldMove()
        .verify(moveSelectionWarning())
        .clickConfirmSelectedContainer();

    apiMockService.verify(request);
  }

  @Test
  @Issue("TCL-14694")
  @TmsLink("7810690")
  public void loadHeldMoveRightDeclineConfirmDialogIfContainerSelectedWithHoldMove() {
    val containerPlan =
        terminalController.planContainerOnUtrFromYardToVessel(
            MoveStatus.Held, MoveSegmentStatus.Inactive, false);

    vesselClerkProfile.canLoadHeldMove = true;

    stubProfileData(vesselClerkProfile);
    stubTestData(
        terminalController.getContainerEmbedMovesAndSegments(containerPlan.intermodalUnit));
    val request = stubSelectSegmentRequest(containerPlan.segments.get(0).id);

    getOperationsPage()
        .selectVessel(containerPlan.operation.vessel)
        .selectCrane(containerPlan.operation.crane)
        .clickSelectOperationButton()
        .searchContainer(containerPlan.getDigitsFromContainerNumber())
        .<ContainerSearchPage>selectContainer(containerPlan.intermodalUnit.id)
        .verify(loadHeldMoveDialog())
        .clickDeclineLoadHeldMove()
        .verify(searchTextFieldIsEmpty());

    apiMockService.verify(request, 0);
  }
}
