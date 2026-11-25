package io.student.rcc.jupiter.extension;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import lombok.SneakyThrows;

public class AllureBackendLogsExtension implements SuiteExtension {

  public static final String caseName = "rcc backends logs";

  @SneakyThrows
  @Override
  public void afterSuite() {
    final AllureLifecycle allureLifecycle = Allure.getLifecycle();
    final String caseId = UUID.randomUUID().toString();
    allureLifecycle.scheduleTestCase(
        new TestResult().setUuid(caseId).setName(caseName).setStatus(Status.PASSED)
    );
    allureLifecycle.startTestCase(caseId);

    attachIfExists(allureLifecycle, "rcc-auth log", Path.of("./logs/rcc-auth/app.log"));
    attachIfExists(allureLifecycle, "rcc-api log", Path.of("./logs/rcc-api/app.log"));

    allureLifecycle.stopTestCase(caseId);
    allureLifecycle.writeTestCase(caseId);
  }

  @SneakyThrows
  private static void attachIfExists(AllureLifecycle lifecycle, String name, Path path) {
    if (!Files.exists(path)) {
      return;
    }
    try (InputStream inputStream = Files.newInputStream(path)) {
      lifecycle.addAttachment(name, "text/html", ".log", inputStream);
    }
  }
}
