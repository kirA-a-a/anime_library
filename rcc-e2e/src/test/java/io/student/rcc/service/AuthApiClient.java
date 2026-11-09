package io.student.rcc.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.student.rcc.api.AuthApi;
import io.student.rcc.api.BaseRestClient;
import io.student.rcc.api.core.CodeInterceptor;
import io.student.rcc.config.Config;
import io.student.rcc.jupiter.extension.ApiLoginExtension;
import io.student.rcc.utils.OauthUtils;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import retrofit2.Response;

public class AuthApiClient extends BaseRestClient {

  private static final Config CFG = Config.getInstance();
  private final AuthApi authApi;

  public AuthApiClient() {
    super(CFG.authUrl(), true, new CodeInterceptor());
    this.authApi = getRetrofit().create(AuthApi.class);
  }

  @SneakyThrows
  public String login(String username, String password) {
    final String clientId = "client";
    final String redirectUri = CFG.frontUrl() + "authorized";
    final String codeVerifier = OauthUtils.generateCodeVerifier();
    final String codeChallenge = OauthUtils.generateCodeChallenge(codeVerifier);

    authApi.authorize("code", clientId, redirectUri, "openid", codeChallenge, "S256").execute();

    String csrf = Jsoup.parse(authApi.requestLoginForm().execute().body().string())
        .selectFirst("input[name=_csrf]")
        .attr("value");

    authApi.login(csrf, username, password).execute();

    Response<JsonNode> tokenResponse = authApi.token(
        clientId,
        redirectUri,
        "authorization_code",
        ApiLoginExtension.getCode(),
        codeVerifier
    ).execute();

    String idToken = tokenResponse.body().get("id_token").asText();
    ApiLoginExtension.setToken(idToken);
    return idToken;
  }
}
