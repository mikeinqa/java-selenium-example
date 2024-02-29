package com.tideworks.che;

import static com.tideworks.verifications.che.Verifications.unexpectedErrorHandlingPagePresence;

import com.tideworks.base.CheBase;
import com.tideworks.pages.che.WorklistPage;

import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import lombok.var;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/** Tests for Che with login page. */
public class CheWithLoginTests extends CheBase {

  @BeforeClass
  public void beforeCheClass() {
    cheProfile = getDefaultCheProfile();
    remoteDriverService.setAutoLoginEnabled(false);

    stubTerminalTestData();
  }

  @BeforeMethod
  public void beforeMethodLaunchAndLogin() {
    launchApp();
    getLoginPage().login(cheProfile, userName, userPassword, apiMockService.getServiceAddress());
  }

  @Test
  @Issue("TCL-15971")
  @TmsLink("8918477")
  public void cheVerifyUnhandledExceptionPage() {
    var worklistPage = new WorklistPage();
    var selectedContainer = terminalController.planContainerInYardFromYardToHeap();
    selectedContainer.segments.get(0).to = null;
    stubTestData(selectedContainer);

    worklistPage.selectContainer(selectedContainer.intermodalUnit);

    getUnexpectedErrorHandlingPage()
        .verify(unexpectedErrorHandlingPagePresence())
        .clickRestartButtonAndGetLoginPage()
        .waitUntilPageLoaded();
  }
}
