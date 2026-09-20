package io.student.rcc.config;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public enum LocalConfig implements Config {
  INSTANCE;

  @Override
  public @Nonnull String frontUrl() {
    return "http://localhost:3000/";
  }

  @Override
  public @Nonnull String rccApiJdbcUrl() {
    return "jdbc:mysql://localhost:3306/rococo-api?serverTimezone=UTC&createDatabaseIfNotExist=true";
  }

  @Override
  public @Nonnull String authJdbcUrl() {
    return "jdbc:mysql://localhost:3306/rococo-auth?serverTimezone=UTC&createDatabaseIfNotExist=true";
  }

  @Override
  public @Nonnull String dbUserName() {
    return "root";
  }

  @Override
  public @Nonnull String dbPassword() {
    return "secret";
  }

  @Override
  public @Nonnull String ghUrl() {
    return "https://api.github.com/";
  }

  @Override
  public @Nonnull String authUrl() {
    return "http://localhost:9000/";
  }

  @Override
  public @Nonnull String apiUrl() {
    return "http://localhost:8080/";
  }
}
