package com.tideworks.pages.che.yardview;

import static com.tideworks.utilities.controllers.enviroment.EnvironmentController.getDriverType;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static java.lang.String.format;

import com.tideworks.json.objectmodel.IntermodalUnit;
import com.tideworks.json.objectmodel.Move;
import com.tideworks.json.objectmodel.SegmentKind;
import com.tideworks.json.objectmodel.locations.YardLocation;
import com.tideworks.utilities.selectors.DriverSwitchBy;
import com.tideworks.utilities.services.driver.DriverTypes;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.val;
import org.openqa.selenium.By;

import java.util.List;

/** Page for testing EndRowView page in Che application. */
public class EndRowViewPage extends YardViewBasePage<EndRowViewPage> {

  @Getter private final String ervContainerPattern = "erv-container-%s";
  private final String ervCellPattern = "erv-cell-%s-%s";
  private final String ervSetAsideCellPattern = "erv-cell-set-aside-%s";

  /**
   * Formats container number.
   *
   * @param container Container to format.
   * @return Container number in Erv cell format.
   */
  public static String formatContainerNo(IntermodalUnit container) {
    return format(
        "%s%s %s",
        container.move != null ? getSegmentKind(container.move) + " " : "",
        container.id.substring(0, 4),
        container.id.substring(4));
  }

  /**
   * Maps move type to segment kind.
   *
   * @param move Move with to location information.
   * @return Segment kind of given move.
   */
  public static SegmentKind getSegmentKind(Move move) {
    switch (move.to.type) {
      case yard:
        return SegmentKind.Y;
      case vessel:
        return SegmentKind.V;
      case rail:
        return SegmentKind.R;
      case gate:
        return SegmentKind.G;
      default:
        return null;
    }
  }

  @Step("Selects container by current location.")
  public EndRowViewPage selectContainer(IntermodalUnit container) {
    val containerLocation = (YardLocation) container.location;

    return selectCell(containerLocation.stack, containerLocation.tier);
  }

  @Step("Select grid cell by given stack and tier.")
  public EndRowViewPage selectCell(String stack, Integer tier) {
    $(getCell(stack, tier)).click();

    return this;
  }

  @Step("Select grid cell by given yard location.")
  public EndRowViewPage selectCell(YardLocation location) {
    return selectCell(location.stack, location.tier);
  }

  @Step("Select empty grid cell.")
  public EndRowViewPage selectEmptyCell() {
    $(getEmptySetAsideCell()).click();

    return this;
  }

  @Step("Select set aside grid cell by given container id.")
  public EndRowViewPage selectSetAsideCell(String containerId) {
    $(getSetAsideCell(containerId)).click();

    return this;
  }

  /**
   * Gets grid cell by given location.
   *
   * @param location Yard location to find.
   * @return Element of grid cell.
   */
  public SelenideElement getCell(YardLocation location) {
    return getCell(location.stack, location.tier);
  }

  /**
   * Gets grid cell by given stack and tier.
   *
   * @param stack Yard stack to find.
   * @param tier Yard tier to find.
   * @return Element of grid cell.
   */
  public SelenideElement getCell(String stack, Integer tier) {
    val cellId = format(ervCellPattern, stack, tier);

    return $(DriverSwitchBy.id(cellId));
  }

  /**
   * Gets empty set aside cell.
   *
   * @return Element of empty set aside cell.
   */
  public SelenideElement getEmptySetAsideCell() {
    return $(DriverSwitchBy.id("erv-cell-empty-set-aside-5"));
  }

  /**
   * Gets set aside cell with given container.
   *
   * @param containerId Number of container to select.
   * @return Element of set aside cell with given container.
   */
  public SelenideElement getSetAsideCell(String containerId) {
    val cellId = format(ervSetAsideCellPattern, containerId);

    return $(DriverSwitchBy.id(cellId));
  }

  /**
   * Gets set aside cells with given container.
   *
   * @param containerId Number of container to select.
   * @return Elements of set aside cells with given container.
   */
  public List<SelenideElement> getSetAsideCells(String containerId) {
    val cellId = format(ervSetAsideCellPattern, containerId);

    return $$(DriverSwitchBy.id(cellId));
  }

  /**
   * Gets grid cell with given container.
   *
   * @param container Container to find.
   * @return Cell with given container
   */
  public SelenideElement getCell(IntermodalUnit container) {
    if (getDriverType() == DriverTypes.Chrome) {
      val containerId = String.format(ervContainerPattern, container.id);

      return $(By.id(containerId));
    } else {
      val containerId = formatContainerNo(container);

      return $(By.xpath(format("//Pane/Button[@Name='%s']", containerId)));
    }
  }

  /**
   * Finds label for stack with given text.
   *
   * @param labelText Labels text to find.
   * @return Element of label.
   */
  public SelenideElement findLabel(String labelText) {
    return $(
        DriverSwitchBy.xpath(
            format("//div/div[text() = '%s']", labelText),
            format("//Pane/Text[@Name='%s']", labelText)));
  }

  @Override
  public boolean isDisplayed() {
    return super.isDisplayed();
  }
}
