package com.tideworks.utilities.listeners;

import static java.lang.String.format;

import io.appium.java_client.events.api.general.AppiumWebDriverEventListener;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;

/** Listener for logging action, performed by application driver. */
public class LogListener extends BaseListener implements AppiumWebDriverEventListener {

  private String logPattern = "EventListener: ";

  @Override
  public void beforeAlertAccept(WebDriver driver) {
    log(logPattern + "Attempt to accept alert");
  }

  @Override
  public void afterAlertAccept(WebDriver driver) {
    log(logPattern + "The alert was accepted\n");
  }

  @Override
  public void afterAlertDismiss(WebDriver driver) {
    log(logPattern + "Attempt to dismiss alert\n");
  }

  @Override
  public void beforeAlertDismiss(WebDriver driver) {
    log(logPattern + "The alert was dismissed");
  }

  @Override
  public void beforeNavigateTo(String url, WebDriver driver) {
    log(logPattern + "Attempt to navigate to " + url);
  }

  @Override
  public void afterNavigateTo(String url, WebDriver driver) {
    log(logPattern + "Navigation to " + url + " was successful\n");
  }

  @Override
  public void beforeNavigateBack(WebDriver driver) {
    log(logPattern + "Attempt to navigate back");
  }

  @Override
  public void afterNavigateBack(WebDriver driver) {
    log(logPattern + "Navigation back was successful\n");
  }

  @Override
  public void beforeNavigateForward(WebDriver driver) {
    log(logPattern + "Attempt to navigate forward");
  }

  @Override
  public void afterNavigateForward(WebDriver driver) {
    log(logPattern + "Navigation forward was successful\n");
  }

  @Override
  public void beforeNavigateRefresh(WebDriver driver) {
    log(logPattern + "Attempt to refresh");
  }

  @Override
  public void afterNavigateRefresh(WebDriver driver) {
    log(logPattern + "The refreshing was successful\n");
  }

  @Override
  public void beforeFindBy(By by, WebElement element, WebDriver driver) {
    log(
        logPattern + "Attempt to find element using "
            + by.toString()
            + ". The root element is "
            + getElementInfo(element));
  }

  @Override
  public void afterFindBy(By by, WebElement element, WebDriver driver) {
    log(logPattern + "The searching element using " + by.toString() + " has been finished.\n");
  }

  @Override
  public void beforeClickOn(WebElement element, WebDriver driver) {
    log(logPattern + "Attempt to click on the element" + getElementInfo(element));
  }

  @Override
  public void afterClickOn(WebElement element, WebDriver driver) {
    log(logPattern + "The element was clicked\n");
  }

  @Override
  public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
    log(
        logPattern + "Attempt to change value the element"
            + getElementInfo(element)
            + "Value to set: "
            + Arrays.toString(keysToSend));
  }

  @Override
  public void beforeChangeValueOf(WebElement element, WebDriver driver) {
    log(logPattern + "Attempt to change value of the element" + getElementInfo(element));
  }

  @Override
  public void afterChangeValueOf(WebElement element, WebDriver driver) {
    log(logPattern + "The value of the element was changed\n");
  }

  @Override
  public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
    log(
        logPattern + "The value of the element was changed to "
            + Arrays.toString(keysToSend)
            + "\n");
  }

  @Override
  public void beforeScript(String script, WebDriver driver) {
    log(logPattern + "Attempt to perform java script: " + script);
  }

  @Override
  public void afterScript(String script, WebDriver driver) {
    log(logPattern + "Java script " + script + " was performed\n");
  }

  @Override
  public void onException(Throwable throwable, WebDriver driver) {
    log(logPattern + "The exception was thrown: " + throwable.getMessage());
  }

  @Override
  public <X> void beforeGetScreenshotAs(OutputType<X> target) {
    log(format(logPattern + "Attempt to take screen shot. Target type is %s", target));
  }

  @Override
  public <X> void afterGetScreenshotAs(OutputType<X> target, X screenshot) {
    log(
        format(
            logPattern + "Screen shot was taken successfully. "
                + "Target type is %s, result is %s\n",
            target, screenshot));
  }

  @Override
  public void beforeSwitchToWindow(String windowName, WebDriver driver) {
    log(format(logPattern + "Attempt to switch to window %s", windowName));
  }

  @Override
  public void afterSwitchToWindow(String windowName, WebDriver driver) {
    log(format(logPattern + "driver is switched to window %s\n", windowName));
  }

  private String getElementInfo(WebElement element) {

    return element == null
        ? "null"
        : String.format(
            "\n\tElement info: "
                + "\n\tFound by: %s"
                + "\n\tTag name: %s"
                + "\n\tText: %s"
                + "\n\tIs displayed: %s"
                + "\n\tIs enabled: %s"
                + "\n\tIs selected: %s",
            element.toString(),
            element.getTagName(),
            element.getText(),
            element.isDisplayed(),
            element.isEnabled(),
            element.isSelected());
  }
}
