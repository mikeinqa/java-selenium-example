package com.tideworks.pages.elements;

import com.tideworks.pages.BasePage;
import com.tideworks.utilities.listeners.DriverTypeSkipListener;

import javassist.util.proxy.ProxyFactory;
import lombok.val;
import org.testng.Assert;

import java.lang.reflect.InvocationTargetException;

/** Factory wrapper for Elements, which support verification handlers. */
public class ElementFactory {

  /**
   * Creates instance of element.
   *
   * @param elementType Type of element to create.
   * @param basePage Page instance for element initialization.
   * @param <T> Generic parameter for element creation.
   * @param locator Other arguments which can be pass to elements constructor.
   * @return Element instance.
   */
  public static <T extends BaseElement> T getElement(
      Class<T> elementType, BasePage basePage, String locator) {
    val proxyFactory = new ProxyFactory();
    proxyFactory.setSuperclass(elementType);
    proxyFactory.setFilter(new DriverTypeSkipListener());

    return getElementProxy(
        elementType,
        new Class<?>[] {BasePage.class, String.class},
        new Object[] {basePage, locator});
  }

  /**
   * Creates instance of element.
   *
   * @param elementType Type of element to create.
   * @param basePage Page instance for element initialization.
   * @param <T> Generic parameter for element creation.
   * @return Element instance.
   */
  public static <T extends BaseElement> T getElement(Class<T> elementType, BasePage basePage) {
    return getElementProxy(elementType, new Class<?>[] {BasePage.class}, new Object[] {basePage});
  }

  private static <T extends BaseElement> T getElementProxy(
      Class<T> elementType, Class<?>[] classes, Object[] objects) {
    val proxyFactory = new ProxyFactory();
    proxyFactory.setSuperclass(elementType);
    proxyFactory.setFilter(new DriverTypeSkipListener());

    try {
      return (T) proxyFactory.create(classes, objects, new DriverTypeSkipListener());
    } catch (NoSuchMethodException
        | InstantiationException
        | IllegalAccessException
        | InvocationTargetException exc) {
      Assert.fail(exc.getMessage());
    }

    throw new UnsupportedOperationException(
        "Cannot create element of type " + elementType.toString());
  }
}
