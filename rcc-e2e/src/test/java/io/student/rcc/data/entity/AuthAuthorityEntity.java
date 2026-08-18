package io.student.rcc.data.entity;

import io.student.rcc.model.AuthAuthorityJson;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthAuthorityEntity {

  private UUID id;
  private UUID userId;
  private String authority;

  public static AuthAuthorityEntity fromJson(AuthAuthorityJson json) {
    AuthAuthorityEntity entity = new AuthAuthorityEntity();
    entity.setUserId(json.userId());
    entity.setAuthority(json.authority());
    return entity;
  }
}
