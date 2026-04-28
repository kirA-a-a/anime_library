package io.student.rcc.page;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

public class RegistrationPage {

  private final SelenideElement userNameInput = $("input[id='username']"),
      passwordInput = $("input[id='password']"),
      passwordSubmitInput = $("input[id='passwordSubmit']"),
      submitBtn = $("button[type='submit']"),
      signInBtn = $(By.xpath("//a[contains(text(), 'Войти в систему')]")),
      userNameMessageError = $("span[class*='error__username')]");

  public MainPage successRegistrationUser(String userName, String password) {
    userNameInput.setValue(userName);
    passwordInput.setValue(password);
    passwordSubmitInput.setValue(password);
    submitBtn.click();
    signInBtn.shouldBe(visible).click();
    return new MainPage();
  }

  public RegistrationPage unSuccessRegister(String userName, String password) {
    userNameInput.setValue(userName);
    passwordInput.setValue(password);
    passwordSubmitInput.setValue(password);
    submitBtn.click();
    userNameMessageError.isDisplayed();
    return this;
  }
}
