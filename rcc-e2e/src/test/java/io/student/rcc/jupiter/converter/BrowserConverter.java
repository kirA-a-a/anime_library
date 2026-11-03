package io.student.rcc.jupiter.converter;

import com.codeborne.selenide.SelenideDriver;
import io.student.rcc.utils.Browser;
import io.student.rcc.utils.SelenideUtils;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

public class BrowserConverter implements ArgumentConverter {

  @Override
  public Object convert(Object source, ParameterContext context) {
    if (!(source instanceof Browser browser)) {
      throw new ArgumentConversionException("Browser argument expected");
    }
    return switch (browser) {
      case CHROME -> new SelenideDriver(SelenideUtils.chromeConfig);
      case FIREFOX -> new SelenideDriver(SelenideUtils.fireFoxConfig);
    };
  }
}
