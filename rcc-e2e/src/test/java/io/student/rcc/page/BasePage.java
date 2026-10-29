package io.student.rcc.page;

import static com.codeborne.selenide.Condition.text;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;

/**
 * Базовый page object.
 *
 * <p><b>Зачем вообще BasePage</b>: сюда кладут общие методы для всех страниц
 * (например {@link #checkAlertMessage(String)}). Написал один раз — пользуются все page objects.
 * Если общих методов нет, BasePage почти не нужен.
 *
 * <p><b>Зачем {@code T extends BasePage<T>}</b>: методы базы возвращают конкретную страницу
 * ({@code AuthenticationPage}, {@code MainPage}…), а не {@code BasePage}. Иначе после вызова метода
 * из базы цепочка «отваливается» к общему типу и методы дочерней страницы недоступны без cast.
 *
 * <p>Как объявлять страницу:
 * <pre>{@code
 * public class AuthenticationPage extends BasePage<AuthenticationPage> { ... }
 * }</pre>
 *
 * <p>Как работает в тесте:
 * <pre>{@code
 * MainPage.open(driver)
 *     .clickLoginButton()
 *     .login(user, password)
 *     .clickInProfile()
 *     .editProfile(name, lastName)
 *     .checkAlertMessage("Профиль обновлен")  // вернёт ProfilePage (T), не BasePage
 *     ; // дальше снова методы ProfilePage, если нужно
 * }</pre>
 *
 * @param <T> тип конкретной страницы (обычно сама страница)
 */
public abstract class BasePage<T extends BasePage<T>> {

  protected final SelenideDriver driver;

  /**
   * Алерт лежит в BasePage, потому что он может появиться на любой странице.
   */
  private final SelenideElement alert;

  protected BasePage(SelenideDriver driver) {
    this.driver = driver;
    this.alert = driver.$(".MuiAlert-message");
  }

  /**
   * Проверяет текст алерта и возвращает {@code this} как {@code T} для chaining.
   *
   * @param message ожидаемый текст сообщения
   * @return текущий page object как {@code T}
   */
  @SuppressWarnings("unchecked")
  public T checkAlertMessage(String message) {
    alert.should(text(message));
    return (T) this;
  }

}
