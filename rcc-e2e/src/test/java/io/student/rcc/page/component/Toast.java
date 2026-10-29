package io.student.rcc.page.component;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import java.time.Duration;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("UnusedReturnValue")
public class Toast extends BaseComponent<Toast> {

  public Toast(SelenideElement self) {
    super(self);
  }

  @Step("Найти toast с текстом: {message}")
  public static @Nonnull Toast withMessage(SelenideDriver driver, String message) {
    return new Toast(driver.$$(".toast").findBy(text(message)));
  }

  @Step("Проверить, что toast виден")
  public @Nonnull Toast shouldBeVisible() {
    self.shouldBe(visible, Duration.ofSeconds(10));
    return this;
  }
}
