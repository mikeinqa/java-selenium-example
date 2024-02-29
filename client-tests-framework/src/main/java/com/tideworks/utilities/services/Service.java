package com.tideworks.utilities.services;

/** Service definition. */
public interface Service {

  /** Starts service. */
  void startService();

  /** Stops service. */
  void stopService();

  /** Reset service to initial state. */
  void resetService();

  /**
   * Get current service address.
   *
   * @return service address as String
   */
  String getServiceAddress();
}
