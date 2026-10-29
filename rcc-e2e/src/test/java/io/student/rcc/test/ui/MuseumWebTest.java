package io.student.rcc.test.ui;

import static io.student.rcc.utils.RandomUserDataUtils.randomCity;
import static io.student.rcc.utils.RandomUserDataUtils.randomMuseumTitle;
import static io.student.rcc.utils.RandomUserDataUtils.randomName;
import static io.student.rcc.utils.RandomUserDataUtils.randomSentence;

import com.codeborne.selenide.SelenideDriver;
import io.student.rcc.jupiter.annotation.Country;
import io.student.rcc.jupiter.annotation.Museum;
import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.jupiter.annotation.meta.WebTest;
import io.student.rcc.jupiter.extension.BrowserExtension;
import io.student.rcc.model.MuseumJson;
import io.student.rcc.model.UserJson;
import io.student.rcc.page.MainPage;
import io.student.rcc.page.component.Header;
import io.student.rcc.utils.SelenideUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@WebTest
public class MuseumWebTest {

  private static final String PASSWORD = "12345";
  private static final String PHOTO = "images/museum.png";
  private static final String COUNTRY = "Россия";

  @RegisterExtension
  private final BrowserExtension browserExtension = new BrowserExtension();
  private final SelenideDriver selenideDriver = new SelenideDriver(SelenideUtils.chromeConfig);

  @Test
  @User
  @DisplayName("Авторизованный пользователь редактирует профиль и видит toast об успешном обновлении")
  void shouldEditProfile(UserJson user) {
    String firstname = randomName();
    String lastname = randomName();

    browserExtension.getDrivers().add(selenideDriver);

    MainPage.open(selenideDriver)
        .clickLoginButton()
        .login(user.username(), PASSWORD)
        .clickInProfile()
        .editProfile(firstname, lastname)
        .checkToast("Профиль обновлен");
  }

  @Test
  @User
  @DisplayName("Авторизованный пользователь создаёт музей и видит карточку с новым названием в списке")
  void shouldCreateMuseum(UserJson user) {
    String title = randomMuseumTitle();
    String city = randomCity();
    String description = randomSentence(8);

    browserExtension.getDrivers().add(selenideDriver);

    MainPage.open(selenideDriver)
        .clickLoginButton()
        .login(user.username(), PASSWORD)
        .toMuseums()
        .checkPageTitle()
        .openCreateMuseumForm()
        .createMuseum(title, COUNTRY, city, description, PHOTO)
        .checkMuseumCard(title);
  }

  @Test
  @User(
      countries = @Country,
      museums = @Museum
  )
  @DisplayName("Авторизованный пользователь открывает страницу музея и редактирует его данные")
  void shouldEditMuseum(UserJson user, MuseumJson museum) {
    String newTitle = randomMuseumTitle();
    String newCity = randomCity();
    String newDescription = randomSentence(8);

    browserExtension.getDrivers().add(selenideDriver);

    MainPage.open(selenideDriver)
        .clickLoginButton()
        .login(user.username(), PASSWORD)
        .toMuseums()
        .openMuseumById(museum.id())
        .editMuseum(newTitle, COUNTRY, newCity, newDescription)
        .checkToast("Обновлен музей: " + newTitle)
        .checkMuseumDetails(newTitle, newCity, newDescription);
  }

  @ParameterizedTest(name = "boundary: title={0}, description={1}, city={2}")
  @User
  @DisplayName("Форма создания музея показывает ошибки валидации на граничных значениях полей")
  @CsvSource({
      "ab, Valid museum description text, Paris, Название не может быть короче 3 символов",
      "Valid title, short, Paris, Описание не может быть короче 10 символов",
      "Valid title, Valid museum description text, ab, Город не может быть короче 3 символов",
      "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, Valid museum description text, Paris, Название не может быть длиннее 255 символов"
  })
  void shouldShowMuseumFormBoundaryErrors(
      String title,
      String description,
      String city,
      String expectedError,
      UserJson user
  ) {
    browserExtension.getDrivers().add(selenideDriver);

    MainPage.open(selenideDriver)
        .clickLoginButton()
        .login(user.username(), PASSWORD);

    new Header(selenideDriver).toMuseums()
        .openCreateMuseumForm()
        .setTitle(title)
        .selectCountry(COUNTRY)
        .setCity(city)
        .uploadPhoto(PHOTO)
        .setDescription(description)
        .submitForm()
        .checkFormError(expectedError);
  }
}
