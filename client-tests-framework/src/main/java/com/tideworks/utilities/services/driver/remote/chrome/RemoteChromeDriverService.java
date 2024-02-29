package com.tideworks.utilities.services.driver.remote.chrome;

import static com.tideworks.utilities.configs.appconfig.PropertyNames.APPLICATION_PORT;
import static com.tideworks.utilities.configs.appconfig.PropertyNames.DOMAIN;

import static com.codeborne.selenide.Selenide.open;

import com.tideworks.utilities.configs.appconfig.PropertyProvider;
import com.tideworks.utilities.controllers.enviroment.EnvironmentController;
import com.tideworks.utilities.services.driver.remote.RemoteDriverService;

import com.codeborne.selenide.WebDriverRunner;
import lombok.var;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/** Service for windows application driver. */
public class RemoteChromeDriverService extends RemoteDriverService {

  private final String applicationAddress;

  public RemoteChromeDriverService() {
    applicationAddress = formatApplicationAddress();
  }

  @Override
  public void startService() {
    if (eventDriver == null) {
      createRemoteDriver();
    }

    open(applicationAddress);
  }

  @Override
  public void resetService() {}

  @Override
  protected void createRemoteDriver() {
    try {
      eventDriver =
          createEventFiringWebDriver(
              new RemoteWebDriver(new URL(driverServiceAddress), getChromeOptions()));
      eventDriver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
      eventDriver.manage().window().maximize();

      WebDriverRunner.setWebDriver(eventDriver);

      webDriverWait = createWebDriverWait();
    } catch (MalformedURLException exception) {
      Assert.fail(exception.getMessage());
      exception.printStackTrace();
    }
  }

  private String formatApplicationAddress() {
    return "http://" + getApplicationDomain() + ":" + PropertyProvider.getValue(APPLICATION_PORT);
  }

  /**
   * Remote domain has greater priority for tests runs.
   *
   * @return Remote domain if present, local domain otherwise.
   */
  private String getApplicationDomain() {
    var applicationDomain = EnvironmentController.getRemoteDomain();

    return applicationDomain == null ? PropertyProvider.getValue(DOMAIN) : applicationDomain;
  }

  private ChromeOptions getChromeOptions() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--disable-web-security");
    options.setHeadless(EnvironmentController.isChromeHeadless());
    options.addArguments("--window-size=1920,1080");

    return options;
  }
}
