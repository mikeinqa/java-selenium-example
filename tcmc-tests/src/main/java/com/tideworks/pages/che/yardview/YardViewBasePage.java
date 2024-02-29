package com.tideworks.pages.che.yardview;

import static com.codeborne.selenide.Selenide.$;

import com.tideworks.pages.BasePage;
import com.tideworks.pages.che.WorklistPage;
import com.tideworks.pages.elements.ElementFactory;
import com.tideworks.pages.elements.dialogs.BlockPickerDialog;
import com.tideworks.pages.elements.dialogs.SelectUtrDialog;
import com.tideworks.utilities.selectors.DriverSwitchBy;

import com.codeborne.selenide.SelenideElement;
import io.appium.java_client.pagefactory.WindowsFindBy;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.val;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Container page to display EndRowView page or Heap view page based on selected block.
 *
 * @param <T> Type of page class
 */
public abstract class YardViewBasePage<T extends YardViewBasePage> extends BasePage<T> {

  @Getter private final String truckSelectionPattern = "truck-selection-display-grid-item-%s";
  private final String truckPickerButtonId = "truck-picker-button";
  private final String blockPickerButtonId = "block-picker-button";
  private final String deselectButtonId = "deselect-button";
  private final String completeButtonId = "complete-button";
  private final String backButtonId = "back-button";

  @Getter
  @FindBy(id = truckPickerButtonId)
  @WindowsFindBy(accessibility = truckPickerButtonId)
  protected RemoteWebElement truckPickerButton;

  @FindBy(id = deselectButtonId)
  @WindowsFindBy(accessibility = deselectButtonId)
  private RemoteWebElement deselectButton;

  @FindBy(id = completeButtonId)
  @WindowsFindBy(accessibility = completeButtonId)
  private RemoteWebElement completeButton;

  @FindBy(id = blockPickerButtonId)
  @WindowsFindBy(accessibility = blockPickerButtonId)
  private RemoteWebElement blockPickerButton;

  @FindBy(id = backButtonId)
  @WindowsFindBy(accessibility = backButtonId)
  private RemoteWebElement backButton;

  @Step("Clicks on block picker button.")
  public BlockPickerDialog clickBlockPickerButton() {
    $(blockPickerButton).click();

    return ElementFactory.getElement(BlockPickerDialog.class, this);
  }

  @Step("Deselects currently selected container by clicking deselect button.")
  public T deselectContainer() {
    $(deselectButton).click();

    return (T) this;
  }

  @Step("Completes move for selected container by clicking on complete button.")
  public T completeContainer() {
    $(completeButton).click();

    return (T) this;
  }

  @Step("Returns to Work-list page.")
  public WorklistPage clickBackButton() {
    $(backButton).click();

    return new WorklistPage();
  }

  @Step("Select truck form right truck panel with given number.")
  public T selectTruck(String truckNo) {
    $(getTruck(truckNo)).click();

    return (T) this;
  }

  /**
   * Return truck element with given number.
   *
   * @param truckNo Truck number to search.
   * @return Truck element.
   */
  public SelenideElement getTruck(String truckNo) {
    val truckItemId = String.format(truckSelectionPattern, truckNo);

    return $(DriverSwitchBy.id(truckItemId));
  }

  @Step("Click truck selection button.")
  public SelectUtrDialog clickTruckPickerButton() {
    $(truckPickerButton).click();

    return ElementFactory.getElement(SelectUtrDialog.class, this);
  }

  @Override
  public boolean isDisplayed() {
    return completeButton.isDisplayed()
        && deselectButton.isDisplayed()
        && truckPickerButton.isDisplayed()
        && blockPickerButton.isDisplayed()
        && backButton.isDisplayed();
  }
}
