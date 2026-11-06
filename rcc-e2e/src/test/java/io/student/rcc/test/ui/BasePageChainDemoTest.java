package io.student.rcc.test.ui;

import static io.student.rcc.utils.RandomUserDataUtils.randomCity;
import static io.student.rcc.utils.RandomUserDataUtils.randomMuseumTitle;
import static io.student.rcc.utils.RandomUserDataUtils.randomName;
import static io.student.rcc.utils.RandomUserDataUtils.randomSentence;

import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.jupiter.annotation.meta.WebTest;
import io.student.rcc.model.UserJson;
import io.student.rcc.page.BasePage;
import io.student.rcc.page.MainPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Примеры chaining с {@link BasePage#checkAlertMessage(String)} — см. JavaDoc у {@link BasePage}.
 */
@WebTest
public class BasePageChainDemoTest {

  private static final String PASSWORD = "12345";
  private static final String PHOTO = "images/museum.png";
  private static final String COUNTRY = "Россия";

  @Test
  @User
  @DisplayName("после редактирования профиля checkAlertMessage проверяет текст и сохраняет тип")
  void checkAlertMessageAfterProfileEdit(UserJson user) {
    String firstname = randomName();
    String lastname = randomName();

    MainPage.open()
        .clickLoginButton()
        .login(user.username(), PASSWORD)
        .clickInProfile()
        .editProfile(firstname, lastname)
        .checkAlertMessage("Профиль обновлен");
  }

  @Test
  @User
  @DisplayName("цепочка MainPage → музей → checkAlertMessage с текстом алерта")
  void checkAlertMessageAfterMuseumCreate(UserJson user) {
    String title = randomMuseumTitle();
    String city = randomCity();
    String description = randomSentence(8);

    MainPage.open()
        .clickLoginButton()
        .login(user.username(), PASSWORD)
        .toMuseums()
        .openCreateMuseumForm()
        .createMuseum(title, COUNTRY, city, description, PHOTO)
        .submitForm()
        .checkAlertMessage("Добавлен музей: " + title);
  }

  @Test
  @User
  @DisplayName("checkAlertMessage возвращает T — после алерта снова доступны методы MuseumPage")
  void checkAlertMessageKeepsConcretePageType(UserJson user) {
    String title = randomMuseumTitle();
    String city = randomCity();
    String description = randomSentence(8);

    MainPage.open()
        .clickLoginButton()
        .login(user.username(), PASSWORD)
        .toMuseums()
        .openCreateMuseumForm()
        .createMuseum(title, COUNTRY, city, description, PHOTO)
        .submitForm()
        .checkAlertMessage("Добавлен музей: " + title)
        .checkMuseumCard(title);
  }
}
