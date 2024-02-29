package com.tideworks.utilities.services;

import com.tideworks.utilities.controllers.enviroment.EnvironmentController;
import com.tideworks.utilities.services.apimockservice.ApiMockService;
import com.tideworks.utilities.services.apimockservice.WindowsApiMockService;
import com.tideworks.utilities.services.driver.appium.AppiumService;
import com.tideworks.utilities.services.driver.chrome.ChromeService;
import com.tideworks.utilities.services.driver.remote.chrome.RemoteChromeDriverService;
import com.tideworks.utilities.services.driver.remote.windows.RemoteWindowsDriverService;

/** Service factory to create services. */
public class ServiceFactory {

  /**
   * Get MockServerWrapper depending on the current operation system.
   *
   * @return Api mock service
   */
  public static ApiMockService createApiMockService() {
    switch (EnvironmentController.getOsName()) {
      case Windows10:
      case Windows7:
      case WindowsServer08R2:
        return new WindowsApiMockService();
      default:
        return null;
    }
  }

  /**
   * Creates windows driver service.
   *
   * @return Windows driver service.
   */
  public static RemoteWindowsDriverService createWindowsDriverService() {
    return new RemoteWindowsDriverService();
  }

  /**
   * Creates appium service.
   *
   * @return Appium driver service.
   */
  public static AppiumService createAppiumService() {
    return new AppiumService();
  }

  /**
   * Creates chrome driver service.
   *
   * @return Chrome driver service.
   */
  public static ChromeService createChromeService() {
    return new ChromeService();
  }

  /**
   * Creates remote chrome driver service.
   *
   * @return Remote connection to chrome driver service.
   */
  public static RemoteChromeDriverService createRemoteChromeDriverService() {
    return new RemoteChromeDriverService();
  }
}
