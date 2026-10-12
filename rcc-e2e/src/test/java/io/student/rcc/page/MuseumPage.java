package io.student.rcc.page;

import static com.codeborne.selenide.Condition.attributeMatching;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import io.student.rcc.config.Config;
import io.student.rcc.page.component.Header;
import io.student.rcc.page.component.ItemCard;
import io.student.rcc.page.component.Toast;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

@ParametersAreNonnullByDefault
@SuppressWarnings("UnusedReturnValue")
public class MuseumPage extends BasePage<MuseumPage>{

  private static final Config CFG = Config.getInstance();
  public static final String URL = CFG.frontUrl() + "museum";
  private final Header header = new Header();
  private final SelenideElement
      pageTitle = $("h2"),
      addMuseumButton = $(By.xpath("//button[text()='Добавить музей']")),
      editMuseumButton = $("[data-testid='edit-museum']"),
      form = $("form.modal-form"),
      titleInput = $("input[name='title']"),
      cityInput = $("input[name='city']"),
      descriptionInput = $("textarea[name='description']"),
      countrySelect = $("select[name='countryId']"),
      photoInput = $("input[name='photo']"),
      submitButton = $("form.modal-form button[type='submit']"),
      museumCard = $("article.card"),
      museumTitle = museumCard.$("header.card-header"),
      museumLocation = museumCard.$("header.card-header + div"),
      museumDescription = museumCard.$("div.w-56 + div"),
      museumPhoto = $("img.my-4");

  @Step("Открыть страницу музея: {museumId}")
  public static @Nonnull MuseumPage open(UUID museumId) {
    return Selenide.open(URL + "/" + museumId, MuseumPage.class);
  }

  @Step("Проверить заголовок страницы музеев")
  public @Nonnull MuseumPage checkPageTitle() {
    pageTitle.shouldHave(text("Музеи"));
    return this;
  }

  @Step("Открыть форму создания музея")
  public @Nonnull MuseumPage openCreateMuseumForm() {
    addMuseumButton.shouldBe(visible).click();
    form.shouldBe(visible);
    countrySelect.$("option").shouldBe(visible, Duration.ofSeconds(10));
    return this;
  }

  @Step("Открыть форму редактирования музея")
  public @Nonnull MuseumPage openEditMuseumForm() {
    header.shouldHaveAvatar();
    editMuseumButton.shouldBe(visible, Duration.ofSeconds(10)).click();
    form.shouldBe(visible);
    countrySelect.$("option").shouldBe(visible, Duration.ofSeconds(10));
    return this;
  }

  @Step("Заполнить название музея: {title}")
  public @Nonnull MuseumPage setTitle(String title) {
    type(titleInput, title);
    return this;
  }

  @Step("Заполнить город: {city}")
  public @Nonnull MuseumPage setCity(String city) {
    type(cityInput, city);
    return this;
  }

  @Step("Заполнить описание: {description}")
  public @Nonnull MuseumPage setDescription(String description) {
    type(descriptionInput, description);
    return this;
  }

  @Step("Выбрать страну: {country}")
  public @Nonnull MuseumPage selectCountry(String country) {
    countrySelect.shouldBe(visible).selectOption(country);
    return this;
  }

  @Step("Загрузить фото музея: {classpathPath}")
  public @Nonnull MuseumPage uploadPhoto(String classpathPath) {
    photoInput.uploadFromClasspath(classpathPath);
    return this;
  }

  @Step("Отправить форму музея")
  public @Nonnull MuseumPage submitForm() {
    submitButton.shouldBe(visible).click();
    return this;
  }

  @Step("Создать музей {title}")
  public @Nonnull MuseumPage createMuseum(
      String title,
      String country,
      String city,
      String description,
      String photoClasspath
  ) {
    setTitle(title)
        .selectCountry(country)
        .setCity(city)
        .uploadPhoto(photoClasspath)
        .setDescription(description);
    return this;
  }

  @Step("Редактировать музей: {title}")
  public @Nonnull MuseumPage editMuseum(String title, String country, String city, String description) {
    openEditMuseumForm()
        .setTitle(title)
        .selectCountry(country)
        .setCity(city)
        .setDescription(description)
        .submitForm();
    return this;
  }

  @Step("Проверить toast: {message}")
  public @Nonnull MuseumPage checkToast(String message) {
    Toast.withMessage(message).shouldBeVisible();
    return this;
  }

  @Step("Проверить данные музея: {title}, {city}")
  public @Nonnull MuseumPage checkMuseumDetails(String title, String city, String description) {
    museumTitle.shouldHave(text(title));
    museumLocation.shouldHave(text(city));
    museumDescription.shouldHave(text(description));
    return this;
  }

  @Step("Проверить ошибку на форме: {error}")
  public @Nonnull MuseumPage checkFormError(String error) {
    form.$$(".text-error-400").findBy(text(error)).shouldBe(visible, Duration.ofSeconds(5));
    return this;
  }

  @Step("Проверить карточку музея: {title}")
  public @Nonnull MuseumPage checkMuseumCard(String title) {
    ItemCard.byTitle(title).shouldBeVisible().shouldHaveTitle(title);
    return this;
  }

  @Step("Дождаться загрузки фото музея на странице деталей")
  public @Nonnull MuseumPage waitForMuseumPhotoLoaded() {
    museumPhoto.shouldBe(visible, Duration.ofSeconds(10));
    museumPhoto.shouldHave(attributeMatching("src", "data:image/.*"), Duration.ofSeconds(10));
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
        museumPhoto
    );
    return this;
  }

  @Step("Сделать скриншот фото музея")
  public @Nonnull BufferedImage takeMuseumPhotoScreenshot() throws IOException {
    museumPhoto.shouldBe(visible, Duration.ofSeconds(10));
    museumPhoto.shouldHave(attributeMatching("src", "data:image/.*"), Duration.ofSeconds(10));

    String src = Objects.requireNonNull(museumPhoto.getAttribute("src"));
    byte[] bytes = Base64.getDecoder().decode(src.substring(src.indexOf(',') + 1));
    return ImageIO.read(new ByteArrayInputStream(bytes));
  }

  private static void type(SelenideElement element, String value) {
    element.shouldBe(visible).click();
    element.sendKeys(Keys.chord(Keys.COMMAND, "a"));
    element.sendKeys(Keys.BACK_SPACE);
    element.sendKeys(value);
  }
}
