package com.tideworks.pages.elements.dialogs;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.tideworks.pages.BasePage;
import com.tideworks.pages.PageFactory;
import com.tideworks.pages.che.WorklistPage;
import com.tideworks.pages.elements.ElementFactory;
import com.tideworks.utilities.selectors.DriverSwitchBy;

import io.appium.java_client.pagefactory.WindowsFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;

/** Lifting context selector dialog box element. */
public class LiftingContextSelectorDialog extends DialogContainer {

  private static final String LIFTING_CONTEXT_SELECTOR_DIALOG = "lifting-context-selector-dialog";
  private final String btnDeliverId = "btn-deliver";
  private final String btnReceiveId = "btn-receive";
  private final String errorDialogId = "error-dialog";

  @FindBy(id = btnDeliverId)
  @WindowsFindBy(accessibility = btnDeliverId)
  private RemoteWebElement deliverButton;

  @FindBy(id = btnReceiveId)
  @WindowsFindBy(accessibility = btnReceiveId)
  private RemoteWebElement receiveButton;

  public LiftingContextSelectorDialog(final BasePage basePage) {
    super(basePage, DriverSwitchBy.id(LIFTING_CONTEXT_SELECTOR_DIALOG));
  }

  @Step("Click on deliver button.")
  public WorklistPage clickDeliverButton() {
    $(deliverButton).shouldBe(visible).click();

    return PageFactory.getPage(WorklistPage.class);
  }

  @Step("Click on receive button.")
  public <T> T clickReceiveButton() {
    $(receiveButton).shouldBe(visible).click();

    if ($(DriverSwitchBy.id(errorDialogId)).exists()) {
      return (T) ElementFactory.getElement(NotificationDialog.class, currentPage, errorDialogId);
    }

    return (T) PageFactory.getPage(WorklistPage.class);
  }
}
