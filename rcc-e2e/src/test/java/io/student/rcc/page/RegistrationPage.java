package io.student.rcc.page;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

public class RegistrationPage {

  private final SelenideElement userNameInput = $(By.xpath("//input[@id='username']"));
  private final SelenideElement passwordInput = $(By.xpath("//input[@id='password']"));
  private final SelenideElement passwordSubmitInput = $(By.xpath("//input[@id='passwordSubmit']"));
  private final SelenideElement submitBtn = $(By.xpath("//button[@type='submit']"));
  private final SelenideElement signInBtn = $(By.xpath("//a[contains(text(), 'Войти в систему')]"));
  private final SelenideElement userNameMessageError = $(By.xpath("//span[contains(@class, 'error__username')]"));

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
