package com.tideworks.verifications.che;

import static com.tideworks.pages.che.yardview.EndRowViewPage.formatContainerNo;
import static com.tideworks.pages.che.yardview.HeapViewPage.getStatusIconColor;
import static com.tideworks.utilities.controllers.enviroment.EnvironmentController.getDriverType;

import static com.codeborne.selenide.Condition.cssValue;
import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static java.lang.String.format;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import com.tideworks.json.objectmodel.Equipment;
import com.tideworks.json.objectmodel.IntermodalUnit;
import com.tideworks.json.objectmodel.VesselOperation;
import com.tideworks.json.objectmodel.Zone;
import com.tideworks.json.objectmodel.locations.YardLocation;
import com.tideworks.pages.BasePage;
import com.tideworks.pages.UnexpectedErrorHandlingPage;
import com.tideworks.pages.che.WorklistPage;
import com.tideworks.pages.che.ZoneViewPage;
import com.tideworks.pages.che.yardview.EndRowViewPage;
import com.tideworks.pages.che.yardview.HeapViewPage;
import com.tideworks.pages.che.yardview.YardViewBasePage;
import com.tideworks.pages.elements.ElementFactory;
import com.tideworks.pages.elements.YardViewHeader;
import com.tideworks.utilities.annotations.RunForDriver;
import com.tideworks.utilities.controllers.enviroment.EnvironmentController;
import com.tideworks.utilities.css.CssProperties;
import com.tideworks.utilities.css.CssRgbColors;
import com.tideworks.utilities.css.CssRgbaColors;
import com.tideworks.utilities.selectors.DriverSwitchBy;
import com.tideworks.utilities.services.driver.DriverTypes;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.val;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.Assert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/** Verification for che tests. */
@SuppressWarnings("Convert2Lambda")
public class Verifications {

  @Step("Verifies visibility of block-picker button.")
  public static Predicate<WorklistPage> blockPickerButtonVisibility(boolean expectedState) {
    return new Predicate<WorklistPage>() {
      @Override
      public boolean test(final WorklistPage worklistPage) {
        return $(DriverSwitchBy.id(worklistPage.getBlockPickerButtonId()))
            .has(expectedState ? visible : not(exist));
      }
    };
  }

  @Step("Verify that cell for given container contains content equals to given.")
  public static Predicate<EndRowViewPage> cellContent(IntermodalUnit container) {
    return cellContent((YardLocation) container.location, formatContainerNo(container));
  }

  @Step("Verify that cell for given yard location is empty")
  public static Predicate<EndRowViewPage> cellContentIsBlank(YardLocation location) {
    return new Predicate<EndRowViewPage>() {
      @Override
      public boolean test(final EndRowViewPage endRowViewPage) {
        endRowViewPage.getCell(location.stack, location.tier).shouldBe(exactText(""));

        return true;
      }
    };
  }

  @Step("Verify that cell for given stack and tier contains content equals to given.")
  public static Predicate<EndRowViewPage> cellContent(YardLocation location, String content) {
    return cellContent(location.stack, location.tier, content);
  }

  private static Predicate<EndRowViewPage> cellContent(String stack, Integer tier, String content) {
    return new Predicate<EndRowViewPage>() {
      @Override
      public boolean test(final EndRowViewPage endRowViewPage) {
        return endRowViewPage.getCell(stack, tier).has(exactText(content));
      }
    };
  }

  @Step("Verify cell border color.")
  @RunForDriver(DriverTypes.Chrome)
  public static Predicate<EndRowViewPage> containerBorderColor(
      YardLocation location, CssRgbColors color) {
    return new Predicate<EndRowViewPage>() {
      @Override
      public boolean test(final EndRowViewPage endRowViewPage) {
        return elementBorderColor(endRowViewPage.getCell(location), color, "solid", 2)
            .test(endRowViewPage);
      }
    };
  }

  @Step("Verify cell border is not set color.")
  @RunForDriver(DriverTypes.Chrome)
  public static Predicate<EndRowViewPage> containerBorderColor(YardLocation location) {
    return new Predicate<EndRowViewPage>() {
      @Override
      public boolean test(final EndRowViewPage endRowViewPage) {
        return elementBorderColor(endRowViewPage.getCell(location), CssRgbColors.Black, "none", 0)
            .test(endRowViewPage);
      }
    };
  }

  @Step("Verify container border color.")
  @RunForDriver(DriverTypes.Chrome)
  public static Predicate<HeapViewPage> containerBorderColor(
      IntermodalUnit expectedContainer, CssRgbColors cssRgbColor) {
    return new Predicate<HeapViewPage>() {
      @Override
      public boolean test(final HeapViewPage heapViewPage) {
        val container =
            $(
                DriverSwitchBy.id(
                    String.format(heapViewPage.getHeapTableRowPattern(), expectedContainer.id)));

        return elementCssProperty(container, CssProperties.Border, "3px solid " + cssRgbColor)
            .test(heapViewPage);
      }
    };
  }

  @Step("Verify cell background color")
  @RunForDriver(DriverTypes.Chrome)
  public static Predicate<EndRowViewPage> containerCellBackgroundColor(
      YardLocation location, CssRgbaColors color) {
    return new Predicate<EndRowViewPage>() {
      @Override
      public boolean test(final EndRowViewPage endRowViewPage) {
        return elementCssProperty(endRowViewPage.getCell(location), CssProperties.Background, color)
            .test(endRowViewPage);
      }
    };
  }

  @Step("Verifies that work-list contains containers equals to expected.")
  public static Predicate<WorklistPage> containerData(List<IntermodalUnit> expectedContainers) {
    return new Predicate<WorklistPage>() {
      @Override
      public boolean test(final WorklistPage worklistPage) {
        val actualContainers =
            expectedContainers
                .stream()
                .map(
                    s ->
                        $(
                            DriverSwitchBy.id(
                                String.format(worklistPage.getWorkListTableRowPattern(), s.id))))
                .collect(Collectors.toList());

        assertEquals(actualContainers.size(), expectedContainers.size());
        IntStream.range(0, expectedContainers.size())
            .forEach(
                i -> {
                  val containerFields =
                      actualContainers
                          .get(i)
                          .findElements(DriverSwitchBy.xpath("./*", "*/*"))
                          .stream()
                          .map(WebElement::getText)
                          .collect(Collectors.toList());

                  val expectedContainer =
                      expectedContainers
                          .stream()
                          .filter(s -> s.id.equals(containerFields.get(0)))
                          .findFirst()
                          .orElse(null);

                  assertNotNull(expectedContainer, "Found container was not expected");
                  assertEquals(containerFields.get(0), expectedContainer.id);
                  assertEquals(containerFields.get(1), expectedContainer.sizeType);
                  assertEquals(containerFields.get(2), expectedContainer.location.toString());
                  assertEquals(containerFields.get(3), expectedContainer.segment.to.toString());
                });
        return true;
      }
    };
  }

  @Step("Verify that container displayed in heap view with appropriate status icon.")
  @RunForDriver(DriverTypes.Chrome)
  public static Predicate<HeapViewPage> containerStatusIcon(
      IntermodalUnit container, boolean isSelected) {
    return new Predicate<HeapViewPage>() {
      @Override
      public boolean test(final HeapViewPage heapViewPage) {
        val statusIcon = $(By.id(String.format(heapViewPage.getStatusIconPattern(), container.id)));

        return elementCssProperty(
                statusIcon, CssProperties.Background, getStatusIconColor(container, isSelected))
            .test(heapViewPage);
      }
    };
  }

  @Step("Verify containers list content according to given containers list.")
  public static Predicate<HeapViewPage> containersFields(
      final List<IntermodalUnit> expectedContainers) {
    return new Predicate<HeapViewPage>() {
      @Override
      public boolean test(final HeapViewPage heapViewPage) {
        val actualContainers =
            expectedContainers
                .stream()
                .map(
                    s ->
                        $(
                            DriverSwitchBy.id(
                                String.format(heapViewPage.getHeapTableRowPattern(), s.id))))
                .collect(Collectors.toList());

        IntStream.range(0, expectedContainers.size())
            .forEach(
                i -> {
                  val containerFields =
                      $(actualContainers.get(i)).$$(DriverSwitchBy.xpath("./*", "*/*")).texts();

                  val expectedContainer =
                      expectedContainers
                          .stream()
                          .filter(s -> s.id.equals(containerFields.get(1)))
                          .collect(Collectors.toList())
                          .get(0);

                  assertEquals(containerFields.get(1), expectedContainer.id);
                  assertEquals(containerFields.get(2), expectedContainer.sizeType);

                  if (expectedContainer.segment != null) {
                    assertEquals(containerFields.get(3), expectedContainer.location.toString());
                    assertEquals(containerFields.get(4), expectedContainer.segment.to.toString());
                    assertEquals(containerFields.get(5), expectedContainer.weight);
                  }
                });

        return true;
      }
    };
  }

  @Step("Verify that containers in heap view displayed in expected order.")
  public static Predicate<HeapViewPage> containersOrder(List<String> expectedContainersList) {
    return new Predicate<HeapViewPage>() {
      @Override
      public boolean test(final HeapViewPage heapViewPage) {
        val actualContainersList =
            $$(DriverSwitchBy.xpath("//tr/td[position() = 2]", "//Group/DataItem[position() = 2]"))
                .texts();

        assertEquals(actualContainersList, expectedContainersList);

        return true;
      }
    };
  }

  @Step("Verify that cell for given location contains content equals to given.")
  public static Predicate<EndRowViewPage> displayedContainers(
      List<IntermodalUnit> expectedContainers) {
    return new Predicate<EndRowViewPage>() {
      @Override
      public boolean test(final EndRowViewPage endRowViewPage) {
        expectedContainers.forEach(
            container ->
                Assert.assertEquals(
                    endRowViewPage.getCell(container).getText(),
                    endRowViewPage.getCell((YardLocation) container.location).getText()));

        return true;
      }
    };
  }

  @Step("Verify element border color.")
  private static Predicate<BasePage> elementBorderColor(
      SelenideElement element, CssRgbColors color, String style, int width) {
    return new Predicate<BasePage>() {
      @Override
      public boolean test(final BasePage basePage) {
        return elementCssProperty(
                element, CssProperties.Outline, String.format("%s %s %dpx", color, style, width))
            .test(basePage);
      }
    };
  }

  /**
   * Verify element css property.
   *
   * @param element Element to verify.
   * @param property Css property which will be verified.
   * @param expectedValue Value for equals
   * @return Current page.
   */
  private static Predicate<BasePage> elementCssProperty(
      SelenideElement element, CssProperties property, Object expectedValue) {
    return elementCssProperty(
        (RemoteWebElement) element.getWrappedElement(), property, expectedValue);
  }

  /**
   * Verify element css property.
   *
   * @param element Element to verify.
   * @param property Css property which will be verified.
   * @param expectedValue Value for equals
   * @return Current page.
   */
  @RunForDriver(DriverTypes.Chrome)
  private static Predicate<BasePage> elementCssProperty(
      RemoteWebElement element, CssProperties property, Object expectedValue) {
    return new Predicate<BasePage>() {
      @Override
      public boolean test(final BasePage basePage) {
        return $(element).has(cssValue(property.toString(), expectedValue.toString()));
      }
    };
  }

  @Step("Scroll to searchingItem and verify that it displayed.")
  public static Predicate<EndRowViewPage> elementDisplayedAfterScroll(
      IntermodalUnit searchingItem) {
    return new Predicate<EndRowViewPage>() {
      @Override
      public boolean test(final EndRowViewPage endRowViewPage) {
        val elementToDisplay =
            $(By.id(format(endRowViewPage.getErvContainerPattern(), searchingItem.id)));
        $(elementToDisplay).scrollIntoView(true);

        return $(elementToDisplay).has(visible);
      }
    };
  }

  @Step("Scroll to searchingItem and verify that it displayed.")
  public static Predicate<EndRowViewPage> elementDisplayedAfterScroll(Equipment searchingItem) {
    return new Predicate<EndRowViewPage>() {
      @Override
      public boolean test(final EndRowViewPage endRowViewPage) {
        val elementToDisplay =
            $(By.id(format(endRowViewPage.getTruckSelectionPattern(), searchingItem.equipmentNo)));
        $(elementToDisplay).scrollIntoView(true);

        return $(elementToDisplay).has(visible);
      }
    };
  }

  @Step("Verify that current yard view header displayed according to container selection state.")
  public static Predicate<YardViewBasePage> header(IntermodalUnit container, String blockName) {
    return new Predicate<YardViewBasePage>() {
      @Override
      public boolean test(final YardViewBasePage yardViewBasePage) {
        if (container == null) {
          return header(null, null, null, null, blockName).test(yardViewBasePage);
        }
        if (container.segment == null) {
          return header(
                  container.id, container.weight, container.location.toString(), null, blockName)
              .test(yardViewBasePage);
        }
        return header(
                container.id,
                container.weight,
                container.location.toString(),
                container.segment.to.toString(),
                blockName)
            .test(yardViewBasePage);
      }
    };
  }

  @Step("Verify that current yard view header displayed according to container selection state.")
  public static Predicate<YardViewBasePage> header(
      String expectedContainerId,
      String expectedContainerWeight,
      String expectedFromLocation,
      String expectedToLocation,
      String blockName) {
    return new Predicate<YardViewBasePage>() {
      @Override
      public boolean test(final YardViewBasePage yardViewBasePage) {
        $(DriverSwitchBy.xpath(
                String.format("//header/h2[text()='%s']", blockName),
                "//Pane/Group[@LocalizedControlType='header']/Text"))
            .shouldHave(exactText(blockName));

        val yardViewHeader = ElementFactory.getElement(YardViewHeader.class, yardViewBasePage);

        if (getDriverType() == DriverTypes.Chrome) {
          if (expectedContainerId != null) {
            assertEquals(yardViewHeader.getContainerId().getText(), expectedContainerId);
            assertEquals(
                yardViewHeader.getWeight().getText(), format("WT: %s", expectedContainerWeight));
            assertEquals(
                yardViewHeader.getFromLocation().getText(),
                format("FROM: %s", expectedFromLocation));
            if (expectedToLocation != null) {
              assertEquals(
                  yardViewHeader.getToLocation().getText(), format("TO: %s", expectedToLocation));
            } else {
              assertEquals(yardViewHeader.getToLocation().getText(), "TO:");
            }
          } else {
            assertEquals(yardViewHeader.getContainerId().getText(), "");
            assertEquals(yardViewHeader.getWeight().getText(), "WT:");
            assertEquals(yardViewHeader.getFromLocation().getText(), "FROM:");
            assertEquals(yardViewHeader.getToLocation().getText(), "TO:");
          }
        } else {
          if (expectedContainerId != null) {
            assertEquals(
                yardViewHeader.getChildText(yardViewHeader.getContainerId()), expectedContainerId);
            assertEquals(
                yardViewHeader.getChildText(yardViewHeader.getWeight()),
                format("WT:%s", expectedContainerWeight));
            assertEquals(
                yardViewHeader.getChildText(yardViewHeader.getFromLocation()),
                format("FROM:%s", expectedFromLocation));
            if (expectedToLocation != null) {
              assertEquals(
                  yardViewHeader.getChildText(yardViewHeader.getToLocation()),
                  format("TO:%s", expectedToLocation));
            } else {
              assertEquals(yardViewHeader.getChildText(yardViewHeader.getToLocation()), "TO:");
            }
          } else {
            assertEquals(yardViewHeader.getChildText(yardViewHeader.getContainerId()), "");
            assertEquals(yardViewHeader.getChildText(yardViewHeader.getWeight()), "WT:");
            assertEquals(yardViewHeader.getChildText(yardViewHeader.getFromLocation()), "FROM:");
            assertEquals(yardViewHeader.getChildText(yardViewHeader.getToLocation()), "TO:");
          }
        }

        return true;
      }
    };
  }

  @Step("Verify that label correctly align to associated stack.")
  public static Predicate<EndRowViewPage> labelAlignToStack(String labelName) {
    return new Predicate<EndRowViewPage>() {
      @Override
      public boolean test(final EndRowViewPage endRowViewPage) {
        val label = endRowViewPage.findLabel(labelName).getCoordinates().onPage();
        val stack = endRowViewPage.getCell(labelName, 1).getCoordinates().onPage();

        return stack.y < label.y && Math.abs(stack.x - label.x) < 15;
      }
    };
  }

  @Step(
      "Verifies that the expected list of containers are displayed in the left split list for the"
          + " work list.")
  private static Predicate<WorklistPage> leftWorklistContainers(List<String> expectedContainers) {
    return new Predicate<WorklistPage>() {
      @Override
      public boolean test(final WorklistPage worklistPage) {
        return worklistContainers(expectedContainers, worklistPage.getWorkListTableLeftRowPattern())
            .test(worklistPage);
      }
    };
  }

  @Step(
      "Verifies that the expected list of containers are displayed in the left split list for the"
          + " work list.")
  public static Predicate<WorklistPage> rightWorklistContainers(List<String> expectedContainers) {
    return new Predicate<WorklistPage>() {
      @Override
      public boolean test(final WorklistPage worklistPage) {
        return worklistContainers(
                expectedContainers, worklistPage.getWorkListTableRightRowPattern())
            .test(worklistPage);
      }
    };
  }

  @Step("Verify set aside containers count")
  public static Predicate<EndRowViewPage> setAsideContainersCount(String containerId) {
    return new Predicate<EndRowViewPage>() {
      @Override
      public boolean test(final EndRowViewPage endRowViewPage) {
        val setAsideCell = endRowViewPage.getSetAsideCells(containerId);
        return setAsideCell.size() == 1;
      }
    };
  }

  @Step("Verify cell background color")
  @RunForDriver(DriverTypes.Chrome)
  public static Predicate<EndRowViewPage> setAsideCellBackgroundColor(
      String containerId, CssRgbaColors color) {
    return new Predicate<EndRowViewPage>() {
      @Override
      public boolean test(final EndRowViewPage endRowViewPage) {
        return elementCssProperty(
                endRowViewPage.getSetAsideCell(containerId), CssProperties.Background, color)
            .test(endRowViewPage);
      }
    };
  }

  @Step("Verify cell border color")
  @RunForDriver(DriverTypes.Chrome)
  public static Predicate<EndRowViewPage> setAsideCellBorderColor(
      String containerId, CssRgbColors color) {
    return new Predicate<EndRowViewPage>() {
      @Override
      public boolean test(final EndRowViewPage endRowViewPage) {
        return elementBorderColor(endRowViewPage.getSetAsideCell(containerId), color, "solid", 2)
            .test(endRowViewPage);
      }
    };
  }

  @Step(
      "Verifies that the split work-list page column headers match the"
          + " expected values based on user profile settings.")
  public static Predicate<WorklistPage> splitWorklistColumnHeaders(
      List<String> expectedColumnHeaders) {
    return new Predicate<WorklistPage>() {
      @Override
      public boolean test(final WorklistPage worklistPage) {
        val actualLeftColumnHeaders =
            worklistPage.getColumnHeaders(worklistPage.getSplitViewLeftHeader());
        val actualRightColumnHeaders =
            worklistPage.getColumnHeaders(worklistPage.getSplitViewRightHeader());

        assertEquals(actualLeftColumnHeaders, expectedColumnHeaders);
        assertEquals(actualRightColumnHeaders, expectedColumnHeaders);

        return true;
      }
    };
  }

  @Step("Verifies that split work-list contains containers equals to expected.")
  public static Predicate<WorklistPage> splitWorklistContainers(List<String> expectedContainers) {
    return new Predicate<WorklistPage>() {
      @Override
      public boolean test(final WorklistPage worklistPage) {
        val containerRow =
            worklistPage.getContainerRow(
                expectedContainers.get(0), worklistPage.getWorkListTableLeftRowPattern());
        val rowHeight = containerRow.getSize().height;
        val tableHeight =
            EnvironmentController.getDriverType() == DriverTypes.Chrome
                ? $(worklistPage.getSplitViewLeftBody()).getSize().height
                : $(By.xpath("//*[contains(@AutomationId, 'worklist-table-left-row-')]/.."))
                    .getSize()
                    .height;

        val rowCount = tableHeight / rowHeight + 1;

        return leftWorklistContainers(expectedContainers.subList(0, rowCount))
            .and(
                rightWorklistContainers(
                    expectedContainers.subList(
                        rowCount,
                        2 * rowCount > expectedContainers.size()
                            ? expectedContainers.size()
                            : 2 * rowCount)))
            .test(worklistPage);
      }
    };
  }

  @Step("Verify track selector button color.")
  @RunForDriver(DriverTypes.Chrome)
  public static Predicate<YardViewBasePage> truckPickerButtonColor() {
    return new Predicate<YardViewBasePage>() {
      @Override
      public boolean test(final YardViewBasePage yardViewBasePage) {
        return $(yardViewBasePage.getTruckPickerButton())
            .has(
                cssValue(
                    CssProperties.Background.toString(),
                    yardViewBasePage.getTruckPickerButton().isEnabled()
                        ? "rgba(33, 150, 243, 1)"
                        : "rgba(0, 0, 0, 0.12)"));
      }
    };
  }

  @Step("Verify truck selector button is enable or disable.")
  @RunForDriver(DriverTypes.Chrome)
  public static Predicate<YardViewBasePage> truckPickerButtonIsEnable(Boolean enable) {
    return new Predicate<YardViewBasePage>() {
      @Override
      public boolean test(final YardViewBasePage yardViewBasePage) {
        return $(yardViewBasePage.getTruckPickerButton()).has(enable ? enabled : disabled);
      }
    };
  }

  @Step("Check presence of unexpected error handling page.")
  public static Predicate<UnexpectedErrorHandlingPage> unexpectedErrorHandlingPagePresence() {
    return new Predicate<UnexpectedErrorHandlingPage>() {
      @Override
      public boolean test(final UnexpectedErrorHandlingPage unexpectedErrorHandlingPage) {
        $(unexpectedErrorHandlingPage.getRestartButton()).shouldBe(visible);

        return unexpectedErrorHandlingPage.getRestartButton().isEnabled()
            && unexpectedErrorHandlingPage.getUnhandledExceptionText().isEnabled()
            && unexpectedErrorHandlingPage.getSentimentVeryDissatisfied().isEnabled();
      }
    };
  }

  @Step("Verify utr background color.")
  @RunForDriver(DriverTypes.Chrome)
  public static Predicate<YardViewBasePage> utrBackgroundColor(String utr, CssRgbaColors color) {
    return new Predicate<YardViewBasePage>() {
      @Override
      public boolean test(final YardViewBasePage yardViewBasePage) {
        return elementCssProperty(
                yardViewBasePage.getTruck(utr), CssProperties.Background, color.toString())
            .test(yardViewBasePage);
      }
    };
  }

  @Step("Verify utr border color.")
  @RunForDriver(DriverTypes.Chrome)
  public static Predicate<YardViewBasePage> utrBorderColor(String utr, CssRgbColors color) {
    return new Predicate<YardViewBasePage>() {
      @Override
      public boolean test(final YardViewBasePage yardViewBasePage) {
        return elementBorderColor(yardViewBasePage.getTruck(utr), color, "solid", 2)
            .test(yardViewBasePage);
      }
    };
  }

  @Step("Verify utr border is not set color.")
  @RunForDriver(DriverTypes.Chrome)
  public static Predicate<YardViewBasePage> utrBorderColor(final String utr) {
    return new Predicate<YardViewBasePage>() {
      @Override
      public boolean test(final YardViewBasePage yardViewBasePage) {
        return elementBorderColor(yardViewBasePage.getTruck(utr), CssRgbColors.Black, "none", 0)
            .test(yardViewBasePage);
      }
    };
  }

  @Step("Verify utr buttons are present according to given list.")
  public static Predicate<YardViewBasePage> utrIsPresent(final List<Equipment> expectedUtrs) {
    return new Predicate<YardViewBasePage>() {
      @Override
      public boolean test(final YardViewBasePage yardViewBasePage) {
        val actualUtrs =
            expectedUtrs
                .stream()
                .map(s -> yardViewBasePage.getTruck(s.id))
                .collect(Collectors.toList());

        if (expectedUtrs.isEmpty()) {
          return actualUtrs.isEmpty();
        }

        IntStream.range(0, expectedUtrs.size())
            .forEach(
                i -> {
                  val utrId = expectedUtrs.get(i).id;
                  val utrButton = yardViewBasePage.getTruck(utrId);

                  Assert.assertEquals(utrId, utrButton.getText());
                });

        return true;
      }
    };
  }

  private static Predicate<WorklistPage> verifyWorklistTableItem(
      String itemColumn, String containerId, String expected) {
    return new Predicate<WorklistPage>() {
      @Override
      public boolean test(final WorklistPage worklistPage) {
        val position = String.format("%%s[position()=%s]", itemColumn);

        return $(worklistPage.getContainerRow(
                containerId, worklistPage.getWorkListTableRowPattern()))
            .$(DriverSwitchBy.xpath(String.format(position, "./*"), String.format(position, "*/*")))
            .has(exactText(expected));
      }
    };
  }

  @Step("Verifies that zone view contains operations equals to expected.")
  public static Predicate<ZoneViewPage> vesselOperationsData(
      final List<VesselOperation> expectedOperations) {
    return new Predicate<ZoneViewPage>() {
      @Override
      public boolean test(final ZoneViewPage zoneViewPage) {
        val actualOperations =
            expectedOperations
                .stream()
                .map(
                    s ->
                        $(
                            DriverSwitchBy.id(
                                String.format(
                                    zoneViewPage.getVesselOperationTableRowPattern(),
                                    s.vessel,
                                    s.crane))))
                .collect(Collectors.toList());

        assertEquals(expectedOperations.size(), expectedOperations.size());
        IntStream.range(0, expectedOperations.size())
            .forEach(
                i -> {
                  val operationFields =
                      actualOperations
                          .get(i)
                          .findElements(DriverSwitchBy.xpath("./*", "*/*"))
                          .stream()
                          .map(WebElement::getText)
                          .collect(Collectors.toList());

                  val expectedOperation =
                      expectedOperations
                          .stream()
                          .filter(s -> s.vessel.equals(operationFields.get(0)))
                          .findFirst()
                          .orElse(null);

                  assertNotNull(expectedOperation, "Found operation was not expected");
                  DateFormat date = new SimpleDateFormat("MM/dd/yyyy");
                  DateFormat time = new SimpleDateFormat("HH:mm");

                  assertEquals(operationFields.get(0), expectedOperation.vessel);
                  assertEquals(operationFields.get(1), date.format(expectedOperation.dateTime));
                  assertEquals(operationFields.get(2), time.format(expectedOperation.dateTime));
                  assertEquals(operationFields.get(3), expectedOperation.shift);
                  assertEquals(operationFields.get(4), expectedOperation.crane);
                  assertEquals(operationFields.get(5), expectedOperation.gang);
                });
        return true;
      }
    };
  }

  @Step("Verifies Vessel Operations tab visibility.")
  public static Predicate<ZoneViewPage> vesselOperationsTabVisibility(boolean isVisible) {
    return new Predicate<ZoneViewPage>() {
      @Override
      public boolean test(final ZoneViewPage zoneViewPage) {
        return $(DriverSwitchBy.id(zoneViewPage.getVesselOperationsTabId()))
            .has(isVisible ? visible : not(exist));
      }
    };
  }

  @Step("Verifies Work Queues tab visibility.")
  public static Predicate<ZoneViewPage> workQueuesTabVisibility(boolean isVisible) {
    return new Predicate<ZoneViewPage>() {
      @Override
      public boolean test(final ZoneViewPage zoneViewPage) {
        return $(DriverSwitchBy.id(zoneViewPage.getWorkQueuesTabId()))
            .has(isVisible ? visible : not(exist));
      }
    };
  }

  @Step(
      "Verifies that the work-list page column headers match the expected values based on user"
          + " profile settings.")
  public static Predicate<WorklistPage> worklistColumnHeaders(List<String> expectedColumnHeaders) {
    return new Predicate<WorklistPage>() {
      @Override
      public boolean test(final WorklistPage worklistPage) {
        val actualColumnHeaders =
            worklistPage.getColumnHeaders(worklistPage.getWorklistTableHeader());

        assertEquals(actualColumnHeaders, expectedColumnHeaders);

        return true;
      }
    };
  }

  @Step("Verifies that work-list contains container with specified from location.")
  public static Predicate<WorklistPage> worklistContainerFromLocation(
      String containerId, String fromLocation) {
    return new Predicate<WorklistPage>() {
      @Override
      public boolean test(final WorklistPage worklistPage) {
        return verifyWorklistTableItem("3", containerId, fromLocation).test(worklistPage);
      }
    };
  }

  @Step("Verifies that work-list contains container with specified origin location.")
  public static Predicate<WorklistPage> worklistContainerOriginLocation(
      String containerId, String originalLocation) {
    return new Predicate<WorklistPage>() {
      @Override
      public boolean test(final WorklistPage worklistPage) {
        return verifyWorklistTableItem("5", containerId, originalLocation).test(worklistPage);
      }
    };
  }

  @Step("Verifies that work-list contains container with specified plan location.")
  public static Predicate<WorklistPage> worklistContainerPlanLocation(
      String containerId, String fromLocation) {
    return new Predicate<WorklistPage>() {
      @Override
      public boolean test(final WorklistPage worklistPage) {
        return verifyWorklistTableItem("6", containerId, fromLocation).test(worklistPage);
      }
    };
  }

  @Step("Verifies that work-list contains container with specified to location.")
  public static Predicate<WorklistPage> worklistContainerToLocation(
      String containerId, String toLocation) {
    return new Predicate<WorklistPage>() {
      @Override
      public boolean test(final WorklistPage worklistPage) {
        return verifyWorklistTableItem("4", containerId, toLocation).test(worklistPage);
      }
    };
  }

  @Step("Verifies that work-list contains containers equals to expected.")
  public static Predicate<WorklistPage> worklistContainers(List<String> expectedContainers) {
    return new Predicate<WorklistPage>() {
      @Override
      public boolean test(final WorklistPage worklistPage) {
        return worklistContainers(expectedContainers, worklistPage.getWorkListTableRowPattern())
            .test(worklistPage);
      }
    };
  }

  private static Predicate<WorklistPage> worklistContainers(
      List<String> expectedContainers, String tableRowPattern) {
    return worklistPage -> {
      $(worklistPage.getWorkListTitle()).shouldBe(visible);
      expectedContainers.forEach(
          containerNo ->
              Assert.assertTrue(
                  worklistPage.getContainerRow(containerNo, tableRowPattern).isDisplayed()));

      return true;
    };
  }

  @Step("Verifies that zone view contains zones equals to expected.")
  public static Predicate<ZoneViewPage> zoneData(final List<Zone> expectedZones) {
    return new Predicate<ZoneViewPage>() {
      @Override
      public boolean test(final ZoneViewPage zoneViewPage) {
        val actualZones =
            expectedZones
                .stream()
                .map(
                    s ->
                        $(
                            DriverSwitchBy.id(
                                String.format(zoneViewPage.getZoneTableRowPattern(), s.id))))
                .collect(Collectors.toList());

        assertEquals(actualZones.size(), expectedZones.size());
        IntStream.range(0, expectedZones.size())
            .forEach(
                i -> {
                  val zoneFields =
                      actualZones
                          .get(i)
                          .findElements(DriverSwitchBy.xpath("./*", "*/*"))
                          .stream()
                          .map(WebElement::getText)
                          .collect(Collectors.toList());

                  val expectedZone =
                      expectedZones
                          .stream()
                          .filter(s -> s.id.equals(zoneFields.get(0)))
                          .findFirst()
                          .orElse(null);

                  assertNotNull(expectedZone, "Found zone was not expected");
                  assertEquals(zoneFields.get(0), expectedZone.id);
                  assertEquals(zoneFields.get(1), Integer.toString(expectedZone.equipmentCount));
                  assertEquals(zoneFields.get(2), Integer.toString(expectedZone.yardMoveCount));
                  assertEquals(zoneFields.get(3), Integer.toString(expectedZone.unloadMoveCount));
                  assertEquals(zoneFields.get(4), Integer.toString(expectedZone.loadMoveCount));
                  assertEquals(zoneFields.get(5), Integer.toString(expectedZone.stowMoveCount));
                  assertEquals(
                      zoneFields.get(6), Integer.toString(expectedZone.dischargeMoveCount));
                });
        return true;
      }
    };
  }

  @Step("Verifies zone view header.")
  public static Predicate<ZoneViewPage> zoneTableHeader(String expectedResult) {
    return new Predicate<ZoneViewPage>() {
      @Override
      public boolean test(final ZoneViewPage zoneViewPage) {
        return $(zoneViewPage.getZonesTab()).has(exactText(expectedResult));
      }
    };
  }

  @Step("Verifies Zones tab visibility.")
  public static Predicate<ZoneViewPage> zonesTabVisibility(boolean isVisible) {
    return new Predicate<ZoneViewPage>() {
      @Override
      public boolean test(final ZoneViewPage zoneViewPage) {
        return $(DriverSwitchBy.id(zoneViewPage.getZonesTabId()))
            .has(isVisible ? visible : not(exist));
      }
    };
  }

  @Step("Verifies visibility of escalation filter button.")
  public static Predicate<WorklistPage> escalationButtonVisibility(boolean expectedState) {
    return new Predicate<WorklistPage>() {
      @Override
      public boolean test(final WorklistPage worklistPage) {
        return $(DriverSwitchBy.xpath(
                worklistPage.getEscalateMoveXpathChrome(),
                worklistPage.getEscalateMoveXpathWindows()))
            .has(expectedState ? exist : not(exist));
      }
    };
  }
}
