package com.tideworks.vesselclerk;

import static com.tideworks.verifications.che.Verifications.unexpectedErrorHandlingPagePresence;

import com.tideworks.base.VesselClerkBase;
import com.tideworks.pages.vesselclerk.OperationsPage;

import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import lombok.val;
import lombok.var;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/** Tests for vessel clerk client which start at login page. */
public class VesselClerkWithLoginTests extends VesselClerkBase {

  @BeforeClass
  public void beforeCheClass() {
    remoteDriverService.setAutoLoginEnabled(false);
  }

  @BeforeMethod
  public void beforeMethodLaunchAndLogin() {
    launchApp();
    getLoginPage()
        .login(vesselClerkProfile, userName, userPassword, apiMockService.getServiceAddress());
  }

  @Test
  @Issue("TCL-15971")
  @TmsLink("8918479")
  public void vesselClerkVerifyUnhandledExceptionPage() {
    val containerPlan = terminalController.planContainerOnVesselFromVesselToYard();
    val container =
        terminalController.getContainerEmbedMovesAndSegments(containerPlan.intermodalUnit);
    container.moves.get(0).to = null;

    stubTestData(container);

    var operationPage = new OperationsPage();

    operationPage
        .selectVessel(containerPlan.operation.vessel)
        .selectCrane(containerPlan.operation.crane)
        .clickSelectOperationButton()
        .searchContainer(containerPlan.getDigitsFromContainerNumber())
        .selectContainer(containerPlan.intermodalUnit.id);

    getUnexpectedErrorHandlingPage()
        .verify(unexpectedErrorHandlingPagePresence())
        .clickRestartButtonAndGetLoginPage()
        .waitUntilPageLoaded();
  }
}
