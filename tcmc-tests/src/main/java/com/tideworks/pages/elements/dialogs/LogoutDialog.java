package com.tideworks.pages.elements.dialogs;

import com.tideworks.pages.BasePage;
import com.tideworks.pages.LoginPage;

import io.qameta.allure.Step;

/** Logout dialog. */
public class LogoutDialog extends ConfirmationDialog {

  public LogoutDialog(final BasePage basePage, String elementId) {
    super(basePage,elementId);
  }

  @Step("Confirms logout dialog.")
  public LoginPage confirmLogout() {
    clickConfirmButton();

    return new LoginPage();
  }
}
