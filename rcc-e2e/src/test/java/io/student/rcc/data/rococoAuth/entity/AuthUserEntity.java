package io.student.rcc.data.rococoAuth.entity;

import io.student.rcc.model.AuthUserJson;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthUserEntity {

  private UUID id;
  private String username;
  private String password;
  private boolean enabled;
  private boolean accountNonExpired;
  private boolean accountNonLocked;
  private boolean credentialsNonExpired;
  private List<AuthAuthorityEntity> authorities = new ArrayList<>();

  public static AuthUserEntity fromJson(AuthUserJson json) {
    AuthUserEntity entity = new AuthUserEntity();
    entity.setId(json.id());
    entity.setUsername(json.username());
    entity.setPassword(json.password());
    entity.setEnabled(json.enabled());
    entity.setAccountNonExpired(json.accountNonExpired());
    entity.setAccountNonLocked(json.accountNonLocked());
    entity.setCredentialsNonExpired(json.credentialsNonExpired());
    return entity;
  }
}
