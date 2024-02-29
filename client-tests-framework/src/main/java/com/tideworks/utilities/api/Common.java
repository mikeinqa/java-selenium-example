package com.tideworks.utilities.api;

/** Common Api for all applications. */
public class Common {

  /**
   * Creates url for profile.
   *
   * @param deviceId Profile's device Id.
   * @return Formatted Url.
   */
  public static String profileUrl(String deviceId) {
    return String.format("/devices/%s/profiles", deviceId);
  }

  /**
   * Creates url for authorization.
   *
   * @return Formatted Url.
   */
  public static String authorizationUrl() {
    return "/auth/token";
  }

  /**
   * Creates url for segment manipulation.
   *
   * @param segmentId Segment Id.
   * @return Formatted Url.
   */
  public static String segmentsUrl(String segmentId) {
    return String.format("/segments/%s/selected-by-equipment", segmentId);
  }
}
