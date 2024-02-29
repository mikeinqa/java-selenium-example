package com.tideworks.pages.vesselclerk;

import static com.codeborne.selenide.Selenide.$;

import com.tideworks.pages.BasePage;
import com.tideworks.utilities.selectors.DriverSwitchBy;

import com.codeborne.selenide.SelenideElement;
import io.appium.java_client.pagefactory.WindowsFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;

/** Operation page. */
public class OperationsPage extends BasePage<OperationsPage> {

  private final String vesselSelectFieldToggleId = "vessel-select-field-toggle";
  private final String craneSelectFieldToggleId = "crane-select-field-toggle";
  private final String selectOperationButtonId = "select-operation-button";
  private final String refreshOperationButtonId = "refresh-operation-button";

  @FindBy(id = vesselSelectFieldToggleId)
  @WindowsFindBy(accessibility = vesselSelectFieldToggleId)
  private RemoteWebElement vesselSelect;

  @FindBy(id = craneSelectFieldToggleId)
  @WindowsFindBy(accessibility = craneSelectFieldToggleId)
  private RemoteWebElement craneSelect;

  @FindBy(id = selectOperationButtonId)
  @WindowsFindBy(accessibility = selectOperationButtonId)
  private RemoteWebElement selectOperationButton;

  @FindBy(id = refreshOperationButtonId)
  @WindowsFindBy(accessibility = refreshOperationButtonId)
  private RemoteWebElement refreshOperationButton;

  @Override
  public boolean isDisplayed() {
    return vesselSelect.isDisplayed()
        && craneSelect.isDisplayed()
        && selectOperationButton.isDisplayed()
        && refreshOperationButton.isDisplayed();
  }

  @Step("Selects vessel from drop down list.")
  public OperationsPage selectVessel(String vessel) {
    $(vesselSelect).click();
    $(findElementFromDropDownList(vessel)).click();

    return this;
  }

  @Step("Selects crane from drop down list.")
  public OperationsPage selectCrane(String crane) {
    $(craneSelect).click();
    $(findElementFromDropDownList(crane)).click();

    return this;
  }

  @Step("Click on select operation button.")
  public ContainerSearchPage clickSelectOperationButton() {
    $(selectOperationButton).click();

    return new ContainerSearchPage();
  }

  private SelenideElement findElementFromDropDownList(String elementName) {
    return $(
        DriverSwitchBy.xpath(
            String.format("//div[@data-value='%s']", elementName),
            String.format("//*[@Name='%s']", elementName)));
  }
}
