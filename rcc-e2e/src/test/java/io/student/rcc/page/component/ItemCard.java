package io.student.rcc.page.component;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("UnusedReturnValue")
public class ItemCard extends BaseComponent<ItemCard> {

  public ItemCard(SelenideElement self) {
    super(self);
  }

  @Step("Найти карточку по названию: {title}")
  public static @Nonnull ItemCard byTitle(SelenideDriver driver, String title) {
    return new ItemCard(driver.$$("ul.grid li").findBy(text(title)));
  }

  @Step("Проверить, что карточка видна")
  public @Nonnull ItemCard shouldBeVisible() {
    self.shouldBe(visible);
    return this;
  }

  @Step("Проверить название карточки: {title}")
  public @Nonnull ItemCard shouldHaveTitle(String title) {
    self.shouldHave(text(title));
    return this;
  }
}
