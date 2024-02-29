package com.tideworks.utilities.api;

/** Api for Micro-utr application. */
public class MicroUtr {

  /**
   * Creates url for equipment.
   *
   * @param equipmentId Equipment Id to set.
   * @return @return Formatted Url.
   */
  public static String equipmentUrl(String equipmentId) {
    return String.format(
        "/equipment/%s", equipmentId);
  }

  /**
   * Creates url for containers on equipment.
   *
   * @param equipmentId Equipment Id to set.
   * @return @return Formatted Url.
   */
  public static String containersOnEquipmentUrl(String equipmentId) {
    return String.format(
        "/intermodal-units?location.type=equipment&location.equipmentId=%s", equipmentId);
  }

  /**
   * Creates url for assigned segments.
   *
   * @param equipmentId Equipment Id to set.
   * @return @return Formatted Url.
   */
  public static String assignedSegmentsUrl(String equipmentId) {
    return String.format(
        "/segments?assignedEquipment=%s&status=in,Active,Selected&_expand=intermodalUnit,move"
            + "&_sort=sequence",
        equipmentId);
  }
}
