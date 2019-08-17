package io.github.mufasa1976.spring.test.softwareday;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class Routes {
  @NoArgsConstructor(access = PRIVATE)
  public static final class Param {
    public static final String
        REFERENCE = "reference",
        LAST_UPDATED_AT = "last-updated-at";
  }

  private static final String
      API = "/api",
      API_V1 = API + "/v1";

  public static final String
      NOTES = API_V1 + "/notes",
      NOTE = NOTES + "/{" + Param.REFERENCE + "}";
}
