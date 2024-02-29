package com.tideworks.pages.elements;

import com.tideworks.pages.BasePage;
import com.tideworks.utilities.listeners.DriverTypeSkipListener;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.function.Predicate;

/** Base element. */
public abstract class BaseElement {

  public BasePage currentPage;
  @Getter @Setter private By selector;

  protected BaseElement(BasePage basePage) {
    currentPage = basePage;

    PageFactory.initElements(new AppiumFieldDecorator(currentPage.driver), this);
  }

  /**
   * Indicates if element can be found and that it displayed on page.
   *
   * @return True if element exist and displayed, false otherwise.
   */
  public boolean isDisplayed() {
    val element = currentPage.driver.findElements(selector);
    return !element.isEmpty() && element.get(0).isDisplayed();
  }

  /**
   * Performs verification on current page with given predicate.
   *
   * @param function Predicate to test.
   * @param <T> Type of element to return.
   * @return Current page.
   */
  public <T extends BaseElement> T verify(Predicate function) {
    val verification = function.getClass().getEnclosingMethod();

    if (verification != null && DriverTypeSkipListener.isDriverSupported(verification)) {
      Assert.assertTrue(function.test(this));
    }

    return (T) this;
  }
}
