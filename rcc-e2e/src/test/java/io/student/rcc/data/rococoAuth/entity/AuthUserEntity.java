package io.student.rcc.data.rococoAuth.entity;

import io.student.rcc.model.AuthUserJson;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nonnull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

@Getter
@Setter
@ToString
@Entity
@Table(name = "user")
public class AuthUserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, unique = true, columnDefinition = "BINARY(16)")
  private UUID id;

  @Column(name = "username", nullable = false, unique = true, length = 50)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "enabled", nullable = false)
  private boolean enabled;

  @Column(name = "account_non_expired", nullable = false)
  private boolean accountNonExpired;

  @Column(name = "account_non_locked", nullable = false)
  private boolean accountNonLocked;

  @Column(name = "credentials_non_expired", nullable = false)
  private boolean credentialsNonExpired;

  @OneToMany(
      fetch = FetchType.EAGER,
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      mappedBy = "user"
  )
  private List<AuthAuthorityEntity> authorities = new ArrayList<>();

  public void addAuthorities(AuthAuthorityEntity... authorities) {
    for (AuthAuthorityEntity authority : authorities) {
      this.authorities.add(authority);
      authority.setUser(this);
    }
  }

  public void removeAuthority(AuthAuthorityEntity authority) {
    this.authorities.remove(authority);
    authority.setUser(null);
  }

  public static @Nonnull AuthUserEntity fromJson(AuthUserJson json) {
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
    AuthUserEntity that = (AuthUserEntity) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}
