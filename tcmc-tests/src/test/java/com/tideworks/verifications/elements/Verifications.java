package com.tideworks.verifications.elements;

import static com.codeborne.selenide.Selenide.$;

import com.tideworks.json.objectmodel.Equipment;
import com.tideworks.pages.elements.dialogs.AboutDialog;
import com.tideworks.pages.elements.dialogs.DialogContainer;
import com.tideworks.pages.elements.dialogs.SelectUtrDialog;
import com.tideworks.utilities.selectors.DriverSwitchBy;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;
import lombok.val;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/** Verification for elements. */
@SuppressWarnings("Convert2Lambda")
public class Verifications {
  @Step("Verifies title of dialog.")
  public static Predicate<DialogContainer> dialogTitle(String title) {
    return new Predicate<DialogContainer>() {
      @Override
      public boolean test(final DialogContainer dialogContainer) {
        return $(dialogContainer.getTitleSelector()).has(Condition.exactText(title));
      }
    };
  }

  @Step("Verifies content of dialog.")
  public static Predicate<DialogContainer> dialogContent(String content) {
    return new Predicate<DialogContainer>() {
      @Override
      public boolean test(final DialogContainer dialogContainer) {
        return $(dialogContainer.getSelector())
            .$(
                DriverSwitchBy.xpath(
                    "//section/p",
                    "//Group[@LocalizedControlType='section']//Text[@LocalizedControlType='text']"))
            .has(Condition.exactText(content));
      }
    };
  }

  @Step("Verify all Utrs are present in grid")
  public static Predicate<SelectUtrDialog> utrIsPresentInDialog(
      final List<Equipment> expectedUtrs) {
    return new Predicate<SelectUtrDialog>() {
      @Override
      public boolean test(final SelectUtrDialog utrDialog) {
        IntStream.range(0, expectedUtrs.size())
            .forEach(
                i -> {
                  val utrId = expectedUtrs.get(i).id;
                  val utrButton =
                      $(DriverSwitchBy.id(String.format(utrDialog.getGridButtonPattern(), utrId)));

                  Assert.assertEquals(utrId, utrButton.getText());
                });

        return true;
      }
    };
  }

  @Step(
      "Search elements from given list in about dialog element Fails if any of"
          + " given element was not")
  public static Predicate<AboutDialog> aboutDialogContent(String... expectedList) {
    return new Predicate<AboutDialog>() {
      @Override
      public boolean test(final AboutDialog aboutDialog) {
        $(aboutDialog.getAboutDialogTitle()).shouldHave(Condition.exactText("About"));
        $(aboutDialog.getCloseButton()).shouldHave(Condition.text("Close"));

        val actualList =
            aboutDialog
                .getInfo()
                .stream()
                .map(RemoteWebElement::getText)
                .collect(Collectors.toList());

        Arrays.stream(expectedList)
            .forEach(
                expectedItem ->
                    Assert.assertTrue(
                        actualList
                            .stream()
                            .anyMatch(actualItem -> actualItem.contains(expectedItem))));

        return true;
      }
    };
  }
}
