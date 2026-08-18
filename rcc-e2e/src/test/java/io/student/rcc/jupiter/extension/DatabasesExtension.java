package io.student.rcc.jupiter.extension;

import io.student.rcc.data.Databases;

public class DatabasesExtension implements SuiteExtension {

  @Override
  public void afterSuite() {
    Databases.closeAllConnections();
  }

}
