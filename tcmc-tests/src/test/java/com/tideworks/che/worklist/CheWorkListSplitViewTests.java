package com.tideworks.che.worklist;

import static com.tideworks.verifications.che.Verifications.rightWorklistContainers;
import static com.tideworks.verifications.che.Verifications.splitWorklistColumnHeaders;
import static com.tideworks.verifications.che.Verifications.splitWorklistContainers;

import static java.util.Collections.singletonList;

import com.tideworks.base.CheBase;
import com.tideworks.json.objectmodel.CheGridConfiguration;
import com.tideworks.json.objectmodel.ColumnNames;
import com.tideworks.json.objectmodel.GridColumn;
import com.tideworks.json.objectmodel.ZoneKind;
import com.tideworks.pages.che.WorklistPage;
import com.tideworks.pages.elements.dialogs.LiftingContextSelectorDialog;

import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import lombok.val;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Tests for work list with split view. */
public class CheWorkListSplitViewTests extends CheBase {

  @DataProvider(name = "zoneType")
  static Object[] zoneType() {
    return new Object[] {ZoneKind.Regular, ZoneKind.WorkQueue};
  }

  @Test(dataProvider = "zoneType")
  @Issue("TCL-16599")
  @TmsLink("7381048")
  public void cheWorklistSplitViewExpectedDivisionOfSegments(ZoneKind zoneToVerify) {
    cheProfile.splitWorklist = true;
    cheProfile.selfAssignWorkQueues = true;
    cheProfile.selfAssignZones = true;

    val zone = createZone("Zone", zoneToVerify);
    val container = terminalController.planContainerInHeapFromHeapToYard();
    for (int i = 0; i < 16; i++) {
      terminalController.planContainerInYardFromYardToHeap();
    }

    stubProfileData(cheProfile);
    stubTestData(container);

    val containers =
        terminal.getContainers().stream().map(cont -> cont.id).collect(Collectors.toList());

    val workListPage =
        (zoneToVerify == ZoneKind.Regular
                ? getZoneViewPage().clickZonesTab().<WorklistPage>selectZone(zone.id)
                : getZoneViewPage()
                    .clickWorkQueuesTab()
                    .<LiftingContextSelectorDialog>selectZone(zone.id)
                    .<WorklistPage>clickReceiveButton())
            .verify(splitWorklistContainers(containers));

    val addedContainer = terminalController.planContainerInYardFromYardToHeap();
    stubTestData(container);

    workListPage.verify(rightWorklistContainers(singletonList(addedContainer.intermodalUnit.id)));
  }

  @Test(dataProvider = "zoneType")
  @Issue("TCL-16190")
  @TmsLink("8680073")
  @TmsLink("8625501")
  public void cheWorkList_splitConfigurableColumns(ZoneKind zoneToVerify) {
    val zone = createZone("WorkQueueZone", zoneToVerify);

    val worklistGridConfiguration = new CheGridConfiguration();
    worklistGridConfiguration.id = "worklist";
    worklistGridConfiguration.columns =
        Stream.of(
                ColumnNames.size,
                ColumnNames.containerNo,
                ColumnNames.from,
                ColumnNames.to,
                ColumnNames.departBy,
                ColumnNames.plan,
                ColumnNames.status)
            .map(GridColumn::new)
            .collect(Collectors.toList());

    val splitWorklistGridConfiguration = new CheGridConfiguration();
    splitWorklistGridConfiguration.id = "splitworklist";
    splitWorklistGridConfiguration.columns =
        Stream.of(
                ColumnNames.containerNo,
                ColumnNames.size,
                ColumnNames.from,
                ColumnNames.to,
                ColumnNames.departBy,
                ColumnNames.plan,
                ColumnNames.status)
            .map(GridColumn::new)
            .collect(Collectors.toList());

    cheProfile.splitWorklist = true;
    cheProfile.selfAssignWorkQueues = true;
    cheProfile.selfAssignZones = true;
    cheProfile.gridConfigurations =
        Arrays.asList(worklistGridConfiguration, splitWorklistGridConfiguration);

    stubProfileData(cheProfile);
    stubTestData(terminalController.planContainerInYardFromYardToHeap());

    val expectedHeaderItems =
        splitWorklistGridConfiguration
            .columns
            .stream()
            .map(gridColumn -> gridColumn.id.toString())
            .collect(Collectors.toList());

    val zoneViewPage =
        zoneToVerify == ZoneKind.WorkQueue
            ? getZoneViewPage()
                .clickWorkQueuesTab()
                .<LiftingContextSelectorDialog>selectZone(zone.id)
                .<WorklistPage>clickReceiveButton()
            : getZoneViewPage().<WorklistPage>selectZone(zone.id);

    zoneViewPage.verify(splitWorklistColumnHeaders(expectedHeaderItems));
  }

  @Test(dataProvider = "zoneType")
  @Issue("TCL-16190")
  @TmsLink("9522428")
  public void cheWorkList_splitConfigurableColumns_showsDefaultInCaseIfConfigurationWasNotProvided(
      ZoneKind zoneToVerify) {
    val zone = createZone("WorkQueueZone", zoneToVerify);

    val worklistGridConfiguration = new CheGridConfiguration();
    worklistGridConfiguration.id = "worklist";
    worklistGridConfiguration.columns =
        Stream.of(
                ColumnNames.size,
                ColumnNames.containerNo,
                ColumnNames.from,
                ColumnNames.to,
                ColumnNames.departBy,
                ColumnNames.plan,
                ColumnNames.status)
            .map(GridColumn::new)
            .collect(Collectors.toList());

    cheProfile.splitWorklist = true;
    cheProfile.selfAssignWorkQueues = true;
    cheProfile.selfAssignZones = true;
    cheProfile.gridConfigurations = Collections.singletonList(worklistGridConfiguration);

    stubProfileData(cheProfile);
    stubTestData(terminalController.planContainerInYardFromYardToHeap());

    val expectedHeaderItems = defaultSplitConfiguration();

    val zoneViewPage =
        zoneToVerify == ZoneKind.WorkQueue
            ? getZoneViewPage()
                .clickWorkQueuesTab()
                .<LiftingContextSelectorDialog>selectZone(zone.id)
                .<WorklistPage>clickReceiveButton()
            : getZoneViewPage().<WorklistPage>selectZone(zone.id);

    zoneViewPage.verify(splitWorklistColumnHeaders(expectedHeaderItems));
  }

  private List<String> defaultSplitConfiguration() {
    return Stream.of(ColumnNames.containerNo, ColumnNames.size, ColumnNames.from, ColumnNames.to)
        .map(ColumnNames::toString)
        .collect(Collectors.toList());
  }
}
