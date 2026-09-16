package io.student.rcc.data.rococoAuth.entity;

import io.student.rcc.model.UserJson;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

@Getter
@Setter
@ToString
@Entity
@Table(name = "user")
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, unique = true, columnDefinition = "BINARY(16)")
  private UUID id;

  @Column(name = "username", nullable = false, unique = true, length = 50)
  private String username;

  @Column(name = "firstname", length = 255)
  private String firstname;

  @Column(name = "lastname", length = 255)
  private String lastname;

  @Lob
  @Column(name = "avatar", columnDefinition = "LONGBLOB")
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

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> oEffectiveClass = o instanceof HibernateProxy
        ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
        : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
        : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) {
      return false;
    }
    UserEntity that = (UserEntity) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}
