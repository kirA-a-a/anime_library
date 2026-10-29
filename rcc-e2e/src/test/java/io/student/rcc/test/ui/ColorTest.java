package io.student.rcc.test.ui;

import com.codeborne.selenide.SelenideDriver;
import io.student.rcc.condition.Color;
import io.student.rcc.jupiter.extension.BrowserExtension;
import io.student.rcc.page.MainPage;
import io.student.rcc.utils.SelenideUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class ColorTest {
  @RegisterExtension
  private static final BrowserExtension browserExtension = new BrowserExtension();
  private final SelenideDriver chromeDriver = new SelenideDriver(SelenideUtils.chromeConfig);
  private final SelenideDriver fireFoxDriver = new SelenideDriver(SelenideUtils.fireFoxConfig);

  @Test
  public void checkButtonPurpleColor() {
    browserExtension.getDrivers().add(chromeDriver);

    MainPage.open(chromeDriver)
        .checkColorLoginInput(Color.PURPLE);
  }

  @Test
  public void checkButtonYellowColor() {
    browserExtension.getDrivers().add(chromeDriver);

    MainPage.open(chromeDriver)
        .clickThemeSwitch()
        .checkColorLoginInput(Color.YELLOW);
  }

  @Test
  public void checkButtonColors() throws InterruptedException {
    browserExtension.getDrivers().add(chromeDriver);
    browserExtension.getDrivers().add(fireFoxDriver);

    MainPage.open(chromeDriver)
        .clickThemeSwitch()
        .checkColorsLoginInput(Color.YELLOW);

    Thread.sleep(10000);

    MainPage.open(fireFoxDriver)
        .clickThemeSwitch()
        .checkColorsLoginInput(Color.YELLOW);

    Thread.sleep(10000);
  }

}
