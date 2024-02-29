package com.tideworks.utilities.listeners;

import com.tideworks.utilities.services.driver.remote.RemoteDriverService;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.util.List;
import java.util.Objects;

/** Simple listener for tests runs. */
public class TestListener extends BaseListener implements ITestListener {

  @Override
  public void onTestStart(final ITestResult result) {
    log("Starting of test: " + result.getName());
  }

  @Override
  public void onTestSuccess(final ITestResult result) {
    log("Test: " + result.getName() + " finished successfully.");
    MESSAGES.clear();
  }

  @Override
  public void onTestFailure(final ITestResult result) {
    byte[] screenShot = captureScreenShot();
    List<String> reporterOutput = Reporter.getOutput(result);

    reporterOutput.forEach(System.out::println);

    Allure.addAttachment("Test Reporter Output", String.join("\n", reporterOutput));
    Allure.addByteAttachmentAsync("Screenshot", "image/png", () -> screenShot);
  }

  @Override
  public void onTestSkipped(final ITestResult result) {
    log(
        "Test: "
            + result.getTestName()
            + " was skipped because of "
            + result.getThrowable().getMessage());
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(final ITestResult result) {}

  @Override
  public void onStart(final ITestContext context) {
    log("Starting of: " + context.getName());
  }

  @Override
  public void onFinish(final ITestContext context) {
    log("End of: " + context.getName());
  }

  private byte[] captureScreenShot() {
    RemoteWebDriver driver = RemoteDriverService.getEventDriver();

    if (!Objects.isNull(driver)) {
      return driver.getScreenshotAs(OutputType.BYTES);
    }

    return null;
  }
}
