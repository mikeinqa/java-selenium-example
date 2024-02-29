package com.tideworks.verifications.vesselclerk;

import static com.tideworks.verifications.elements.Verifications.dialogContent;
import static com.tideworks.verifications.elements.Verifications.dialogTitle;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import com.tideworks.json.objectmodel.Damage;
import com.tideworks.json.objectmodel.DamageObject;
import com.tideworks.json.objectmodel.Oversize;
import com.tideworks.json.objectmodel.Seal;
import com.tideworks.pages.elements.ElementFactory;
import com.tideworks.pages.elements.dialogs.ConfirmationDialog;
import com.tideworks.pages.elements.dialogs.NotificationDialog;
import com.tideworks.pages.elements.dialogs.OperationSucceededDialog;
import com.tideworks.pages.vesselclerk.ContainerSearchPage;
import com.tideworks.pages.vesselclerk.EditDamagePage;
import com.tideworks.pages.vesselclerk.EditOversizePage;
import com.tideworks.pages.vesselclerk.EditSealsPage;
import com.tideworks.pages.vesselclerk.MoveCompletionPage;
import com.tideworks.utilities.controllers.enviroment.EnvironmentController;
import com.tideworks.utilities.selectors.DriverSwitchBy;

import io.qameta.allure.Step;
import lombok.val;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.Assert;

import java.util.function.Predicate;

/** Verifications for vessel clerk tests. */
@SuppressWarnings("Convert2Lambda")
public class Verifications {

  @Step("Verifies that move on hold error dialog displayed on page with appropriate content.")
  public static Predicate<ContainerSearchPage> moveOnHoldError() {
    return new Predicate<ContainerSearchPage>() {
      @Override
      public boolean test(final ContainerSearchPage containerSearchPage) {
        ElementFactory.getElement(
                ConfirmationDialog.class,
                containerSearchPage,
                containerSearchPage.getHeldMoveErrorDialogId())
            .verify(dialogTitle("Error"))
            .verify(dialogContent("Move is on hold"));

        return true;
      }
    };
  }

  @Step("Verifies that load held move dialog displayed on page with appropriate content.")
  public static Predicate<ContainerSearchPage> loadHeldMoveDialog() {
    return new Predicate<ContainerSearchPage>() {
      @Override
      public boolean test(final ContainerSearchPage containerSearchPage) {
        ElementFactory.getElement(
                ConfirmationDialog.class,
                containerSearchPage,
                containerSearchPage.getConfirmLoadHeldMoveId())
            .verify(dialogTitle("Move is on hold."))
            .verify(dialogContent("Continue with loading held move?"));

        return true;
      }
    };
  }

  @Step("Verifies that no move error dialog displayed on page with appropriate content.")
  public static Predicate<ContainerSearchPage> noMovesError() {
    return new Predicate<ContainerSearchPage>() {
      @Override
      public boolean test(final ContainerSearchPage containerSearchPage) {
        ElementFactory.getElement(
                ConfirmationDialog.class,
                containerSearchPage,
                containerSearchPage.getErrorDialogId())
            .verify(dialogTitle("Error"))
            .verify(dialogContent("No move found"));

        return true;
      }
    };
  }

  @Step("Verifies that no valid move error dialog displayed on page with appropriate content.")
  public static Predicate<ContainerSearchPage> noValidMovesError() {
    return new Predicate<ContainerSearchPage>() {
      @Override
      public boolean test(final ContainerSearchPage containerSearchPage) {
        ElementFactory.getElement(
                ConfirmationDialog.class,
                containerSearchPage,
                containerSearchPage.getErrorDialogId())
            .verify(dialogTitle("Error"))
            .verify(dialogContent("No valid move found"));

        return true;
      }
    };
  }

  @Step("Verifies that move selection warning displayed on page.")
  public static Predicate<ContainerSearchPage> moveSelectionWarning() {
    return new Predicate<ContainerSearchPage>() {
      @Override
      public boolean test(final ContainerSearchPage containerSearchPage) {
        new NotificationDialog(
                containerSearchPage, containerSearchPage.getConfirmSelectContainerDialogId())
            .verify(dialogTitle("Warning"))
            .verify(
                dialogContent(
                    "Selected move is not planned for current crane or vessel. Continue?"));

        return true;
      }
    };
  }

  @Step("Verifies that text in container field equal to expected.")
  public static Predicate<MoveCompletionPage> containerNumberTextField(String expectedResult) {
    return new Predicate<MoveCompletionPage>() {
      @Override
      public boolean test(final MoveCompletionPage moveCompletionPage) {
        return $(moveCompletionPage.getContainerNumberTextField()).has(exactText(expectedResult));
      }
    };
  }

  @Step("Verifies that text in next location field equal to expected.")
  public static Predicate<MoveCompletionPage> nextLocationTextField(String expectedResult) {
    return new Predicate<MoveCompletionPage>() {
      @Override
      public boolean test(final MoveCompletionPage moveCompletionPage) {
        switch (EnvironmentController.getDriverType()) {
          case Windows:
            return $(moveCompletionPage.getNextLocationTextField())
                .has(exactText(expectedResult + "\n"));
          case Chrome:
            return $(moveCompletionPage.getNextLocationTextField()).has(value(expectedResult));
          default:
            return false;
        }
      }
    };
  }

  @Step("Verifies that next location text field is disabled.")
  public static Predicate<MoveCompletionPage> nextLocationTextFieldIsDisabled() {
    return new Predicate<MoveCompletionPage>() {
      @Override
      public boolean test(final MoveCompletionPage moveCompletionPage) {
        return $(moveCompletionPage.getNextLocationTextField()).has(disabled);
      }
    };
  }

  @Step("Verifies that chassis text field contains text equal to expected.")
  public static Predicate<MoveCompletionPage> chassisTextField(String expectedResult) {
    return new Predicate<MoveCompletionPage>() {
      @Override
      public boolean test(final MoveCompletionPage moveCompletionPage) {
        switch (EnvironmentController.getDriverType()) {
          case Windows:
            return $(moveCompletionPage.getChassisTextField())
                .has(exactText(expectedResult + "\n"));
          case Chrome:
            return $(moveCompletionPage.getChassisTextField()).has(value(expectedResult));
          default:
            return false;
        }
      }
    };
  }

  @Step("Verifies that chassis text field is empty.")
  public static Predicate<MoveCompletionPage> chassisTextField() {
    return new Predicate<MoveCompletionPage>() {
      @Override
      public boolean test(final MoveCompletionPage moveCompletionPage) {
        return chassisTextField("").test(moveCompletionPage);
      }
    };
  }

  @Step("Verifies that chassis text field is disabled.")
  public static Predicate<MoveCompletionPage> chassisTextFieldIsDisabled() {
    return new Predicate<MoveCompletionPage>() {
      @Override
      public boolean test(final MoveCompletionPage moveCompletionPage) {
        return $(moveCompletionPage.getChassisTextField()).has(disabled);
      }
    };
  }

  @Step("Verifies that chassis text field is absent.")
  public static Predicate<MoveCompletionPage> chassisTextFieldIsAbsent() {
    return new Predicate<MoveCompletionPage>() {
      @Override
      public boolean test(final MoveCompletionPage moveCompletionPage) {

        $(DriverSwitchBy.id(moveCompletionPage.getChassisTextfieldId()))
            .shouldNotBe(visible)
            .shouldNot(exist);
        return true;
      }
    };
  }

  @Step("Verifies that load title is present on page.")
  public static Predicate<MoveCompletionPage> loadTitle() {
    return new Predicate<MoveCompletionPage>() {
      @Override
      public boolean test(final MoveCompletionPage moveCompletionPage) {
        return $(moveCompletionPage.getLoadTitle()).has(visible);
      }
    };
  }

  @Step("Verifies that load discharge title is present on page.")
  public static Predicate<MoveCompletionPage> dischargeTitle() {
    return new Predicate<MoveCompletionPage>() {
      @Override
      public boolean test(final MoveCompletionPage moveCompletionPage) {
        return $(moveCompletionPage.getDischargeTitle()).has(visible);
      }
    };
  }

  @Step("Verifies that backreach toggle is equals to given one.")
  public static Predicate<MoveCompletionPage> completeToBackreachToggleActivationState(
      boolean isActive) {
    return new Predicate<MoveCompletionPage>() {
      @Override
      public boolean test(final MoveCompletionPage moveCompletionPage) {
        switch (EnvironmentController.getDriverType()) {
          case Windows:
            return $(moveCompletionPage.getCompleteToBackreachToggle())
                .$(By.xpath("//Button[@LocalizedControlType='button']"))
                .has(attribute("Toggle.ToggleState", isActive ? "1" : "0"));
          case Chrome:
            return $(moveCompletionPage.getCompleteToBackreachToggle())
                .$(By.xpath("./div/div"))
                .has(attribute("aria-pressed", Boolean.toString(isActive)));
          default:
            return false;
        }
      }
    };
  }

  /**
   * Verifies content of dialog.
   *
   * @return Operation succeeded dialog.
   */
  public static Predicate<OperationSucceededDialog> operationSucceededDialogContent() {
    return new Predicate<OperationSucceededDialog>() {
      @Override
      public boolean test(final OperationSucceededDialog operationSucceededDialog) {
        operationSucceededDialog
            .verify(dialogTitle("Operation succeeded"))
            .verify(dialogContent("Move has been completed"));

        return true;
      }
    };
  }

  @Step("Checks that container search filed contains text which belongs to empty state.")
  public static Predicate<ContainerSearchPage> searchTextFieldIsEmpty() {
    return new Predicate<ContainerSearchPage>() {
      @Override
      public boolean test(final ContainerSearchPage containerSearchPage) {
        switch (EnvironmentController.getDriverType()) {
          case Windows:
            return $(containerSearchPage.getSearchTextField())
                .has(exactText("\nSearch containers"));
          case Chrome:
            return $(containerSearchPage.getSearchTextField()).has(value(""))
                && $(containerSearchPage.getSearchTextField())
                    .has(attribute("placeholder", "Search containers"));
          default:
            return false;
        }
      }
    };
  }

  @Step("Verify damage locations and types, and verify toggle state")
  public static Predicate<EditDamagePage> damageState(Damage damage) {
    return new Predicate<EditDamagePage>() {
      @Override
      public boolean test(final EditDamagePage editDamagePage) {
        return verifyDamagesToggleActivationState(damage.damaged).test(editDamagePage)
            && verifyDamages(damage).test(editDamagePage);
      }
    };
  }

  @Step("Verify damage locations and types, and verify toggle state")
  public static Predicate<EditDamagePage> damageOffState() {
    return new Predicate<EditDamagePage>() {
      @Override
      public boolean test(final EditDamagePage editDamagePage) {
        return verifyDamagesToggleActivationState(false).test(editDamagePage)
            && verifyDamages(null).test(editDamagePage);
      }
    };
  }

  @Step("Verifies that damages toggle is equals to given one.")
  public static Predicate<EditDamagePage> verifyDamagesToggleActivationState(boolean isActive) {
    return new Predicate<EditDamagePage>() {
      @Override
      public boolean test(final EditDamagePage editDamagePage) {
        return editDamagePage.getToggleState() == isActive;
      }
    };
  }

  private static Predicate<EditDamagePage> assertDamageElementText(
      int damageIndex, DamageObject damage) {
    return new Predicate<EditDamagePage>() {
      @Override
      public boolean test(final EditDamagePage editDamagePage) {
        switch (EnvironmentController.getDriverType()) {
          case Windows:
            val damageTypeXpath =
                String.format(
                    "//*[@AutomationId='%s-toggle']/Text",
                    editDamagePage.getDamageTypeTextFieldPattern());
            val damageLocationXpath =
                String.format(
                    "//*[@AutomationId='%s-toggle']/Text",
                    editDamagePage.getDamageLocationTextFieldPattern());

            if (damage == null) {
              $$(By.xpath(String.format(damageTypeXpath, damageIndex))).shouldHaveSize(1);
              $$(By.xpath(String.format(damageLocationXpath, damageIndex))).shouldHaveSize(1);
            } else {
              $(By.xpath(String.format(damageTypeXpath, damageIndex)))
                  .shouldBe(exactText(damage.type));
              $(By.xpath(String.format(damageLocationXpath, damageIndex)))
                  .shouldBe(exactText(damage.location));
            }
            break;
          case Chrome:
            val damageTypeId =
                String.format(editDamagePage.getDamageTypeTextFieldPattern(), damageIndex);
            val damageLocationId =
                String.format(editDamagePage.getDamageLocationTextFieldPattern(), damageIndex);

            if (damage != null) {
              $(By.id(damageTypeId)).shouldBe(enabled).shouldHave(value(damage.type));
              $(By.id(damageLocationId))
                  .shouldBe(enabled)
                  .shouldBe(attribute("value", damage.location));
            } else if (!editDamagePage.getToggleState()) {
              $(By.id(damageTypeId)).shouldBe(disabled);
              $(By.id(damageLocationId)).shouldBe(disabled);
            } else {
              $(By.id(damageTypeId)).shouldBe(enabled).shouldHave(value(""));
              $(By.id(damageLocationId)).shouldBe(enabled).shouldBe(value(""));
            }
            break;
          default:
            Assert.fail("Unsupported driver type");
        }
        return true;
      }
    };
  }

  private static Predicate<EditDamagePage> verifyDamages(Damage damage) {
    return new Predicate<EditDamagePage>() {
      @Override
      public boolean test(final EditDamagePage editDamagePage) {
        int damagesSize = damage == null ? 0 : damage.damages.size();
        return assertDamageElementText(1, damagesSize >= 1 ? damage.damages.get(0) : null)
                .test(editDamagePage)
            && assertDamageElementText(2, damagesSize >= 2 ? damage.damages.get(1) : null)
                .test(editDamagePage)
            && assertDamageElementText(3, damagesSize >= 3 ? damage.damages.get(2) : null)
                .test(editDamagePage)
            && assertDamageElementText(4, damagesSize >= 4 ? damage.damages.get(3) : null)
                .test(editDamagePage);
      }
    };
  }

  @Step("Compare expected oversize and current oversize page")
  public static Predicate<EditOversizePage> oversizeTexts(final Oversize oversize) {
    return new Predicate<EditOversizePage>() {
      @Override
      public boolean test(final EditOversizePage editOversizePage) {
        return $(editOversizePage.getAftTextField()).has(visible)
            && assertOversizeFieldText(editOversizePage.getAftTextField(), oversize.aft)
            && assertOversizeFieldText(editOversizePage.getForwardTextField(), oversize.forward)
            && assertOversizeFieldText(editOversizePage.getHeightTextField(), oversize.height)
            && assertOversizeFieldText(editOversizePage.getLeftTextField(), oversize.left)
            && assertOversizeFieldText(editOversizePage.getRightTextField(), oversize.right);
      }
    };
  }

  private static boolean assertOversizeFieldText(RemoteWebElement field, String expectedText) {
    switch (EnvironmentController.getDriverType()) {
      case Windows:
        return $(field).has(exactText(expectedText + "\n"));
      case Chrome:
        return $(field).has(value(expectedText));
      default:
        return false;
    }
  }

  @Step("Compare expected seals and current seals page")
  public static Predicate<EditSealsPage> sealsTexts(Seal expectedSeal) {
    return new Predicate<EditSealsPage>() {
      @Override
      public boolean test(final EditSealsPage editSealsPage) {
        return assertSealsFieldText(editSealsPage.getSeal1TextField(), expectedSeal.seal1)
            && assertSealsFieldText(editSealsPage.getSeal2TextField(), expectedSeal.seal2)
            && assertSealsFieldText(editSealsPage.getSeal3TextField(), expectedSeal.seal3)
            && assertSealsFieldText(editSealsPage.getSeal4TextField(), expectedSeal.seal4);
      }
    };
  }

  private static boolean assertSealsFieldText(RemoteWebElement field, String expectedText) {
    switch (EnvironmentController.getDriverType()) {
      case Windows:
        return $(field).has(exactText(expectedText + "\n"));
      case Chrome:
        return $(field).has(attribute("value", expectedText));
      default:
        return false;
    }
  }
}
