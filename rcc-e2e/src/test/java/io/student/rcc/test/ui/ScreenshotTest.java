package io.student.rcc.test.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;

import io.student.rcc.jupiter.annotation.ApiLogin;
import io.student.rcc.jupiter.annotation.Country;
import io.student.rcc.jupiter.annotation.Museum;
import io.student.rcc.jupiter.annotation.ScreenShotTest;
import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.jupiter.extension.BrowserExtension;
import io.student.rcc.model.MuseumJson;
import io.student.rcc.model.UserJson;
import io.student.rcc.page.MainPage;
import io.student.rcc.page.MuseumPage;
import io.student.rcc.page.ProfilePage;
import io.student.rcc.utils.ScreenDiffResult;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class ScreenshotTest {

  private static final String MUSEUM_PHOTO = "images/museum-image.png";

  @User
  @ApiLogin
  @ScreenShotTest("images/expected-avatar.png")
  public void checkAvatar(UserJson user, BufferedImage expected) throws IOException {
    final String TITLE = "Ваши любимые картины и художники всегда рядом";

    new MainPage()
        .checkTitle(TITLE)
        .clickInProfile()
        .waitForModalOpen()
        .checkUserTag("@" + user.username())
        .uploadAvatar("images/avatar.jpg")
        .saveAndClose();

    new ProfilePage().checkToast("Профиль обновлен");

    new MainPage()
        .clickInProfile()
        .waitForModalOpen()
        .shouldDisplayAvatar()
        .waitForAvatarLoaded();

    BufferedImage actual = new ProfilePage().takeAvatarScreenshot();
    assertFalse(new ScreenDiffResult(expected, actual), "Screen comparison failure");
  }

  @User(
      countries = @Country,
      museums = @Museum
  )
  @ApiLogin
  @ScreenShotTest("images/expected-museum-before-rewrite.png")
  public void checkMuseumAvatar(MuseumJson museum, BufferedImage expected) throws IOException {

    new MainPage()
        .toMuseums()
        .openMuseumById(museum.id())
        .openEditMuseumForm()
        .uploadPhoto(MUSEUM_PHOTO)
        .submitForm()
        .checkToast("Обновлен музей: " + museum.title())
        .waitForMuseumPhotoLoaded();

    BufferedImage actual = new MuseumPage().takeMuseumPhotoScreenshot();
    assertFalse(new ScreenDiffResult(expected, actual), "Screen comparison failure");
  }

}
