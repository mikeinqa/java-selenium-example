package com.tideworks.json.objectmodel;

import com.google.gson.annotations.SerializedName;

/** Authorization token for http requests. */
public class AuthorizationToken {

  @SerializedName("access_token")
  public String accessToken;
}
