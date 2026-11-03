package io.student.rcc.page;

import com.codeborne.selenide.SelenideDriver;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("UnusedReturnValue")
public class ArtistPage extends BasePage<ArtistPage> {

  public ArtistPage(SelenideDriver driver) {
    super(driver);
  }
}
