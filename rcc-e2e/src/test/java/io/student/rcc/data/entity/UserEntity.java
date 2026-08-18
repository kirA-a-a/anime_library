package io.student.rcc.data.entity;

import io.student.rcc.model.UserJson;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserEntity {

  private UUID id;
  private String username;
  private String firstname;
  private String lastname;
  private String avatar;

  public static UserEntity fromJson(UserJson json) {
    UserEntity entity = new UserEntity();
    entity.setId(json.id());
    entity.setUsername(json.username());
    entity.setFirstname(json.firstname());
    entity.setLastname(json.lastname());
    entity.setAvatar(json.avatar());
    return entity;
  }

}
