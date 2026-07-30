package io.student.rcc.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }

  String frontUrl();
  String rccApiJdbcUrl();
  String authJdbcUrl();
  String dbUserName();
  String dbPassword();
  String ghUrl();
}
