package io.student.rcc.data.rococoAuth.entity;

import io.student.rcc.model.AuthAuthorityJson;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthAuthorityEntity {

  public enum Authority {
    READ("read"),
    WRITE("write");

    private final String dbValue;

    Authority(String dbValue) {
      this.dbValue = dbValue;
    }

    public String dbValue() {
      return dbValue;
    }
  }

  private UUID id;
  private UUID userId;
  private Authority authority;

  public static AuthAuthorityEntity fromJson(AuthAuthorityJson json) {
    AuthAuthorityEntity entity = new AuthAuthorityEntity();
    entity.setUserId(json.userId());
    entity.setAuthority(json.authority());
    return entity;
  }
}
