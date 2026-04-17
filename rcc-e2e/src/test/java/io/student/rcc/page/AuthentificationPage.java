package io.student.rcc.page;


import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

public class AuthentificationPage {

  private final SelenideElement userNameInput = $(By.xpath("//input[@name='username']"));
  private final SelenideElement passwordInput = $(By.xpath("//input[@name='password']"));
  private final SelenideElement submitBtn = $(By.xpath("//button[@type='submit']"));
  private final SelenideElement registrationBtn = $(By.xpath("//a[@href='/register']"));
  private final SelenideElement incorrectDataError = $(By.xpath("//p[contains(@class, 'login__error')]"));


  public MainPage login(String username, String password) {
    userNameInput.setValue(username);
    passwordInput.setValue(password);
    submitBtn.click();

    return new MainPage();
  }

  public AuthentificationPage incorrectLogin(String username, String password) {
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
