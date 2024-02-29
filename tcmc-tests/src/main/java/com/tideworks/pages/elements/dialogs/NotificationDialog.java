package com.tideworks.pages.elements.dialogs;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.tideworks.pages.BasePage;
import com.tideworks.pages.PageFactory;
import com.tideworks.utilities.selectors.DriverSwitchBy;

import io.appium.java_client.pagefactory.WindowsFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;

/** Notification dialog element. */
public class NotificationDialog extends DialogContainer {

  @FindBy(xpath = "//button[text()='OK']")
  @WindowsFindBy(xpath = "//Button[@Name='OK']")
  private RemoteWebElement okButton;

  public NotificationDialog(final BasePage basePage, String elementId) {
    super(basePage, DriverSwitchBy.id(elementId));
  }

  @Step("Clicks on Ok button and closes dialog.")
  public void clickOkButton() {
    $(okButton).shouldBe(visible).click();
  }

  /**
   * Clicks on Ok button and closes dialog.
   *
   * @param pageType Class of page to be returned after action.
   * @param <T> Type of page to be returned after action.
   * @return Page instance.
   */
  public <T extends BasePage> T clickOkButton(Class<T> pageType) {
    clickOkButton();

    return PageFactory.getPage(pageType);
  }
}
