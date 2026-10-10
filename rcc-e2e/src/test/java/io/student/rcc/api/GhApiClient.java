package io.student.rcc.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import retrofit2.Response;

@ParametersAreNonnullByDefault
public class GhApiClient extends BaseRestClient {

  private static final String GH_TOKEN_ENV = "GITHUB_TOKEN";
  private final GhApi ghApi;

  public GhApiClient() {
    super(CFG.ghUrl());
    this.ghApi = retrofit.create(GhApi.class);
  }

  @Step("Получить статус GitHub issue: {issueNumber}")
  public @Nonnull String issueState(String issueNumber) {
    final Response<JsonNode> response;
    try {
      response = ghApi.issue(
          "Bearer " + System.getenv(GH_TOKEN_ENV),
          issueNumber
      ).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return Objects.requireNonNull(response.body()).get("state").asText();
  }
}
