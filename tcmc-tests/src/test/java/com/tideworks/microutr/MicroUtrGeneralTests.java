package com.tideworks.microutr;

import static com.tideworks.verifications.elements.Verifications.aboutDialogContent;
import static com.tideworks.verifications.microutr.Verifications.deliverTitleToolBar;
import static com.tideworks.verifications.microutr.Verifications.heldLabel;
import static com.tideworks.verifications.microutr.Verifications.nextLocationLabel;
import static com.tideworks.verifications.microutr.Verifications.noTitleToolBar;
import static com.tideworks.verifications.microutr.Verifications.pickupTitleToolBar;

import com.tideworks.base.MicroUtrBase;
import com.tideworks.json.objectmodel.EquipmentLocationKind;
import com.tideworks.json.objectmodel.IntermodalUnit;
import com.tideworks.json.objectmodel.Segment;
import com.tideworks.json.objectmodel.locations.EquipmentLocation;
import com.tideworks.json.objectmodel.locations.LocationFormat;
import com.tideworks.json.objectmodel.locations.LocationType;
import com.tideworks.json.objectmodel.locations.YardLocation;
import com.tideworks.pages.elements.dialogs.AboutDialog;
import com.tideworks.pages.microutr.MainPage;

import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import lombok.val;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;

/** General tests for micro utr client. */
public class MicroUtrGeneralTests extends MicroUtrBase {

  @DataProvider(name = "YardLocationFormats")
  public static Object[][] yardLocationFormat() {
    return new Object[][] {
      {"", "BLOCK 102 A1", "10298756"},
      {"b r st k", "BLOCK 102 A1 123", "10298756"},
      {"b z r st", "BLOCK \n 102 A1", "10298756"},
      {"`b r st", "b r st", "10298756"},
      {"`b` r st", "b 102 A1", "10298756"},
      {"b `r` st", "BLOCK r A1", "10298756"}
    };
  }

  @DataProvider(name = "EquipmentLocationFormats")
  public static Object[][] equipmentLocationFormat() {
    return new Object[][] {
      {"", "Crane 01", "10298757"},
      {"c", "01", "10298757"},
      {"c `Crane`", "01 Crane", "10298757"},
      {"`Crane c", "Crane c", "10298757"},
      {"`Crane` z c", "Crane \n 01", "10298757"}
    };
  }

  @Test
  @Issue("TCL-14444")
  @TmsLink("7339729")
  public void unknownLocationWhenContainerIfMoveWasDeleted() {
    EquipmentLocation containerLocation =
        getEquipmentLocation(utrProfile.equipmentId, EquipmentLocationKind.truck);
    YardLocation toLocation = getYardLocation(block, row, stack, tier);
    IntermodalUnit container = getContainer(containerNo, containerLocation);

    Segment move = getSegment(container, containerLocation, toLocation, 1);

    stubTestData(move);
    stubTestData(container);

    val mainPage = getMainPage().verify(nextLocationLabel(toLocation.toString()));

    stubTestData(createEmptyArray(Segment.class));

    mainPage.verify(nextLocationLabel(unknownLocation));
  }

  @Test
  @Issue("TCL-14444")
  @TmsLink("6576311")
  public void unknownLocationWhenContainerDoesNotHaveMove() {
    IntermodalUnit container = getContainer(containerNo, null);

    stubTestData(createEmptyArray(Segment.class));
    stubTestData(container);

    getMainPage().verify(nextLocationLabel(unknownLocation));
  }

  @Test
  @Issue("TCL-14444")
  @TmsLink("6585406")
  public void getLocationIfMoveWasCreatedToContainer() {
    EquipmentLocation containerLocation =
        getEquipmentLocation(utrProfile.equipmentId, EquipmentLocationKind.truck);
    YardLocation toLocation = getYardLocation(block, row, stack, tier);
    IntermodalUnit container = getContainer(containerNo, containerLocation);

    Segment move = getSegment(container, containerLocation, toLocation, 1);

    stubTestData(createEmptyArray(Segment.class));
    stubTestData(container);

    val mainPage = getMainPage().verify(nextLocationLabel(unknownLocation));

    stubTestData(move);

    mainPage.verify(nextLocationLabel(toLocation.toString()));
  }

  @Test
  @Issue("TCL-14438")
  @TmsLink("6394925")
  public void loginPageVerifyAboutDialogContent() {
    getMainPage()
        .clickRightMenuToggle()
        .clickAboutMenuButton()
        .<AboutDialog>verify(
            aboutDialogContent(
                "Traffic Control Mobile Client", "Version:", "Device ID:", utrProfile.deviceId))
        .clickCloseAboutDialog();
  }

  @Test
  @Issue("TCL-14438")
  @TmsLink("6394937")
  public void microUtrMainPageVerifyAboutDialogContent() {
    getMainPage()
        .clickRightMenuToggle()
        .clickAboutMenuButton()
        .<AboutDialog>verify(
            aboutDialogContent(
                "Traffic Control Mobile Client",
                "Version:",
                "Server",
                serverDomain,
                "Device ID:",
                utrProfile.deviceId,
                "Equipment No",
                utrProfile.equipmentId,
                "User Name",
                userName))
        .clickCloseAboutDialog();
  }

  @Test
  @Issue("TCL-14438")
  @TmsLink("6565291")
  public void microUtrMainPageVerifyAboutDialogContentAfterLogout() {
    getMainPage()
        .clickRightMenuToggle()
        .clickAboutMenuButton()
        .<AboutDialog>verify(
            aboutDialogContent(
                "Traffic Control Mobile Client",
                "Version:",
                "Server",
                serverDomain,
                "Device ID:",
                utrProfile.deviceId,
                "Equipment No",
                utrProfile.equipmentId,
                "User Name",
                userName))
        .clickCloseAboutDialog()
        .clickRightMenuToggle()
        .clickLogoutMenuButton()
        .confirmLogout()
        .clickRightMenuToggle()
        .clickAboutMenuButton()
        .verify(
            aboutDialogContent(
                "Traffic Control Mobile Client", "Version:", "Device ID:", utrProfile.deviceId));
  }

  @Test
  @Issue("TCL-14718")
  @TmsLink("6537265")
  public void snackbarIsAppearedIfConnectionLost() {
    stubTestData(createEmptyArray(Segment.class));
    stubTestData(createEmptyArray(IntermodalUnit.class));

    val mainPage = getMainPage().waitSnackbarIsNotDisplayed();

    try {
      apiMockService.stopService();

      mainPage.waitSnackbarIsDisplayed().verifyText("Connection failed");
    } catch (Exception exc) {
      Assert.fail(exc.getMessage());
    } finally {
      apiMockService.startService();
    }
  }

  @Test
  @Issue("TCL-14718")
  @TmsLink("6537266")
  public void snackbarIsHidedIfConnectionRestored() {
    val mainPage = getMainPage().<MainPage>waitUntilPageLoaded();

    try {
      apiMockService.stopService();
      mainPage.waitSnackbarIsDisplayed().verifyText("Connection failed");
    } catch (Exception exc) {
      Assert.fail(exc.getMessage());
    } finally {
      apiMockService.startService();
    }

    stubTestData(createEmptyArray(Segment.class));
    stubTestData(createEmptyArray(IntermodalUnit.class));

    mainPage.waitSnackbarIsNotDisplayed();
  }

  @Test
  @Issue("TCL-14443")
  @TmsLink("9168385")
  public void microUtrHeldUtr() {
    val utr = createUtrEquipment();
    utr.held = true;
    stubTestData();
    stubTestData(utr);

    getMainPage().verify(heldLabel()).verify(noTitleToolBar());
  }

  @Test
  @Issue("TCL-14443")
  @TmsLink("9215250")
  public void microUtrHeldUtrAboutDialog() {
    val utr = createUtrEquipment();
    utr.held = true;
    stubTestData();
    stubTestData(utr);

    getMainPage()
        .verify(heldLabel())
        .clickRightMenuToggle()
        .clickAboutMenuButton()
        .<AboutDialog>verify(
            aboutDialogContent(
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
  @Issue("TCL-14443")
  @TmsLink("9215251")
  public void microUtrHeldUtrLogoutDialog() {
    val utr = createUtrEquipment();
    utr.held = true;
    stubTestData();
    stubTestData(utr);

    getMainPage()
        .verify(heldLabel())
        .clickRightMenuToggle()
        .clickLogoutMenuButton()
        .confirmLogout();
  }

  @Test
  @Issue("TCL-14443")
  @TmsLink("9215252")
  public void microUtrHeldUtrBecomeActiveWithoutMove() {
    val utr = createUtrEquipment();
    utr.held = true;
    stubTestData();
    stubTestData(utr);

    val mainPage = getMainPage().verify(heldLabel()).verify(noTitleToolBar());

    utr.held = false;
    stubTestData(utr);

    mainPage.verify(nextLocationLabel(awaitingInstructionsLocation));
  }

  @Test
  @Issue("TCL-14443")
  @TmsLink("9215253")
  public void microUtrHeldUtrBecomeActiveWithMove() {
    YardLocation fromLocation = getYardLocation(blockAandB, row102, stackA, tier);
    IntermodalUnit containerOnTruck = getContainer(containerNo, fromLocation);
    YardLocation toLocation = getYardLocation(blockCss, row106, stackB, tier);
    Segment segment = getSegment(containerOnTruck, fromLocation, toLocation, sequence);
    val utr = createUtrEquipment();
    utr.held = true;
    stubTestData();
    stubTestData(utr);

    val mainPage = getMainPage().verify(heldLabel()).verify(noTitleToolBar());

    utr.held = false;
    stubTestData(utr);
    stubTestData(containerOnTruck, segment);

    mainPage.verify(nextLocationLabel(fromLocation.toString())).verify(pickupTitleToolBar());
  }

  @Test
  @Issue("TCL-14443")
  @TmsLink("9215254")
  public void microUtrUtrWithMoveBecomeHeld() {
    YardLocation fromLocation = getYardLocation(blockAandB, row102, stackA, tier);
    IntermodalUnit containerOnTruck = getContainer(containerNo, fromLocation);
    YardLocation toLocation = getYardLocation(blockCss, row106, stackB, tier);
    Segment segment = getSegment(containerOnTruck, fromLocation, toLocation, sequence);
    val utr = createUtrEquipment();
    stubTestData(containerOnTruck, segment);

    val mainPage =
        getMainPage()
            .verify(nextLocationLabel(fromLocation.toString()))
            .verify(pickupTitleToolBar());

    utr.held = true;

    stubTestData(utr);

    mainPage.verify(heldLabel()).verify(noTitleToolBar());
  }

  @Test
  @Issue("TCL-14443")
  @TmsLink("9215255")
  public void microUtrUtrWithoutMoveBecomeHeld() {
    val utr = createUtrEquipment();
    stubTestData();

    val mainPage = getMainPage().verify(nextLocationLabel(awaitingInstructionsLocation));

    utr.held = true;

    stubTestData(utr);

    mainPage.verify(heldLabel()).verify(noTitleToolBar());
  }

  @Test(dataProvider = "YardLocationFormats")
  @Issue("TCL-19173")
  @TmsLink("10298756")
  public void microUtrYardLocationFormatsTest(String format, String expected, String testcase) {
    val locationFormat = new LocationFormat(LocationType.yard, format);
    utrProfile.locationFormats = Collections.singletonList(locationFormat);
    stubProfileData(utrProfile);
    YardLocation fromLocation = getYardLocation(block, row102, stackA, tier);
    fromLocation.footmark = 123;
    IntermodalUnit containerOnTruck = getContainer(containerNo, fromLocation);
    YardLocation toLocation = getYardLocation(block, row106, stackB, tier);
    Segment segment = getSegment(containerOnTruck, fromLocation, toLocation, sequence);

    stubTestData(containerOnTruck, segment);

    getMainPage().verify(nextLocationLabel(expected)).verify(pickupTitleToolBar());
  }

  @Test(dataProvider = "EquipmentLocationFormats")
  @Issue("TCL-19173")
  @TmsLink("10298757")
  public void microUtrEquipmentLocationFormatsTest(
      String format, String expected, String testcase) {
    val locationFormat = new LocationFormat(LocationType.equipment, format);
    utrProfile.locationFormats = Collections.singletonList(locationFormat);
    stubProfileData(utrProfile);
    EquipmentLocation fromLocation = getEquipmentLocation(crane01, EquipmentLocationKind.crane);
    IntermodalUnit containerOnTruck = getContainer(containerNo, fromLocation);
    YardLocation toLocation = getYardLocation(block, row106, stackB, tier);
    Segment segment = getSegment(containerOnTruck, fromLocation, toLocation, sequence);

    stubTestData(containerOnTruck, segment);

    getMainPage().verify(nextLocationLabel(expected)).verify(pickupTitleToolBar());
  }

  @Test
  @Issue("TCL-19173")
  @TmsLink("10346203")
  public void microUtrDefaultYardLocationFormatTest() {
    YardLocation fromLocation = getYardLocation(block, row102, stackA, tier);
    val expected = fromLocation.toString();
    fromLocation.footmark = 123;
    IntermodalUnit containerOnTruck = getContainer(containerNo, fromLocation);
    YardLocation toLocation = getYardLocation(block, row106, stackB, tier);
    Segment segment = getSegment(containerOnTruck, fromLocation, toLocation, sequence);

    stubTestData(containerOnTruck, segment);

    getMainPage().verify(nextLocationLabel(expected)).verify(pickupTitleToolBar());
  }

  @Test
  @Issue("TCL-19173")
  @TmsLink("10346203")
  public void microUtrDefaultEquipmentLocationFormatTest() {
    EquipmentLocation fromLocation = getEquipmentLocation(crane01, EquipmentLocationKind.crane);
    IntermodalUnit containerOnTruck = getContainer(containerNo, fromLocation);
    YardLocation toLocation = getYardLocation(block, row106, stackB, tier);
    Segment segment = getSegment(containerOnTruck, fromLocation, toLocation, sequence);

    stubTestData(containerOnTruck, segment);

    getMainPage().verify(nextLocationLabel("Crane " + crane01)).verify(pickupTitleToolBar());
  }

  @Test
  @Issue("TCL-19173")
  @TmsLink("10356502")
  public void microUtrYardLocationFormatChangedToUnknownAndBack() {
    val locationFormat = new LocationFormat(LocationType.yard, "b r st k");
    utrProfile.locationFormats = Collections.singletonList(locationFormat);
    stubProfileData(utrProfile);
    YardLocation fromLocation = getYardLocation(block, row102, stackA, tier);
    fromLocation.footmark = 123;
    IntermodalUnit containerOnTruck = getContainer(containerNo, fromLocation);
    YardLocation toLocation = getYardLocation(block, row106, stackB, tier);
    Segment segment = getSegment(containerOnTruck, fromLocation, toLocation, sequence);

    stubTestData(containerOnTruck, segment);

    val mainPage =
        getMainPage()
            .verify(nextLocationLabel(fromLocation.toString()))
            .verify(pickupTitleToolBar());

    stubTestData(createEmptyArray(Segment.class));

    mainPage.verify(nextLocationLabel(unknownLocation)).verify(deliverTitleToolBar());

    stubTestData(containerOnTruck, segment);

    mainPage.verify(nextLocationLabel(fromLocation.toString())).verify(pickupTitleToolBar());
  }

  @Test
  @Issue("TCL-19173")
  @TmsLink("10356501")
  public void microUtrYardLocationFormatChangedToAwaitingInstructionsAndBack() {
    val locationFormat = new LocationFormat(LocationType.yard, "b r st k");
    utrProfile.locationFormats = Collections.singletonList(locationFormat);
    stubProfileData(utrProfile);
    YardLocation fromLocation = getYardLocation(block, row102, stackA, tier);
    fromLocation.footmark = 123;
    IntermodalUnit containerOnTruck = getContainer(containerNo, fromLocation);
    YardLocation toLocation = getYardLocation(block, row106, stackB, tier);
    Segment segment = getSegment(containerOnTruck, fromLocation, toLocation, sequence);

    stubTestData(containerOnTruck, segment);

    val mainPage =
        getMainPage()
            .verify(nextLocationLabel(fromLocation.toString()))
            .verify(pickupTitleToolBar());

    stubTestData(createEmptyArray(Segment.class));
    stubTestData(createEmptyArray(IntermodalUnit.class));

    mainPage.verify(nextLocationLabel(awaitingInstructionsLocation)).verify(noTitleToolBar());

    stubTestData(containerOnTruck, segment);

    mainPage.verify(nextLocationLabel(fromLocation.toString())).verify(pickupTitleToolBar());
  }

  @Test
  @Issue("TCL-19173")
  @TmsLink("10356500")
  public void microUtrYardLocationFormatChangedToHeldAndBack() {
    val locationFormat = new LocationFormat(LocationType.yard, "b r st k");
    utrProfile.locationFormats = Collections.singletonList(locationFormat);
    stubProfileData(utrProfile);
    YardLocation fromLocation = getYardLocation(block, row102, stackA, tier);
    fromLocation.footmark = 123;
    IntermodalUnit containerOnTruck = getContainer(containerNo, fromLocation);
    YardLocation toLocation = getYardLocation(block, row106, stackB, tier);
    Segment segment = getSegment(containerOnTruck, fromLocation, toLocation, sequence);

    stubTestData(containerOnTruck, segment);

    val mainPage =
        getMainPage()
            .verify(nextLocationLabel(fromLocation.toString()))
            .verify(pickupTitleToolBar());

    val utr = createUtrEquipment();
    utr.held = true;
    stubTestData(utr);

    mainPage.verify(heldLabel()).verify(noTitleToolBar());

    utr.held = false;
    stubTestData(utr);

    mainPage.verify(nextLocationLabel(fromLocation.toString())).verify(pickupTitleToolBar());
  }

  @Test
  @Issue("TCL-19173")
  @TmsLink("10356499")
  public void microUtrBothLocationFormatsTest() {
    val yardLocationFormat = new LocationFormat(LocationType.yard, "b r st k");
    val equipmentLocationFormat = new LocationFormat(LocationType.equipment, "c `Crane`");
    utrProfile.locationFormats = Arrays.asList(yardLocationFormat, equipmentLocationFormat);
    stubProfileData(utrProfile);
    EquipmentLocation fromLocation = getEquipmentLocation(crane01, EquipmentLocationKind.crane);
    IntermodalUnit containerOnTruck = getContainer(containerNo, fromLocation);
    YardLocation toLocation = getYardLocation(block, row106, stackB, tier);
    Segment segment = getSegment(containerOnTruck, fromLocation, toLocation, sequence);

    stubTestData(containerOnTruck, segment);

    val mainPage =
        getMainPage().verify(nextLocationLabel(crane01 + " Crane")).verify(pickupTitleToolBar());

    val fromLocation2 = getYardLocation(block, row102, stackA, tier);
    fromLocation2.footmark = 123;
    IntermodalUnit containerOnTruck2 = getContainer(containerNo, fromLocation2);
    Segment segment2 = getSegment(containerOnTruck2, fromLocation2, toLocation, sequence);

    stubTestData(containerOnTruck2, segment2);

    mainPage.verify(nextLocationLabel(fromLocation2.toString())).verify(pickupTitleToolBar());
  }

  @Test
  @Issue("TCL-19173")
  @TmsLink("10356497")
  public void microUtrYardLocationFormatLocationWithoutFootmark() {
    val locationFormat = new LocationFormat(LocationType.yard, "b r st k");
    utrProfile.locationFormats = Collections.singletonList(locationFormat);
    stubProfileData(utrProfile);
    YardLocation fromLocation = getYardLocation(block, row102, stackA, tier);
    IntermodalUnit containerOnTruck = getContainer(containerNo, fromLocation);
    YardLocation toLocation = getYardLocation(block, row106, stackB, tier);
    Segment segment = getSegment(containerOnTruck, fromLocation, toLocation, sequence);

    stubTestData(containerOnTruck, segment);

    getMainPage().verify(nextLocationLabel(fromLocation.toString())).verify(pickupTitleToolBar());
  }

  @Test
  @Issue("TCL-19173")
  @TmsLink("10414931")
  public void microUtrYardLocationFormatHeapLocation() {
    val locationFormat = new LocationFormat(LocationType.yard, "b r st k");
    utrProfile.locationFormats = Collections.singletonList(locationFormat);
    stubProfileData(utrProfile);
    YardLocation fromLocation = getYardLocation("HEAP", "", "", null);
    IntermodalUnit containerOnTruck = getContainer(containerNo, fromLocation);
    YardLocation toLocation = getYardLocation(block, row106, stackB, tier);
    Segment segment = getSegment(containerOnTruck, fromLocation, toLocation, sequence);

    stubTestData(containerOnTruck, segment);

    getMainPage().verify(nextLocationLabel(fromLocation.toString())).verify(pickupTitleToolBar());
  }
}
