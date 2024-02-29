package com.tideworks.che;

import static com.tideworks.json.objectmodel.ColumnNames.containerNo;
import static com.tideworks.json.objectmodel.ColumnNames.from;
import static com.tideworks.json.objectmodel.ColumnNames.origin;
import static com.tideworks.json.objectmodel.ColumnNames.plan;
import static com.tideworks.json.objectmodel.ColumnNames.size;
import static com.tideworks.json.objectmodel.ColumnNames.to;
import static com.tideworks.verifications.che.Verifications.worklistContainerFromLocation;
import static com.tideworks.verifications.che.Verifications.worklistContainerOriginLocation;
import static com.tideworks.verifications.che.Verifications.worklistContainerPlanLocation;
import static com.tideworks.verifications.che.Verifications.worklistContainerToLocation;

import com.tideworks.base.CheBase;
import com.tideworks.json.objectmodel.ContainerKind;
import com.tideworks.json.objectmodel.ContainerPlan;
import com.tideworks.json.objectmodel.EquipmentLocationKind;
import com.tideworks.json.objectmodel.GridColumn;
import com.tideworks.json.objectmodel.IntermodalUnit;
import com.tideworks.json.objectmodel.MoveSegmentStatus;
import com.tideworks.json.objectmodel.MoveStatus;
import com.tideworks.json.objectmodel.locations.EquipmentLocation;
import com.tideworks.json.objectmodel.locations.GateLocation;
import com.tideworks.json.objectmodel.locations.LocationType;
import com.tideworks.json.objectmodel.locations.RailLocation;
import com.tideworks.json.objectmodel.locations.RailLocation.RailTier;
import com.tideworks.json.objectmodel.locations.VesselLocation;
import com.tideworks.json.objectmodel.locations.YardLocation;

import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import lombok.var;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;

/** Che location formatting tests. */
public class CheLocationFormattingTests extends CheBase {
  YardLocation fromYardLocation = new YardLocation();
  YardLocation toYardLocation = new YardLocation();
  EquipmentLocation truckLocation = new EquipmentLocation();

  ContainerPlan c1;
  ContainerPlan c2;
  ContainerPlan c3;

  /**
   * Location type data provider.
   *
   * @return set of parametrized data
   */
  @DataProvider(name = "Location type provider")
  public static Object[][] locationType() {
    return new Object[][] {{LocationType.vessel, "8183439"}, {LocationType.rail, "8183441"}};
  }

  @BeforeClass
  public void setupSuitData() {

    getExtendedProfile(
        Arrays.asList(
            new GridColumn(containerNo),
            new GridColumn(size),
            new GridColumn(from),
            new GridColumn(to),
            new GridColumn(origin),
            new GridColumn(plan)));

    fromYardLocation.type = LocationType.yard;
    fromYardLocation.block = "North";
    fromYardLocation.row = "002";
    fromYardLocation.stack = "A";
    fromYardLocation.tier = 1;

    toYardLocation.type = LocationType.yard;
    toYardLocation.block = "EAST";
    toYardLocation.row = "002";
    toYardLocation.stack = "A";
    toYardLocation.tier = 1;

    truckLocation.equipmentId = truck1;
    truckLocation.kind = EquipmentLocationKind.truck;
    truckLocation.type = LocationType.equipment;
  }

  @Test(dataProvider = "Location type provider")
  @Issue("TCL-15897")
  @Issue("TCL-15897")
  @TmsLink("8183439")
  @TmsLink("8183441")
  public void locationFormattingMovesTest(LocationType locationType, String testCaseNumber) {
    switch (locationType) {
      case vessel:
        setupVesselTestData();
        break;
      case rail:
        setupRailTestData();
        break;
      default:
        break;
    }
    getWorklistPage()
        .verify(
            worklistContainerFromLocation(
                c1.intermodalUnit.id, c1.getFromSegment().from.toString()))
        .verify(
            worklistContainerToLocation(c2.intermodalUnit.id, c2.getFromSegment().to.toString()))
        .verify(worklistContainerPlanLocation(c2.intermodalUnit.id, c2.move.to.toString()))
        .verify(worklistContainerOriginLocation(c1.intermodalUnit.id, c1.move.from.toString()));
  }

  @Test
  @Issue("TCL-15897")
  @TmsLink("8183440")
  public void locationFormattingSupportGateMovesTest() {
    setupGateTestData();

    getWorklistPage()
        .verify(
            worklistContainerFromLocation(c1.intermodalUnit.id, c1.segments.get(0).from.toString()))
        .verify(worklistContainerToLocation(c2.intermodalUnit.id, c2.segments.get(0).to.toString()))
        .verify(
            worklistContainerFromLocation(c3.intermodalUnit.id, c3.segments.get(0).from.toString()))
        .verify(worklistContainerOriginLocation(c3.intermodalUnit.id, c3.move.from.toString()))
        .verify(worklistContainerPlanLocation(c2.intermodalUnit.id, c2.move.to.toString()))
        .verify(worklistContainerOriginLocation(c1.intermodalUnit.id, c1.move.from.toString()));
  }

  void setupRailTestData() {
    var fromRailLocation = new RailLocation();
    fromRailLocation.type = LocationType.rail;
    fromRailLocation.railcar = "RAILCAR123456";
    fromRailLocation.well = "A";
    fromRailLocation.tier = RailTier.B;
    fromRailLocation.stack = "1";

    var toRailLocation = new RailLocation();
    toRailLocation.type = LocationType.rail;
    toRailLocation.railcar = "BNSF72536880";
    toRailLocation.well = "F";
    toRailLocation.tier = RailTier.T;
    toRailLocation.stack = "2";

    var truckLocation = new EquipmentLocation();
    truckLocation.equipmentId = truck1;
    truckLocation.kind = EquipmentLocationKind.truck;
    truckLocation.type = LocationType.equipment;

    IntermodalUnit containerFromRail = new IntermodalUnit();
    containerFromRail.id = "1000001";
    containerFromRail.kind = ContainerKind.container;
    containerFromRail.location = fromRailLocation;
    containerFromRail.sizeType = "20NC";

    IntermodalUnit containerToRail = new IntermodalUnit();
    containerToRail.id = "1000002";
    containerToRail.kind = ContainerKind.container;
    containerToRail.location = truckLocation;
    containerToRail.sizeType = "40HC";

    c1 =
        terminalController.planContainer(
            containerFromRail,
            toYardLocation,
            MoveStatus.Active,
            truckLocation,
            MoveSegmentStatus.Active,
            true);
    c2 =
        terminalController.planContainer(
            containerToRail,
            toRailLocation,
            MoveStatus.Active,
            toRailLocation,
            MoveSegmentStatus.Active,
            false);

    stubTestData(c2);
  }

  void setupVesselTestData() {
    var fromVesselLocation = new VesselLocation();
    fromVesselLocation.vessel = "vessel1";
    fromVesselLocation.bay = "17(16)";
    fromVesselLocation.stack = "01";
    fromVesselLocation.tier = "082";

    var toVesselLocation = new VesselLocation();
    toVesselLocation.vessel = "vessel2";
    toVesselLocation.bay = "01(02)";
    toVesselLocation.stack = "12";
    toVesselLocation.tier = "092";

    IntermodalUnit containerFromVessel = new IntermodalUnit();
    containerFromVessel.id = "1000001";
    containerFromVessel.kind = ContainerKind.container;
    containerFromVessel.location = fromVesselLocation;
    containerFromVessel.sizeType = "20NC";

    IntermodalUnit containerToVessel = new IntermodalUnit();
    containerToVessel.id = "1000002";
    containerToVessel.kind = ContainerKind.container;
    containerToVessel.location = truckLocation;
    containerToVessel.sizeType = "40HC";

    c1 =
        terminalController.planContainer(
            containerFromVessel,
            toYardLocation,
            MoveStatus.Active,
            truckLocation,
            MoveSegmentStatus.Active,
            true);
    c2 =
        terminalController.planContainer(
            containerToVessel,
            toVesselLocation,
            MoveStatus.Active,
            toVesselLocation,
            MoveSegmentStatus.Active,
            false);

    stubTestData(c2);
  }

  void setupGateTestData() {
    var fromGateLocation = new GateLocation();
    fromGateLocation.truckLicense = "t1";

    var toGateLocation = new GateLocation();
    toGateLocation.truckLicense = "t2";

    IntermodalUnit containerFromGate = new IntermodalUnit();
    containerFromGate.id = "1000001";
    containerFromGate.kind = ContainerKind.container;
    containerFromGate.location = fromGateLocation;
    containerFromGate.sizeType = "20NC";

    IntermodalUnit containerToGate = new IntermodalUnit();
    containerToGate.id = "1000002";
    containerToGate.kind = ContainerKind.container;
    containerToGate.location = truckLocation;
    containerToGate.sizeType = "40HC";

    IntermodalUnit containerFromNoneGate = new IntermodalUnit();
    containerFromNoneGate.id = "1000003";
    containerFromNoneGate.kind = ContainerKind.container;
    containerFromNoneGate.location = new GateLocation();
    containerFromNoneGate.sizeType = "20NC";

    c1 =
        terminalController.planContainer(
            containerFromGate,
            toYardLocation,
            MoveStatus.Active,
            truckLocation,
            MoveSegmentStatus.Active,
            true);
    c2 =
        terminalController.planContainer(
            containerToGate,
            toGateLocation,
            MoveStatus.Active,
            toGateLocation,
            MoveSegmentStatus.Active,
            false);
    c3 =
        terminalController.planContainer(
            containerFromNoneGate,
            toYardLocation,
            MoveStatus.Active,
            truckLocation,
            MoveSegmentStatus.Active,
            true);

    stubTestData(c2);
  }
}
