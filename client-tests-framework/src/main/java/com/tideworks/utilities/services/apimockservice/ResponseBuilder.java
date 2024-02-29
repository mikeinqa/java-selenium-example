package com.tideworks.utilities.services.apimockservice;

/**
 * Response builder.
 *
 * @param <T> Response definition.
 */
public interface ResponseBuilder<T> {

  /**
   * Ads response status.
   *
   * @param status Response status.
   * @return Response builder.
   */
  ResponseBuilder withStatus(int status);

  /**
   * Ads response body.
   *
   * @param body Response body.
   * @return response builder.
   */
  ResponseBuilder withBody(String body);

  /**
   * Build response.
   *
   * @return Response object.
   */
  T build();
}
