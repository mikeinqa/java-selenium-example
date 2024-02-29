package com.tideworks.microutr;

import static com.tideworks.verifications.che.Verifications.unexpectedErrorHandlingPagePresence;
import static com.tideworks.verifications.elements.Verifications.aboutDialogContent;

import com.tideworks.base.MicroUtrBase;
import com.tideworks.json.objectmodel.EquipmentLocationKind;
import com.tideworks.json.objectmodel.IntermodalUnit;
import com.tideworks.json.objectmodel.Segment;
import com.tideworks.json.objectmodel.locations.EquipmentLocation;
import com.tideworks.json.objectmodel.locations.YardLocation;
import com.tideworks.pages.elements.dialogs.AboutDialog;
import com.tideworks.pages.microutr.MainPage;

import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import lombok.var;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/** Tests for micro utr client which start at login page. */
public class MicroUtrWithLoginTests extends MicroUtrBase {

  @BeforeClass
  public void createUtrProfile() {
    utrProfile = getDefaultUtrProfile();
  }

  @BeforeMethod
  void beforeMethodLaunchAndLogin() {
    remoteDriverService.setAutoLoginEnabled(false);
    stubTestData();
    launchApp();
    login();
  }

  @Test
  @Issue("TCL-14438")
  @TmsLink("6518715")
  public void microUtrMainPageVerifyAboutDialogContentInCaseUnavailableEquipment() {
    var mainPage = new MainPage();

    mainPage
        .clickRightMenuToggle()
        .clickAboutMenuButton()
        .<AboutDialog>verify(aboutDialogContent(
            "Traffic Control Mobile Client",
            "Version:",
            "Server",
            serverDomain,
            "Device ID:",
            utrProfile.deviceId,
            "User Name",
            userName))
        .clickCloseAboutDialog();
  }

  @Test
  @Issue("TCL-15971")
  @TmsLink("8918478")
  public void microUtrVerifyUnhandledExceptionPage() {
    EquipmentLocation containerLocation =
        getEquipmentLocation(utrProfile.equipmentId, EquipmentLocationKind.truck);
    YardLocation toLocation = getYardLocation(block, row, stack, tier);
    IntermodalUnit container = new IntermodalUnit();
    Segment move = getSegment(container, containerLocation, toLocation, 1);
    stubTestData(move);
    stubTestData(container);

    getUnexpectedErrorHandlingPage()
        .verify(unexpectedErrorHandlingPagePresence())
        .clickRestartButtonAndGetLoginPage()
        .waitUntilPageLoaded();
  }
}
