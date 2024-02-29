package com.tideworks.verifications.microutr;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import com.tideworks.pages.microutr.MainPage;

import com.codeborne.selenide.CollectionCondition;
import io.qameta.allure.Step;

import java.util.function.Predicate;

/** Verification fot microutr tests. */
@SuppressWarnings("Convert2Lambda")
public class Verifications {
  private static final String PICKUP = "Pickup";
  private static final String TWIN_DELIVER = "Twin Deliver";
  private static final String TWIN_PICKUP = "Twin Pickup";
  private static final String DELIVER = "Deliver";

  @Step("Verifies that currently displayed title is Deliver.")
  public static Predicate<MainPage> deliverTitleToolBar() {
    return new Predicate<MainPage>() {
      @Override
      public boolean test(final MainPage mainPage) {
        return $(mainPage.getHeaderTitle()).has(exactText(DELIVER));
      }
    };
  }

  @Step("Verifies content of held label.")
  public static Predicate<MainPage> heldLabel() {
    return new Predicate<MainPage>() {
      @Override
      public boolean test(final MainPage mainPage) {
        return $(mainPage.getHeldLabelText()).has(exactText("Equipment on Hold..."));
      }
    };
  }

  @Step("Verifies content of next location label.")
  public static Predicate<MainPage> nextLocationLabel(String expectedResult) {
    return new Predicate<MainPage>() {
      @Override
      public boolean test(final MainPage mainPage) {
        return $(mainPage.getNextLocationLabelText()).has(exactText(expectedResult));
      }
    };
  }

  @Step("Verifies that currently there are no title on page.")
  public static Predicate<MainPage> noTitleToolBar() {
    return new Predicate<MainPage>() {
      @Override
      public boolean test(final MainPage mainPage) {
        $$(mainPage.getHeader().findElementsByXPath("./*[child::node()]"))
            .shouldBe(CollectionCondition.size(1));
        return $(mainPage.getRightMenuToggleButton()).has(visible);
      }
    };
  }

  @Step("Verifies that currently displayed title is Pickup.")
  public static Predicate<MainPage> pickupTitleToolBar() {
    return new Predicate<MainPage>() {
      @Override
      public boolean test(final MainPage mainPage) {
        return $(mainPage.getHeaderTitle()).has(exactText(PICKUP));
      }
    };
  }

  @Step("Verifies that currently displayed title is Twin Deliver.")
  public static Predicate<MainPage> twinDeliverTitleToolBar() {
    return new Predicate<MainPage>() {
      @Override
      public boolean test(final MainPage mainPage) {
        return $(mainPage.getHeaderTitle()).has(exactText(TWIN_DELIVER));
      }
    };
  }

  @Step("Verifies that currently displayed title is Twin Pickup.")
  public static Predicate<MainPage> twinPickupTitleToolBar() {
    return new Predicate<MainPage>() {
      @Override
      public boolean test(final MainPage mainPage) {
        return $(mainPage.getHeaderTitle()).has(exactText(TWIN_PICKUP));
      }
    };
  }
}
