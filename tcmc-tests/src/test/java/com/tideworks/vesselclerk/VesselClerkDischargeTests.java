package com.tideworks.vesselclerk;

import static com.tideworks.verifications.vesselclerk.Verifications.chassisTextField;
import static com.tideworks.verifications.vesselclerk.Verifications.chassisTextFieldIsAbsent;
import static com.tideworks.verifications.vesselclerk.Verifications.chassisTextFieldIsDisabled;
import static com.tideworks.verifications.vesselclerk.Verifications.completeToBackreachToggleActivationState;
import static com.tideworks.verifications.vesselclerk.Verifications.containerNumberTextField;
import static com.tideworks.verifications.vesselclerk.Verifications.dischargeTitle;
import static com.tideworks.verifications.vesselclerk.Verifications.nextLocationTextField;
import static com.tideworks.verifications.vesselclerk.Verifications.nextLocationTextFieldIsDisabled;
import static com.tideworks.verifications.vesselclerk.Verifications.operationSucceededDialogContent;

import com.tideworks.base.VesselClerkBase;
import com.tideworks.json.objectmodel.locations.EquipmentLocation;
import com.tideworks.pages.elements.dialogs.OperationSucceededDialog;
import com.tideworks.pages.vesselclerk.MoveCompletionPage;

import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import lombok.val;
import org.testng.annotations.Test;

/** Tests for vessel discharge moves. */
public class VesselClerkDischargeTests extends VesselClerkBase {

  @Test
  @Issue("TCL-14660")
  @TmsLink("6965476")
  public void moveFromCraneToWheeledChassisFieldIsPopulatedIfChassisIsPresent() {
    val containerPlan = terminalController.planContainerOnVesselFromVesselToWheeled();
    val truck = ((EquipmentLocation) containerPlan.segments.get(0).to).equipmentId;

    stubTestData(
        terminalController.getContainerEmbedMovesAndSegments(containerPlan.intermodalUnit));

    getOperationsPage()
        .selectVessel(containerPlan.operation.vessel)
        .selectCrane(containerPlan.operation.crane)
        .clickSelectOperationButton()
        .searchContainer(containerPlan.getDigitsFromContainerNumber())
        .<MoveCompletionPage>selectContainer(containerPlan.intermodalUnit.id)
        .verify(containerNumberTextField(containerPlan.intermodalUnit.id))
        .verify(nextLocationTextField(truck))
        .verify(chassisTextField(containerPlan.chassisId));
  }

  @Test
  @Issue("TCL-14660")
  @TmsLink("6965477")
  public void moveFromCraneToUtrChassisFieldIsAbsentIfChassisIsPresent() {
    val containerPlan = terminalController.planContainerOnVesselFromVesselToWheeled();
    val truck = ((EquipmentLocation) containerPlan.segments.get(0).to).equipmentId;

    stubTestData(
        terminalController.getContainerEmbedMovesAndSegments(containerPlan.intermodalUnit));

    vesselClerkProfile.requireChassis = false;
    stubProfileData(vesselClerkProfile);

    getOperationsPage()
        .selectVessel(containerPlan.operation.vessel)
        .selectCrane(containerPlan.operation.crane)
        .clickSelectOperationButton()
        .searchContainer(containerPlan.getDigitsFromContainerNumber())
        .<MoveCompletionPage>selectContainer(containerPlan.intermodalUnit.id)
        .verify(containerNumberTextField(containerPlan.intermodalUnit.id))
        .verify(nextLocationTextField(truck))
        .verify(chassisTextFieldIsAbsent());
  }

  @Test
  @Issue("TCL-14660")
  @TmsLink("7020823")
  public void moveFromCraneToWheeledChassisFieldIsEmptyIfChassisIsNotPresent() {
    val containerPlan = terminalController.planContainerOnVesselFromVesselToWheeledWithoutChassis();
    val truck = ((EquipmentLocation) containerPlan.segments.get(0).to).equipmentId;

    stubTestData(
        terminalController.getContainerEmbedMovesAndSegments(containerPlan.intermodalUnit));

    getOperationsPage()
        .selectVessel(containerPlan.operation.vessel)
        .selectCrane(containerPlan.operation.crane)
        .clickSelectOperationButton()
        .searchContainer(containerPlan.getDigitsFromContainerNumber())
        .<MoveCompletionPage>selectContainer(containerPlan.intermodalUnit.id)
        .verify(containerNumberTextField(containerPlan.intermodalUnit.id))
        .verify(nextLocationTextField(truck))
        .verify(chassisTextField());
  }

  @Test
  @Issue("TCL-14628")
  @TmsLink("6965479")
  public void moveFromCraneToWheeledCompletedToBackreach() {
    val containerPlan = terminalController.planContainerOnVesselFromVesselToWheeled();
    val truck = ((EquipmentLocation) containerPlan.segments.get(0).to).equipmentId;

    stubTestData(
        terminalController.getContainerEmbedMovesAndSegments(containerPlan.intermodalUnit));

    val completeMoveRequest =
        stubUpdateUnitLocation(
            containerPlan.intermodalUnit.id, "{\"type\":\"backreach\",\"crane\":\"CRANE 1\"}");

    getOperationsPage()
        .selectVessel(containerPlan.operation.vessel)
        .selectCrane(containerPlan.operation.crane)
        .clickSelectOperationButton()
        .searchContainer(containerPlan.getDigitsFromContainerNumber())
        .<MoveCompletionPage>selectContainer(containerPlan.intermodalUnit.id)
        .verify(dischargeTitle())
        .verify(nextLocationTextField(truck))
        .clickCompleteToBackreachToggle()
        .verify(nextLocationTextField("BACKREACH"))
        .verify(nextLocationTextFieldIsDisabled())
        .verify(chassisTextFieldIsDisabled())
        .clickCompleteMoveButton()
        .confirmCompleteMoveDialog()
        .<OperationSucceededDialog>verify(operationSucceededDialogContent())
        .closeOperationSucceededDialog();

    apiMockService.verify(completeMoveRequest);
  }

  @Test
  @Issue("TCL-14628")
  @TmsLink("6965467")
  public void moveFromCraneToUtrCompletedToBackreach() {
    val containerPlan = terminalController.planContainerOnVesselFromVesselToYard();
    val truck = ((EquipmentLocation) containerPlan.segments.get(0).to).equipmentId;

    stubTestData(
        terminalController.getContainerEmbedMovesAndSegments(containerPlan.intermodalUnit));

    val completeMoveRequest =
        stubUpdateUnitLocation(
            containerPlan.intermodalUnit.id, "{\"type\":\"backreach\",\"crane\":\"CRANE 1\"}");

    getOperationsPage()
        .selectVessel(containerPlan.operation.vessel)
        .selectCrane(containerPlan.operation.crane)
        .clickSelectOperationButton()
        .searchContainer(containerPlan.getDigitsFromContainerNumber())
        .<MoveCompletionPage>selectContainer(containerPlan.intermodalUnit.id)
        .verify(dischargeTitle())
        .verify(nextLocationTextField(truck))
        .clickCompleteToBackreachToggle()
        .verify(nextLocationTextField("BACKREACH"))
        .verify(nextLocationTextFieldIsDisabled())
        .clickCompleteMoveButton()
        .confirmCompleteMoveDialog()
        .<OperationSucceededDialog>verify(operationSucceededDialogContent())
        .closeOperationSucceededDialog();

    apiMockService.verify(completeMoveRequest);
  }

  @Test
  @Issue("TCL-14628")
  @TmsLink("6965466")
  public void moveFromCraneToUtrCompletedToPlaned() {
    val containerPlan = terminalController.planContainerOnVesselFromVesselToYard();
    val truck = ((EquipmentLocation) containerPlan.segments.get(0).to).equipmentId;

    stubTestData(
        terminalController.getContainerEmbedMovesAndSegments(containerPlan.intermodalUnit));

    val completeMoveRequest =
        stubUpdateUnitLocation(
            containerPlan.intermodalUnit.id,
            "{\"type\":\"equipment\",\"kind\":\"truck\",\"equipmentId\":\"UTR01\"}");

    getOperationsPage()
        .selectVessel(containerPlan.operation.vessel)
        .selectCrane(containerPlan.operation.crane)
        .clickSelectOperationButton()
        .searchContainer(containerPlan.getDigitsFromContainerNumber())
        .<MoveCompletionPage>selectContainer(containerPlan.intermodalUnit.id)
        .verify(dischargeTitle())
        .verify(nextLocationTextField(truck))
        .verify(completeToBackreachToggleActivationState(false))
        .clickCompleteMoveButton()
        .confirmCompleteMoveDialog()
        .<OperationSucceededDialog>verify(operationSucceededDialogContent())
        .closeOperationSucceededDialog();

    apiMockService.verify(completeMoveRequest);
  }

  @Test
  @Issue("TCL-14628")
  @TmsLink("6965467")
  public void moveFromCraneToBackreachCompletedToPlaned() {
    val containerPlan = terminalController.planContainerOnVesselFromVesselToBackreach();

    stubTestData(
        terminalController.getContainerEmbedMovesAndSegments(containerPlan.intermodalUnit));

    val completeMoveRequest =
        stubUpdateUnitLocation(
            containerPlan.intermodalUnit.id, "{\"type\":\"backreach\",\"crane\":\"CRANE 1\"}");

    getOperationsPage()
        .selectVessel(containerPlan.operation.vessel)
        .selectCrane(containerPlan.operation.crane)
        .clickSelectOperationButton()
        .searchContainer(containerPlan.getDigitsFromContainerNumber())
        .<MoveCompletionPage>selectContainer(containerPlan.intermodalUnit.id)
        .verify(dischargeTitle())
        .verify(completeToBackreachToggleActivationState(true))
        .verify(nextLocationTextFieldIsDisabled())
        .verify(nextLocationTextField("BACKREACH"))
        .clickCompleteMoveButton()
        .confirmCompleteMoveDialog()
        .<OperationSucceededDialog>verify(operationSucceededDialogContent())
        .closeOperationSucceededDialog();

    apiMockService.verify(completeMoveRequest);
  }
}
