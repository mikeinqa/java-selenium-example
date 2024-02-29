package com.tideworks.utilities.services.apimockservice;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;

/** Response builder for windows services. */
public class WindowsResponseBuilder implements ResponseBuilder<ResponseDefinitionBuilder> {

  private static final ResponseDefinitionBuilder DEFINITION_BUILDER =
      new ResponseDefinitionBuilder();

  @Override
  public WindowsResponseBuilder withStatus(final int status) {
    DEFINITION_BUILDER.withStatus(status);
    return this;
  }

  @Override
  public WindowsResponseBuilder withBody(String body) {
    DEFINITION_BUILDER.withBody(body);
    return this;
  }

  @Override
  public ResponseDefinitionBuilder build() {
    return DEFINITION_BUILDER;
  }
}
