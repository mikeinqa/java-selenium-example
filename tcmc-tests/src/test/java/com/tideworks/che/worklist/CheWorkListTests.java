package com.tideworks.che.worklist;

import static com.tideworks.verifications.che.Verifications.blockPickerButtonVisibility;
import static com.tideworks.verifications.che.Verifications.escalationButtonVisibility;
import static com.tideworks.verifications.che.Verifications.worklistContainers;

import com.tideworks.base.CheBase;

import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import lombok.val;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/** Tests for che work-list page. */
public class CheWorkListTests extends CheBase {

  /**
   * Move types data provider.
   *
   * @return set of parametrized data
   */
  @DataProvider()
  public static Object[][] userPermission() {
    return new Object[][] {
      {null, null, "8946094"},
      {null, false, "8946081"},
      {null, true, "8946081"},
      {false, null, "8946082"},
      {true, null, "8946082"}
    };
  }

  @Test
  @Issue("TCL-14653")
  @TmsLink("7381047")
  public void cheWorklistFilteringToShowOnlyEscalatedMoves() {
    val containerPlan = terminalController.planContainerInYardFromYardToHeap();
    terminalController.planContainerInYardFromYardToHeap();
    terminalController.planContainerInYardFromYardToHeap();
    terminalController.planContainerInHeapFromHeapToYard();
    terminalController.planContainerInHeapFromHeapToYard();
    terminalController.planContainerInHeapFromHeapToYard();

    val escalatedContainers = terminalController.getTerminal().getContainers().subList(0, 2);
    terminalController.setEscalation(escalatedContainers, true);

    stubTestData(containerPlan);

    getWorklistPage()
        .verify(
            worklistContainers(
                terminalController
                    .getTerminal()
                    .getContainers()
                    .stream()
                    .map(container -> container.id)
                    .collect(Collectors.toList())))
        .clickEscalatedFilterButton()
        .verify(
            worklistContainers(
                escalatedContainers
                    .stream()
                    .map(container -> container.id)
                    .collect(Collectors.toList())));
  }

  @Test(dataProvider = "userPermission")
  @Issue("TCL-19157")
  @TmsLink("8946094")
  @TmsLink("8946081")
  @TmsLink("8946081")
  @TmsLink("8946082")
  @TmsLink("8946082")
  public void cheWorkListRoleBasedPrivileges(
      Boolean isEscalatedButtonVisible, Boolean isBlockPickerVisible, String testCase) {
    cheProfile.escalatedMovesFilter = isEscalatedButtonVisible;
    cheProfile.blockPicker = isBlockPickerVisible;
    stubProfileData(cheProfile);
    stubTestData(terminalController.planContainerInYardFromYardToHeap());

    getWorklistPage()
        .verify(escalationButtonVisibility(
            isEscalatedButtonVisible != null ? isEscalatedButtonVisible : false))
        .verify(
            blockPickerButtonVisibility(
                isBlockPickerVisible != null ? isBlockPickerVisible : false));
  }

  @Test
  @Issue("TCL-16205")
  @TmsLink("8946121")
  public void cheWorklist_setAsideMovesDoNotShowUp() {
    val containerPlan = terminalController.planContainerInYardFromYardToHeap();
    IntStream.range(0, 10).forEach(index -> terminalController.planContainerInYardFromYardToHeap());
    terminal.getMoves().subList(0, 5).forEach(move -> move.systemMove = true);
    terminal.getMoves().subList(5, 11).forEach(move -> move.systemMove = false);

    stubTestData(containerPlan);

    getWorklistPage()
        .verify(
            worklistContainers(
                terminalController
                    .getContainersExpandMoveAndSegment()
                    .stream()
                    .filter(container -> !container.move.systemMove)
                    .map(container -> container.id)
                    .collect(Collectors.toList())));
  }
}
