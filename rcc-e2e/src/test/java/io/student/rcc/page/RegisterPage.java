package io.student.rcc.page;

import static com.codeborne.selenide.Condition.visible;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.openqa.selenium.By;

@ParametersAreNonnullByDefault
@SuppressWarnings("UnusedReturnValue")
public class RegisterPage extends BasePage<RegisterPage> {

  private final SelenideElement userNameInput;
  private final SelenideElement passwordInput;
  private final SelenideElement passwordSubmitInput;
  private final SelenideElement submitBtn;
  private final SelenideElement signInBtn;
  private final SelenideElement userNameMessageError;

  public RegisterPage(SelenideDriver driver) {
    super(driver);
    this.userNameInput = driver.$("input[id='username']");
    this.passwordInput = driver.$("input[id='password']");
    this.passwordSubmitInput = driver.$("input[id='passwordSubmit']");
    this.submitBtn = driver.$("button[type='submit']");
    this.signInBtn = driver.$(By.xpath("//a[contains(text(), 'Войти в систему')]"));
    this.userNameMessageError = driver.$("span[class*='error__username')]");
  }

  @Step("Зарегистрировать пользователя: {userName}")
  public @Nonnull MainPage successRegistrationUser(String userName, String password) {
    userNameInput.setValue(userName);
    passwordInput.setValue(password);
    passwordSubmitInput.setValue(password);
    submitBtn.click();
    signInBtn.shouldBe(visible).click();
    return new MainPage(driver);
  }

  @Step("Зарегистрировать существующего пользователя: {userName}")
  public @Nonnull RegisterPage unSuccessRegister(String userName, String password) {
    userNameInput.setValue(userName);
    passwordInput.setValue(password);
    passwordSubmitInput.setValue(password);
    submitBtn.click();
    userNameMessageError.isDisplayed();
    return this;
  }
}
