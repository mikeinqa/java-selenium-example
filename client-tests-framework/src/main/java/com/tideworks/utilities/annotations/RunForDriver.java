package com.tideworks.utilities.annotations;

import com.tideworks.utilities.services.driver.DriverTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Annotation for marks tests with supported driver type. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RunForDriver {

  /**
   * Supported driver type.
   *
   * @return Driver type value.
   */
  DriverTypes value();
}
