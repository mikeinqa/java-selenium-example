package com.tideworks.base;

import static com.tideworks.utilities.api.Common.authorizationUrl;
import static com.tideworks.utilities.api.Common.profileUrl;
import static com.tideworks.utilities.configs.appconfig.PropertyNames.DEVICE_ID;
import static com.tideworks.utilities.configs.appconfig.PropertyNames.DOMAIN;
import static com.tideworks.utilities.configs.appconfig.PropertyNames.PASSWORD;
import static com.tideworks.utilities.configs.appconfig.PropertyNames.USERNAME;
import static com.tideworks.utilities.configs.appconfig.PropertyProvider.getValue;
import static com.tideworks.utilities.controllers.enviroment.EnvironmentController.getDriverType;

import com.tideworks.json.objectmodel.AuthorizationToken;
import com.tideworks.json.objectmodel.Profile;
import com.tideworks.json.objectmodel.terminal.Terminal;
import com.tideworks.pages.LoginPage;
import com.tideworks.pages.PageFactory;
import com.tideworks.pages.UnexpectedErrorHandlingPage;
import com.tideworks.utilities.controllers.terminal.TerminalController;
import com.tideworks.utilities.listeners.DriverTypeSkipListener;
import com.tideworks.utilities.listeners.TestListener;
import com.tideworks.utilities.services.Service;
import com.tideworks.utilities.services.ServiceFactory;
import com.tideworks.utilities.services.apimockservice.ApiMockService;
import com.tideworks.utilities.services.driver.remote.RemoteDriverService;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

/** Base class for all tests. */
@Listeners({DriverTypeSkipListener.class, TestListener.class})
public class Base {

  protected final TerminalController terminalController = new TerminalController();
  protected final String userName = getValue(USERNAME);
  protected final String userPassword = getValue(PASSWORD);
  protected final String serverDomain = getValue(DOMAIN);
  protected final String deviceId = getValue(DEVICE_ID);
  protected ApiMockService apiMockService;
  protected Terminal terminal;
  protected FluentWait<RemoteWebDriver> webDriverWait;
  protected RemoteWebDriver eventDriver;
  protected Service driverService;
  protected RemoteDriverService remoteDriverService;

  @BeforeClass
  public void beforeClass() {
    apiMockService = ServiceFactory.createApiMockService();
    switch (getDriverType()) {
      case Chrome:
        driverService = ServiceFactory.createChromeService();
        remoteDriverService = ServiceFactory.createRemoteChromeDriverService();
        break;
      case Windows:
        driverService = ServiceFactory.createAppiumService();
        remoteDriverService = ServiceFactory.createWindowsDriverService();
        break;
      default:
        Assert.fail("Unsupported test platform.");
        break;
    }

    apiMockService.startService();
    driverService.startService();

    terminal = terminalController.getTerminal();
  }

  @AfterClass
  public void afterClass() {
    apiMockService.stopService();
    remoteDriverService.stopService();
    driverService.stopService();
  }

  @BeforeMethod
  public void beforeMethod() {
    stubAuthorization();
  }

  /** launch app. */
  public void launchApp() {
    remoteDriverService.startService();
    eventDriver = remoteDriverService.getEventDriver();
    webDriverWait = remoteDriverService.getWebDriverWait();
  }

  @AfterMethod
  public void resetApplication() {
    apiMockService.resetService();
    remoteDriverService.resetService();
  }

  @AfterMethod
  public void clearTerminal() {
    terminal.clear();
  }

  private void stubAuthorization() {
    AuthorizationToken token = new AuthorizationToken();
    token.accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";

    apiMockService.setJsonResponseForPost(
        authorizationUrl(),
        token,
        String.format("username=%s&password=%s", userName, userPassword));
  }

  /**
   * Stubs profile data.
   *
   * @param profile Profile to setup stub.
   */
  protected void stubProfileData(Profile profile) {
    apiMockService.setJsonResponseForGet(
        profileUrl(profile.deviceId), Collections.singletonList(profile));
  }

  /**
   * Returns login page.
   *
   * @return LoginPage
   */
  public LoginPage getLoginPage() {
    return PageFactory.getPage(LoginPage.class);
  }

  /**
   * Returns Unexpected Error Handling page.
   *
   * @return UnexpectedErrorHandlingPage
   */
  public UnexpectedErrorHandlingPage getUnexpectedErrorHandlingPage() {
    return PageFactory.getPage(UnexpectedErrorHandlingPage.class);
  }

  /**
   * Creates empty array of given type.
   *
   * @param type Class for array creation .
   * @param <T> Generic type for array creation.
   * @return Empty array.
   */
  public <T> T[] createEmptyArray(Class<T> type) {
    @SuppressWarnings("unchecked")
    T[] arr = (T[]) Array.newInstance(type, 0);

    return new ArrayList<T>().toArray(arr);
  }
}
