package com.tideworks.pages.elements.dialogs;

import static com.tideworks.utilities.configs.appconfig.PropertyNames.TIMEOUT;
import static com.tideworks.utilities.configs.appconfig.PropertyProvider.getValue;

import static com.codeborne.selenide.Selenide.$;
import static java.lang.Long.parseLong;
import static java.util.concurrent.TimeUnit.SECONDS;

import com.tideworks.pages.BasePage;
import com.tideworks.pages.PageFactory;
import com.tideworks.pages.che.yardview.HeapViewPage;
import com.tideworks.pages.elements.BaseElement;
import com.tideworks.pages.elements.ElementFactory;
import com.tideworks.utilities.selectors.DriverSwitchBy;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.appium.java_client.pagefactory.WindowsFindBy;
import io.qameta.allure.Step;
import lombok.val;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;

/** Block picker dialog for Che application. */
public class BlockPickerDialog extends BaseElement {

  private static final String BLOCKS = "Blocks";
  private static final String HEAPS = "Heaps";

  private final String blockPickerId = "block-picker";
  private final String gridButtonPattern = "grid-button-%s";
  private final String blockPickerDialogId = "block-picker-dialog";
  private final String blockPickerFirstTabId = "block-picker-tab-0";
  private final String blockPickerSecondTabId = "block-picker-tab-1";
  private final String blockPickerFirstTabPanelId = "block-picker-tab-panel-0";
  private final String blockPickerSecondTabPanelId = "block-picker-tab-panel-1";

  @FindBy(id = blockPickerId)
  @WindowsFindBy(accessibility = blockPickerId)
  private RemoteWebElement blockPicker;

  @FindBy(id = blockPickerDialogId)
  @WindowsFindBy(accessibility = blockPickerDialogId)
  private RemoteWebElement blockPickerDialog;

  @FindBy(id = blockPickerFirstTabId)
  @WindowsFindBy(accessibility = blockPickerFirstTabId)
  private RemoteWebElement blocksTab;

  @FindBy(id = blockPickerSecondTabId)
  @WindowsFindBy(accessibility = blockPickerSecondTabId)
  private RemoteWebElement heapsTab;

  @FindBy(id = blockPickerFirstTabPanelId)
  @WindowsFindBy(accessibility = blockPickerFirstTabPanelId)
  private RemoteWebElement blockTabPanel;

  @FindBy(id = blockPickerSecondTabPanelId)
  @WindowsFindBy(accessibility = blockPickerSecondTabPanelId)
  private RemoteWebElement heapTabPanel;

  public BlockPickerDialog(final BasePage basePage) {
    super(basePage);
  }

  @Step("Selects Heap block.")
  public HeapViewPage selectHeapBlock(String heapBlockName) {
    $(heapsTab).shouldBe(Condition.visible).click();

    waitUntilSlideAnimationIsComplete(heapTabPanel);
    findBlock(heapBlockName).shouldBe(Condition.visible).click();

    return PageFactory.getPage(HeapViewPage.class);
  }

  @Step("Selects block.")
  public RowPickerDialog selectBlock(String blockName) {
    $(blocksTab).shouldBe(Condition.visible).click();

    waitUntilSlideAnimationIsComplete(blockTabPanel);
    findBlock(blockName).shouldBe(Condition.visible).click();

    return ElementFactory.getElement(RowPickerDialog.class, currentPage);
  }

  private SelenideElement findBlock(String blockName) {
    val blockId = String.format(gridButtonPattern, blockName);

    return $(DriverSwitchBy.id(blockId));
  }

  private void waitUntilSlideAnimationIsComplete(RemoteWebElement webElement) {
    val firstChildElement = ((RemoteWebElement) webElement.findElement(By.xpath("*/*")));

    $(webElement)
        .waitUntil(
            new Condition("Wait for animation to be completed") {
              @Override
              public boolean apply(final WebElement element) {
                return webElement.getCoordinates().onPage().x
                    == firstChildElement.getCoordinates().onPage().x;
              }
            },
            SECONDS.toMillis(parseLong(getValue(TIMEOUT))));
  }
}
