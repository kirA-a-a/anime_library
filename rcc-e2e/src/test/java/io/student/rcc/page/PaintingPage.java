package io.student.rcc.page;

import com.codeborne.selenide.SelenideDriver;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("UnusedReturnValue")
public class PaintingPage extends BasePage<PaintingPage> {

  public PaintingPage(SelenideDriver driver) {
    super(driver);
  }
}
