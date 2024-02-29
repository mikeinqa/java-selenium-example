package com.tideworks.pages.vesselclerk;

import static com.codeborne.selenide.Selenide.$;

import com.tideworks.json.objectmodel.Oversize;
import com.tideworks.pages.BasePage;
import com.tideworks.pages.elements.dialogs.NotificationDialog;
import com.tideworks.utilities.controllers.enviroment.EnvironmentController;

import io.appium.java_client.pagefactory.WindowsFindBy;
import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.Keys;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

/** Edit oversize page for vessel clerk application. */
public class EditOversizePage extends BasePage<EditOversizePage> {

  private final String containerNumberId = "label-text-container-number-textfield";
  private final String heightTextFieldId = "height-textfield";
  private final String leftTextFieldId = "left-textfield";
  private final String rightTextFieldId = "right-textfield";
  private final String aftTextFieldId = "aft-textfield";
  private final String forwardTextFieldId = "forward-textfield";
  private final String saveButtonId = "save-button";
  private final String successDialogId = "success-dialog";

  @FindBy(id = containerNumberId)
  @WindowsFindBy(accessibility = containerNumberId)
  private RemoteWebElement containerNumber;

  @FindBy(id = saveButtonId)
  @WindowsFindBy(accessibility = saveButtonId)
  private RemoteWebElement saveButton;

  @Getter
  @FindBy(id = heightTextFieldId)
  @WindowsFindBy(accessibility = heightTextFieldId)
  private RemoteWebElement heightTextField;

  @Getter
  @FindBy(id = rightTextFieldId)
  @WindowsFindBy(accessibility = rightTextFieldId)
  private RemoteWebElement rightTextField;

  @Getter
  @FindBy(id = leftTextFieldId)
  @WindowsFindBy(accessibility = leftTextFieldId)
  private RemoteWebElement leftTextField;

  @Getter
  @FindBy(id = aftTextFieldId)
  @WindowsFindBy(accessibility = aftTextFieldId)
  private RemoteWebElement aftTextField;

  @Getter
  @FindBy(id = forwardTextFieldId)
  @WindowsFindBy(accessibility = forwardTextFieldId)
  private RemoteWebElement forwardTextField;

  @Step("Click save button")
  public NotificationDialog clickSaveButton() {
    $(saveButton).click();

    return new NotificationDialog(this, successDialogId);
  }

  @Step("Set new state for oversize")
  public EditOversizePage changeOversizeTexts(final Oversize oversize) {
    rewriteInput(aftTextField, oversize.aft);
    rewriteInput(forwardTextField, oversize.forward);
    rewriteInput(heightTextField, oversize.height);
    rewriteInput(leftTextField, oversize.left);
    rewriteInput(rightTextField, oversize.right);

    return this;
  }

  @Override
  public boolean isDisplayed() {
    return heightTextField.isDisplayed();
  }

  private void rewriteInput(RemoteWebElement inputElement, String newText) {
    $(inputElement).click();

    switch (EnvironmentController.getDriverType()) {
      case Windows:
        int length = inputElement.getText().length();
        for (int i = 0; i < length; i++) {
          $(inputElement).sendKeys(Keys.BACK_SPACE);
        }
        break;
      case Chrome:
        $(inputElement).clear();
        break;
      default:
        Assert.fail("Unsupported driver type");
    }

    $(inputElement).sendKeys(newText);
  }
}
