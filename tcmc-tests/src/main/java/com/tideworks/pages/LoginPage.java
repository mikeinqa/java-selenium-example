package com.tideworks.pages;

import static com.codeborne.selenide.Selenide.$;

import com.tideworks.json.objectmodel.Che;
import com.tideworks.json.objectmodel.Profile;
import com.tideworks.pages.che.WorklistPage;
import com.tideworks.pages.che.ZoneViewPage;
import com.tideworks.pages.microutr.MainPage;
import com.tideworks.pages.vesselclerk.OperationsPage;

import io.appium.java_client.pagefactory.WindowsFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;

/** Login page of application. */
public class LoginPage extends BasePage<LoginPage> {

  private final String usernameTextfieldId = "username-textfield";
  private final String passwordTextfieldId = "password-textfield";
  private final String serverAddressTextfieldId = "server-address-textfield";
  private final String loginButtonId = "login-button";

  @FindBy(id = usernameTextfieldId)
  @WindowsFindBy(accessibility = usernameTextfieldId)
  private RemoteWebElement loginTextField;

  @FindBy(id = passwordTextfieldId)
  @WindowsFindBy(accessibility = passwordTextfieldId)
  private RemoteWebElement passwordTextField;

  @FindBy(id = serverAddressTextfieldId)
  @WindowsFindBy(accessibility = serverAddressTextfieldId)
  private RemoteWebElement serverAddressTextField;

  @FindBy(id = loginButtonId)
  @WindowsFindBy(accessibility = loginButtonId)
  private RemoteWebElement loginButton;

  @Override
  public boolean isDisplayed() {
    return loginTextField.isDisplayed()
        && passwordTextField.isDisplayed()
        && serverAddressTextField.isDisplayed()
        && loginButton.isDisplayed();
  }

  @Step("Fills in user credentials and clicks on login button.")
  public <T extends BasePage> T login(
      Profile profile, String login, String password, String serverAddress) {
    $(loginTextField).setValue(login);
    $(passwordTextField).setValue(password);
    $(serverAddressTextField).setValue(serverAddress);
    $(loginButton).click();

    switch (profile.type) {
      case microutr:
        return (T) PageFactory.getPage(MainPage.class);
      case vesselclerk:
        return (T) PageFactory.getPage(OperationsPage.class);
      case che:
        Che cheProfile = ((Che) profile);
        if (cheProfile.selfAssign) {
          return (T) PageFactory.getPage(ZoneViewPage.class);
        } else {
          return (T) PageFactory.getPage(WorklistPage.class);
        }

      default:
        throw new IllegalArgumentException("Profile type does not supported");
    }
  }
}
