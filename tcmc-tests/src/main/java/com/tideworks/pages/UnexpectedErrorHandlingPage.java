package com.tideworks.pages;

import static com.codeborne.selenide.Selenide.$;

import io.appium.java_client.pagefactory.WindowsFindBy;
import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;

/** Page handle unexpected error at application. */
public class UnexpectedErrorHandlingPage extends BasePage<UnexpectedErrorHandlingPage> {

  private final String restartButtonXpathWindows = "//Button[@Name='Restart']";
  private final String unhandledExceptionTextXpathWindows =
      "//Text[@Name='Unhandled exception occurred']";
  private final String sentimentVeryDissatisfiedXpathWindows =
      "//Text[@Name='sentiment_very_dissatisfied']";

  private final String restartButtonXpathChrome = "//button[text()='Restart']";
  private final String unhandledExceptionTextXpathChrome =
      "//div[text()='Unhandled exception occurred']";
  private final String sentimentVeryDissatisfiedXpathChrome =
      "//i[text()='sentiment_very_dissatisfied']";

  @Getter
  @FindBy(xpath = restartButtonXpathChrome)
  @WindowsFindBy(xpath = restartButtonXpathWindows)
  private RemoteWebElement restartButton;

  @Getter
  @FindBy(xpath = unhandledExceptionTextXpathChrome)
  @WindowsFindBy(xpath = unhandledExceptionTextXpathWindows)
  private RemoteWebElement unhandledExceptionText;

  @Getter
  @FindBy(xpath = sentimentVeryDissatisfiedXpathChrome)
  @WindowsFindBy(xpath = sentimentVeryDissatisfiedXpathWindows)
  private RemoteWebElement sentimentVeryDissatisfied;

  @Step("Clicks reload button.")
  public LoginPage clickRestartButtonAndGetLoginPage() {
    $(restartButton).click();

    return PageFactory.getPage(LoginPage.class);
  }

  @Override
  public boolean isDisplayed() {
    return restartButton.isDisplayed()
        && unhandledExceptionText.isDisplayed()
        && sentimentVeryDissatisfied.isDisplayed();
  }
}
