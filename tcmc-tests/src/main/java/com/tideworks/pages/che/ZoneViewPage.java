package com.tideworks.pages.che;

import static com.codeborne.selenide.Selenide.$;

import com.tideworks.json.objectmodel.VesselOperation;
import com.tideworks.pages.BasePage;
import com.tideworks.pages.PageFactory;
import com.tideworks.pages.elements.ElementFactory;
import com.tideworks.pages.elements.dialogs.LiftingContextSelectorDialog;
import com.tideworks.pages.elements.dialogs.NotificationDialog;
import com.tideworks.utilities.selectors.DriverSwitchBy;

import io.appium.java_client.pagefactory.WindowsFindBy;
import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;

/** Zone view page. */
public class ZoneViewPage extends BasePage<ZoneViewPage> {

  private final String liftingContextSelectorDialogId = "lifting-context-selector-dialog";
  @Getter private final String zonesTabId = "zones-tab";
  @Getter private final String vesselOperationsTabId = "vessel-operations-tab";
  @Getter private final String workQueuesTabId = "work-queues-tab";

  @Getter private final String zoneTableRowPattern = "zone-table-row-%s";
  @Getter private final String vesselOperationTableRowPattern = "vessel-operation-table-row-%s-%s";

  @Getter
  @FindBy(id = zonesTabId)
  @WindowsFindBy(accessibility = zonesTabId)
  private RemoteWebElement zonesTab;

  @FindBy(id = vesselOperationsTabId)
  @WindowsFindBy(accessibility = vesselOperationsTabId)
  private RemoteWebElement vesselOperationsTab;

  @FindBy(id = workQueuesTabId)
  @WindowsFindBy(accessibility = workQueuesTabId)
  private RemoteWebElement workQueuesTab;

  private NotificationDialog errorDialog;

  /** Zone View Page in Che client. */
  public ZoneViewPage() {
    super();

    errorDialog = ElementFactory.getElement(NotificationDialog.class, this, "error-dialog");
  }

  @Step("Click on selected zone.")
  public <T> T selectZone(String zoneId) {
    return selectRow(String.format(zoneTableRowPattern, zoneId));
  }

  @Step("Click on selected vessel operation.")
  public <T> T selectVesselOperation(VesselOperation vesselOperation) {
    return selectRow(
        String.format(
            vesselOperationTableRowPattern, vesselOperation.vessel, vesselOperation.crane));
  }

  private <T> T selectRow(String rowId) {
    $(DriverSwitchBy.id(rowId)).click();

    if (isAnyElementsDisplayed(DriverSwitchBy.id(liftingContextSelectorDialogId))) {
      return (T) ElementFactory.getElement(LiftingContextSelectorDialog.class, this);
    }

    if (isAnyElementsDisplayed(errorDialog.getSelector())) {
      return (T) errorDialog;
    }

    return (T) PageFactory.getPage(WorklistPage.class);
  }

  @Step("Click on Zones tab.")
  public ZoneViewPage clickZonesTab() {
    $(zonesTab).click();

    return this;
  }

  @Step("Click on Vessel Operations tab.")
  public ZoneViewPage clickVesselOperationsTab() {
    $(vesselOperationsTab).click();

    return this;
  }

  @Step("Click on Work Queues tab.")
  public ZoneViewPage clickWorkQueuesTab() {
    $(workQueuesTab).click();

    return this;
  }

  @Override
  public boolean isDisplayed() {
    return isAnyElementsDisplayed(
        DriverSwitchBy.id(vesselOperationsTabId),
        DriverSwitchBy.id(zonesTabId),
        DriverSwitchBy.id(workQueuesTabId));
  }
}
