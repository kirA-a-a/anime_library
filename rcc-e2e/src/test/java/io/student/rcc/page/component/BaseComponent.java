package io.student.rcc.page.component;

import com.codeborne.selenide.SelenideElement;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("UnusedReturnValue")
public class BaseComponent {

  protected final SelenideElement self;

  public BaseComponent(SelenideElement self) {
    this.self = self;
  }
}
