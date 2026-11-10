package io.student.rcc.page;


import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static io.student.rcc.condition.ButtonConditions.color;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import io.student.rcc.condition.Color;
import io.student.rcc.config.Config;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.openqa.selenium.By;

@ParametersAreNonnullByDefault
@SuppressWarnings("UnusedReturnValue")
public class MainPage extends BasePage<MainPage>{

  public static final String URL = Config.getInstance().frontUrl();

  private final SelenideElement
      loginInput = $(By.xpath("//button[contains(text(), 'Войти')]")),
      pageContent = $("main[id='page-content']"),
      avatar = $("figure[class*='avatar']"),
      museumsLink = $("nav[aria-label='Основная навигация'] a[href='/museum']"),
      themeSwitch = $("div.lightswitch-track[aria-label='Light Switch']");

  private final ElementsCollection
      loginButtons = $$(By.xpath("//button[contains(text(), 'Войти')]"));

  @Step("Открыть главную страницу")
  public static @Nonnull MainPage open() {
    return Selenide.open(URL, MainPage.class);
  }

  @Step("Нажать кнопку Войти")
  public @Nonnull AuthenticationPage clickLoginButton() {
    loginInput.click();
    return new AuthenticationPage();
  }

  @Step("Проверить цвет кнопки 'Войти' ")
  public @Nonnull MainPage checkColorLoginInput(Color color) {
    loginInput.should(color(color));
    return this;
  }

  @Step("Проверить цвета кнопок 'Войти'")
  public @Nonnull MainPage checkColorsLoginInput(Color... colors) {
    loginButtons.should(color(colors));
    return this;
  }

  @Step("Переключить тему сайта")
  public @Nonnull MainPage clickThemeSwitch() {
    themeSwitch.shouldBe(visible).click();
    return this;
  }

  @Step("Проверить заголовок: {title}")
  public @Nonnull MainPage checkTitle(String title) {
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
