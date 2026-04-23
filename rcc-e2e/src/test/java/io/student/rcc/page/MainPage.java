package io.student.rcc.page;


import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

public class MainPage {

  private final SelenideElement
      loginInput = $(By.xpath("//button[contains(text(), 'Войти')]")),
      pageContent = $("main[id='page-content']"),
      avatar = $("figure[class*='avatar']");

  public AuthenticationPage clickLoginButton() {
    loginInput.click();
    return new AuthenticationPage();
  }

  public MainPage checkTitle(String title) {
    pageContent.shouldBe(text(title));

    return this;
  }

  public ProfileModal clickInProfile() {
    avatar.shouldBe(visible).click();
    return new ProfileModal();
  }
}
