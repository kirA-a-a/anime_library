package io.student.rcc.page;


import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

public class MainPage {

  private final SelenideElement loginInput = $(By.xpath("//button[contains(text(), 'Войти')]"));

  private final SelenideElement pageContent = $(By.xpath("//main[@id='page-content']"));

  public AuthentificationPage clickLoginButton() {
    loginInput.click();
    return new AuthentificationPage();
  }

  public MainPage checkTitle(String title) {
    pageContent.shouldBe(text(title));

    return this;
  }
}
