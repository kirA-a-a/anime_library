package io.student.rcc.jupiter.extension;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public interface SuiteExtension extends BeforeAllCallback {


  @Override
  default void beforeAll(ExtensionContext context) throws Exception {
    final ExtensionContext rootContext = context.getRoot();
    rootContext.getStore(ExtensionContext.Namespace.GLOBAL)
        .computeIfAbsent(
            this.getClass(),
            key -> {
              beforeSuite(rootContext);
              return new AutoCloseable() {
                public void close() {
                  afterSuite();
                }
              };
            }
        );
  }

  default void beforeSuite(ExtensionContext context) {
  }

  default void afterSuite() {
  }

}
