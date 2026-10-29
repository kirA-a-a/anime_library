package io.student.rcc.page;

import static com.codeborne.selenide.Condition.visible;

import com.codeborne.selenide.SelenideDriver;
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
  private final SelenideElement userNameInput;
  private final SelenideElement passwordInput;
  private final SelenideElement submitBtn;
  private final SelenideElement registrationBtn;
  private final SelenideElement avatar;
  private final SelenideElement incorrectDataError;

  public AuthenticationPage(SelenideDriver driver) {
    super(driver);
    this.userNameInput = driver.$("input[name='username']");
    this.passwordInput = driver.$("input[name='password']");
    this.submitBtn = driver.$("button[type='submit']");
    this.registrationBtn = driver.$("a[href='/register']");
    this.avatar = driver.$("figure.avatar");
    this.incorrectDataError = driver.$(By.xpath("//p[class*='login__error']"));
  }

  @Step("Авторизоваться под пользователем: {username}")
  public @Nonnull MainPage login(String username, String password) {
    userNameInput.setValue(username);
    passwordInput.setValue(password);
    submitBtn.click();
    avatar.shouldBe(visible);
    return new MainPage(driver);
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
    return new RegisterPage(driver);
  }
}
