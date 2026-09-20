package io.student.rcc.page.component;

import static com.codeborne.selenide.Selenide.$;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("UnusedReturnValue")
public class SearchField extends BaseComponent {

  public SearchField() {
    super($("input[type=search]"));
  }
}
