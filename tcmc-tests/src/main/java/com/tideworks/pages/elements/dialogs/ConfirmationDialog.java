package com.tideworks.pages.elements.dialogs;

import static com.codeborne.selenide.Selenide.$;

import com.tideworks.pages.BasePage;
import com.tideworks.utilities.selectors.DriverSwitchBy;

import io.appium.java_client.pagefactory.WindowsFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;

/** Confirmation dialog box element. */
public class ConfirmationDialog extends DialogContainer {

  @FindBy(xpath = "//button[text()='Yes' or text()='Continue']")
  @WindowsFindBy(xpath = "//*[@Name='Yes' or @Name='Continue']")
  private RemoteWebElement confirmButton;

  @FindBy(xpath = "//button[text()='No' or text()='Cancel']")
  @WindowsFindBy(xpath = "//*[@Name='No' or @Name='Cancel']")
  private RemoteWebElement declineButton;

  public ConfirmationDialog(final BasePage basePage, String elementId) {
    super(basePage, DriverSwitchBy.id(elementId));
  }

  @Step("Click on confirmation button.")
  public ConfirmationDialog clickConfirmButton() {
    $(confirmButton).click();

    return this;
  }

  @Step("Click on decline button.")
  public <T extends BasePage> T clickDeclineButton() {
    $(declineButton).click();

    return (T) currentPage;
  }
}
