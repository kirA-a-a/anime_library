package io.student.rcc.page.component;

import com.codeborne.selenide.SelenideDriver;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("UnusedReturnValue")
public class SearchField extends BaseComponent<SearchField> {

  public SearchField(SelenideDriver driver) {
    super(driver.$("input[type=search]"));
  }
}
