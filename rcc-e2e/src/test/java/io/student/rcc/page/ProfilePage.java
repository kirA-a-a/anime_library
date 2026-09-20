package io.student.rcc.page;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import io.student.rcc.page.component.Toast;
import java.time.Duration;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("UnusedReturnValue")
public class ProfilePage {

  private final SelenideElement form = $("form.modal-form"),
      firstnameInput = $("input[name='firstname']"),
      lastnameInput = $("input[name='surname']"),
      submitButton = form.$("button[type='submit']"),
      userTag = form.$("h4");

  @Step("Проверить username в профиле: {userTag}")
  public @Nonnull ProfilePage checkUserTag(String userTag) {
    this.userTag.shouldBe(text(userTag));
    return this;
  }

  @Step("Заполнить имя: {firstname}")
  public @Nonnull ProfilePage setFirstname(String firstname) {
    firstnameInput.shouldBe(visible).setValue(firstname);
    return this;
  }

  @Step("Заполнить фамилию: {lastname}")
  public @Nonnull ProfilePage setLastname(String lastname) {
    lastnameInput.shouldBe(visible).setValue(lastname);
    return this;
  }

  @Step("Сохранить профиль")
  public @Nonnull ProfilePage save() {
    submitButton.click();
    form.should(disappear, Duration.ofSeconds(10));
    return this;
  }

  @Step("Проверить toast: {message}")
  public @Nonnull ProfilePage checkToast(String message) {
    Toast.withMessage(message).shouldBeVisible();
    return this;
  }

  @Step("Редактировать профиль: {firstname} {lastname}")
  public @Nonnull ProfilePage editProfile(String firstname, String lastname) {
    return setFirstname(firstname)
        .setLastname(lastname)
        .save();
  }
}
