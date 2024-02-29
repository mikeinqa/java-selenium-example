package com.tideworks.pages.vesselclerk;

import static com.tideworks.utilities.selectors.DriverSwitchBy.id;

import static com.codeborne.selenide.Selenide.$;

import com.tideworks.pages.BasePage;
import com.tideworks.pages.PageFactory;
import com.tideworks.pages.elements.ElementFactory;
import com.tideworks.pages.elements.dialogs.ConfirmationDialog;
import com.tideworks.pages.elements.dialogs.NotificationDialog;

import io.appium.java_client.pagefactory.WindowsFindBy;
import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;

/** Container search page for vessel clerk application. */
public class ContainerSearchPage extends BasePage<ContainerSearchPage> {

  @Getter private final String heldMoveErrorDialogId = "held-move-error-dialog";
  @Getter private final String confirmSelectContainerDialogId = "confirm-select-container-dialog";
  @Getter private final String confirmLoadHeldMoveId = "confirm-load-held-move-dialog";
  @Getter private final String errorDialogId = "error-dialog";
  @Getter private final String searchTextfieldId = "search-textfield";

  @Getter
  @FindBy(id = searchTextfieldId)
  @WindowsFindBy(accessibility = searchTextfieldId)
  private RemoteWebElement searchTextField;

  @Override
  public boolean isDisplayed() {
    return searchTextField.isDisplayed();
  }

  @Step("Sends text to search field.")
  public ContainerSearchPage searchContainer(String containerNo) {
    $(searchTextField).setValue(containerNo);

    return this;
  }

  @Step("Selects container from search result.")
  public <T extends BasePage> T selectContainer(String containerNo) {
    $(id(containerNo)).click();

    if (isAnyDialogOpen()) {
      return (T) this;
    }

    return (T) new MoveCompletionPage();
  }

  @Step("Closes currently opened error dialog.")
  public ContainerSearchPage clickCloseErrorDialog() {
    ElementFactory.getElement(NotificationDialog.class, this, errorDialogId).clickOkButton();

    return this;
  }

  @Step("Closes move on hold error.")
  public ContainerSearchPage clickCloseMoveOnHoldError() {
    ElementFactory.getElement(NotificationDialog.class, this, heldMoveErrorDialogId)
        .clickOkButton();

    return this;
  }

  @Step("Confirms container selection.")
  public MoveCompletionPage clickConfirmSelectedContainer() {
    ElementFactory.getElement(ConfirmationDialog.class, this, confirmSelectContainerDialogId)
        .clickConfirmButton();

    return PageFactory.getPage(MoveCompletionPage.class);
  }

  @Step("Declines container selection.")
  public ContainerSearchPage clickDeclineSelectedContainer() {
    ElementFactory.getElement(ConfirmationDialog.class, this, confirmSelectContainerDialogId)
        .clickDeclineButton();

    return this;
  }

  @Step("Confirms load held move.")
  public <T extends BasePage> T clickConfirmLoadHeldMove() {
    ElementFactory.getElement(ConfirmationDialog.class, this, confirmLoadHeldMoveId)
        .clickConfirmButton();

    if (isAnyDialogOpen()) {
      return (T) this;
    }

    return (T) PageFactory.getPage(MoveCompletionPage.class);
  }

  @Step("Declines load held move dialog.")
  public ContainerSearchPage clickDeclineLoadHeldMove() {
    ElementFactory.getElement(ConfirmationDialog.class, this, confirmLoadHeldMoveId)
        .clickDeclineButton();

    return this;
  }

  private Boolean isAnyDialogOpen() {
    return isAnyElementsDisplayed(
        id(heldMoveErrorDialogId),
        id(errorDialogId),
        id(confirmSelectContainerDialogId),
        id(confirmLoadHeldMoveId));
  }
}
