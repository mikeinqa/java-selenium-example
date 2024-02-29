package com.tideworks.base;

import static com.tideworks.utilities.api.Common.segmentsUrl;
import static com.tideworks.utilities.api.VesselClerk.activateMoveUrl;
import static com.tideworks.utilities.api.VesselClerk.containerDamageUrl;
import static com.tideworks.utilities.api.VesselClerk.containerSealsUrl;
import static com.tideworks.utilities.api.VesselClerk.getContainerOversize;
import static com.tideworks.utilities.api.VesselClerk.getOperationByIdUrl;
import static com.tideworks.utilities.api.VesselClerk.getOperationsUrl;
import static com.tideworks.utilities.api.VesselClerk.getPreferencesUrl;
import static com.tideworks.utilities.api.VesselClerk.getWheeledBlocksUrl;
import static com.tideworks.utilities.api.VesselClerk.loadMoveUrl;
import static com.tideworks.utilities.api.VesselClerk.searchContainerUrl;
import static com.tideworks.utilities.api.VesselClerk.updateUnitLocationUrl;
import static com.tideworks.utilities.controllers.enviroment.EnvironmentController.getDriverType;
import static com.tideworks.utilities.services.driver.DriverTypes.Chrome;

import com.tideworks.json.builders.ContainerBuilder;
import com.tideworks.json.objectmodel.Block;
import com.tideworks.json.objectmodel.BlockKind;
import com.tideworks.json.objectmodel.Damage;
import com.tideworks.json.objectmodel.Equipment;
import com.tideworks.json.objectmodel.EquipmentLocationKind;
import com.tideworks.json.objectmodel.IntermodalUnit;
import com.tideworks.json.objectmodel.Oversize;
import com.tideworks.json.objectmodel.Preference;
import com.tideworks.json.objectmodel.ProfileType;
import com.tideworks.json.objectmodel.Seal;
import com.tideworks.json.objectmodel.VesselClerk;
import com.tideworks.json.objectmodel.VesselOperation;
import com.tideworks.pages.PageFactory;
import com.tideworks.pages.vesselclerk.OperationsPage;

import lombok.val;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

/** Base class for Vessel-clerk application tests. */
public class VesselClerkBase extends Base {

  private final String vessel1 = "VESSEL1";
  private final String vessel2 = "VESSEL2";
  private final String crane = "CRANE 1";
  private final String crane2 = "CRANE 2";
  private final String truckNo = "UTR01";
  private final String block = "BLOCK";
  private final String wheeledBlock = "WHEEL";

  protected VesselClerk vesselClerkProfile;

  @BeforeClass
  public void createVesselClerkProfile() {
    vesselClerkProfile = getDefaultVesselClerkProfile();
    remoteDriverService.setAutoLoginEnabled(true);
  }

  @BeforeMethod
  public void setupTerminal() {
    vesselClerkProfile = getDefaultVesselClerkProfile();
    terminalController.createVesselOperation(
        vesselOperationBuilder ->
            vesselOperationBuilder
                .withCrane(crane)
                .withIsActive(true)
                .withVessel(vessel1)
                .withId("1"));
    terminalController.createVesselOperation(
        vesselOperationBuilder ->
            vesselOperationBuilder
                .withCrane(crane2)
                .withIsActive(true)
                .withVessel(vessel2)
                .withId("2"));
    terminalController.createBlock(
        blockBuilder ->
            blockBuilder
                .ofKind(BlockKind.Wheeled)
                .withName(wheeledBlock)
                .withRow("A01", 1, Arrays.asList("A", "B", "C", "D", "E")));
    terminalController.createBlock(
        blockBuilder ->
            blockBuilder
                .ofKind(BlockKind.Grounded)
                .withName(block)
                .withRow("A", 5, Arrays.asList("A", "B", "C", "D", "E"))
                .withRow("B", 5, Arrays.asList("A", "B", "C", "D", "E"))
                .withRow("C", 5, Arrays.asList("A", "B", "C", "D", "E")));
    terminalController.createEquipment(
        new Equipment(truckNo, EquipmentLocationKind.truck, truckNo, false, true));
  }

  @BeforeMethod
  public void setupVesselBaseStub() {
    stubVesselOperationsData(terminal.getVesselOperations());

    stubProfileData(vesselClerkProfile);
    stubDefaultPreference();
    stubContainersData(terminal.getContainers());
    stubSelectSegmentCommonRequest();
    stubUpdateUnitLocationCommonRequest();
    stubTestData(
        terminal
            .getBlocks()
            .stream()
            .filter(b -> b.kind == BlockKind.Wheeled)
            .collect(Collectors.toList()));
  }

  @AfterMethod
  public void resetBuilders() {
    ContainerBuilder.reset();
  }

  /** Stubs preferences for vessel clerk. */
  protected void stubDefaultPreference() {
    val damageCodes = new Preference();
    damageCodes.id = "damageCodes";
    damageCodes.value = "";

    val damageLocations = new Preference();
    damageLocations.id = "damageLocations";
    damageLocations.value = "";

    stubPreference(damageCodes);
    stubPreference(damageLocations);
  }

  /**
   * Stubs preference.
   *
   * @param preference Preference for request
   */
  protected void stubPreference(final Preference preference) {
    apiMockService.setJsonResponseForGet(getPreferencesUrl(preference.id), preference);
  }

  /**
   * Stubs operations.
   *
   * @param operations Operations to stub.
   */
  protected void stubVesselOperationsData(Iterable<VesselOperation> operations) {
    apiMockService.setJsonResponseForGet(getOperationsUrl(), operations);

    operations.forEach(o -> apiMockService.setJsonResponseForGet(getOperationByIdUrl(o.id), o));
  }

  /**
   * Stub update unit location Url.
   *
   * @param containerNo Unit which may be update.
   * @param body Body for match. *
   * @return Request object for verify calls.
   */
  protected Object stubUpdateUnitLocation(String containerNo, String body) {
    return apiMockService.setJsonResponseForPut(
        updateUnitLocationUrl(containerNo),
        apiMockService.response().withStatus(201).build(),
        body);
  }

  /** Stub common response for update unit location. */
  protected void stubUpdateUnitLocationCommonRequest() {
    apiMockService.setJsonResponseForPut(
        updateUnitLocationUrl("(.*)\\"),
        apiMockService.response().withStatus(201).build(),
        null,
        null,
        true);
  }

  /**
   * Stubs vessel move.
   *
   * @param container Move to stub.
   * @return object with stubs requests if they was be stubbed
   */
  protected Object stubTestData(IntermodalUnit container) {

    stubContainersData(terminal.getContainers());

    if (container.moves != null && !container.moves.isEmpty()) {
      apiMockService.setJsonResponseForPut(
          activateMoveUrl(container.moves.get(0).id),
          apiMockService.response().withStatus(201).build());
    }

    return apiMockService.setJsonResponseForGet(
        loadMoveUrl(container.id), Collections.singletonList(container));
  }

  /**
   * Stubs containers.
   *
   * @param containers Containers to stub.
   */
  protected void stubContainersData(Iterable<IntermodalUnit> containers) {
    containers.forEach(
        container ->
            apiMockService.setJsonResponseForGet(
                searchContainerUrl(15, container.id.substring(4)),
                Collections.singletonList(container)));
  }

  /**
   * Stubs select segment request.
   *
   * @param segmentId Segment id.
   * @return select container request mapper.
   */
  protected Object stubSelectSegmentRequest(String segmentId) {
    return apiMockService.setJsonResponseForPut(
        segmentsUrl(segmentId), apiMockService.response().withStatus(201).build());
  }

  /** Stubs select segment common request. */
  private void stubSelectSegmentCommonRequest() {
    apiMockService.setJsonResponseForPut(
        segmentsUrl("(.*)\\"), apiMockService.response().withStatus(200).build(), null, null, true);
  }

  /**
   * Stubs wheeled blocks request.
   *
   * @param blocks Blocks list for stub
   */
  protected void stubTestData(Iterable<Block> blocks) {
    apiMockService.setJsonResponseForGet(getWheeledBlocksUrl(), blocks);
  }

  /**
   * Stubs get seal request.
   *
   * @param containerNo Container number
   * @param seal Seal which will be returned
   */
  protected void stubGetSeals(final String containerNo, final Seal seal) {
    apiMockService.setJsonResponseForGet(containerSealsUrl(containerNo), seal);
  }

  /**
   * Stubs set seal request.
   *
   * @param containerNo Container number
   * @param seal Seal for body matched
   * @return Request object for call verify
   */
  protected Object stubSetSeals(final String containerNo, final Seal seal) {
    return apiMockService.setJsonResponseForPut(
        containerSealsUrl(containerNo),
        apiMockService.response().withStatus(200).build(),
        String.format(
            "{\"seal1\":\"%s\",\"seal2\":\"%s\",\"seal3\":\"%s\",\"seal4\":\"%s\"}",
            seal.seal1, seal.seal2, seal.seal3, seal.seal4),
        null,
        false);
  }

  /**
   * Stubs get oversize request.
   *
   * @param containerNo Container number
   * @param oversize Oversize which will be returned
   */
  protected void stubGetOversize(final String containerNo, final Oversize oversize) {
    apiMockService.setJsonResponseForGet(getContainerOversize(containerNo), oversize);
  }

  /**
   * Stubs set seal request.
   *
   * @param containerNo Container number
   * @param oversize Oversize for body matched
   * @return Request object for call verify
   */
  protected Object stubSetOversize(final String containerNo, final Oversize oversize) {
    return apiMockService.setJsonResponseForPut(
        getContainerOversize(containerNo),
        apiMockService.response().withStatus(200).build(),
        String.format(
            "{\"height\":\"%s\",\"left\":\"%s\",\"right\":\"%s\",\"aft\":\"%s\","
                + "\"forward\":\"%s\"}",
            oversize.height, oversize.left, oversize.right, oversize.aft, oversize.forward),
        null,
        false);
  }

  /**
   * Stub get container damage response.
   *
   * @param containerNo Container number
   * @param damage Damage body response
   */
  protected void stubGetDamage(final String containerNo, final Damage damage) {
    apiMockService.setJsonResponseForGet(containerDamageUrl(containerNo), damage);
  }

  /**
   * Stub set damage for container request.
   *
   * @param containerNo Container number
   * @param damage Damage for body matched
   * @return Request object for call verify
   */
  protected Object stubSetDamage(final String containerNo, final Damage damage) {
    return apiMockService.setJsonResponseForPut(
        containerDamageUrl(containerNo),
        apiMockService.response().withStatus(200).build(),
        damage,
        null,
        false);
  }

  /**
   * Gets Operation page of launched application.
   *
   * @return Operations page
   */
  protected OperationsPage getOperationsPage() {
    launchApp();

    if (getDriverType() == Chrome && remoteDriverService.isAutoLoginEnabled()) {
      getLoginPage()
          .login(vesselClerkProfile, userName, userPassword, apiMockService.getServiceAddress());
    }

    return PageFactory.getPage(OperationsPage.class);
  }

  /**
   * Gets default vessel clerk profile.
   *
   * @return vessel clerk profile.
   */
  protected VesselClerk getDefaultVesselClerkProfile() {
    VesselClerk vesselClerk = new VesselClerk();
    vesselClerk.requireChassis = true;
    vesselClerk.prePopulateUtr = true;
    vesselClerk.deviceId = deviceId;
    vesselClerk.id = 111111;
    vesselClerk.type = ProfileType.vesselclerk;
    return vesselClerk;
  }
}
