package io.student.rcc.jupiter.extension;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.logevents.LogEvent;
import com.codeborne.selenide.logevents.LogEventListener;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.LifecycleMethodExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class BrowserExtension implements BeforeEachCallback, AfterEachCallback,
    TestExecutionExceptionHandler, LifecycleMethodExecutionExceptionHandler {

  private final ThreadLocal<List<SelenideDriver>> drivers = ThreadLocal.withInitial(ArrayList::new);

  public List<SelenideDriver> getDrivers() {
    return drivers.get();
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    SelenideLogger.addListener("Allure-selenide", new AllureSelenide()
        .savePageSource(false)
        .screenshots(false)
    );

    SelenideLogger.addListener("selenide-driver-screenshot", new LogEventListener() {
      @Override
      public void afterEvent(LogEvent event) {
        if (event.getStatus() == LogEvent.EventStatus.FAIL) {
          doScreenshot();
        }
      }

      @Override
      public void beforeEvent(LogEvent currentLog) {
      }
    });
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    for (SelenideDriver driver : getDrivers()) {
      if (driver.hasWebDriverStarted()) {
        driver.close();
      }
    }
    drivers.remove();
  }

  @Override
  public void handleTestExecutionException(ExtensionContext context, Throwable throwable)
      throws Throwable {
    doScreenshot();
    throw throwable;
  }

  @Override
  public void handleAfterEachMethodExecutionException(ExtensionContext context, Throwable throwable)
      throws Throwable {
    doScreenshot();
    throw throwable;
  }

  private void doScreenshot() {
    for (SelenideDriver driver : getDrivers()) {
      if (driver.hasWebDriverStarted()) {
        Allure.addAttachment(
            "Screen on fail for browser: '%s'".formatted(driver.getSessionId()),
            "image/png",
            new ByteArrayInputStream(
                ((TakesScreenshot) driver.getWebDriver()).getScreenshotAs(OutputType.BYTES)
            ),
            "png"
        );
      }
    }
  }
}
