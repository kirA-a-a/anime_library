package io.student.rcc.condition;

import static com.codeborne.selenide.CheckResult.rejected;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.WebElementsCondition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.jspecify.annotations.NonNull;
import org.openqa.selenium.WebElement;

public class ButtonConditions {

  public static WebElementCondition color(Color expectedColor) {
    return new WebElementCondition("color") {
      @Override
      public @NonNull CheckResult check(@NonNull Driver driver, @NonNull WebElement element) {

        final String actualColor = element.getCssValue("background-color");

        return new CheckResult(
            expectedColor.getRgba().equals(actualColor),
            actualColor
        );
      }
    };
  }

  public static WebElementsCondition color(Color... expectedColors) {

    return new WebElementsCondition() {
      @Override
      public @NonNull CheckResult check(@NonNull Driver driver,
          @NonNull List<WebElement> elements) {

        if (ArrayUtils.isEmpty(expectedColors)) {
          throw new IllegalArgumentException("No expected colors given");
        }

        if (expectedColors.length != elements.size()) {

          String message = String.format(
              "List size mismatch (expected: %s, actual: %s)", expectedColors.length,
              elements.size()
          );

          List<String> actualRgbaList = new ArrayList<>();
          for (WebElement element : elements) {
            actualRgbaList.add(element.getCssValue("background-color"));
          }
          return rejected(message, actualRgbaList.toString());
        }

        boolean passed = true;
        List<String> actualRgbaList = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
          final WebElement elementToCheck = elements.get(i);
          final Color colorToCheck = expectedColors[i];
          final String actualColor = elementToCheck.getCssValue("background-color");
          actualRgbaList.add(actualColor);

          if (passed) {
            passed = colorToCheck.getRgba().equals(actualColor);
          }
        }

        if (!passed) {
          final String expectedRgba = Arrays.stream(expectedColors).map(Color::getRgba).toList()
              .toString();
          final String actualRgba = actualRgbaList.toString();
          final String message = String.format(
              "List colors mismatch (expected: %s, actual: %s)", expectedRgba, actualRgba
          );

          return rejected(message, actualRgba);
        }

        return CheckResult.accepted();
      }

      @Override
      public @NonNull String toString() {
        return Arrays.stream(expectedColors).map(Color::getRgba).toList().toString();
      }
    };
  }

  public static WebElementsCondition color(LoginButton... expectedButton) {
    return new WebElementsCondition() {
      @Override
      public @NonNull CheckResult check(@NonNull Driver driver, @NonNull List<WebElement> elements) {
        if (ArrayUtils.isEmpty(expectedButton)) {
          throw new IllegalArgumentException("No expected buttons given");
        }

        if (expectedButton.length != elements.size()) {
          String message = String.format(
              "List size mismatch (expected: %s, actual: %s)",
              expectedButton.length,
              elements.size()
          );

          List<String> actualButtons = new ArrayList<>();
          for (WebElement element : elements) {
            actualButtons.add(describe(element));
          }
          return rejected(message, actualButtons.toString());
        }

        boolean passed = true;
        List<String> actualButtons = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
          final WebElement elementToCheck = elements.get(i);
          final Color colorToCheck = expectedButton[i].color();
          final String textToCheck = expectedButton[i].text();
          final String actualColor = elementToCheck.getCssValue("background-color");
          final String actualText = elementToCheck.getText();

          actualButtons.add(describe(actualColor, actualText));

          if (passed) {
            passed = colorToCheck.getRgba().equals(actualColor) && textToCheck.equals(actualText);
          }
        }

        if (!passed) {
          final String expected = Arrays.stream(expectedButton)
              .map(ButtonConditions::describe)
              .toList()
              .toString();
          final String actual = actualButtons.toString();
          final String message = String.format(
              "Buttons mismatch (expected: %s, actual: %s)", expected, actual
          );

          return rejected(message, actual);
        }

        return CheckResult.accepted();
      }

      @Override
      public @NonNull String toString() {
        return Arrays.stream(expectedButton)
            .map(ButtonConditions::describe)
            .toList()
            .toString();
      }
    };
  }

  public static WebElementsCondition colorInAnyOrder(LoginButton... expectedButton) {
    return new WebElementsCondition() {
      @Override
      public @NonNull CheckResult check(@NonNull Driver driver, @NonNull List<WebElement> elements) {

        if (ArrayUtils.isEmpty(expectedButton)) {
          throw new IllegalArgumentException("No expected buttons given");
        }

        if (expectedButton.length != elements.size()) {
          String message = String.format(
              "List size mismatch (expected: %s, actual: %s)",
              expectedButton.length,
              elements.size()
          );

          List<String> actualButtons = new ArrayList<>();
          for (WebElement element : elements) {
            actualButtons.add(describe(element));
          }
          return rejected(message, actualButtons.toString());
        }

        List<String> actualButtons = new ArrayList<>();

        for (final WebElement elementToCheck : elements) {
          final String actualColor = elementToCheck.getCssValue("background-color");
          final String actualText = elementToCheck.getText();

          actualButtons.add(describe(actualColor, actualText));
        }

        List<String> expectedButtons = new ArrayList<>();
        for (LoginButton loginButton : expectedButton) {
          final String expectedColor = loginButton.color().getRgba();
          final String expectedText = loginButton.text();

          expectedButtons.add(describe(expectedColor, expectedText));
        }

        boolean passed = true;
        ArrayList<String> remaining = new ArrayList<>(actualButtons);

        for (String desc : expectedButtons) {
          if (remaining.contains(desc)) {
            remaining.remove(desc);
          }
          else  {
            passed = false;
          }
        }

        if (!passed) {
          final String message = String.format(
              "Buttons mismatch in any order (expected: %s, actual: %s)",
              expectedButtons,
              actualButtons
          );
          return rejected(message, actualButtons.toString());
        }

        return CheckResult.accepted();
      }

      @Override
      public @NonNull String toString() {
        return Arrays.stream(expectedButton)
            .map(ButtonConditions::describe)
            .toList()
            .toString();
      }
    };
  }

  public static WebElementsCondition colorContains(LoginButton... expectedButton) {
    return new WebElementsCondition() {
      @Override
      public @NonNull CheckResult check(@NonNull Driver driver, @NonNull List<WebElement> elements) {

        if (ArrayUtils.isEmpty(expectedButton)) {
          throw new IllegalArgumentException("No expected buttons given");
        }

        List<String> actualButtons = new ArrayList<>();

        for (final WebElement elementToCheck : elements) {
          final String actualColor = elementToCheck.getCssValue("background-color");
          final String actualText = elementToCheck.getText();

          actualButtons.add(describe(actualColor, actualText));
        }

        List<String> expectedButtons = new ArrayList<>();
        for (LoginButton loginButton : expectedButton) {
          final String expectedColor = loginButton.color().getRgba();
          final String expectedText = loginButton.text();

          expectedButtons.add(describe(expectedColor, expectedText));
        }

        boolean passed = true;
        ArrayList<String> remaining = new ArrayList<>(actualButtons);

        for (String desc : expectedButtons) {
          if (remaining.contains(desc)) {
            remaining.remove(desc);
          }
          else  {
            passed = false;
          }
        }

        if (!passed) {
          final String message = String.format(
              "Buttons contains mismatch (expected: %s, actual: %s)",
              expectedButtons,
              actualButtons
          );
          return rejected(message, actualButtons.toString());
        }

        return CheckResult.accepted();
      }

      @Override
      public @NonNull String toString() {
        return Arrays.stream(expectedButton)
            .map(ButtonConditions::describe)
            .toList()
            .toString();
      }
    };
  }

  private static String describe(LoginButton button) {
    return describe(button.color().getRgba(), button.text());
  }

  private static String describe(WebElement element) {
    return describe(element.getCssValue("background-color"), element.getText());
  }

  private static String describe(String color, String text) {
    return String.format("color: %s, text: %s", color, text);
  }

  public record LoginButton(Color color, String text) {

  }

}
