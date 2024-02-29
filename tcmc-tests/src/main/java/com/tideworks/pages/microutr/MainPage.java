package com.tideworks.pages.microutr;

import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.tideworks.pages.BasePage;
import com.tideworks.pages.elements.ConnectionNotification;
import com.tideworks.pages.elements.ElementFactory;

import io.appium.java_client.pagefactory.WindowsFindBy;
import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;

import java.util.concurrent.TimeUnit;

/** Main page of Micro-utr application. */
public class MainPage extends BasePage<MainPage> {

  private final String nextLocationLabelId = "label-text-next-location-label";
  private final String labelTextHeldId = "label-text-held-label";
  private final String mainscreenConnectionFailedSnackbarId =
      "mainscreen-connection-failed-snackbar";

  private ConnectionNotification connectionSnackbar;

  @Getter
  @FindBy(id = nextLocationLabelId)
  @WindowsFindBy(xpath = "//Group[@AutomationId='" + nextLocationLabelId + "']/Text")
  private RemoteWebElement nextLocationLabelText;

  @Getter
  @FindBy(id = labelTextHeldId)
  @WindowsFindBy(xpath = "//Group[@AutomationId='" + labelTextHeldId + "']/Text")
  private RemoteWebElement heldLabelText;

  @Getter
  @FindBy(xpath = "//header")
  @WindowsFindBy(xpath = "//Group[@LocalizedControlType='header']")
  private RemoteWebElement header;

  @Getter
  @FindBy(xpath = "//header/h2")
  @WindowsFindBy(xpath = "//Group[@LocalizedControlType='header']/Text")
  private RemoteWebElement headerTitle;

  /** Main page in UTR micro client. */
  public MainPage() {
    super();

    connectionSnackbar =
        ElementFactory.getElement(
            ConnectionNotification.class, this, mainscreenConnectionFailedSnackbarId);
  }

  @Override
  public boolean isDisplayed() {
    return header.isDisplayed();
  }

  @Step("Waits until connection snackbar appears on page.")
  public ConnectionNotification waitSnackbarIsDisplayed() {
    $(connectionSnackbar.getSelector()).waitUntil(visible, TimeUnit.SECONDS.toMillis(longTimeout));

    return connectionSnackbar;
  }

  @Step("Waits until connection snackbar disappears from page.")
  public MainPage waitSnackbarIsNotDisplayed() {
    $(connectionSnackbar.getSelector())
        .waitUntil(not(visible), TimeUnit.SECONDS.toMillis(longTimeout));

    return this;
  }
}
