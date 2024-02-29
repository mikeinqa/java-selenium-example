package com.tideworks.utilities.configs.mobileclientconfig;

import com.tideworks.utilities.controllers.enviroment.EnvironmentController;

/** Factory for client's configuration. */
public class ClientConfigFactory {
  /**
   * Get MobileClientConfig depending on the current operation system.
   *
   * @return Mobile client config.
   */
  public static MobileClientConfig getConfig() {
    switch (EnvironmentController.getOsName()) {
      case Windows10:
      case Windows7:
      case WindowsServer08R2:
        return new WindowsMobileClientConfig();
      default:
        return null;
    }
  }
}
