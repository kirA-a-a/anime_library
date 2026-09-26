package io.student.rcc.config;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface Config {

  static @Nonnull Config getInstance() {
    return switch (System.getProperty("test.env", "local")) {
      case "docker" -> DockerConfig.INSTANCE;
      default -> LocalConfig.INSTANCE;
    };
  }

  @Nonnull
  String frontUrl();

  @Nonnull
  String rccApiJdbcUrl();

  @Nonnull
  String authJdbcUrl();

  @Nonnull
  String dbUserName();

  @Nonnull
  String dbPassword();

  @Nonnull
  default String ghUrl(){
    return "https://api.github.com/";
  };

  @Nonnull
  String authUrl();

  @Nonnull
  String apiUrl();
}
