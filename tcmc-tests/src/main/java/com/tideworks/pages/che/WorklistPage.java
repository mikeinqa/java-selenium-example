package com.tideworks.pages.che;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.tideworks.json.objectmodel.IntermodalUnit;
import com.tideworks.json.objectmodel.locations.YardLocation;
import com.tideworks.pages.BasePage;
import com.tideworks.pages.PageFactory;
import com.tideworks.pages.che.yardview.EndRowViewPage;
import com.tideworks.pages.che.yardview.HeapViewPage;
import com.tideworks.pages.che.yardview.YardViewBasePage;
import com.tideworks.pages.elements.ElementFactory;
import com.tideworks.pages.elements.dialogs.BlockPickerDialog;
import com.tideworks.utilities.controllers.enviroment.EnvironmentController;
import com.tideworks.utilities.selectors.DriverSwitchBy;
import com.tideworks.utilities.services.driver.DriverTypes;

import com.codeborne.selenide.ElementsCollection;
import io.appium.java_client.pagefactory.WindowsFindBy;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.val;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

/** Work list page. */
public class WorklistPage extends BasePage<WorklistPage> {

  @Getter private final String blockPickerButtonId = "block-picker-button";
  private final String backButtonId = "back-button";
  private final String worklistTableHeaderId = "worklist-table-header";
  private final String worklistTableLeftHeaderId = "worklist-table-left-header";
  private final String worklistTableRightHeaderId = "worklist-table-right-header";
  private final String worklistTableRightBodyId = "worklist-table-right-body";
  private final String worklistTableLeftBodyId = "worklist-table-left-body";

  @Getter private final String workListTableRowPattern = "worklist-table-row-%s";
  @Getter private final String workListTableLeftRowPattern = "worklist-table-left-row-%s";
  @Getter private final String workListTableRightRowPattern = "worklist-table-right-row-%s";

  @Getter private final String escalateMoveXpathChrome = "//button/span[text()='Escalated']";
  @Getter private final String escalateMoveXpathWindows = "//Button[@Name='Escalated']";

  @Getter
  @FindBy(xpath = "//header/h2[text()='Work List']")
  @WindowsFindBy(xpath = "//Text[@Name='Work List']")
  private RemoteWebElement workListTitle;

  @FindBy(xpath = escalateMoveXpathChrome)
  @WindowsFindBy(xpath = escalateMoveXpathWindows)
  private RemoteWebElement escalateMove;

  @FindBy(id = blockPickerButtonId)
  @WindowsFindBy(accessibility = blockPickerButtonId)
  private RemoteWebElement blockPickerButton;

  @FindBy(id = backButtonId)
  @WindowsFindBy(accessibility = backButtonId)
  private RemoteWebElement backButton;

  @Getter
  @FindBy(id = worklistTableHeaderId)
  @WindowsFindBy(xpath = "//HeaderItem/..")
  private RemoteWebElement worklistTableHeader;

  @Getter
  @FindBy(id = worklistTableLeftHeaderId)
  @WindowsFindBy(xpath = "//Pane/Table[position()=1]")
  private RemoteWebElement splitViewLeftHeader;

  @Getter
  @FindBy(id = worklistTableLeftBodyId)
  private RemoteWebElement splitViewLeftBody;

  @Getter
  @FindBy(id = worklistTableRightHeaderId)
  @WindowsFindBy(xpath = "//Pane/Table[position()=3]")
  private RemoteWebElement splitViewRightHeader;

  @FindBy(id = worklistTableRightBodyId)
  private RemoteWebElement splitViewRightBody;

  @Override
  public boolean isDisplayed() {
    return workListTitle.isDisplayed();
  }

  @Step("Clicks on Escalated button.")
  public WorklistPage clickEscalatedFilterButton() {
    $(escalateMove).click();

    return new WorklistPage();
  }

  @Step("Clicks on block picker button.")
  public BlockPickerDialog clickBlockPickerButton() {
    $(blockPickerButton).click();

    return ElementFactory.getElement(BlockPickerDialog.class, this);
  }

  @Step("Clicks on back button.")
  public ZoneViewPage clickBackButton() {
    $(backButton).click();

    return new ZoneViewPage();
  }

  /**
   * Get row for specified container id from work-list page.
   *
   * @param containerId container id.
   * @param tableRowPattern pattern of tables row.
   * @return container row web element page
   */
  public WebElement getContainerRow(String containerId, String tableRowPattern) {
    return $(DriverSwitchBy.id(String.format(tableRowPattern, containerId))).shouldBe(visible);
  }

  @Step("Selects container from work-list.")
  public <T extends YardViewBasePage> T selectContainer(IntermodalUnit container) {
    val containerRowId = String.format(workListTableRowPattern, container.id);

    $(DriverSwitchBy.id(containerRowId)).shouldBe(visible).click();

    return (container.location instanceof YardLocation
            && ((YardLocation) container.location).isHeap())
        ? (T) PageFactory.getPage(HeapViewPage.class)
        : (T) PageFactory.getPage(EndRowViewPage.class);
  }

  /**
   * Gets header of worklist table column.
   *
   * @param tableHeader Header to find.
   * @return Text represented in columns header.
   */
  public List<String> getColumnHeaders(WebElement tableHeader) {
    return ElementsCollection.texts(
        new ArrayList<>(
            $(tableHeader)
                .$$(
                    EnvironmentController.getDriverType() == DriverTypes.Chrome
                        ? By.cssSelector("[class^=md-table-column]")
                        : By.xpath("*/HeaderItem"))));
  }
}
