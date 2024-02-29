package com.tideworks.che;

import static com.tideworks.utilities.api.Che.assignToVesselOperation;
import static com.tideworks.utilities.api.Che.assignToZoneUrl;
import static com.tideworks.verifications.che.Verifications.containerData;
import static com.tideworks.verifications.che.Verifications.vesselOperationsData;
import static com.tideworks.verifications.che.Verifications.vesselOperationsTabVisibility;
import static com.tideworks.verifications.che.Verifications.workQueuesTabVisibility;
import static com.tideworks.verifications.che.Verifications.zoneData;
import static com.tideworks.verifications.che.Verifications.zoneTableHeader;
import static com.tideworks.verifications.che.Verifications.zonesTabVisibility;
import static com.tideworks.verifications.elements.Verifications.dialogTitle;

import com.tideworks.base.CheBase;
import com.tideworks.json.objectmodel.ZoneKind;
import com.tideworks.pages.che.WorklistPage;
import com.tideworks.pages.che.ZoneViewPage;
import com.tideworks.pages.che.yardview.HeapViewPage;
import com.tideworks.pages.elements.dialogs.LiftingContextSelectorDialog;
import com.tideworks.pages.elements.dialogs.NotificationDialog;

import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import lombok.val;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;

/** Tests for Che zone-view tests. */
public class CheZoneViewTests extends CheBase {

  /**
   * Gets a list of Zone Ids.
   *
   * @return Zone Ids.
   */
  @DataProvider(name = "SelectingZonesWithDifferentIds")
  public static Object[] zoneId() {
    return new Object[] {"Zone1", "Zone 1", "", "!#$%&?"};
  }

  /**
   * Gets a list of visibility variables for user rights.
   *
   * @return Visibilities.
   */
  @DataProvider(name = "TabsVisibility")
  public static Object[] isVisible() {
    return new Object[][] {
      {true, true, true},
      {true, true, false},
      {true, false, true},
      {true, false, false},
      {false, true, true},
      {false, true, false},
      {false, false, true}
    };
  }

  /** Setup che profile before tests execution. */
  @BeforeMethod
  public void beforeCheMethod() {
    cheProfile.selfAssignZones = true;
    cheProfile.selfAssignVesselOperations = true;
    cheProfile.selfAssignWorkQueues = true;
    stubProfileData(cheProfile);
  }

  @Test
  @Issue("TCL-16330")
  @TmsLink("7381094")
  public void cheAutoLoginToZone() {
    val container = terminalController.planContainerInHeapFromHeapToYard();
    createZone(zoneId, ZoneKind.Regular);

    stubTestData(container);

    getZoneViewPage().verify(zoneTableHeader("Zones"));
  }

  @Test(dataProvider = "TabsVisibility")
  @Issue("TCL-16330")
  @TmsLink("8851336")
  @TmsLink("8363031")
  @TmsLink("8363038")
  @TmsLink("8363034")
  public void cheZoneViewShowTabs(
      boolean isZonesVisible, boolean isOperationsVisible, boolean isWorkQueuesVisible) {
    val container = terminalController.planContainerInHeapFromHeapToYard();
    cheProfile.selfAssignZones = isZonesVisible;
    cheProfile.selfAssignVesselOperations = isOperationsVisible;
    cheProfile.selfAssignWorkQueues = isWorkQueuesVisible;
    stubProfileData(cheProfile);

    stubTestData(container);

    getZoneViewPage()
        .verify(zonesTabVisibility(isZonesVisible))
        .verify(vesselOperationsTabVisibility(isOperationsVisible))
        .verify(workQueuesTabVisibility(isWorkQueuesVisible));
  }

  @Test(dataProvider = "SelectingZonesWithDifferentIds")
  @Issue("TCL-16330")
  @TmsLink("7381182")
  public void cheZoneViewSelectZonesWithDifferentIds(String zoneId) {
    val container = terminalController.planContainerInHeapFromHeapToYard();
    val zone = createZone(zoneId, ZoneKind.Regular);

    stubTestData(container);

    getZoneViewPage()
        .verify(zoneData(terminalController.getTerminal().getZones()))
        .<WorklistPage>selectZone(zone.id)
        .verify(containerData(terminalController.getContainersExpandMoveAndSegment()));
  }

  @Test
  @Issue("TCL-16330")
  @TmsLink("8851334")
  public void cheZoneViewSelectOperations() {
    val container = terminalController.planContainerInHeapFromHeapToYard();
    val operation = createVesselOperation();

    stubTestData(container);

    getZoneViewPage()
        .clickVesselOperationsTab()
        .verify(vesselOperationsData(terminalController.getTerminal().getVesselOperations()))
        .<WorklistPage>selectVesselOperation(operation)
        .verify(containerData(terminalController.getContainersExpandMoveAndSegment()));
  }

  @Test
  @Issue("TCL-16330")
  @TmsLink("8851332")
  @TmsLink("8363037")
  @TmsLink("8363039")
  public void cheZoneViewSelectReceiveWorkQueue() {
    val container = terminalController.planContainerInHeapFromHeapToYard();
    val workQueue = createZone("WorkQueue", ZoneKind.WorkQueue);

    stubTestData(container);

    getZoneViewPage()
        .clickWorkQueuesTab()
        .verify(zoneData(Collections.singletonList(workQueue)))
        .<LiftingContextSelectorDialog>selectZone(workQueue.id)
        .<WorklistPage>clickReceiveButton()
        .verify(containerData(terminalController.getContainersExpandMoveAndSegment()));
  }

  @Test
  @Issue("TCL-16330")
  @TmsLink("8851332")
  @TmsLink("8363039")
  public void cheZoneViewSelectDeliverWorkQueue() {
    val container = terminalController.planContainerInHeapFromHeapToYard();
    val workQueue = createZone("WorkQueue", ZoneKind.WorkQueue);

    stubTestData(container);

    getZoneViewPage()
        .clickWorkQueuesTab()
        .verify(zoneData(Collections.singletonList(workQueue)))
        .<LiftingContextSelectorDialog>selectZone(workQueue.id)
        .clickDeliverButton()
        .verify(containerData(terminalController.getContainersExpandMoveAndSegment()));
  }

  @Test
  @Issue("TCL-16330")
  @TmsLink("8851326")
  public void cheZoneViewShowOnlyZonesInZonesTab() {
    val container = terminalController.planContainerInHeapFromHeapToYard();
    val zone = createZone("Zone", ZoneKind.Regular);
    createZone("WorkQueue", ZoneKind.WorkQueue);
    createVesselOperation();

    stubTestData(container);

    getZoneViewPage().verify(zoneData(Collections.singletonList(zone)));
  }

  @Test
  @Issue("TCL-16330")
  @TmsLink("8851326")
  public void cheZoneViewShowOnlyWorkQueuesInWorkQueuesTab() {
    val container = terminalController.planContainerInHeapFromHeapToYard();
    createZone("Zone", ZoneKind.Regular);
    val workQueue = createZone("WorkQueue", ZoneKind.WorkQueue);
    createVesselOperation();

    stubTestData(container);

    getZoneViewPage().clickWorkQueuesTab().verify(zoneData(Collections.singletonList(workQueue)));
  }

  @Test
  @Issue("TCL-16330")
  @TmsLink("7381183")
  public void cheZoneViewShowBackFromZonesAndWorklistPage() {
    val container = terminalController.planContainerInHeapFromHeapToYard();
    val zone = createZone(zoneId, ZoneKind.Regular);

    stubTestData(container);

    getZoneViewPage()
        .verify(zoneData(terminalController.getTerminal().getZones()))
        .<WorklistPage>selectZone(zone.id)
        .clickBackButton()
        .verify(zoneData(terminalController.getTerminal().getZones()));
  }

  @Test
  @Issue("TCL-16330")
  @TmsLink("7381183")
  public void cheZoneViewShowBackFromOperationsAndWorklistPage() {
    val container = terminalController.planContainerInHeapFromHeapToYard();
    val operation = createVesselOperation();

    stubTestData(container);

    getZoneViewPage()
        .clickVesselOperationsTab()
        .verify(vesselOperationsData(terminalController.getTerminal().getVesselOperations()))
        .<WorklistPage>selectVesselOperation(operation)
        .clickBackButton()
        .verify(zoneData(Collections.emptyList()));
  }

  @Test
  @Issue("TCL-16330")
  @TmsLink("7381183")
  public void cheZoneViewShowBackFromWorkQueuesAndWorklistPage() {
    val container = terminalController.planContainerInHeapFromHeapToYard();
    val workQueue = createZone(zoneId, ZoneKind.WorkQueue);

    stubTestData(container);

    getZoneViewPage()
        .clickWorkQueuesTab()
        .verify(zoneData(Collections.singletonList(workQueue)))
        .<LiftingContextSelectorDialog>selectZone(workQueue.id)
        .<WorklistPage>clickReceiveButton()
        .clickBackButton()
        .verify(zoneData(Collections.emptyList()));
  }

  @Test
  @Issue("TCL-16330")
  @TmsLink("7381183")
  public void cheZoneViewShowBackFromHeapViewPage() {
    val container = terminalController.planContainerInHeapFromHeapToYard();
    val zone = createZone(zoneId, ZoneKind.Regular);

    stubTestData(container);

    getZoneViewPage()
        .verify(zoneData(terminalController.getTerminal().getZones()))
        .<WorklistPage>selectZone(zone.id)
        .<HeapViewPage>selectContainer(container.intermodalUnit)
        .clickBackButton()
        .clickBackButton()
        .verify(zoneData(terminalController.getTerminal().getZones()));
  }

  @Test
  @Issue("TCL-16518")
  @TmsLink("8921125")
  public void cheZoneView_operationSelectionFailed_errorDialogIsShown() {
    val vesselOperation = createVesselOperation();

    stubTestData(terminalController.planContainerInYardFromYardToHeap());
    apiMockService.setJsonResponseForPost(
        assignToVesselOperation(vesselOperationId),
        apiMockService.response().withStatus(404).build(),
        String.format("{\"equipmentId\":\"%s\"}", equipmentId));

    getZoneViewPage()
        .clickVesselOperationsTab()
        .<NotificationDialog>selectVesselOperation(vesselOperation)
        .<NotificationDialog>verify(dialogTitle("Error"))
        .clickOkButton(ZoneViewPage.class)
        .waitUntilPageLoaded();
  }

  @Test
  @Issue("TCL-16518")
  @TmsLink("8921124")
  public void cheZoneView_workQueueSelectionFailed_errorDialogIsShown() {
    val workQueue = createZone("WorkQueue", ZoneKind.WorkQueue);

    stubTestData(terminalController.planContainerInYardFromYardToHeap());
    apiMockService.setJsonResponseForPost(
        assignToZoneUrl(workQueue.id),
        apiMockService.response().withStatus(404).build(),
        String.format("{\"equipmentId\":\"%s\",\"context\":\"Receive\"}", equipmentId));

    getZoneViewPage()
        .clickWorkQueuesTab()
        .<LiftingContextSelectorDialog>selectZone(workQueue.id)
        .<NotificationDialog>clickReceiveButton()
        .<NotificationDialog>verify(dialogTitle("Error"))
        .clickOkButton(ZoneViewPage.class)
        .waitUntilPageLoaded();
  }

  @Test
  @Issue("TCL-16518")
  @TmsLink("8921123")
  public void cheZoneView_zoneSelectionFailed_errorDialogIsShown() {
    val zone = createZone("Zone", ZoneKind.Regular);

    stubTestData(terminalController.planContainerInYardFromYardToHeap());
    apiMockService.setJsonResponseForPost(
        assignToZoneUrl(zone.id),
        apiMockService.response().withStatus(404).build(),
        String.format("{\"equipmentId\":\"%s\"}", equipmentId));

    getZoneViewPage()
        .<NotificationDialog>selectZone(zone.id)
        .<NotificationDialog>verify(dialogTitle("Error"))
        .clickOkButton(ZoneViewPage.class)
        .waitUntilPageLoaded();
  }

  @Test
  @Issue("TCL-16518")
  @TmsLink("8918475")
  public void cheZoneView_operationSelectionSucceed_operationAssignRequestSend() {
    val vesselOperation = createVesselOperation();

    stubTestData(terminalController.planContainerInYardFromYardToHeap());

    getZoneViewPage()
        .clickVesselOperationsTab()
        .<WorklistPage>selectVesselOperation(vesselOperation)
        .waitUntilPageLoaded();

    val assignRequest =
        apiMockService.setJsonResponseForPost(
            assignToVesselOperation(vesselOperationId),
            apiMockService.response().withStatus(200).build(),
            String.format("{\"equipmentId\":\"%s\"}", equipmentId));

    apiMockService.verify(assignRequest);
  }

  @Test
  @Issue("TCL-16518")
  @TmsLink("8918474")
  public void cheZoneView_workQueueSelectionSucceed_zoneAssignRequestSend() {
    val workQueue = createZone("WorkQueue", ZoneKind.WorkQueue);

    stubTestData(terminalController.planContainerInYardFromYardToHeap());

    getZoneViewPage()
        .clickWorkQueuesTab()
        .<LiftingContextSelectorDialog>selectZone(workQueue.id)
        .<WorklistPage>clickReceiveButton()
        .waitUntilPageLoaded();

    val assignRequest =
        apiMockService.setJsonResponseForPost(
            assignToZoneUrl(workQueue.id),
            apiMockService.response().withStatus(200).withBody("OK").build(),
            String.format("{\"equipmentId\":\"%s\",\"context\":\"Receive\"}", equipmentId));

    apiMockService.verify(assignRequest);
  }

  @Test
  @Issue("TCL-16518")
  @TmsLink("8918473")
  public void cheZoneView_zoneSelectionSucceed_zoneAssignRequestSend() {
    val zone = createZone("Zone", ZoneKind.Regular);

    stubTestData(terminalController.planContainerInYardFromYardToHeap());

    val assignRequest =
        apiMockService.setJsonResponseForPost(
            assignToZoneUrl(zone.id),
            apiMockService.response().withStatus(200).withBody("OK").build(),
            String.format("{\"equipmentId\":\"%s\"}", equipmentId));

    getZoneViewPage().<WorklistPage>selectZone(zone.id).waitUntilPageLoaded();

    apiMockService.verify(assignRequest);
  }
}
