package io.student.rcc.page;

import static com.codeborne.selenide.Condition.attributeMatching;
import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import io.student.rcc.page.component.Toast;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Base64;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;

@ParametersAreNonnullByDefault
@SuppressWarnings("UnusedReturnValue")
public class ProfilePage extends BasePage<ProfilePage> {

  private final SelenideElement form = $("form.modal-form"),
      firstnameInput = $("input[name='firstname']"),
      lastnameInput = $("input[name='surname']"),
      submitButton = form.$("button[type='submit']"),
      userTag = form.$("h4"),
      chooseFileInput = form.$("input[type=file]");

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

  @Step("Загрузить аватар: {classpathPath}")
  public @Nonnull ProfilePage uploadAvatar(String classpathPath) {
    chooseFileInput.uploadFromClasspath(classpathPath);
    return this;
  }

  @Step("Сохранить профиль")
  public @Nonnull ProfilePage save() {
    submitButton.click();
    form.should(disappear, Duration.ofSeconds(10));
    return this;
  }

  @Step("Сохранить профиль и дождаться закрытия модалки")
  public @Nonnull MainPage saveAndClose() {
    save();
    return new MainPage();
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

  @Step("Дождаться открытия модального окна профиля")
  public @Nonnull ProfilePage waitForModalOpen() {
    form.shouldBe(visible, Duration.ofSeconds(10));
    return this;
  }

  @Step("Дождаться закрытия модального окна профиля")
  public @Nonnull ProfilePage waitForModalClose() {
    form.shouldBe(hidden, Duration.ofSeconds(10));
    return this;
  }

  @Step("Проверить, что отображается аватар")
  public @Nonnull ProfilePage shouldDisplayAvatar() {
    visibleAvatar().shouldBe(visible, Duration.ofSeconds(10));
    return this;
  }

  @Step("Дождаться полной загрузки аватара")
  public @Nonnull ProfilePage waitForAvatarLoaded() {
    SelenideElement avatar = visibleAvatar();
    avatar.shouldBe(visible, Duration.ofSeconds(10));
    avatar.shouldHave(attributeMatching("src", "data:image/.*"), Duration.ofSeconds(10));

    Selenide.executeJavaScript(
        """
            const img = arguments[0];
            if (!img.complete || img.naturalWidth === 0) {
              return new Promise((resolve) => {
                img.onload = () => resolve(true);
                img.onerror = () => resolve(false);
                setTimeout(() => resolve(false), 10000);
              });
            }
            return true;
            """,
        avatar
    );
    return this;
  }

  @Step("Сделать скриншот аватара")
  public @Nonnull BufferedImage takeAvatarScreenshot() throws IOException {
    SelenideElement avatar = visibleAvatar();
    avatar.shouldBe(visible, Duration.ofSeconds(10));
    avatar.shouldHave(attributeMatching("src", "data:image/.*"), Duration.ofSeconds(10));

    String src = Objects.requireNonNull(avatar.getAttribute("src"));
    byte[] bytes = Base64.getDecoder().decode(src.substring(src.indexOf(',') + 1));
    return ImageIO.read(new ByteArrayInputStream(bytes));
  }

  private @Nonnull SelenideElement visibleAvatar() {
    return $$(".modal .avatar-image").find(Condition.visible);
  }
}
