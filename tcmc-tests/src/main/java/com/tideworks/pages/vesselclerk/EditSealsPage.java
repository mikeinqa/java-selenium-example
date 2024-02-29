package com.tideworks.pages.vesselclerk;

import static com.codeborne.selenide.Selenide.$;

import com.tideworks.json.objectmodel.Seal;
import com.tideworks.pages.BasePage;
import com.tideworks.pages.elements.dialogs.NotificationDialog;

import io.appium.java_client.pagefactory.WindowsFindBy;
import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;

/** Edit seals page for vessel clerk application. */
public class EditSealsPage extends BasePage<EditSealsPage> {

  private final String containerNumberId = "container-number-textfield";
  private final String seal1TextFieldId = "seal1-textfield";
  private final String seal2TextFieldId = "seal2-textfield";
  private final String seal3TextFieldId = "seal3-textfield";
  private final String seal4TextFieldId = "seal4-textfield";
  private final String saveButtonId = "save-button";
  private final String successDialogId = "success-dialog";

  @FindBy(id = containerNumberId)
  @WindowsFindBy(accessibility = containerNumberId)
  private RemoteWebElement containerNumber;

  @Getter
  @FindBy(id = seal1TextFieldId)
  @WindowsFindBy(accessibility = seal1TextFieldId)
  private RemoteWebElement seal1TextField;

  @Getter
  @FindBy(id = seal2TextFieldId)
  @WindowsFindBy(accessibility = seal2TextFieldId)
  private RemoteWebElement seal2TextField;

  @Getter
  @FindBy(id = seal3TextFieldId)
  @WindowsFindBy(accessibility = seal3TextFieldId)
  private RemoteWebElement seal3TextField;

  @Getter
  @FindBy(id = seal4TextFieldId)
  @WindowsFindBy(accessibility = seal4TextFieldId)
  private RemoteWebElement seal4TextField;

  @FindBy(id = saveButtonId)
  @WindowsFindBy(accessibility = saveButtonId)
  private RemoteWebElement saveButton;

  @Override
  public boolean isDisplayed() {
    return seal1TextField.isDisplayed();
  }

  @Step("Click save button")
  public NotificationDialog clickSaveButton() {
    $(saveButton).click();

    return new NotificationDialog(this, successDialogId);
  }

  @Step("Rewrite all seals state")
  public EditSealsPage changeSealsTexts(final Seal seal) {
    rewriteSealInput(seal1TextField, seal.seal1);
    rewriteSealInput(seal2TextField, seal.seal2);
    rewriteSealInput(seal3TextField, seal.seal3);
    rewriteSealInput(seal4TextField, seal.seal4);

    return this;
  }

  private void rewriteSealInput(RemoteWebElement seal, String newText) {
    $(seal).click();
    $(seal).clear();
    $(seal).sendKeys(newText);
  }
}
