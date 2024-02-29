package com.tideworks.pages;

import static com.tideworks.utilities.configs.appconfig.PropertyNames.LONG_TIMEOUT;
import static com.tideworks.utilities.configs.appconfig.PropertyNames.TIMEOUT;

import static com.codeborne.selenide.Selenide.$;

import com.tideworks.pages.elements.ElementFactory;
import com.tideworks.pages.elements.RightMenuList;
import com.tideworks.utilities.configs.appconfig.PropertyProvider;
import com.tideworks.utilities.listeners.DriverTypeSkipListener;
import com.tideworks.utilities.services.driver.remote.RemoteDriverService;

import com.codeborne.selenide.Condition;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.WindowsFindBy;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.val;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * Base class to describe application pages.
 *
 * @param <T> Type of page class
 */
public abstract class BasePage<T extends BasePage> {

  private final String rightMenuToggleId = "right_menu-toggle";

  public RemoteWebDriver driver;

  @Getter
  @FindBy(id = rightMenuToggleId)
  @WindowsFindBy(accessibility = rightMenuToggleId)
  protected RemoteWebElement rightMenuToggleButton;

  protected int timeout;
  protected int longTimeout;
  private FluentWait<RemoteWebDriver> wait;

  protected BasePage() {
    driver = RemoteDriverService.getEventDriver();
    wait = RemoteDriverService.getWebDriverWait();

    timeout = Integer.parseInt(PropertyProvider.getValue(TIMEOUT));
    longTimeout = Integer.parseInt(PropertyProvider.getValue(LONG_TIMEOUT));

    PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(timeout)), this);
  }

  @Step("Clicks right menu toggle button.")
  public RightMenuList clickRightMenuToggle() {
    $(rightMenuToggleButton).should(Condition.visible).click();

    return ElementFactory.getElement(RightMenuList.class, this);
  }

  /**
   * Waits until page loaded.
   *
   * @return Awaiting element.
   */
  public T waitUntilPageLoaded() {
    wait.ignoring(NoSuchElementException.class).until(s -> isDisplayed());

    return (T) this;
  }

  /**
   * Indicates if page displayed with all important elements.
   *
   * @return Result of page check.
   */
  public abstract boolean isDisplayed();

  /**
   * Checks if element can be found by any of given locators.
   *
   * @param locators List of locators o verify.
   * @return True if any of elements could be found on page.
   */
  public boolean isAnyElementsDisplayed(By... locators) {
    driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

    val result = !driver.findElements(new ByAll(locators)).isEmpty();

    driver
        .manage()
        .timeouts()
        .implicitlyWait(Integer.parseInt(PropertyProvider.getValue(TIMEOUT)), TimeUnit.SECONDS);

    return result;
  }

  /**
   * Performs verification on current page with given predicate.
   *
   * @param function Predicate to test.
   * @return Current page.
   */
  public T verify(Predicate function) {
    val verification = function.getClass().getEnclosingMethod();

    if (verification != null && DriverTypeSkipListener.isDriverSupported(verification)) {
      Assert.assertTrue(function.test(this));
    }

    return (T) this;
  }
}
