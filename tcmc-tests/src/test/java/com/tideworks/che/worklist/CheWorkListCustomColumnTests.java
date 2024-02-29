package com.tideworks.che.worklist;

import static com.tideworks.json.objectmodel.ColumnNames.containerNo;
import static com.tideworks.json.objectmodel.ColumnNames.from;
import static com.tideworks.json.objectmodel.ColumnNames.values;
import static com.tideworks.verifications.che.Verifications.worklistColumnHeaders;
import static com.tideworks.verifications.che.Verifications.worklistContainers;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import com.tideworks.base.CheBase;
import com.tideworks.json.objectmodel.ColumnNames;
import com.tideworks.json.objectmodel.GridColumn;

import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import lombok.val;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Tests for work list configurable columns. */
public class CheWorkListCustomColumnTests extends CheBase {

  @Test
  @Issue("TCL-16599")
  @TmsLink("7381048")
  public void cheWorklistConfigurableColumnsDefaults() {
    val containerPlan = terminalController.planContainerInYardFromYardToHeap();

    stubTestData(containerPlan);
    stubProfileData(getDefaultCheProfile());

    getWorklistPage()
        .verify(
            worklistColumnHeaders(
                Stream.of(
                        ColumnNames.containerNo, ColumnNames.size, ColumnNames.from, ColumnNames.to)
                    .map(ColumnNames::toString)
                    .collect(Collectors.toList())));
  }

  @Test
  @Issue("TCL-16599")
  @TmsLink("7381048")
  public void cheWorklistConfigurableColumnsSingleColumnId() {
    getExtendedProfile(singletonList(new GridColumn(containerNo)));
    val containerPlan = terminalController.planContainerInYardFromYardToHeap();

    stubProfileData(cheProfile);
    stubTestData(containerPlan);

    getWorklistPage()
        .verify(worklistColumnHeaders(singletonList("Container Number")))
        .verify(worklistContainers(singletonList(containerPlan.intermodalUnit.id)));
  }

  @Test
  @Issue("TCL-16599")
  @TmsLink("7381048")
  public void cheWorklistConfigurableColumnsAllColumnId() {
    val allConfigurableColumnsNames =
        Arrays.stream(values()).map(GridColumn::new).collect(Collectors.toList());

    getExtendedProfile(new ArrayList<>(allConfigurableColumnsNames));
    stubProfileData(cheProfile);

    val containerPlan = terminalController.planContainerInYardFromYardToHeap();
    stubTestData(containerPlan);

    getWorklistPage()
        .verify(
            worklistColumnHeaders(
                Stream.of(ColumnNames.values()).map(Enum::toString).collect(Collectors.toList())))
        .verify(worklistContainers(singletonList(containerPlan.intermodalUnit.id)));
  }

  @Test
  @Issue("TCL-16599")
  @TmsLink("7381048")
  public void cheWorklistConfigurableColumnsDuplicateColumnIds() {
    getExtendedProfile(
        asList(new GridColumn(containerNo), new GridColumn(from), new GridColumn(containerNo)));
    stubProfileData(cheProfile);

    val containerPlan = terminalController.planContainerInYardFromYardToHeap();
    stubTestData(containerPlan);

    getWorklistPage()
        .verify(
            worklistColumnHeaders(
                Stream.of(ColumnNames.containerNo, ColumnNames.from)
                    .map(ColumnNames::toString)
                    .collect(Collectors.toList())))
        .verify(worklistContainers(singletonList(containerPlan.intermodalUnit.id)));
  }
}
