package com.tideworks.utilities.configs.mobileclientconfig;

import com.tideworks.json.objectmodel.ApplicationConfiguration;
import com.tideworks.json.parser.JsonParser;

import java.lang.reflect.Field;

/** Model for client's configuration. */
public abstract class MobileClientConfigBase implements MobileClientConfig {

  protected String configPath;
  protected JsonParser parser;

  public MobileClientConfigBase() {
    parser = new JsonParser();
  }

  @Override
  public String getServerAddress() {
    return getCurrentState().serverAddress;
  }

  @Override
  public void setServerAddress(final String serverAddress) {
    setField("serverAddress", serverAddress);
  }

  @Override
  public String getDeviceId() {
    return getCurrentState().deviceId;
  }

  @Override
  public void setDeviceId(final String deviceId) {
    setField("deviceId", deviceId);
  }

  @Override
  public String getClientId() {
    return getCurrentState().clientId;
  }

  @Override
  public void setClientId(final String clientId) {
    setField("clientId", clientId);
  }

  @Override
  public String getSecret() {
    return getCurrentState().clientId;
  }

  @Override
  public void setSecret(final String secret) {
    setField("secret", secret);
  }

  @Override
  public Boolean getLock() {
    return getCurrentState().lock;
  }

  @Override
  public void setLock(final Boolean lock) {
    setField("lock", lock);
  }

  @Override
  public ApplicationConfiguration getConfig() {
    return getCurrentState();
  }

  @Override
  public void rewriteConfig(ApplicationConfiguration config) {
    applyState(config);
  }

  private <T> void setField(String fieldName, T value) {
    try {
      Field field = ApplicationConfiguration.class.getField(fieldName);
      assert field.getType() == value.getClass();
      ApplicationConfiguration config = getCurrentState();
      field.set(config, value);
      applyState(config);
    } catch (NoSuchFieldException | IllegalAccessException exc) {
      throw new RuntimeException(exc);
    }
  }

  /**
   * Gets current state of application.
   *
   * @return Current application configuration.
   */
  protected abstract ApplicationConfiguration getCurrentState();

  /**
   * Applies state to configuration.
   *
   * @param config Configuration to apply.
   */
  protected abstract void applyState(ApplicationConfiguration config);
}
