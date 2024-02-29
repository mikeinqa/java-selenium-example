package com.tideworks.pages.elements.dialogs;

import com.tideworks.pages.BasePage;
import com.tideworks.pages.elements.ElementFactory;

import io.qameta.allure.Step;

/** Move completion dialog element. */
public class MoveCompletionDialog extends ConfirmationDialog {

  private static final String CONFIRM_COMPLETE_DIALOG = "confirm-complete-dialog";

  public MoveCompletionDialog(final BasePage basePage) {
    super(basePage, CONFIRM_COMPLETE_DIALOG);
  }

  @Step("Confirms move completion dialog.")
  public OperationSucceededDialog confirmCompleteMoveDialog() {
    clickConfirmButton();

    return ElementFactory.getElement(OperationSucceededDialog.class, currentPage);
  }
}
