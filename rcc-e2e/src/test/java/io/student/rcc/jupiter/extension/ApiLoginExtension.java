package io.student.rcc.jupiter.extension;

import static com.codeborne.selenide.Selenide.localStorage;
import static com.codeborne.selenide.Selenide.open;
import static io.student.rcc.jupiter.extension.UserExtension.DEFAULT_PASSWORD;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.student.rcc.api.core.ThreadSafeCookieStore;
import io.student.rcc.config.Config;
import io.student.rcc.jupiter.annotation.ApiLogin;
import io.student.rcc.jupiter.annotation.Token;
import io.student.rcc.model.UserJson;
import io.student.rcc.page.MainPage;
import io.student.rcc.service.AuthApiClient;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;
import org.openqa.selenium.Cookie;

public class ApiLoginExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(
      ApiLoginExtension.class);
  private static final Config CFG = Config.getInstance();

  private final AuthApiClient authApiClient = new AuthApiClient();
  private final boolean setupBrowser;

  public ApiLoginExtension() {
    this(true);
  }

  private ApiLoginExtension(boolean setupBrowser) {
    this.setupBrowser = setupBrowser;
  }

  public static ApiLoginExtension rest() {
    return new ApiLoginExtension(false);
  }

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(
        context.getRequiredTestMethod(),
        ApiLogin.class
    ).ifPresent(
        apiLogin -> {
          final UserJson userFromUserExtension = UserExtension.getUserJson();
          final UserJson userToLogin;
          final String password;

          if (apiLogin.username().isEmpty() || apiLogin.password().isEmpty()) {
            if (userFromUserExtension == null) {
              throw new IllegalStateException(
                  "@User must be present in case that @ApiLogin is empty!"
              );
            }
            userToLogin = userFromUserExtension;
            password = DEFAULT_PASSWORD;
          } else {
            if (userFromUserExtension != null) {
              throw new IllegalStateException(
                  "@User must not be present in case that @ApiLogin contains username or password!"
              );
            }
            UserJson fakeUser = new UserJson(
                null,
                apiLogin.username(),
                null,
                null,
                null
            );
            UserExtension.setUser(fakeUser);
            userToLogin = fakeUser;
            password = apiLogin.password();
          }

          final String token = authApiClient.login(userToLogin.username(), password);
          setToken(token);

          if (setupBrowser) {
            open(CFG.frontUrl());
            localStorage().setItem("id_token", token);
            WebDriverRunner.getWebDriver().manage().addCookie(getJsessionIdCookie());
            MainPage.open();
          }
        }
    );
  }

  @Override
  public void afterEach(ExtensionContext context) {
    if (setupBrowser && WebDriverRunner.hasWebDriverStarted()) {
      Selenide.closeWebDriver();
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(String.class)
        && AnnotationSupport.isAnnotated(parameterContext.getParameter(), Token.class);
  }

  @Override
  public @Nullable String resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return getToken();
  }

  public static void setToken(String token) {
    TestMethodContextExtension.context().getStore(NAMESPACE).put("token", token);
  }

  public static String getToken() {
    return "Bearer " + TestMethodContextExtension.context().getStore(NAMESPACE).get("token", String.class);
  }

  public static void setCode(String code) {
    TestMethodContextExtension.context().getStore(NAMESPACE).put("code", code);
  }

  public static String getCode() {
    return TestMethodContextExtension.context().getStore(NAMESPACE).get("code", String.class);
  }

  public static Cookie getJsessionIdCookie() {
    return new Cookie(
        "JSESSIONID",
        ThreadSafeCookieStore.INSTANCE.cookieValue("JSESSIONID")
    );
  }
}
