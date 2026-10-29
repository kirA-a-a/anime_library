package io.student.rcc.page;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.attributeMatching;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideDriver;
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
public class MuseumPage extends BasePage<MuseumPage> {

  private static final Config CFG = Config.getInstance();
  public static final String URL = CFG.frontUrl() + "museum";
  private final Header header;
  private final ElementsCollection museumListCards;
  private final SelenideElement pageTitle;
  private final SelenideElement addMuseumButton;
  private final SelenideElement editMuseumButton;
  private final SelenideElement form;
  private final SelenideElement titleInput;
  private final SelenideElement cityInput;
  private final SelenideElement descriptionInput;
  private final SelenideElement countrySelect;
  private final SelenideElement photoInput;
  private final SelenideElement submitButton;
  private final SelenideElement museumCard;
  private final SelenideElement museumTitle;
  private final SelenideElement museumLocation;
  private final SelenideElement museumDescription;
  private final SelenideElement museumPhoto;

  public MuseumPage(SelenideDriver driver) {
    super(driver);
    this.header = new Header(driver);
    this.museumListCards = driver.$$("[data-testid='museum-card']");
    this.pageTitle = driver.$("h2");
    this.addMuseumButton = driver.$(By.xpath("//button[text()='Добавить музей']"));
    this.editMuseumButton = driver.$("[data-testid='edit-museum']");
    this.form = driver.$("form.modal-form");
    this.titleInput = driver.$("input[name='title']");
    this.cityInput = driver.$("input[name='city']");
    this.descriptionInput = driver.$("textarea[name='description']");
    this.countrySelect = driver.$("select[name='countryId']");
    this.photoInput = driver.$("input[name='photo']");
    this.submitButton = driver.$("form.modal-form button[type='submit']");
    this.museumCard = driver.$("article.card");
    this.museumTitle = museumCard.$("header.card-header");
    this.museumLocation = museumCard.$("header.card-header + div");
    this.museumDescription = museumCard.$("div.w-56 + div");
    this.museumPhoto = driver.$("img.my-4");
  }

  @Step("Открыть список музеев")
  public static @Nonnull MuseumPage open(SelenideDriver driver) {
    driver.open(URL);
    return new MuseumPage(driver);
  }

  @Step("Открыть страницу музея кликом по карточке: {museumId}")
  public static @Nonnull MuseumPage open(SelenideDriver driver, UUID museumId) {
    return open(driver).openMuseumById(museumId);
  }

  @Step("Открыть музей кликом по карточке: {museumId}")
  public @Nonnull MuseumPage openMuseumById(UUID museumId) {
    museumListCards
        .findBy(attribute("data-museum-id", museumId.toString()))
        .shouldBe(visible, Duration.ofSeconds(10))
        .click();
    return this;
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
    Toast.withMessage(driver, message).shouldBeVisible();
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
    ItemCard.byTitle(driver, title).shouldBeVisible().shouldHaveTitle(title);
    return this;
  }

  @Step("Дождаться загрузки фото музея на странице деталей")
  public @Nonnull MuseumPage waitForMuseumPhotoLoaded() {
    museumPhoto.shouldBe(visible, Duration.ofSeconds(10));
    museumPhoto.shouldHave(attributeMatching("src", "data:image/.*"), Duration.ofSeconds(10));
    driver.executeJavaScript(
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
