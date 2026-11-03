package io.student.rcc.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static io.student.rcc.condition.ButtonConditions.color;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import io.student.rcc.condition.Color;
import io.student.rcc.config.Config;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.openqa.selenium.By;

@ParametersAreNonnullByDefault
@SuppressWarnings("UnusedReturnValue")
public class MainPage extends BasePage<MainPage> {

  public static final String URL = Config.getInstance().frontUrl();
  private final SelenideElement loginInput;
  private final SelenideElement pageContent;
  private final SelenideElement avatar;
  private final SelenideElement museumsLink;
  private final SelenideElement themeSwitch;
  private final ElementsCollection loginButtons;

  public MainPage(SelenideDriver driver) {
    super(driver);
    this.loginInput = driver.$(By.xpath("//button[contains(text(), 'Войти')]"));
    this.pageContent = driver.$("main[id='page-content']");
    this.avatar = driver.$("figure[class*='avatar']");
    this.museumsLink = driver.$("nav[aria-label='Основная навигация'] a[href='/museum']");
    this.themeSwitch = driver.$("div.lightswitch-track[aria-label='Light Switch']");
    this.loginButtons = driver.$$(By.xpath("//button[contains(text(), 'Войти')]"));
  }

  @Step("Открыть главную страницу")
  public static @Nonnull MainPage open(SelenideDriver driver) {
    driver.open(URL);
    return new MainPage(driver);
  }

  @Step("Нажать кнопку Войти")
  public @Nonnull AuthenticationPage clickLoginButton() {
    loginInput.click();
    return new AuthenticationPage(driver);
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
    return new ProfilePage(driver);
  }

  @Step("Перейти на страницу музеев")
  public @Nonnull MuseumPage toMuseums() {
    museumsLink.shouldBe(visible).click();
    return new MuseumPage(driver);
  }
}
