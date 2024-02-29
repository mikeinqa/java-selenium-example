package com.tideworks.vesselclerk;

import static com.tideworks.verifications.elements.Verifications.dialogContent;
import static com.tideworks.verifications.vesselclerk.Verifications.containerNumberTextField;
import static com.tideworks.verifications.vesselclerk.Verifications.damageOffState;
import static com.tideworks.verifications.vesselclerk.Verifications.damageState;
import static com.tideworks.verifications.vesselclerk.Verifications.dischargeTitle;
import static com.tideworks.verifications.vesselclerk.Verifications.moveOnHoldError;
import static com.tideworks.verifications.vesselclerk.Verifications.moveSelectionWarning;
import static com.tideworks.verifications.vesselclerk.Verifications.noMovesError;
import static com.tideworks.verifications.vesselclerk.Verifications.noValidMovesError;
import static com.tideworks.verifications.vesselclerk.Verifications.oversizeTexts;
import static com.tideworks.verifications.vesselclerk.Verifications.sealsTexts;

import com.tideworks.base.VesselClerkBase;
import com.tideworks.json.builders.DamageBuilder;
import com.tideworks.json.objectmodel.MoveSegmentStatus;
import com.tideworks.json.objectmodel.MoveStatus;
import com.tideworks.json.objectmodel.Oversize;
import com.tideworks.json.objectmodel.Preference;
import com.tideworks.json.objectmodel.Seal;
import com.tideworks.pages.vesselclerk.ContainerSearchPage;
import com.tideworks.pages.vesselclerk.MoveCompletionPage;

import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import lombok.val;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/** General Vessel-clerk tests. */
public class VesselClerkGeneralTests extends VesselClerkBase {

  @DataProvider(name = "LoadHeldMoveRuleProvider")
  public static Object[][] isRuleOn() {
    return new Object[][] {{true, "7381467"}, {false, "7381467"}};
  }

  @Test
  @Issue("TCL-13550")
  @TmsLink("7381470")
  public void moveDeselectedAfterNavigationBackFromCompleteMovePage() {
    val containerPlan = terminalController.planContainerOnVesselFromVesselToYard();

    stubTestData(
        terminalController.getContainerEmbedMovesAndSegments(containerPlan.intermodalUnit));
    val request = stubSelectSegmentRequest(containerPlan.segments.get(0).id);
    // RequestPattern deselectContainerRequest = stubFor(put(deselectUrl(containerNo))
    // .willReturn(status(201))).getRequest();

    getOperationsPage()
        .selectVessel(containerPlan.operation.vessel)
        .selectCrane(containerPlan.operation.crane)
        .clickSelectOperationButton()
        .searchContainer(containerPlan.getDigitsFromContainerNumber())
        .<MoveCompletionPage>selectContainer(containerPlan.intermodalUnit.id)
        .returnToPreviousPage();

    apiMockService.verify(request);

    // TODO: egolykho: Add deselection verification
    // verify(RequestPatternBuilder.forCustomMatcher(deselectContainerRequest));

  }

  @Test
  @Issue("TCL-13550")
  @TmsLink("7381469")
  public void moveSelectedByEquipmentAfterContainerSelection() {
    val containerPlan = terminalController.planContainerOnVesselFromVesselToYard();

    stubTestData(
        terminalController.getContainerEmbedMovesAndSegments(containerPlan.intermodalUnit));
    val request = stubSelectSegmentRequest(containerPlan.segments.get(0).id);

    getOperationsPage()
        .selectVessel(containerPlan.operation.vessel)
        .selectCrane(containerPlan.operation.crane)
        .clickSelectOperationButton()
        .searchContainer(containerPlan.getDigitsFromContainerNumber())
        .selectContainer(containerPlan.intermodalUnit.id);

    apiMockService.verify(request);
  }

  @Test
  @Issue("TCL-13550")
  @TmsLink("7381468")
  public void warningIfMoveNotPlanedForSelectedCrane() {
    val containerPlan = terminalController.planContainerOnVesselFromVesselToYard();
    val operation =
        terminal
            .getVesselOperations()
            .stream()
            .filter(
                vesselOperation ->
                    !vesselOperation.crane.equals(containerPlan.move.operation.crane))
            .findFirst()
            .get();

    stubTestData(
        terminalController.getContainerEmbedMovesAndSegments(containerPlan.intermodalUnit));
    val request = stubSelectSegmentRequest(containerPlan.segments.get(0).id);

    getOperationsPage()
        .selectVessel(operation.vessel)
        .selectCrane(operation.crane)
        .clickSelectOperationButton()
        .searchContainer(containerPlan.getDigitsFromContainerNumber())
        .<ContainerSearchPage>selectContainer(containerPlan.intermodalUnit.id)
        .verify(moveSelectionWarning())
        .clickConfirmSelectedContainer()
        .verify(dischargeTitle())
        .verify(containerNumberTextField(containerPlan.intermodalUnit.id));

    apiMockService.verify(request);
  }

  @Test(dataProvider = "LoadHeldMoveRuleProvider")
  @Issue("TCL-13550")
  @Issue("TCL-14694")
  @TmsLink("7381467")
  @TmsLink("7699076")
  public void errorIfContainerSelectionWithHoldMove(Boolean isRuleOn, String testCaseNumber) {
    val containerPlan =
        terminalController.planContainerOnVesselFromVesselToYard(
            MoveStatus.Held, MoveSegmentStatus.Inactive, false);
    vesselClerkProfile.canLoadHeldMove = isRuleOn;

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
        .verify(moveOnHoldError())
        .clickCloseMoveOnHoldError();

    apiMockService.verify(request, 0);
  }

  @Test
  @Issue("TCL-13550")
  @TmsLink("7381466")
  public void errorIfContainerHasMoveNotPlannedToVessel() {
    val containerPlan = terminalController.planContainerInYardFromYardToYard();
    val operation = terminal.getVesselOperations().get(0);

    stubTestData(
        terminalController.getContainerEmbedMovesAndSegments(containerPlan.intermodalUnit));
    val request = stubSelectSegmentRequest(containerPlan.segments.get(0).id);

    getOperationsPage()
        .selectVessel(operation.vessel)
        .selectCrane(operation.crane)
        .clickSelectOperationButton()
        .searchContainer(containerPlan.getDigitsFromContainerNumber())
        .<ContainerSearchPage>selectContainer(containerPlan.intermodalUnit.id)
        .verify(noValidMovesError())
        .clickCloseErrorDialog();

    apiMockService.verify(request);
  }

  @Test
  @Issue("TCL-13550")
  @TmsLink("7381465")
  public void errorIfContainerSelectionWithoutMove() {
    val containerPlan = terminalController.planContainerInYardFromYardToYard();
    val operation = terminal.getVesselOperations().get(0);

    stubTestData(containerPlan.intermodalUnit);

    getOperationsPage()
        .selectVessel(operation.vessel)
        .selectCrane(operation.crane)
        .clickSelectOperationButton()
        .searchContainer(containerPlan.getDigitsFromContainerNumber())
        .<ContainerSearchPage>selectContainer(containerPlan.intermodalUnit.id)
        .verify(noMovesError())
        .clickCloseErrorDialog();
  }

  @Test
  @Issue("TCL-15775")
  @TmsLink("9871634")
  public void getAndChangeSealsForContainer() {
    val containerPlan = terminalController.planContainerOnVesselFromVesselToYard();

    val getSeal = new Seal();
    getSeal.seal1 = "get seal1";
    getSeal.seal2 = "get seal2";
    getSeal.seal3 = "get seal3";
    getSeal.seal4 = "get seal4";

    val setSeal = new Seal();
    setSeal.seal1 = "set seal1";
    setSeal.seal2 = "set seal2";
    setSeal.seal3 = "set seal3";
    setSeal.seal4 = "set seal4";

    vesselClerkProfile.canEditSeals = true;

    stubProfileData(vesselClerkProfile);

    stubTestData(
        terminalController.getContainerEmbedMovesAndSegments(containerPlan.intermodalUnit));
    stubGetSeals(containerPlan.intermodalUnit.id, getSeal);
    val request = stubSetSeals(containerPlan.intermodalUnit.id, setSeal);

    getOperationsPage()
        .selectVessel(containerPlan.operation.vessel)
        .selectCrane(containerPlan.operation.crane)
        .clickSelectOperationButton()
        .searchContainer(containerPlan.getDigitsFromContainerNumber())
        .<MoveCompletionPage>selectContainer(containerPlan.intermodalUnit.id)
        .clickRightMenuToggle()
        .clickEditSealsButton()
        .verify(sealsTexts(getSeal))
        .changeSealsTexts(setSeal)
        .clickSaveButton()
        .verify(dialogContent("Seals were updated"));

    apiMockService.verify(request);
  }

  @Test
  @Issue("TCL-15775")
  @TmsLink("9871635")
  public void getAndChangeOversizeForContainer() {
    val containerPlan = terminalController.planContainerOnVesselFromVesselToYard();

    val getOversize = new Oversize();
    getOversize.aft = "10";
    getOversize.forward = "11";
    getOversize.height = "12";
    getOversize.left = "13";
    getOversize.right = "14";

    val setOversize = new Oversize();
    setOversize.aft = "20";
    setOversize.forward = "21";
    setOversize.height = "22";
    setOversize.left = "23";
    setOversize.right = "24";

    vesselClerkProfile.canEditOversize = true;

    stubProfileData(vesselClerkProfile);

    stubTestData(
        terminalController.getContainerEmbedMovesAndSegments(containerPlan.intermodalUnit));
    stubGetOversize(containerPlan.intermodalUnit.id, getOversize);
    val request = stubSetOversize(containerPlan.intermodalUnit.id, setOversize);

    getOperationsPage()
        .selectVessel(containerPlan.operation.vessel)
        .selectCrane(containerPlan.operation.crane)
        .clickSelectOperationButton()
        .searchContainer(containerPlan.getDigitsFromContainerNumber())
        .<MoveCompletionPage>selectContainer(containerPlan.intermodalUnit.id)
        .clickRightMenuToggle()
        .clickEditOversizeButton()
        .verify(oversizeTexts(getOversize))
        .changeOversizeTexts(setOversize)
        .clickSaveButton()
        .verify(dialogContent("Oversize details were updated"));

    apiMockService.verify(request);
  }

  @Test
  @Issue("TCL-15775")
  @TmsLink("9871636")
  public void getAndChangeDamageForContainer() {
    val containerPlan = terminalController.planContainerOnVesselFromVesselToYard();

    val damageCodes = new Preference();
    damageCodes.id = "damageCodes";
    damageCodes.value = "gtp1,gtp2,stp1,stp2,stp3";

    val damageLocations = new Preference();
    damageLocations.id = "damageLocations";
    damageLocations.value = "glk1,glk2,slk1,slk2,slk3";

    val getDamage =
        DamageBuilder.create()
            .isDamaged(true)
            .addDamage("glk1", "gtp1")
            .addDamage("glk2", "gtp2")
            .build();

    val setDamage =
        DamageBuilder.create()
            .isDamaged(true)
            .addDamage("slk1", "stp1")
            .addDamage("slk2", "stp2")
            .addDamage("slk3", "stp3")
            .build();

    vesselClerkProfile.canEditDamages = true;

    stubProfileData(vesselClerkProfile);
    stubPreference(damageCodes);
    stubPreference(damageLocations);
    stubTestData(
        terminalController.getContainerEmbedMovesAndSegments(containerPlan.intermodalUnit));
    stubGetDamage(containerPlan.intermodalUnit.id, getDamage);
    val request = stubSetDamage(containerPlan.intermodalUnit.id, setDamage);

    getOperationsPage()
        .selectVessel(containerPlan.operation.vessel)
        .selectCrane(containerPlan.operation.crane)
        .clickSelectOperationButton()
        .searchContainer(containerPlan.getDigitsFromContainerNumber())
        .<MoveCompletionPage>selectContainer(containerPlan.intermodalUnit.id)
        .clickRightMenuToggle()
        .clickEditDamageButton()
        .verify(damageState(getDamage))
        .clickDamagesToggle()
        .verify(damageOffState())
        .clickDamagesToggle()
        .changeDamages(setDamage)
        .clickSaveButton()
        .verify(dialogContent("Damage details were updated"));

    apiMockService.verify(request);
  }

  @Test
  @Issue("TCL-15775")
  @TmsLink("9871637")
  public void getAndChangeToFalseDamageForContainer() {
    val containerPlan = terminalController.planContainerOnVesselFromVesselToYard();

    val damageCodes = new Preference();
    damageCodes.id = "damageCodes";
    damageCodes.value = "gtp1,gtp2,stp1,stp2,stp3";

    val damageLocations = new Preference();
    damageLocations.id = "damageLocations";
    damageLocations.value = "glk1,glk2,slk1,slk2,slk3";

    val getDamage =
        DamageBuilder.create()
            .isDamaged(true)
            .addDamage("glk1", "gtp1")
            .addDamage("glk2", "gtp2")
            .build();

    val setDamage = DamageBuilder.create().isDamaged(false).build();

    vesselClerkProfile.canEditDamages = true;

    stubProfileData(vesselClerkProfile);
    stubPreference(damageCodes);
    stubPreference(damageLocations);
    stubTestData(
        terminalController.getContainerEmbedMovesAndSegments(containerPlan.intermodalUnit));
    stubGetDamage(containerPlan.intermodalUnit.id, getDamage);
    val request = stubSetDamage(containerPlan.intermodalUnit.id, setDamage);

    getOperationsPage()
        .selectVessel(containerPlan.operation.vessel)
        .selectCrane(containerPlan.operation.crane)
        .clickSelectOperationButton()
        .searchContainer(containerPlan.getDigitsFromContainerNumber())
        .<MoveCompletionPage>selectContainer(containerPlan.intermodalUnit.id)
        .clickRightMenuToggle()
        .clickEditDamageButton()
        .clickDamagesToggle()
        .clickSaveButton()
        .verify(dialogContent("Damage details were updated"));

    apiMockService.verify(request);
  }
}
