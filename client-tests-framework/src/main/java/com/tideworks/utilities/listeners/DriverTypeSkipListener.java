package com.tideworks.utilities.listeners;

import static com.tideworks.utilities.controllers.enviroment.EnvironmentController.getDriverType;

import com.tideworks.utilities.annotations.RunForDriver;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import lombok.val;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.SkipException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/** Listener for skipping test with unsupported driver type annotation. */
public class DriverTypeSkipListener extends BaseListener
    implements IInvokedMethodListener, MethodHandler, MethodFilter {

  /**
   * Indicates if given method supported by current driver type.
   *
   * @param method Method to check.
   * @return True if supported false otherwise.
   */
  public static boolean isDriverSupported(final Method method) {
    if (method.isAnnotationPresent(RunForDriver.class)) {
      val supportedDrivers =
          Arrays.stream(method.getAnnotationsByType(RunForDriver.class))
              .map(RunForDriver::value)
              .collect(Collectors.toList());

      return supportedDrivers.contains(getDriverType());
    }
    return true;
  }

  @Override
  public void beforeInvocation(final IInvokedMethod method, final ITestResult testResult) {
    val testMethod = method.getTestMethod().getConstructorOrMethod().getMethod();
    if (!isDriverSupported(testMethod)) {
      throw new SkipException("Unsupported driver type.");
    }
    isDriverSupported(testMethod);
  }

  @Override
  public void afterInvocation(final IInvokedMethod method, final ITestResult testResult) {}

  @Override
  public boolean isHandled(final Method method) {
    return method.isAnnotationPresent(RunForDriver.class);
  }

  @Override
  public Object invoke(
      final Object self, final Method method, final Method proceed, final Object[] args)
      throws Throwable {
    if (isDriverSupported(method)) {
      return proceed.invoke(self, args);
    }
    return self;
  }
}
