package io.student.rcc.page;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import io.student.rcc.config.Config;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.openqa.selenium.By;

@ParametersAreNonnullByDefault
@SuppressWarnings("UnusedReturnValue")
public class AuthenticationPage extends BasePage<AuthenticationPage> {

  public static String url = Config.getInstance().frontUrl() + "/autificationPage";
  private final SelenideElement userNameInput = $("input[name='username']"),
      passwordInput = $("input[name='password']"),
      submitBtn = $("button[type='submit']"),
      registrationBtn = $("a[href='/register']"),
      avatar = $("figure.avatar"),
      incorrectDataError = $(By.xpath("//p[class*='login__error']"));

  @Step("Авторизоваться под пользователем: {username}")
  public @Nonnull MainPage login(String username, String password) {
    userNameInput.setValue(username);
    passwordInput.setValue(password);
    submitBtn.click();
    avatar.shouldBe(visible);
    return new MainPage();
  }

  @Step("Авторизоваться с невалидными данными: {username}")
  public @Nonnull AuthenticationPage incorrectLogin(String username, String password) {
    userNameInput.setValue(username);
    passwordInput.setValue(password);
    incorrectDataError.shouldBe(visible);

    return this;
  }

  @Step("Перейти на страницу регистрации")
  public @Nonnull RegisterPage clickRegistrationButton() {
    registrationBtn.click();
    return new RegisterPage();
  }
}
