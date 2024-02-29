package com.tideworks.pages.elements;

import com.tideworks.pages.BasePage;

import io.appium.java_client.pagefactory.WindowsFindBy;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;

import java.util.stream.Collectors;

/** Header element for End row view and Heap view pages. */
public class YardViewHeader extends BaseElement {

  private final String cheHeaderContainerId = "che-header-container-id";
  private final String cheHeaderWeightId = "che-header-weight";
  private final String cheHeaderFromLocationId = "che-header-fromLocation";
  private final String cheHeaderToLocationId = "che-header-toLocation";

  @Getter
  @FindBy(id = cheHeaderContainerId)
  @WindowsFindBy(accessibility = cheHeaderContainerId)
  private RemoteWebElement containerId;

  @Getter
  @FindBy(id = cheHeaderWeightId)
  @WindowsFindBy(accessibility = cheHeaderWeightId)
  private RemoteWebElement weight;

  @Getter
  @FindBy(id = cheHeaderFromLocationId)
  @WindowsFindBy(accessibility = cheHeaderFromLocationId)
  private RemoteWebElement fromLocation;

  @Getter
  @FindBy(id = cheHeaderToLocationId)
  @WindowsFindBy(accessibility = cheHeaderToLocationId)
  private RemoteWebElement toLocation;

  public YardViewHeader(final BasePage basePage) {
    super(basePage);
  }

  /**
   * Gets text of first child element.
   *
   * @param remoteWebElement Parrent element.
   * @return Text of girst child element.
   */
  public String getChildText(RemoteWebElement remoteWebElement) {
    return remoteWebElement
        .findElements(By.xpath("*/child::Text"))
        .stream()
        .map(WebElement::getText)
        .collect(Collectors.joining());
  }
}
