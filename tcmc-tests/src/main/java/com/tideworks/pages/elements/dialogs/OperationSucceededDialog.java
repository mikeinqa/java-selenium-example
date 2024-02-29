package com.tideworks.pages.elements.dialogs;

import com.tideworks.pages.BasePage;
import com.tideworks.pages.vesselclerk.ContainerSearchPage;

import io.qameta.allure.Step;

/** Operation succeeded dialog element. */
public class OperationSucceededDialog extends NotificationDialog {

  private static final String SUCCESS_DIALOG = "success-dialog";

  public OperationSucceededDialog(final BasePage basePage) {
    super(basePage, SUCCESS_DIALOG);
  }

  @Step("Closes Operation succeeded dialog.")
  public ContainerSearchPage closeOperationSucceededDialog() {
    clickOkButton();

    return new ContainerSearchPage();
  }
}
