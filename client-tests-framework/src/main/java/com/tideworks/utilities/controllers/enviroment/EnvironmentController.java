package com.tideworks.utilities.controllers.enviroment;

import static com.tideworks.utilities.controllers.enviroment.EnvironmentVariableNames.CHROME_DRIVER_PATH;
import static com.tideworks.utilities.controllers.enviroment.EnvironmentVariableNames.CHROME_HEADLESS;
import static com.tideworks.utilities.controllers.enviroment.EnvironmentVariableNames.REMOTE_DOMAIN;
import static com.tideworks.utilities.controllers.enviroment.EnvironmentVariableNames.TARGET_TEST_PLATFORM;
import static com.tideworks.utilities.controllers.enviroment.EnvironmentVariableNames.WINDOWS_10;
import static com.tideworks.utilities.controllers.enviroment.EnvironmentVariableNames.WINDOWS_7;
import static com.tideworks.utilities.controllers.enviroment.EnvironmentVariableNames.WINDOWS_SERVER_08_R2;

import static org.apache.commons.lang3.SystemUtils.OS_NAME;
import static org.apache.commons.lang3.SystemUtils.USER_HOME;

import com.tideworks.utilities.configs.appconfig.PropertyProvider;
import com.tideworks.utilities.services.driver.DriverTypes;

import lombok.val;
import lombok.var;
import org.testng.Assert;

import java.io.File;

/** Controller for interaction with systems environment variables. */
public class EnvironmentController {

  /**
   * Gets driver type depends on parameter given in build.
   *
   * @return Type of driver which is used if present, default value from config file otherwise.
   */
  public static DriverTypes getDriverType() {
    var targetPlatform = System.getenv(TARGET_TEST_PLATFORM);

    if (targetPlatform == null) {
      targetPlatform = PropertyProvider.getValue(TARGET_TEST_PLATFORM);
    }

    switch (targetPlatform) {
      case "chrome":
        return DriverTypes.Chrome;
      case "windows":
        return DriverTypes.Windows;
      default:
        Assert.fail("Unsupported test platform.");
        return null;
    }
  }

  public static String getUserHomeDirectory() {
    return USER_HOME;
  }

  /**
   * Indicates if tests should run with --headless move on.
   *
   * @return False if value not presented or false, true otherwise.
   */
  public static boolean isChromeHeadless() {
    val isChromeHeadless = System.getenv(CHROME_HEADLESS);
    return Boolean.parseBoolean(isChromeHeadless);
  }

  /**
   * Gets adders of remote domain for tests run if present, default value from config file
   * otherwise.
   *
   * @return Domains name
   */
  public static String getRemoteDomain() {
    val remoteDomain = System.getenv(REMOTE_DOMAIN);

    return remoteDomain != null ? remoteDomain : PropertyProvider.getValue(REMOTE_DOMAIN);
  }

  /**
   * Gets current operation system name.
   *
   * @return OS name.
   */
  public static OsTypes getOsName() {
    switch (OS_NAME) {
      case WINDOWS_10:
        return OsTypes.Windows10;
      case WINDOWS_7:
        return OsTypes.Windows7;
      case WINDOWS_SERVER_08_R2:
        return OsTypes.WindowsServer08R2;
      default:
        throw new NullPointerException(String.format("Os %s is not supported.", OS_NAME));
    }
  }

  /**
   * Gets file for chrome driver, located at given by environment variable path, or in case if
   * driver does not exist assume that default location is user home directory with Npm package
   * installed.
   *
   * @return Chrome driver file.
   */
  public static File getChromeDriver() {
    val chromeDriverPath = System.getenv(CHROME_DRIVER_PATH);

    if (chromeDriverPath != null) {
      var chromeDriver = new File(chromeDriverPath);

      if (chromeDriver.exists() && !chromeDriver.isDirectory() && chromeDriver.canExecute()) {
        return chromeDriver;
      }
    }

    return new File(
        String.format("%s/AppData/Roaming/npm/chromedriver.cmd", getUserHomeDirectory()));
  }
}
