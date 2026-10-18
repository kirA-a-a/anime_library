package io.student.rcc.test.ui;

import static io.student.rcc.data.rococoAuth.repository.iml.UserRepositoryHibernate.CFG;

import com.codeborne.selenide.Selenide;
import io.student.rcc.condition.Color;
import io.student.rcc.page.MainPage;
import org.junit.jupiter.api.Test;

public class ColorTest {

  @Test
  public void checkButtonPurpleColor() {
    Selenide.open(CFG.frontUrl(), MainPage.class)
        .checkColorLoginInput(Color.PURPLE);
  }

  @Test
  public void checkButtonYellowColor() {
    Selenide.open(CFG.frontUrl(), MainPage.class)
        .clickThemeSwitch()
        .checkColorLoginInput(Color.YELLOW);
  }

  @Test
  public void checkButtonColors() {
    Selenide.open(CFG.frontUrl(), MainPage.class)
        .clickThemeSwitch()
        .checkColorsLoginInput(Color.YELLOW, Color.PURPLE);
  }

}
