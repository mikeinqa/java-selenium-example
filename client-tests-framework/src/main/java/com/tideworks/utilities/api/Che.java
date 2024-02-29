package com.tideworks.utilities.api;

/** Api for che application. */
public class Che {

  /**
   * Creates assign to zone url for given zone Id.
   *
   * @param zoneId Zone Id.
   * @return Formatted Url.
   */
  public static String assignToZoneUrl(String zoneId) {
    return String.format("/move-lists/%s/assigned-equipment", zoneId);
  }

  /**
   * Creates url for blocks selection request.
   *
   * @return Formatted Url.
   */
  public static String getBlocksUrl() {
    return "/blocks?_sort=id&_order=asc&_embed=rows";
  }

  /**
   * Creates url for container location update request.
   *
   * @param containerNo Container number.
   * @return Formatted Url.
   */
  public static String updateContainerLocationUrl(String containerNo) {
    return String.format("/intermodal-units/%s/location", containerNo);
  }

  /**
   * Creates url to selection containers in row.
   *
   * @param block Block name to select.
   * @param row Row name to select.
   * @return Formatted Url.
   */
  public static String containersInRowUrl(String block, String row) {
    if (!row.isEmpty()) {
      row = String.format("&location.row=%s", row);
    }
    return String.format(
        "/intermodal-units?_embed=segments,moves&location.type=yard&location.block=%s%s",
        block, row);
  }

  /**
   * Creates url to selection containers in row.
   *
   * @param block Block name to select.
   * @return Formatted Url.
   */
  public static String containersInRowUrl(String block) {
    return containersInRowUrl(block, "");
  }

  /**
   * Creates url to selection containers to row.
   *
   * @param block Block name to select.
   * @param row Row name to select.
   * @return Formatted Url.
   */
  public static String containersToRowUrl(String block, String row) {
    if (!row.isEmpty()) {
      row = String.format("&to.row=%s", row);
    }
    return String.format(
        "/segments?_expand=intermodalUnit,move&to.type=yard&to.block=%s%s"
            + "&status=in,Active,Selected",
        block, row);
  }

  /**
   * Creates url to selection containers to row.
   *
   * @param block Block name to select.
   * @return Formatted Url.
   */
  public static String containersToRowUrl(String block) {
    return containersToRowUrl(block, "");
  }

  /**
   * Creates url for move segment selection.
   *
   * @param containerId Associated container.
   * @return Formatted Url.
   */
  public static String getSelectedContainerUrl(String containerId) {
    return String.format("/intermodal-units?_embed=segments,moves&id=%s", containerId);
  }

  /**
   * Creates url for Utr equipments selection.
   *
   * @return Formatted Url.
   */
  public static String getUtrsUrl() {
    return "/equipment?kind=truck&held=false&enabled=true";
  }

  /**
   * Creates url for selection all segments for equipment.
   *
   * @param equipmentId Equipment's Id
   * @return Formatted Url.
   */
  public static String getWorklistUrl(String equipmentId) {
    return String.format(
        "/segments?_expand=intermodalUnit,move&assignedEquipment=contains,%s&status=in,Active"
            + ",Selected",
        equipmentId);
  }

  /**
   * Creates url for zone selection.
   *
   * @return Formatted Url.
   */
  public static String getZonesUrl() {
    return "/move-lists?selfAssign=true";
  }

  /**
   * Creates url for operations selection.
   *
   * @return Formatted Url.
   */
  public static String getOperationsUrl() {
    return "/operations?isActive=true";
  }

  /**
   * Creates url for assign to vessel operation request.
   *
   * @param vesselOperationId Operation id for assign.
   * @return Formatted Url.
   */
  public static String assignToVesselOperation(String vesselOperationId) {
    return String.format("/operations/%s/assigned-equipment", vesselOperationId);
  }
}
