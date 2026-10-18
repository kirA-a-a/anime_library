package io.student.rcc.jupiter.extension;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;
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

    allureLifecycle.addAttachment(
        "rcc-auth log",
        "text/html",
        ".log",
        Files.newInputStream(
            Path.of("./logs/rcc-auth/app.log")
        )
    );

    allureLifecycle.addAttachment(
        "rcc-api log",
        "text/html",
        ".log",
        Files.newInputStream(
            Path.of("./logs/rcc-api/app.log")
        )
    );

    allureLifecycle.stopTestCase(caseId);
    allureLifecycle.writeTestCase(caseId);
  }
}
