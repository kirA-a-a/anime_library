package io.student.rcc.test.ui;

import static io.student.rcc.utils.RandomUserDataUtils.randomCity;
import static io.student.rcc.utils.RandomUserDataUtils.randomMuseumTitle;
import static io.student.rcc.utils.RandomUserDataUtils.randomName;
import static io.student.rcc.utils.RandomUserDataUtils.randomSentence;

import io.student.rcc.jupiter.annotation.ApiLogin;
import io.student.rcc.jupiter.annotation.Country;
import io.student.rcc.jupiter.annotation.Museum;
import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.jupiter.annotation.meta.WebTest;
import io.student.rcc.model.MuseumJson;
import io.student.rcc.model.UserJson;
import io.student.rcc.page.MainPage;
import io.student.rcc.page.ProfilePage;
import io.student.rcc.page.component.Header;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@WebTest
public class MuseumWebTest {

  private static final String PASSWORD = "12345";
  private static final String PHOTO = "images/museum.png";
  private static final String COUNTRY = "Россия";

  @Test
  @User
  @DisplayName("Авторизованный пользователь редактирует профиль и видит toast об успешном обновлении")
  @ApiLogin
  void shouldEditProfile() {
    String firstname = randomName();
    String lastname = randomName();

    new MainPage()
        .clickInProfile()
        .editProfile(firstname, lastname)
        .checkToast("Профиль обновлен");
  }

  @Test
  @User
  @DisplayName("Авторизованный пользователь создаёт музей и видит карточку с новым названием в списке")
  @ApiLogin
  void shouldCreateMuseum() {
    String title = randomMuseumTitle();
    String city = randomCity();
    String description = randomSentence(8);

    new MainPage()
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
  @ApiLogin
  @DisplayName("Авторизованный пользователь открывает страницу музея и редактирует его данные")
  void shouldEditMuseum(MuseumJson museum) {
    String newTitle = randomMuseumTitle();
    String newCity = randomCity();
    String newDescription = randomSentence(8);

    new MainPage()
        .toMuseums()
        .openMuseumById(museum.id())
        .editMuseum(newTitle, COUNTRY, newCity, newDescription)
        .checkToast("Обновлен музей: " + newTitle)
        .checkMuseumDetails(newTitle, newCity, newDescription);
  }

  @ParameterizedTest(name = "boundary: title={0}, description={1}, city={2}")
  @User
  @ApiLogin
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
      String expectedError
  )
  {
    new Header().toMuseums()
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
