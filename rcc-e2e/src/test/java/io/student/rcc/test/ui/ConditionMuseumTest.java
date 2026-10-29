package io.student.rcc.test.ui;

import com.codeborne.selenide.SelenideDriver;
import io.student.rcc.jupiter.annotation.Country;
import io.student.rcc.jupiter.annotation.Museum;
import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.jupiter.extension.BrowserExtension;
import io.student.rcc.model.MuseumJson;
import io.student.rcc.model.UserJson;
import io.student.rcc.page.MainPage;
import io.student.rcc.utils.SelenideUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class ConditionMuseumTest {

  @RegisterExtension
  private final BrowserExtension browserExtension = new BrowserExtension();
  private final SelenideDriver selenideDriver = new SelenideDriver(SelenideUtils.chromeConfig);

  @Test
  @User(
      countries = @Country,
      museums = @Museum
  )
  public void conditionMuseumTest(UserJson user, MuseumJson museum) {
    browserExtension.getDrivers().add(selenideDriver);

    MainPage.open(selenideDriver)
        .toMuseums()
        .openMuseumById(museum.id());

    System.out.println(museum.title());
  }

}
