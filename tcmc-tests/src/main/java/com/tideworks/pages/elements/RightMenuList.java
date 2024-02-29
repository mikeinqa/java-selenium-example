package com.tideworks.pages.elements;

import static com.codeborne.selenide.Selenide.$;

import com.tideworks.pages.BasePage;
import com.tideworks.pages.elements.dialogs.AboutDialog;
import com.tideworks.pages.elements.dialogs.LogoutDialog;
import com.tideworks.pages.vesselclerk.EditDamagePage;
import com.tideworks.pages.vesselclerk.EditOversizePage;
import com.tideworks.pages.vesselclerk.EditSealsPage;
import com.tideworks.utilities.selectors.DriverSwitchBy;

import io.appium.java_client.pagefactory.WindowsFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;

/** Right menu list element. */
public class RightMenuList extends BaseElement {

  private final String confirmLogoutDialogId = "confirm-logout-dialog";
  private final String logoutMenuButtonId = "logout-menu-button";
  private final String aboutMenuButtonId = "about-menu-button";
  private final String rightMenuListId = "right_menu-list";
  private final String editSealsButtonId = "edit-seals";
  private final String editOversizeButtonId = "edit-oversize";
  private final String editDamageButtonId = "edit-damages";

  @FindBy(id = aboutMenuButtonId)
  @WindowsFindBy(accessibility = aboutMenuButtonId)
  private RemoteWebElement aboutMenuButton;

  @FindBy(id = logoutMenuButtonId)
  @WindowsFindBy(accessibility = logoutMenuButtonId)
  private RemoteWebElement logoutMenuButton;

  @FindBy(id = editSealsButtonId)
  @WindowsFindBy(accessibility = editSealsButtonId)
  private RemoteWebElement editSealsButton;

  @FindBy(id = editOversizeButtonId)
  @WindowsFindBy(accessibility = editOversizeButtonId)
  private RemoteWebElement editOversizeButton;

  @FindBy(id = editDamageButtonId)
  @WindowsFindBy(accessibility = editDamageButtonId)
  private RemoteWebElement editDamageButton;

  public RightMenuList(BasePage basePage) {
    super(basePage);
    setSelector(DriverSwitchBy.id(rightMenuListId));
  }

  @Step("Clicks on about menu button")
  public AboutDialog clickAboutMenuButton() {
    $(aboutMenuButton).click();

    return ElementFactory.getElement(AboutDialog.class, currentPage);
  }

  @Step("Clicks on logout menu button")
  public LogoutDialog clickLogoutMenuButton() {
    $(logoutMenuButton).click();

    return ElementFactory.getElement(LogoutDialog.class, currentPage, confirmLogoutDialogId);
  }

  @Step("Clicks on Edit seals menu button")
  public EditSealsPage clickEditSealsButton() {
    $(editSealsButton).click();

    return new EditSealsPage();
  }

  @Step("Clicks on Edit oversize menu button")
  public EditOversizePage clickEditOversizeButton() {
    $(editOversizeButton).click();

    return new EditOversizePage();
  }

  @Step("Click on Edit damage menu button")
  public EditDamagePage clickEditDamageButton() {
    $(editDamageButton).click();

    return new EditDamagePage();
  }

  @Override
  public boolean isDisplayed() {
    return super.isDisplayed() && aboutMenuButton.isDisplayed() && logoutMenuButton.isDisplayed();
  }
}
