package io.student.rcc.page.component;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import io.qameta.allure.Step;
import io.student.rcc.page.MuseumPage;
import java.time.Duration;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("UnusedReturnValue")
public class Header extends BaseComponent<Header> {

  public Header() {
    super($(".app-bar"));
  }

  @Step("Проверить наличие аватара в шапке")
  public @Nonnull Header shouldHaveAvatar() {
    self.$("figure.avatar").shouldBe(visible, Duration.ofSeconds(15));
    return this;
  }

  @Step("Перейти на страницу музеев из шапки")
  public @Nonnull MuseumPage toMuseums() {
    self.$("a[href='/museum']").shouldBe(visible).click();
    return new MuseumPage();
  }
}
