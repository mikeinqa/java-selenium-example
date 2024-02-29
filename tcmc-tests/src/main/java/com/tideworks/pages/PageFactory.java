package com.tideworks.pages;

import com.tideworks.utilities.listeners.DriverTypeSkipListener;

import javassist.util.proxy.ProxyFactory;
import lombok.val;
import org.testng.Assert;

import java.lang.reflect.InvocationTargetException;

/** Factory wrapper for Pages, which support verification handlers. */
public class PageFactory {

  /**
   * Creates Page instance of given type.
   *
   * @param pageType Page type to create.
   * @param <T> Generic parameter for page creation.
   * @return Page instance.
   */
  public static <T extends BasePage> T getPage(Class<T> pageType) {
    val proxyFactory = new ProxyFactory();
    proxyFactory.setSuperclass(pageType);
    proxyFactory.setFilter(new DriverTypeSkipListener());

    try {
      return (T)
          proxyFactory.create(
              new Class<?>[] {},
              new Object[] {},
              new DriverTypeSkipListener());
    } catch (NoSuchMethodException
        | InstantiationException
        | IllegalAccessException
        | InvocationTargetException exc) {
      Assert.fail(exc.getMessage());
    }

    return null;
  }
}
