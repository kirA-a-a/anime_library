package io.student.rcc.page.component;

import static com.codeborne.selenide.Condition.visible;

import com.codeborne.selenide.SelenideDriver;
import io.qameta.allure.Step;
import io.student.rcc.page.MuseumPage;
import java.time.Duration;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("UnusedReturnValue")
public class Header extends BaseComponent<Header> {

  private final SelenideDriver driver;

  public Header(SelenideDriver driver) {
    super(driver.$(".app-bar"));
    this.driver = driver;
  }

  @Step("Проверить наличие аватара в шапке")
  public @Nonnull Header shouldHaveAvatar() {
    self.$("figure.avatar").shouldBe(visible, Duration.ofSeconds(15));
    return this;
  }

  @Step("Перейти на страницу музеев из шапки")
  public @Nonnull MuseumPage toMuseums() {
    self.$("a[href='/museum']").shouldBe(visible).click();
    return new MuseumPage(driver);
  }
}
