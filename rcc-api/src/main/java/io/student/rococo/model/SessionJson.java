package io.student.rococo.model;

import java.util.Date;
import lombok.NonNull;

public record SessionJson(String username, Date issuedAt, Date expiresAt) {

  public static @NonNull SessionJson empty() {
    return new SessionJson(
        null,
        null,
        null
    );
  }


}
