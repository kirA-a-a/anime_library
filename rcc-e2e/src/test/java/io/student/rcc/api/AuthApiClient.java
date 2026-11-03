package io.student.rcc.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Objects;
import javax.annotation.ParametersAreNonnullByDefault;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import retrofit2.Response;

@Slf4j
@ParametersAreNonnullByDefault
public class AuthApiClient extends BaseRestClient {

  private final AuthApi authApi;

  public AuthApiClient() {
    super(CFG.authUrl());
    this.authApi = retrofit.create(AuthApi.class);
  }

  public void authorize(
      String responseType,
      String clientId,
      String redirectUri,
      String scope,
      String codeChallenge,
      String codeChallengeMethod
  ) {
    try {
      authApi.authorize(
              responseType,
              clientId,
              redirectUri,
              scope,
              codeChallenge,
              codeChallengeMethod
          )
          .execute();
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  public Response<ResponseBody> requestLoginForm() {
    try {
      return authApi.requestLoginForm().execute();
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  public Response<Void> login(String csrf, String username, String password) {
    try {
      return authApi.login(csrf, username, password).execute();
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  public Response<Void> followRedirect(String url) {
    try {
      return authApi.followRedirect(url).execute();
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  public Response<ResponseBody> token(
      String clientId,
      String redirectUri,
      String grantType,
      String code,
      String codeVerifier
  ) {
    try {
      return authApi.token(clientId, redirectUri, grantType, code, codeVerifier).execute();
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  public void registerUser(String username, String password) {
    try {
      Response<ResponseBody> formResponse = authApi.requestRegisterForm().execute();
      String formHtml = Objects.requireNonNull(formResponse.body()).string();

      String csrf = extractCsrf(formHtml);

      Response<Void> registerResponse = authApi.registerUser(csrf, username, password, password)
          .execute();
      assertEquals(201, registerResponse.code());

    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  private String extractCsrf(String html) {
    return Objects.requireNonNull(
        Jsoup.parse(html).selectFirst("input[name=_csrf]")
    ).attr("value");
  }
}
