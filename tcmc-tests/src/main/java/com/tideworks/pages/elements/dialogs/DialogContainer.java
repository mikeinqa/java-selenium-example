package com.tideworks.pages.elements.dialogs;

import com.tideworks.pages.BasePage;
import com.tideworks.pages.elements.BaseElement;
import com.tideworks.utilities.selectors.DriverSwitchBy;

import lombok.Getter;
import org.openqa.selenium.By;

/** Base element for dialogs boxes. */
public abstract class DialogContainer extends BaseElement {

  @Getter private By titleSelector;

  DialogContainer(final BasePage basePage, By by) {
    super(basePage);
    setSelector(by);
    titleSelector = DriverSwitchBy.id(by.toString().replaceAll("By.*(Id|id): ", "") + "-title");
  }
}
