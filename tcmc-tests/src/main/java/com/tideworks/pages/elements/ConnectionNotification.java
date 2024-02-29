package com.tideworks.pages.elements;

import static com.tideworks.utilities.controllers.enviroment.EnvironmentController.getDriverType;

import com.tideworks.pages.BasePage;
import com.tideworks.utilities.selectors.DriverSwitchBy;
import com.tideworks.utilities.services.driver.DriverTypes;

import io.qameta.allure.Step;
import lombok.val;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

/** Connection notification label element. Appears after timeout if connection was broken. */
public class ConnectionNotification extends BaseElement {

  @FindBy(name = "Connection failed")
  private RemoteWebElement label;

  public ConnectionNotification(final BasePage basePage, String snackbarId) {
    super(basePage);
    setSelector(DriverSwitchBy.id(snackbarId));
  }

  @Step("Verifies content of notification dialog.")
  public ConnectionNotification verifyText(String expectedText) {
    val actualText =
        getDriverType() == DriverTypes.Chrome
            ? currentPage.driver.findElement(getSelector()).getText()
            : label.getText();

    Assert.assertEquals(expectedText, actualText);
    return this;
  }
}
