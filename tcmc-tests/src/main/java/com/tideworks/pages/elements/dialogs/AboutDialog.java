package com.tideworks.pages.elements.dialogs;

import static com.codeborne.selenide.Selenide.$;

import com.tideworks.pages.BasePage;
import com.tideworks.pages.elements.BaseElement;
import com.tideworks.utilities.selectors.DriverSwitchBy;

import io.appium.java_client.pagefactory.WindowsBy;
import io.appium.java_client.pagefactory.WindowsFindAll;
import io.appium.java_client.pagefactory.WindowsFindBy;
import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/** About dialog box, which can be opened from right menu. */
public class AboutDialog extends BaseElement {

  private final String aboutDialogTitleId = "about-dialog-title";
  private final String aboutDialogId = "about-dialog";

  @Getter
  @FindAll(@FindBy(xpath = "//div[@id='" + aboutDialogId + "']/section/descendant::p"))
  @WindowsFindAll(
      @WindowsBy(
          xpath =
              "//*[@AutomationId='"
                  + aboutDialogId
                  + "']"
                  + "/child::*[contains(@LocalizedControlType,'section')]"
                  + "/descendant::*[contains(@LocalizedControlType,'text')]"))
  private List<RemoteWebElement> info;

  @Getter
  @FindBy(xpath = "//footer/button")
  @WindowsFindBy(xpath = "//*[@Name='Close']")
  private RemoteWebElement closeButton;

  @Getter
  @FindBy(id = aboutDialogTitleId)
  @WindowsFindBy(accessibility = aboutDialogTitleId)
  private RemoteWebElement aboutDialogTitle;

  public AboutDialog(BasePage basePage) {
    super(basePage);
    setSelector(DriverSwitchBy.id(aboutDialogId));
  }

  @Step("Click on close button.")
  public <T extends BasePage> T clickCloseAboutDialog() {
    $(closeButton).click();

    return (T) currentPage;
  }

  @Override
  public boolean isDisplayed() {
    return super.isDisplayed() && aboutDialogTitle.isDisplayed() && closeButton.isDisplayed();
  }
}
