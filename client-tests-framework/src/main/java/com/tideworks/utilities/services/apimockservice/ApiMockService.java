package com.tideworks.utilities.services.apimockservice;

import com.tideworks.utilities.services.Service;

import java.util.Map;

/**
 * Service which provide stubs for http requests.
 *
 * @param <T> Type of the returning stub mapping.
 */
public interface ApiMockService<T> extends Service {

  /**
   * Response builder for http requests.
   *
   * @return Response builder.
   */
  ResponseBuilder response();

  /** Run stopServer and after it run startServer @ If server running after stopServer. */
  void restartService();

  /** Start service if it is not running. */
  void startIfStopped();

  /** Reset all requests made to server. */
  void resetRequests();

  /**
   * Set response for POST request.
   *
   * @param url for compare on server.
   * @param response that the server returns if utr was equals.
   * @return T Type of the returning stub mapping.
   */
  T setJsonResponseForPost(String url, Object response);

  /**
   * Set response for POST request.
   *
   * @param url for compare on server.
   * @param response that the server returns if utr was equals
   * @param body for compare on server if pass {String} will by setup as http post body else other
   *     will by setup as json
   * @return T Type of the returning stub mapping.
   */
  T setJsonResponseForPost(String url, Object response, Object body);

  /**
   * Set response for POST request.
   *
   * @param url for compare on server
   * @param response that the server returns if utr was equals
   * @param body for compare on server if pass {String} will by setup as http post body else other
   *     will by setup as json
   * @param headers for compare on server
   * @return T Type of the returning stub mapping.
   */
  T setJsonResponseForPost(String url, Object response, Object body, Map<String, String> headers);

  /**
   * Set response for POST request.
   *
   * @param url for compare on server.
   * @param response that the server returns if utr was equals.
   * @param body for compare on server if pass {String} will by setup as http post body else other
   *     will by setup as json
   * @param headers for compare on server
   * @param useRegex indicates if url matching should ues regular expression.
   * @return T Type of the returning stub mapping.
   */
  T setJsonResponseForPost(
      String url, Object response, Object body, Map<String, String> headers, boolean useRegex);

  /**
   * Set response for PUT request.
   *
   * @param url for compare on server
   * @param response that the server returns if utr was equals
   * @return T Type of the returning stub mapping.
   */
  T setJsonResponseForPut(String url, Object response);

  /**
   * Set response for PUT request.
   *
   * @param url for compare on server
   * @param response that the server returns if utr was equals
   * @param body for compare on server if pass {String} will by setup as http post body else other
   *     will by setup as json.
   * @return T Type of the returning stub mapping.
   */
  T setJsonResponseForPut(String url, Object response, Object body);

  /**
   * Set response for PUT request.
   *
   * @param url for compare on server.
   * @param response that the server returns if utr was equals.
   * @param body for compare on server if pass {String} will by setup as http post body else other
   *     will by setup as json
   * @param headers for compare on server
   * @param useRegex indicates if url matching should ues regular expression.
   * @return T Type of the returning stub mapping.
   */
  T setJsonResponseForPut(
      String url, Object response, Object body, Map<String, String> headers, boolean useRegex);

  /**
   * Set response for GET request.
   *
   * @param url for compare on server
   * @param response that the server returns if utr was equals
   * @return T Type of the returning stub mapping.
   */
  T setJsonResponseForGet(String url, Object response);

  /**
   * Set response for GET request.
   *
   * @param url for compare on server
   * @param response that the server returns if utr was equals
   * @param headers for compare on server
   * @return T Type of the returning stub mapping.
   */
  T setJsonResponseForGet(String url, Object response, Map<String, String> headers);

  /**
   * Verify that expected request was matched.
   *
   * @param object Request definition.
   */
  void verify(T object);

  /**
   * Verify that expected request was matched expected number of calls.
   *
   * @param object Request definition.
   * @param times Expected number of calls.
   */
  void verify(T object, int times);
}
