package io.student.rcc.page;


import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

public class AuthenticationPage {

  private final SelenideElement userNameInput = $("input[name='username']"),
      passwordInput = $("input[name='password']"),
      submitBtn = $("button[type='submit']"),
      registrationBtn = $("a[href='/register']"),
      incorrectDataError = $(By.xpath("//p[class*='login__error']"));


  public MainPage login(String username, String password) {
    userNameInput.setValue(username);
    passwordInput.setValue(password);
    submitBtn.click();

    return new MainPage();
  }

  public AuthenticationPage incorrectLogin(String username, String password) {
    userNameInput.setValue(username);
    passwordInput.setValue(password);
    incorrectDataError.isDisplayed();

    return this;
  }

  public RegistrationPage clickRegistrationButton() {
    registrationBtn.click();
    return new RegistrationPage();
  }
}
