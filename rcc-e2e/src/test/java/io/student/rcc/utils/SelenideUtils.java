package io.student.rcc.utils;

import com.codeborne.selenide.SelenideConfig;

/**
 * Конфиги для нестатического {@code SelenideDriver}.
 *
 * <p>Сейчас UI-тесты идут через статическую {@code com.codeborne.selenide.Configuration}
 * в {@code BrowserExtension}. Эти конфиги нужны только для закомментированных демо
 * ({@code BrowserConverterTest}, dual-browser в {@code ColorTest}).
 */
public class SelenideUtils {

  public final static SelenideConfig chromeConfig = new SelenideConfig()
      .browser("chrome")
      .pageLoadStrategy("eager")
      .timeout(5000L);
  public final static SelenideConfig fireFoxConfig = new SelenideConfig()
      .browser("fireFox")
      .pageLoadStrategy("eager")
      .timeout(5000L);
}
