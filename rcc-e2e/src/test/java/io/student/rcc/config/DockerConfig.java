package io.student.rcc.config;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public enum DockerConfig implements Config {
  INSTANCE;

  @Override
  public @Nonnull String frontUrl() {
    return "";
  }

  @Override
  public @Nonnull String rccApiJdbcUrl() {
    return "";
  }

  @Override
  public @Nonnull String authJdbcUrl() {
    return "";
  }

  @Override
  public @Nonnull String dbUserName() {
    return "";
  }

  @Override
  public @Nonnull String dbPassword() {
    return "";
  }

  @Override
  public @Nonnull String authUrl() {
    return "";
  }

  @Override
  public @Nonnull String apiUrl() {
    return "";
  }
}
