package io.student.rcc.jupiter.extension;

import io.student.rcc.api.core.ThreadSafeCookieStore;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * Чистит куки после каждого теста.
 *
 * <p>{@link AfterTestExecutionCallback#afterTestExecution} вызывается и при успешном
 * тесте, и если тест упал с assertion error или другим exception. Так куки не
 * «утекут» в следующий тест даже при падении.
 */
public class CookiesExtension implements AfterTestExecutionCallback {

  @Override
  public void afterTestExecution(ExtensionContext context) {
    ThreadSafeCookieStore.INSTANCE.removeAll();
  }
}
