package com.tideworks.pages.elements.dialogs;

import static com.codeborne.selenide.Selenide.$;

import com.tideworks.json.objectmodel.locations.YardLocation;
import com.tideworks.pages.BasePage;
import com.tideworks.pages.PageFactory;
import com.tideworks.pages.che.yardview.EndRowViewPage;
import com.tideworks.pages.elements.BaseElement;
import com.tideworks.pages.elements.ElementFactory;
import com.tideworks.utilities.selectors.DriverSwitchBy;

import com.codeborne.selenide.Condition;
import io.appium.java_client.pagefactory.WindowsFindBy;
import io.qameta.allure.Step;
import lombok.val;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;

/** Row picker dialog element. */
public class RowPickerDialog extends BaseElement {

  private final String rowSelectionToolbarId = "row-selection-toolbar";
  private final String blockPickerDialogId = "block-picker-dialog";
  private final String backButtonId = "back-button";
  private final String rowSelectionToolbarTileId = "row-selection-toolbar-title";
  private final String gridButtonPattern = "grid-button-%s (%s)";

  @FindBy(id = rowSelectionToolbarId)
  @WindowsFindBy(accessibility = rowSelectionToolbarId)
  private RemoteWebElement rowSelectionToolbar;

  @FindBy(id = blockPickerDialogId)
  @WindowsFindBy(accessibility = blockPickerDialogId)
  private RemoteWebElement blockPickerDialog;

  @FindBy(id = backButtonId)
  @WindowsFindBy(accessibility = backButtonId)
  private RemoteWebElement backButton;

  @FindBy(id = rowSelectionToolbarTileId)
  @WindowsFindBy(accessibility = rowSelectionToolbarTileId)
  private RemoteWebElement rowSelectionToolbarTitle;

  RowPickerDialog(final BasePage basePage) {
    super(basePage);
  }

  @Step("Selects block's row from opened list.")
  public EndRowViewPage selectBlockRow(String rowName, String rowName40) {
    val rowId = String.format(gridButtonPattern, rowName, rowName40);

    $(DriverSwitchBy.id(rowId)).shouldBe(Condition.visible).click();

    return PageFactory.getPage(EndRowViewPage.class);
  }

  @Step("Selects block's row from opened list.")
  public EndRowViewPage selectBlockRow(YardLocation location) {
    return selectBlockRow(location.row, location.row);
  }

  @Step("Returns back to block picker dialog.")
  public BlockPickerDialog returnBackToBlockPickerDialog() {
    backButton.click();

    return ElementFactory.getElement(BlockPickerDialog.class, currentPage);
  }
}
