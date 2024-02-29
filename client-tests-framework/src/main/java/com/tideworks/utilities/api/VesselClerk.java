package com.tideworks.utilities.api;

/** Api for Vessel-clerk application. */
public class VesselClerk {

  /**
   * Creates operation url.
   *
   * @return Url.
   */
  public static String getOperationsUrl() {
    return "/operations";
  }

  /**
   * Creates operation by id url.
   *
   * @param operationId Vessel operation id.
   * @return Formatted Url.
   */
  public static String getOperationByIdUrl(String operationId) {
    return String.format("/operations/%s", operationId);
  }

  /**
   * Creates relocated container url.
   *
   * @param containerId Container id.
   * @return Formatted Url.
   */
  public static String updateUnitLocationUrl(String containerId) {
    return String.format("/intermodal-units/%s/location", containerId);
  }

  /**
   * Creates container search url.
   *
   * @param limit Containers limit to search.
   * @param containerNo Containers number to search.
   * @return Formatted Url.
   */
  public static String searchContainerUrl(int limit, String containerNo) {
    return String.format("/intermodal-units?id=like,%s&_limit=%s", containerNo, limit);
  }

  /**
   * Creates container embed segments and moves url.
   *
   * @param containerId Container number.
   * @return Formatted Url.
   */
  public static String loadMoveUrl(String containerId) {
    return String.format("/intermodal-units?_embed=segments,moves&id=%s", containerId);
  }

  /**
   * Creates activate moves url.
   *
   * @param moveId Move id.
   * @return Formatted Url.
   */
  public static String activateMoveUrl(String moveId) {
    return String.format("/moves/%s/status", moveId);
  }

  /**
   * Creates get preferences url.
   *
   * @param preference Preference name
   * @return Formatted Url.
   */
  public static String getPreferencesUrl(String preference) {
    return String.format("/preferences/%s", preference);
  }

  /**
   * Creates container seals url.
   *
   * @param containerNo Container number to set.
   * @return Formatted Url.
   */
  public static String containerSealsUrl(String containerNo) {
    return String.format("/intermodal-units/%s/seals", containerNo);
  }

  /**
   * Creates container damage url.
   *
   * @param damages Damage to set.
   * @return Formatted Url.
   */
  public static String containerDamageUrl(String damages) {
    return String.format("/intermodal-units/%s/damages", damages);
  }

  /**
   * Creates container oversize url.
   *
   * @param oversize Oversize to set.
   * @return Formatted Url.
   */
  public static String getContainerOversize(String oversize) {
    return String.format("/intermodal-units/%s/oversize", oversize);
  }

  /**
   * Creates container oversize url.
   *
   * @return Url.
   */
  public static String getWheeledBlocksUrl() {
    return "/blocks?kind=Wheeled";
  }
}
