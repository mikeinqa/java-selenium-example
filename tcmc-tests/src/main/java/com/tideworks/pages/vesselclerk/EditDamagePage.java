package com.tideworks.pages.vesselclerk;

import static com.codeborne.selenide.Selenide.$;

import com.tideworks.json.objectmodel.Damage;
import com.tideworks.json.objectmodel.DamageObject;
import com.tideworks.pages.BasePage;
import com.tideworks.pages.elements.dialogs.NotificationDialog;
import com.tideworks.utilities.controllers.enviroment.EnvironmentController;

import io.appium.java_client.pagefactory.WindowsFindBy;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.val;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

/** Edit damage page for vessel clerk application. */
public class EditDamagePage extends BasePage<EditDamagePage> {

  @Getter private final String damageLocationTextFieldPattern = "damage-detail%d-location-field";
  @Getter private final String damageTypeTextFieldPattern = "damage-detail%d-type-field";
  private final String damageLocationTogglePattern = "damage-detail%d-location-field-toggle";
  private final String damageTypeTogglePattern = "damage-detail%d-type-field-toggle";

  private final String damageLocationFieldMenuPattern =
      "damage-detail%d-location-field-menu-options";
  private final String damageTypeFieldMenuPattern = "damage-detail%d-type-field-menu-options";

  private final String containerNumberId = "label-title-container-number-textfield";
  private final String damagesSwitchToggleXpathForChrome =
      "//*[@id='damages-switch-label']/parent::label/div/div";
  private final String damagesSwitchToggleXpathForWin = "//*[@Name='Damaged']/parent::*/Button";
  private final String saveButtonId = "save-button";
  private final String successDialogId = "success-dialog";

  @FindBy(id = containerNumberId)
  @WindowsFindBy(accessibility = containerNumberId)
  private RemoteWebElement containerNumber;

  @FindBy(id = saveButtonId)
  @WindowsFindBy(accessibility = saveButtonId)
  private RemoteWebElement saveButton;

  @FindBy(xpath = damagesSwitchToggleXpathForChrome)
  @WindowsFindBy(xpath = damagesSwitchToggleXpathForWin)
  private RemoteWebElement damagesSwitchToggle;

  @Step("Click save button")
  public NotificationDialog clickSaveButton() {
    $(saveButton).click();

    return new NotificationDialog(this, successDialogId);
  }

  @Step("Set new state for damages")
  public EditDamagePage changeDamages(Damage damage) {
    if (damage != null && damage.damaged) {
      val damagesSize = damage.damages.size();

      if (!getToggleState()) {
        clickDamagesToggle();
      }
      setDamageElement(1, damagesSize >= 1 ? damage.damages.get(0) : null);
      setDamageElement(2, damagesSize >= 2 ? damage.damages.get(1) : null);
      setDamageElement(3, damagesSize >= 3 ? damage.damages.get(2) : null);
      setDamageElement(4, damagesSize >= 4 ? damage.damages.get(3) : null);
    } else {
      if (getToggleState()) {
        clickDamagesToggle();
      }
    }

    return this;
  }

  @Step("Click damage state toggle")
  public EditDamagePage clickDamagesToggle() {
    $(damagesSwitchToggle).click();

    return this;
  }

  @Override
  public boolean isDisplayed() {
    return damagesSwitchToggle.isDisplayed();
  }

  private void setDamageElement(int damageIndex, DamageObject damage) {
    String xpathForFieldMenuButtonsPattern = null;
    String xpathForTogglePattern = null;
    String location = null;
    String type = null;

    switch (EnvironmentController.getDriverType()) {
      case Windows:
        location = damage == null ? "" : damage.location;
        type = damage == null ? "" : damage.type;
        xpathForFieldMenuButtonsPattern = "//*[@AutomationId='%s']/*[@Name='%s']";
        xpathForTogglePattern = "//*[@AutomationId='%s']";
        break;
      case Chrome:
        location = damage == null ? " " : damage.location;
        type = damage == null ? " " : damage.type;
        xpathForFieldMenuButtonsPattern = "//*[@id='%s']/descendant::div[@data-value='%s']";
        xpathForTogglePattern = "//*[@id='%s']";
        break;
      default:
        Assert.fail("Unsupported driver type");
    }

    val xpathForFieldTypeMenuButtons =
        String.format(
            xpathForFieldMenuButtonsPattern,
            String.format(damageTypeFieldMenuPattern, damageIndex),
            type);

    val xpathForFieldLocationMenuButtons =
        String.format(
            xpathForFieldMenuButtonsPattern,
            String.format(damageLocationFieldMenuPattern, damageIndex),
            location);
    val xpathForTypeToggle =
        String.format(xpathForTogglePattern, String.format(damageTypeTogglePattern, damageIndex));
    val xpathForLocationToggle =
        String.format(
            xpathForTogglePattern, String.format(damageLocationTogglePattern, damageIndex));

    $(By.xpath(xpathForLocationToggle)).click();

    $(By.xpath(xpathForFieldLocationMenuButtons)).click();

    $(By.xpath(xpathForTypeToggle)).click();

    $(By.xpath(xpathForFieldTypeMenuButtons)).click();
  }

  /**
   * Gets current state of toggle.
   *
   * @return True if active false otherwise.
   */
  public boolean getToggleState() {
    switch (EnvironmentController.getDriverType()) {
      case Windows:
        return damagesSwitchToggle.getAttribute("Toggle.ToggleState").equals("0");
      case Chrome:
        return damagesSwitchToggle.getAttribute("aria-pressed").equals("false");
      default:
        Assert.fail("Unsupported driver type");
    }

    return false;
  }
}
