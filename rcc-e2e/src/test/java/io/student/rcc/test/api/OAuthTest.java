package io.student.rcc.test.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.student.rcc.api.AuthApiClient;
import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.jupiter.extension.CookiesExtension;
import io.student.rcc.model.UserJson;
import io.student.rcc.utils.OauthUtils;
import okhttp3.HttpUrl;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import retrofit2.Response;

@ExtendWith(CookiesExtension.class)
public class OAuthTest {

  private static final String CLIENT_ID = "client";
  private static final String REDIRECT_URI = "http://localhost:3000/authorized";
  private static final String SCOPE = "openid profile";
  private static final String PASSWORD = "12345";

  private final AuthApiClient authApiClient = new AuthApiClient();

  @Test
  @User
  void shouldReturnIdToken(UserJson user) throws Exception {
    String codeVerifier = OauthUtils.generateCodeVerifier();
    String codeChallenge = OauthUtils.generateCodeChallenge(codeVerifier);

    authApiClient.authorize(
        "code", CLIENT_ID, REDIRECT_URI, SCOPE, codeChallenge, "S256"
    );
    String csrf = Jsoup.parse(authApiClient.requestLoginForm().body().string())
        .selectFirst("input[name=_csrf]")
        .attr("value");

    Response<Void> loginResponse = authApiClient.login(csrf, user.username(), PASSWORD);
    Response<Void> redirectResponse = authApiClient.followRedirect(
        loginResponse.headers().get("Location")
    );
    String code = HttpUrl.parse(redirectResponse.headers().get("Location")).queryParameter("code");

    String idToken = new ObjectMapper()
        .readTree(
            authApiClient.token(CLIENT_ID, REDIRECT_URI, "authorization_code", code, codeVerifier)
                .body()
                .string()
        )
        .get("id_token")
        .asText();

    assertNotNull(idToken);
  }
}
