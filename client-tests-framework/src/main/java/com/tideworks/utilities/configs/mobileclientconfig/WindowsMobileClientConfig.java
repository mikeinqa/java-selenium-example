package com.tideworks.utilities.configs.mobileclientconfig;

import static com.tideworks.utilities.configs.powershell.PowerShellProvider.getConfigPath;

import com.tideworks.json.objectmodel.ApplicationConfiguration;

import wiremock.org.eclipse.jetty.io.RuntimeIOException;

import java.io.IOException;

/** Model for windows client's configuration. */
public class WindowsMobileClientConfig extends MobileClientConfigBase {

  public WindowsMobileClientConfig() {
    super();
    configPath = getConfigPath();
  }

  @Override
  protected ApplicationConfiguration getCurrentState() {
    try {
      return parser.deserialize(configPath, ApplicationConfiguration.class);
    } catch (IOException exc) {
      throw new RuntimeIOException(exc);
    }
  }

  @Override
  protected void applyState(ApplicationConfiguration config) {
    try {
      parser.serialize(config, configPath);
    } catch (IOException exc) {
      throw new RuntimeIOException(exc);
    }
  }
}
