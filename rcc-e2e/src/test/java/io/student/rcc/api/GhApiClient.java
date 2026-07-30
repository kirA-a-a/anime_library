package io.student.rcc.api;

import com.fasterxml.jackson.databind.JsonNode;
import io.student.rcc.config.Config;
import java.util.Objects;
import lombok.SneakyThrows;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class GhApiClient {

  private static final String GH_TOKEN_ENV = "GITHUB_TOKEN";

  private final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(Config.getInstance().ghUrl())
      .addConverterFactory(JacksonConverterFactory.create())
      .build();

  private final GhApi ghApi = retrofit.create(GhApi.class);

  @SneakyThrows
  public String issueState(String issueNumber) {
    JsonNode responseBody = ghApi.issue(
        "Bearer " + System.getenv(GH_TOKEN_ENV),
        issueNumber
    ).execute().body();
    return Objects.requireNonNull(responseBody).get("state").asText();
  }
}
