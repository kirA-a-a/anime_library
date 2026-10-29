package io.student.rcc.utils;

import com.codeborne.selenide.SelenideConfig;

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
