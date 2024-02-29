package com.tideworks.utilities.services.apimockservice;

import static com.tideworks.utilities.configs.appconfig.PropertyNames.DOMAIN;
import static com.tideworks.utilities.configs.appconfig.PropertyNames.RESPONSE_DELAY;
import static com.tideworks.utilities.configs.appconfig.PropertyNames.SERVER_PORT;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static java.lang.Integer.parseInt;

import com.tideworks.json.parser.JsonParser;
import com.tideworks.utilities.configs.appconfig.PropertyProvider;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import org.testng.Assert;

import java.util.Map;

/** Windows service which provide stubs for http requests. */
public class WindowsApiMockService implements ApiMockService<StubMapping> {

  private int serverPort;
  private String serverDomain;

  private WireMockServer server;
  private JsonParser parser;

  /** Wrapper for wire mock server, provided server management and configured it. */
  public WindowsApiMockService() {
    parser = new JsonParser();
    serverPort = parseInt(PropertyProvider.getValue(SERVER_PORT));
    serverDomain = PropertyProvider.getValue(DOMAIN);
    server = new WireMockServer(options().notifier(new ConsoleNotifier(false)).port(serverPort));
    server.setGlobalFixedDelay(Integer.parseInt(PropertyProvider.getValue(RESPONSE_DELAY)));

    configureFor(serverDomain, serverPort);
  }

  @Override
  public ResponseBuilder response() {
    return new WindowsResponseBuilder();
  }

  @Override
  public void stopService() {
    if (server.isRunning()) {
      server.stop();
    }
  }

  @Override
  public void startService() {
    if (server.isRunning()) {
      return;
    }

    server.start();
  }

  @Override
  public void restartService() {
    stopService();
    startService();
  }

  @Override
  public void startIfStopped() {
    if (!server.isRunning()) {
      server.start();
    }
  }

  @Override
  public void resetService() {
    server.resetAll();
  }

  @Override
  public void resetRequests() {
    server.resetRequests();
  }

  @Override
  public StubMapping setJsonResponseForPost(String url, Object response) {
    return setJsonResponseForPost(url, response, null, null);
  }

  @Override
  public StubMapping setJsonResponseForPost(String url, Object response, Object body) {
    return setJsonResponseForPost(url, response, body, null);
  }

  @Override
  public StubMapping setJsonResponseForPost(
      String url, Object response, Object body, Map<String, String> headers) {
    return setJsonResponseForPost(url, response, body, headers, false);
  }

  @Override
  public StubMapping setJsonResponseForPost(
      String url, Object response, Object body, Map<String, String> headers, boolean useRegex) {

    MappingBuilder builder = post(useRegex ? urlMatching(url) : urlEqualTo(url));
    return setStubBase(builder, response, body, headers);
  }

  @Override
  public StubMapping setJsonResponseForPut(String url, Object response) {
    return setJsonResponseForPut(url, response, null, null, false);
  }

  @Override
  public StubMapping setJsonResponseForPut(String url, Object response, Object body) {
    return setJsonResponseForPut(url, response, body, null, false);
  }

  @Override
  public StubMapping setJsonResponseForPut(
      String url, Object response, Object body, Map<String, String> headers, boolean useRegex) {

    MappingBuilder builder = put(useRegex ? urlMatching(url) : urlEqualTo(url));
    return setStubBase(builder, response, body, headers);
  }

  @Override
  public StubMapping setJsonResponseForGet(String url, Object response) {
    return setJsonResponseForGet(url, response, null);
  }

  @Override
  public StubMapping setJsonResponseForGet(
      String url, Object response, Map<String, String> headers) {

    setStubBase(WireMock.options(urlEqualTo(url)), response, null, headers);

    MappingBuilder builder = get(urlEqualTo(url));
    return setStubBase(builder, response, null, headers);
  }

  @Override
  public void verify(final StubMapping object) {
    verify(object, 1);
  }

  @Override
  public void verify(final StubMapping object, int times) {
    WireMock.verify(times, RequestPatternBuilder.forCustomMatcher(object.getRequest()));
  }

  @Override
  public String getServiceAddress() {
    return String.format("%s:%s", serverDomain, serverPort);
  }

  private StubMapping setStubBase(
      MappingBuilder builder, Object response, Object body, Map<String, String> headers) {

    if (!server.isRunning()) {
      Assert.fail("Service is not running. Failed to create stub for http request");
    }

    if (body != null) {
      if (body.getClass() == String.class) {
        builder.withRequestBody(equalTo((String) body));
      } else {
        builder.withRequestBody(equalToJson(parser.serialize(body)));
      }
    }

    if (headers != null && !headers.isEmpty()) {
      headers.forEach((key, value) -> builder.withHeader(key, equalTo(value)));
    }

    if (response.getClass() == ResponseDefinitionBuilder.class) {
      return stubFor(builder.willReturn((ResponseDefinitionBuilder) response));
    }

    return stubFor(builder.willReturn(okJson(parser.serialize(response))));
  }
}
