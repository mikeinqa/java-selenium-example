package com.tideworks.utilities.services.driver.remote.windows;

import static com.tideworks.utilities.configs.appconfig.PropertyNames.DEVICE_ID;

import com.tideworks.json.objectmodel.ApplicationConfiguration;
import com.tideworks.utilities.configs.appconfig.PropertyProvider;
import com.tideworks.utilities.configs.mobileclientconfig.ClientConfigFactory;
import com.tideworks.utilities.configs.mobileclientconfig.MobileClientConfig;
import com.tideworks.utilities.configs.powershell.PowerShellProvider;
import com.tideworks.utilities.services.driver.remote.RemoteDriverService;

import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.MobileBy.ByAccessibilityId;
import io.appium.java_client.windows.WindowsDriver;
import lombok.val;
import lombok.var;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/** Service for windows application driver. */
public class RemoteWindowsDriverService extends RemoteDriverService {

  private final String deviceId = PropertyProvider.getValue(DEVICE_ID);
  private MobileClientConfig appConfig;

  public RemoteWindowsDriverService() {
    appConfig = ClientConfigFactory.getConfig();
  }

  @Override
  public void startService() {
    setAutoLogin();
    startSession();
    waitUntilSplashScreenDisappear();
  }

  @Override
  public void resetService() {
    val windowsDriver = getWindowsDriver();

    if (windowsDriver != null) {
      getWindowsDriver().closeApp();
    }
  }

  private void startSession() {
    if (eventDriver == null) {
      createRemoteDriver();
    } else {
      getWindowsDriver().launchApp();
    }
  }

  private void setAutoLogin() {
    if (isAutoLoginEnabled) {
      appConfig.rewriteConfig(getDefaultConfig());
    } else {
      val config = new ApplicationConfiguration();
      config.deviceId = deviceId;

      appConfig.rewriteConfig(config);
    }
  }

  private WindowsDriver getWindowsDriver() {
    return (WindowsDriver) eventDriver;
  }

  /** Founds existing application and create session to it. */
  public void attachToExistingApp() {
    try {
      val driver =
          new WindowsDriver<RemoteWebElement>(
              new URL(driverServiceAddress), getDesiredCapabilitiesForLaunch("Root"));
      val window = driver.findElementByName("TC Mobile Client");
      var windowHandle = window.getAttribute("NativeWindowHandle");
      driver.quit();

      createEventFiringWebDriver(
          new WindowsDriver<RemoteWebElement>(
              new URL(driverServiceAddress),
              getDesiredCapabilitiesForAttach(Integer.toHexString(Integer.valueOf(windowHandle)))));
    } catch (MalformedURLException exception) {
      Assert.fail(exception.getMessage());
      exception.printStackTrace();
    }
  }

  @Override
  protected void createRemoteDriver() {
    try {
      eventDriver =
          createEventFiringWebDriver(
              new WindowsDriver<RemoteWebElement>(
                  new URL(driverServiceAddress),
                  getDesiredCapabilitiesForLaunch(PowerShellProvider.getAumidTcmc())));
      eventDriver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
      eventDriver.manage().window().maximize();

      WebDriverRunner.setWebDriver(eventDriver);

      webDriverWait = createWebDriverWait();

    } catch (MalformedURLException exception) {
      Assert.fail(exception.getMessage());
      exception.printStackTrace();
    }
  }

  @Override
  public String getServiceAddress() {
    return super.getServiceAddress() + "/wd/hub";
  }

  private void waitUntilSplashScreenDisappear() {
    webDriverWait.until(
        ExpectedConditions.invisibilityOfElementLocated(
            new ByAccessibilityId("extendedSplashProgress")));
    webDriverWait.until(
        ExpectedConditions.invisibilityOfElementLocated(
            new ByAccessibilityId("extendedSplashImage")));
  }

  private ApplicationConfiguration getDefaultConfig() {
    ApplicationConfiguration config = new ApplicationConfiguration();
    config.serverAddress = domain + ":" + serverPort;
    config.clientId = userName;
    config.secret = userPassword;
    config.deviceId = deviceId;

    return config;
  }

  private DesiredCapabilities getDesiredCapabilitiesForLaunch(String appName) {
    DesiredCapabilities capabilities = getDefaultDesiredCapabilities();
    capabilities.setCapability("app", appName);
    return capabilities;
  }

  private DesiredCapabilities getDesiredCapabilitiesForAttach(String appTopLevelWindowName) {
    DesiredCapabilities capabilities = getDefaultDesiredCapabilities();
    capabilities.setCapability("appTopLevelWindow", appTopLevelWindowName);
    return capabilities;
  }

  private DesiredCapabilities getDefaultDesiredCapabilities() {
    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setCapability("platformName", "Windows");
    capabilities.setCapability("deviceName", "WindowsPC");
    return capabilities;
  }
}
