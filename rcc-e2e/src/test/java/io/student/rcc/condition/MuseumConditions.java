package io.student.rcc.condition;

import static com.codeborne.selenide.CheckResult.rejected;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import io.student.rcc.model.MuseumJson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.jspecify.annotations.NonNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class MuseumConditions {

  public static WebElementsCondition museums(MuseumJson... expected) {
    return new WebElementsCondition() {
      @Override
      public @NonNull CheckResult check(@NonNull Driver driver, @NonNull List<WebElement> elements) {

        if (ArrayUtils.isEmpty(expected)) {
          throw new IllegalArgumentException("No expected museums given");
        }

        if (expected.length != elements.size()) {

          String message = String.format(
              "List size mismatch (expected: %s, actual: %s)", expected.length,
              elements.size()
          );

          List<String> actualMuseumList = new ArrayList<>();
          for (WebElement element : elements) {
            actualMuseumList.add(element.getText());
          }
          return rejected(message, actualMuseumList.toString());
        }

        boolean passed = true;
        List<String> actualTitles = new ArrayList<>();
        List<String> actualCities = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
          WebElement card = elements.get(i);
          String expectedTitle = expected[i].title();
          String expectedCity = expected[i].city();
          String actualTitle = card.findElement(By.cssSelector("[data-testid='museum-card-title']")).getText();
          String actualCity = card.findElement(By.cssSelector("[data-testid='museum-card-location']")).getText();
          actualTitles.add(actualTitle);
          actualCities.add(actualCity);

          if (passed) {
            boolean titleOk = expectedTitle.equals(actualTitle);
            boolean cityOk = actualCity.startsWith(expectedCity);
            passed = titleOk && cityOk;
          }
        }

        if (!passed) {
          final String expectedTitles = Arrays.stream(expected).map(MuseumJson::title).toList()
              .toString();
          final String expectedCities = Arrays.stream(expected).map(MuseumJson::city).toList()
              .toString();
          final String actualTitlesStr = actualTitles.toString();
          final String actualCitiesStr = actualCities.toString();
          final String message = String.format(
              "List museums mismatch (expected titles: %s, actual titles: %s; expected cities: %s, actual cities: %s)",
              expectedTitles, actualTitlesStr, expectedCities, actualCitiesStr
          );

          return rejected(message, actualTitlesStr + "; " + actualCitiesStr);
        }

        return CheckResult.accepted();
      }

      @Override
      public @NonNull String toString() {
        return Arrays.stream(expected).map(MuseumJson::title).toList().toString();
      }
    };
  }

}
