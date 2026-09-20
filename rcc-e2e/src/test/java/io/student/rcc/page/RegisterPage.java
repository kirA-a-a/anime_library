package io.student.rcc.page;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.openqa.selenium.By;

@ParametersAreNonnullByDefault
@SuppressWarnings("UnusedReturnValue")
public class RegisterPage {

  private final SelenideElement userNameInput = $("input[id='username']"),
      passwordInput = $("input[id='password']"),
      passwordSubmitInput = $("input[id='passwordSubmit']"),
      submitBtn = $("button[type='submit']"),
      signInBtn = $(By.xpath("//a[contains(text(), 'Войти в систему')]")),
      userNameMessageError = $("span[class*='error__username')]");

  @Step("Зарегистрировать пользователя: {userName}")
  public @Nonnull BasePage successRegistrationUser(String userName, String password) {
    userNameInput.setValue(userName);
    passwordInput.setValue(password);
    passwordSubmitInput.setValue(password);
    submitBtn.click();
    signInBtn.shouldBe(visible).click();
    return new BasePage();
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
