package com.tideworks.utilities.configs.mobileclientconfig;

import com.tideworks.json.objectmodel.ApplicationConfiguration;

/** Interface for client's configuration. */
public interface MobileClientConfig {

  /**
   * Get current server domain.
   *
   * @return server address.
   */
  String getServerAddress();

  /**
   * Set current server domain.
   *
   * @param serverAddress domain example http://localhost:7000/api.
   */
  void setServerAddress(String serverAddress);

  /**
   * Get device id.
   *
   * @return device id
   */
  String getDeviceId();

  /**
   * Set device id.
   *
   * @param deviceId device id.
   */
  void setDeviceId(String deviceId);

  /**
   * Get user name.
   *
   * @return user name.
   */
  String getClientId();

  /**
   * Set user name.
   *
   * @param clientId user name.
   */
  void setClientId(String clientId);

  /**
   * Get user password.
   *
   * @return user password.
   */
  String getSecret();

  /**
   * Set user password.
   *
   * @param secret user password.
   */
  void setSecret(String secret);

  /**
   * Get configuration is locked.
   *
   * @return is configuration locked?
   */
  Boolean getLock();

  /**
   * Set lock config.
   *
   * @param lock if true configuration will be locked.
   */
  void setLock(Boolean lock);

  /**
   * Get current state config.
   *
   * @return current config state as @see {@link ApplicationConfiguration}
   */
  ApplicationConfiguration getConfig();

  /**
   * Rewrite config.
   *
   * @param config record with config properties.
   */
  void rewriteConfig(ApplicationConfiguration config);
}
