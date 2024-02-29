package com.tideworks.utilities.controllers.enviroment;

import static com.tideworks.utilities.controllers.enviroment.EnvironmentVariableNames.WINDOWS_10;
import static com.tideworks.utilities.controllers.enviroment.EnvironmentVariableNames.WINDOWS_7;
import static com.tideworks.utilities.controllers.enviroment.EnvironmentVariableNames.WINDOWS_SERVER_08_R2;

/** Types of supported operation systems. */
public enum OsTypes {
  Windows10(WINDOWS_10),
  Windows7(WINDOWS_7),
  WindowsServer08R2(WINDOWS_SERVER_08_R2);

  private final String propertyString;

  OsTypes(final String property) {
    propertyString = property;
  }

  @Override
  public String toString() {
    return this.propertyString;
  }
}
