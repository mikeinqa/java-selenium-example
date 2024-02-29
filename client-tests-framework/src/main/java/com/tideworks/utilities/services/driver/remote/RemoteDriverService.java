package com.tideworks.utilities.services.driver.remote;

import static com.tideworks.utilities.configs.appconfig.PropertyNames.DEVICE_ID;
import static com.tideworks.utilities.configs.appconfig.PropertyNames.DOMAIN;
import static com.tideworks.utilities.configs.appconfig.PropertyNames.DRIVER_SERVICE_PORT;
import static com.tideworks.utilities.configs.appconfig.PropertyNames.LONG_TIMEOUT;
import static com.tideworks.utilities.configs.appconfig.PropertyNames.PASSWORD;
import static com.tideworks.utilities.configs.appconfig.PropertyNames.SERVER_PORT;
import static com.tideworks.utilities.configs.appconfig.PropertyNames.USERNAME;
import static com.tideworks.utilities.configs.appconfig.PropertyProvider.getValue;

import com.tideworks.utilities.listeners.LogListener;
import com.tideworks.utilities.services.Service;

import io.appium.java_client.AppiumFluentWait;
import io.appium.java_client.events.EventFiringWebDriverFactory;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.Objects;

/** Base class for remote drivers. */
public abstract class RemoteDriverService implements Service {

  @Getter protected static RemoteWebDriver eventDriver;
  @Getter protected static FluentWait<RemoteWebDriver> webDriverWait;
  protected final String driverServicePort = getValue(DRIVER_SERVICE_PORT);
  protected final int timeOut = Integer.parseInt(getValue(LONG_TIMEOUT));
  protected String userName = getValue(USERNAME);
  protected String userPassword = getValue(PASSWORD);
  protected String domain = getValue(DOMAIN);
  protected String serverPort = getValue(SERVER_PORT);
  protected String deviceId = getValue(DEVICE_ID);
  protected String driverServiceAddress;
  @Getter @Setter protected boolean isAutoLoginEnabled;

  protected RemoteDriverService() {
    driverServiceAddress = getServiceAddress();
  }

  @Override
  public String getServiceAddress() {
    return String.format("http://%s:%s", domain, driverServicePort);
  }

  @Override
  public void stopService() {
    if (!Objects.isNull(eventDriver)) {
      eventDriver.quit();
      eventDriver = null;
    }
  }

  /** Creates remote driver with connection to existing driver session. */
  protected abstract void createRemoteDriver();

  /**
   * Creates wrapper for remote driver.
   *
   * @param remoteWebDriver Driver to wrap.
   * @return Event firing wrapper.
   */
  protected RemoteWebDriver createEventFiringWebDriver(RemoteWebDriver remoteWebDriver) {
    return EventFiringWebDriverFactory.getEventFiringWebDriver(remoteWebDriver, new LogListener());
  }

  /**
   * Creates fluent wait for given driver.
   *
   * @return Web driver wait.
   */
  protected FluentWait<RemoteWebDriver> createWebDriverWait() {
    return new AppiumFluentWait(eventDriver)
        .withTimeout(Duration.ofSeconds(timeOut))
        .ignoring(Exception.class);
  }
}
