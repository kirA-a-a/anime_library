package io.student.rcc.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

public class ProfileModal {

  private final SelenideElement userTag = $("form[class*='modal-form']");

  public ProfileModal checkUserTag(String userTag) {
    this.userTag.shouldBe(text(userTag));

    return this;
  }

}
