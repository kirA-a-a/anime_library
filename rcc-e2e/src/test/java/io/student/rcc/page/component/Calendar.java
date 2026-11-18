package io.student.rcc.page.component;

import com.codeborne.selenide.SelenideElement;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("UnusedReturnValue")
public class Calendar extends BaseComponent<Calendar> {

  public Calendar(SelenideElement self) {
    super(self);
  }
}
