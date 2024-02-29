package com.tideworks.pages.elements.dialogs;

import com.tideworks.pages.BasePage;
import com.tideworks.pages.PageFactory;
import com.tideworks.pages.che.yardview.EndRowViewPage;
import com.tideworks.pages.elements.BaseElement;
import com.tideworks.utilities.selectors.DriverSwitchBy;

import io.appium.java_client.pagefactory.WindowsFindBy;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.val;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;

/** Select utr dialog for Che application. */
public class SelectUtrDialog extends BaseElement {

  private final String titleId = "truck-selection-toolbar-title";
  private final String backButtonId = "back-button";

  @Getter private final String gridButtonPattern = "grid-button-%s";

  @FindBy(id = backButtonId)
  @WindowsFindBy(accessibility = backButtonId)
  private RemoteWebElement backButton;

  @FindBy(id = titleId)
  @WindowsFindBy(accessibility = titleId)
  private RemoteWebElement title;

  public SelectUtrDialog(final BasePage basePage) {
    super(basePage);
  }

  @Step("Selects utr from opened list.")
  public EndRowViewPage selectUtr(String utrName) {
    val rowId = String.format(gridButtonPattern, utrName);

    currentPage.driver.findElement(DriverSwitchBy.id(rowId)).click();

    return PageFactory.getPage(EndRowViewPage.class);
  }

  @Step("Returns back to end row view page.")
  public EndRowViewPage returnBackToEndRowViewPage() {
    backButton.click();

    return PageFactory.getPage(EndRowViewPage.class);
  }

  @Override
  public boolean isDisplayed() {
    return title.isDisplayed();
  }
}
