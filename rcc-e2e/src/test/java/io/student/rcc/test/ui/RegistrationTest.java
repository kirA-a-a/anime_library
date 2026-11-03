package io.student.rcc.test.ui;

import static io.student.rcc.utils.RandomUserDataUtils.randomUserName;

import com.codeborne.selenide.SelenideDriver;
import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.jupiter.extension.BrowserExtension;
import io.student.rcc.model.UserJson;
import io.student.rcc.page.MainPage;
import io.student.rcc.utils.SelenideUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class RegistrationTest {

  private static final String PASSWORD = "12345";

  @RegisterExtension
  private final BrowserExtension browserExtension = new BrowserExtension();
  private final SelenideDriver selenideDriver = new SelenideDriver(SelenideUtils.chromeConfig);

  @Test
  public void mainPageShouldBeDisplayedAfterSuccessLogin() {
    final String TITLE = "Ваши любимые картины и художники всегда рядом";
    String userName = randomUserName();

    browserExtension.getDrivers().add(selenideDriver);

    MainPage.open(selenideDriver)
        .clickLoginButton()
        .clickRegistrationButton()
        .successRegistrationUser(userName, PASSWORD)
        .clickLoginButton()
        .login(userName, PASSWORD)
        .checkTitle(TITLE)
        .clickInProfile()
        .checkUserTag("@" + userName);
  }

  @Test
  @User
  public void shouldNotRegisterUserWithExistingUsername(UserJson user) {
    browserExtension.getDrivers().add(selenideDriver);

    MainPage.open(selenideDriver)
        .clickLoginButton()
        .clickRegistrationButton()
        .unSuccessRegister(user.username(), PASSWORD);
  }

  @Test
  @User
  public void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual(UserJson user) {
    browserExtension.getDrivers().add(selenideDriver);

    MainPage.open(selenideDriver)
        .clickLoginButton()
        .incorrectLogin(user.username(), PASSWORD + "12345");
  }

  @Test
  public void shouldRegisterNewUser() {
    String userName = randomUserName();

    browserExtension.getDrivers().add(selenideDriver);

    MainPage.open(selenideDriver)
        .clickLoginButton()
        .clickRegistrationButton()
        .successRegistrationUser(userName, PASSWORD)
        .clickLoginButton();
  }

  @Test
  @User
  public void userShouldStayOnLoginPageAfterLoginWithBadCredentials(UserJson user) {
    browserExtension.getDrivers().add(selenideDriver);

    MainPage.open(selenideDriver)
        .clickLoginButton()
        .incorrectLogin(user.username() + "Test", PASSWORD);
  }
}
