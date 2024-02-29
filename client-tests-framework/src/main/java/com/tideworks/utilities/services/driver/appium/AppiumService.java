package com.tideworks.utilities.services.driver.appium;

import static com.tideworks.utilities.configs.appconfig.PropertyNames.APPIUM_NEW_COMMAND_TIMEOUT;
import static com.tideworks.utilities.configs.appconfig.PropertyProvider.getValue;

import static io.appium.java_client.remote.MobileCapabilityType.NEW_COMMAND_TIMEOUT;

import com.tideworks.utilities.configs.powershell.PowerShellProvider;
import com.tideworks.utilities.services.Service;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import lombok.val;
import org.openqa.selenium.remote.DesiredCapabilities;

/** Appium service wrapper. */
public class AppiumService implements Service {

  private AppiumDriverLocalService appiumDriverLocalService;

  public AppiumService() {
    val builder = new AppiumServiceBuilder();
    builder.withArgument(GeneralServerFlag.LOG_LEVEL, "warn");
    builder.withCapabilities(getDesiredCapabilities());

    appiumDriverLocalService = AppiumDriverLocalService.buildService(builder);
  }

  @Override
  public void startService() {
    try {
      appiumDriverLocalService.start();
    } catch (Throwable exc) {
      PowerShellProvider.killWinAppDriverSession();
      appiumDriverLocalService.start();
    }
  }

  @Override
  public void stopService() {
    if (appiumDriverLocalService != null) {
      appiumDriverLocalService.stop();
    }
  }

  @Override
  public void resetService() {}

  @Override
  public String getServiceAddress() {
    return appiumDriverLocalService.getUrl().toString();
  }

  private DesiredCapabilities getDesiredCapabilities() {
    val desiredCapabilities = new DesiredCapabilities();
    desiredCapabilities.setCapability(
        NEW_COMMAND_TIMEOUT, Integer.parseInt(getValue(APPIUM_NEW_COMMAND_TIMEOUT)));

    return desiredCapabilities;
  }
}
