package io.student.rcc.page;


import static com.codeborne.selenide.Condition.text;
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
public class BasePage {

  public static final String URL = Config.getInstance().frontUrl();

  private final SelenideElement
      loginInput = $(By.xpath("//button[contains(text(), 'Войти')]")),
      pageContent = $("main[id='page-content']"),
      avatar = $("figure[class*='avatar']"),
      museumsLink = $("nav[aria-label='Основная навигация'] a[href='/museum']");

  @Step("Открыть главную страницу")
  public static @Nonnull BasePage open() {
    return Selenide.open(URL, BasePage.class);
  }

  @Step("Нажать кнопку Войти")
  public @Nonnull AuthenticationPage clickLoginButton() {
    loginInput.click();
    return new AuthenticationPage();
  }

  @Step("Проверить заголовок: {title}")
  public @Nonnull BasePage checkTitle(String title) {
    pageContent.shouldBe(text(title));

    return this;
  }

  @Step("Открыть профиль")
  public @Nonnull ProfilePage clickInProfile() {
    avatar.shouldBe(visible).click();
    return new ProfilePage();
  }

  @Step("Перейти на страницу музеев")
  public @Nonnull MuseumPage toMuseums() {
    museumsLink.shouldBe(visible).click();
    return new MuseumPage();
  }
}
