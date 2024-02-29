package com.tideworks.pages.vesselclerk;

import static com.codeborne.selenide.Selenide.$;

import com.tideworks.pages.BasePage;
import com.tideworks.pages.elements.ElementFactory;
import com.tideworks.pages.elements.dialogs.MoveCompletionDialog;

import io.appium.java_client.pagefactory.WindowsFindBy;
import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;

/** Move completion page. */
public class MoveCompletionPage extends BasePage<MoveCompletionPage> {

  @Getter private final String nextLocationTextFieldId = "next-location-textfield";
  @Getter private final String backreachSwitchLabelId = "backreach-switch-label";
  @Getter private final String completeMoveButtonId = "complete-move-button";
  @Getter private final String chassisTextfieldId = "chassis-textfield";
  @Getter private final String backButtonId = "back-button";

  @Getter
  private final String labelTextContainerNumberTextFieldId =
      "label-text-container-number-textfield";

  @Getter
  @FindBy(id = labelTextContainerNumberTextFieldId)
  @WindowsFindBy(xpath = "//*[@AutomationId='" + labelTextContainerNumberTextFieldId + "']/Text")
  private RemoteWebElement containerNumberTextField;

  @Getter
  @FindBy(id = nextLocationTextFieldId)
  @WindowsFindBy(accessibility = nextLocationTextFieldId)
  private RemoteWebElement nextLocationTextField;

  @Getter
  @FindBy(id = chassisTextfieldId)
  @WindowsFindBy(accessibility = chassisTextfieldId)
  private RemoteWebElement chassisTextField;

  @FindBy(id = completeMoveButtonId)
  @WindowsFindBy(accessibility = completeMoveButtonId)
  private RemoteWebElement completeMoveButton;

  @FindBy(id = backButtonId)
  @WindowsFindBy(accessibility = backButtonId)
  private RemoteWebElement backButton;

  @Getter
  @FindBy(xpath = "//header/h2")
  @WindowsFindBy(xpath = "//*[@Name='Load']")
  private RemoteWebElement loadTitle;

  @Getter
  @FindBy(xpath = "//header/h2")
  @WindowsFindBy(xpath = "//*[@Name='Discharge']")
  private RemoteWebElement dischargeTitle;

  @Getter
  @FindBy(xpath = "//*[@id='" + backreachSwitchLabelId + "']/..")
  @WindowsFindBy(xpath = "//Text[@Name='Complete to backreach']/..")
  private RemoteWebElement completeToBackreachToggle;

  @Override
  public boolean isDisplayed() {
    return containerNumberTextField.isDisplayed()
        && nextLocationTextField.isDisplayed()
        && completeMoveButton.isDisplayed();
  }

  @Step("Returns to Container search page.")
  public ContainerSearchPage returnToPreviousPage() {
    $(backButton).click();

    return new ContainerSearchPage();
  }

  @Step("Clicks on backreach toggle.")
  public MoveCompletionPage clickCompleteToBackreachToggle() {
    $(completeToBackreachToggle).click();

    return this;
  }

  @Step("Clicks on complete move button.")
  public MoveCompletionDialog clickCompleteMoveButton() {
    $(completeMoveButton).click();

    return ElementFactory.getElement(MoveCompletionDialog.class, this);
  }
}
