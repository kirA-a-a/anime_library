package io.student.rcc.test.ui;

import static io.student.rcc.data.rococoAuth.repository.iml.UserRepositoryHibernate.CFG;

import com.codeborne.selenide.Selenide;
import io.student.rcc.jupiter.annotation.Country;
import io.student.rcc.jupiter.annotation.Museum;
import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.model.MuseumJson;
import io.student.rcc.model.UserJson;
import io.student.rcc.page.MainPage;
import org.junit.jupiter.api.Test;

public class ConditionMuseumTest {

  @Test
  @User(
      countries = @Country,
      museums = @Museum
  )
  public void conditionMuseumTest(UserJson user, MuseumJson museum) {

    Selenide
        .open(CFG.frontUrl(), MainPage.class)
        .toMuseums()
        .openMuseumById(museum.id());

    System.out.println(museum.title());
  }

}
