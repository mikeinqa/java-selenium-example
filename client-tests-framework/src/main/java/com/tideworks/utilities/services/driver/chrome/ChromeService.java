package com.tideworks.utilities.services.driver.chrome;

import static com.tideworks.utilities.configs.appconfig.PropertyNames.DRIVER_SERVICE_PORT;

import com.tideworks.utilities.configs.appconfig.PropertyProvider;
import com.tideworks.utilities.controllers.enviroment.EnvironmentController;
import com.tideworks.utilities.services.Service;

import org.openqa.selenium.chrome.ChromeDriverService;
import org.testng.Assert;

import java.io.IOException;

/** Appium service wrapper. */
public class ChromeService implements Service {

  private final String driverServicePort = PropertyProvider.getValue(DRIVER_SERVICE_PORT);
  private ChromeDriverService chromeDriverService;

  public ChromeService() {
    chromeDriverService =
        new ChromeDriverService.Builder()
            .usingDriverExecutable(EnvironmentController.getChromeDriver())
            .usingPort(Integer.parseInt(driverServicePort))
            .build();
  }

  @Override
  public void startService() {
    try {
      chromeDriverService.start();
    } catch (IOException exception) {
      exception.printStackTrace();
      Assert.fail("Failed to start Chrome driver");
    }
  }

  @Override
  public void stopService() {
    if (chromeDriverService != null) {
      chromeDriverService.stop();
    }
  }

  @Override
  public void resetService() {}

  @Override
  public String getServiceAddress() {
    return chromeDriverService.getUrl().toString();
  }
}
