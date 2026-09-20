package io.student.rcc.test.ui;

import static io.student.rcc.utils.RandomUserDataUtils.randomUserName;

import com.codeborne.selenide.Selenide;
import io.student.rcc.config.Config;
import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.model.UserJson;
import io.student.rcc.page.BasePage;
import org.junit.jupiter.api.Test;

public class RegistrationTest {

  private static final Config CFG = Config.getInstance();
  private static final String PASSWORD = "12345";

  @Test
  public void mainPageShouldBeDisplayedAfterSuccessLogin() {
    final String TITLE = "Ваши любимые картины и художники всегда рядом";
    String userName = randomUserName();

    Selenide.open(CFG.frontUrl(), BasePage.class)
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
    Selenide.open(CFG.frontUrl(), BasePage.class)
        .clickLoginButton()
        .clickRegistrationButton()
        .unSuccessRegister(user.username(), PASSWORD);
  }

  @Test
  @User
  public void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual(UserJson user) {
    Selenide.open(CFG.frontUrl(), BasePage.class)
        .clickLoginButton()
        .incorrectLogin(user.username(), PASSWORD + "12345");
  }

  @Test
  public void shouldRegisterNewUser() {
    String userName = randomUserName();

    Selenide.open(CFG.frontUrl(), BasePage.class)
        .clickLoginButton()
        .clickRegistrationButton()
        .successRegistrationUser(userName, PASSWORD)
        .clickLoginButton();
  }

  @Test
  @User
  public void userShouldStayOnLoginPageAfterLoginWithBadCredentials(UserJson user) {

    Selenide.open(CFG.frontUrl(), BasePage.class)
        .clickLoginButton()
        .incorrectLogin(user.username() + "Test", PASSWORD);
  }
}