package com.tideworks.pages.che.yardview;

import static com.tideworks.json.objectmodel.MoveStatus.Active;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.tideworks.json.objectmodel.IntermodalUnit;
import com.tideworks.json.objectmodel.MoveStatus;
import com.tideworks.json.objectmodel.locations.LocationType;
import com.tideworks.utilities.css.CssRgbaColors;
import com.tideworks.utilities.selectors.DriverSwitchBy;

import io.appium.java_client.pagefactory.WindowsFindBy;
import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;

/** Heap view page. */
public class HeapViewPage extends YardViewBasePage<HeapViewPage> {

  @Getter private final String heapViewContainerSearchId = "heap-view-container-search";
  @Getter private final String heapTableRowPattern = "heap-table-row-%s";
  @Getter private final String statusIconPattern = "status-icon-%s";

  @FindBy(id = heapViewContainerSearchId)
  @WindowsFindBy(accessibility = heapViewContainerSearchId)
  private RemoteWebElement heapViewContainerSearch;

  /**
   * Gets container status icon.
   *
   * @param container Container with move and segment.
   * @param isSelected Indicates if container selected by equipment.
   * @return Containers status icon.
   */
  public static Object getStatusIconColor(IntermodalUnit container, boolean isSelected) {
    if (container.move != null && container.move.status == MoveStatus.Held) {
      return CssRgbaColors.Red;
    }

    if (container.segment != null) {
      if (container.segment.from != null
          && (container.segment.from.type == LocationType.equipment
              || container.segment.from.type == LocationType.gate)) {
        if (isSelected) {
          return CssRgbaColors.Aqua;
        }

        return CssRgbaColors.Yellow;
      }

      switch (container.segment.status) {
        case Active:
          if (isSelected) {
            return CssRgbaColors.Aqua;
          }
          return CssRgbaColors.Green;
        case Inactive:
          return CssRgbaColors.LightGray;
        default:
          break;
      }
    }

    return CssRgbaColors.White;
  }

  @Step("Send given container number to container search field.")
  public HeapViewPage searchContainers(String containerNo) {
    $(heapViewContainerSearch).setValue(containerNo);

    return this;
  }

  @Step("Selects container by clicking on item in containers list.")
  public HeapViewPage selectContainer(String containerNo) {
    $(DriverSwitchBy.id(String.format(heapTableRowPattern, containerNo))).shouldBe(visible).click();

    return this;
  }

  @Override
  public boolean isDisplayed() {
    return heapViewContainerSearch.isDisplayed();
  }
}
