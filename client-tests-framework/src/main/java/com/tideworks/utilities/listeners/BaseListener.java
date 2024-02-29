package com.tideworks.utilities.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Reporter;

import java.util.ArrayList;
import java.util.List;

class BaseListener {
  private static final Logger LOGGER = LogManager.getLogger(BaseListener.class);
  static final List<String> MESSAGES = new ArrayList<>();

  public static void log(String message) {
    if (!MESSAGES.contains(message)) {
      MESSAGES.add(message);
      LOGGER.info(message);
      Reporter.log(message);
    }
  }
}
