package com.tideworks.utilities.configs.powershell;

import static com.tideworks.utilities.configs.appconfig.PropertyNames.APP_PACKAGE_NAME;
import static com.tideworks.utilities.configs.appconfig.PropertyNames.WIN_CONFIG_PATH;
import static com.tideworks.utilities.configs.appconfig.PropertyProvider.getValue;
import static com.tideworks.utilities.configs.powershell.PropertyNames.NAME;
import static com.tideworks.utilities.configs.powershell.PropertyNames.PACKAGE_FAMILY_NAME;
import static com.tideworks.utilities.configs.powershell.PropertyNames.VERSION;

import com.profesorfalken.jpowershell.PowerShell;

/** Provider for PowerShell commands. */
public class PowerShellProvider {

  public static String getAumidTcmc() {
    return String.format("%s!%s", getProperty(PACKAGE_FAMILY_NAME), getProperty(NAME));
  }

  public static String getVersion() {
    return getProperty(VERSION);
  }

  public static String getPackageFamilyName() {
    return getProperty(PACKAGE_FAMILY_NAME);
  }

  public static String getUserProfile() {
    return executeCommand("$env:USERPROFILE");
  }

  public static String getConfigPath() {
    return String.format(getValue(WIN_CONFIG_PATH), getUserProfile(), getPackageFamilyName());
  }

  /** Tries to kill WinAppDriver process. */
  public static void killWinAppDriverSession() {
    executeCommand("Stop-Process -Name \"WinAppDriver\"");
  }

  private static String getProperty(String name) {
    return executeCommand(
        String.format("(Get-AppxPackage %s).%s", getValue(APP_PACKAGE_NAME), name));
  }

  private static String executeCommand(String command) {
    PowerShell powerShell = PowerShell.openSession();
    powerShell.executeCommand("clear");

    String returnString = powerShell.executeCommand(command).getCommandOutput().trim();

    powerShell.close();

    return returnString;
  }
}
