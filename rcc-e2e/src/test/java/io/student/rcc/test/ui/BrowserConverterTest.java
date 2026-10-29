package io.student.rcc.test.ui;

import com.codeborne.selenide.SelenideDriver;
import io.student.rcc.condition.Color;
import io.student.rcc.jupiter.converter.BrowserConverter;
import io.student.rcc.jupiter.extension.BrowserExtension;
import io.student.rcc.page.MainPage;
import io.student.rcc.utils.Browser;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.EnumSource;

public class BrowserConverterTest {

  @RegisterExtension
  static final BrowserExtension browserExtension = new BrowserExtension();

  @ParameterizedTest
  @EnumSource(Browser.class)
  void checkButtonColors(
      @ConvertWith(BrowserConverter.class) SelenideDriver driver
  ) {
    browserExtension.getDrivers().add(driver);

    MainPage.open(driver)
        .clickThemeSwitch()
        .checkColorsLoginInput(Color.YELLOW);
  }

}
