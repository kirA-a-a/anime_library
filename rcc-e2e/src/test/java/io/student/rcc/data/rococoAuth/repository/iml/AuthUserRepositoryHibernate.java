package io.student.rcc.data.rococoAuth.repository.iml;

import static io.student.rcc.data.jpa.EntityManagers.em;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoAuth.entity.AuthUserEntity;
import io.student.rcc.data.rococoAuth.repository.AuthUserRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthUserRepositoryHibernate implements AuthUserRepository {

  private static final Config CFG = Config.getInstance();
  private final EntityManager entityManager = em(CFG.authJdbcUrl());
  private final PasswordEncoder passwordEncoder =
      PasswordEncoderFactories.createDelegatingPasswordEncoder();

  @Override
  public AuthUserEntity create(AuthUserEntity user) {
    entityManager.joinTransaction();
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    entityManager.persist(user);
    return user;
  }

  @Override
  public Optional<AuthUserEntity> findById(UUID id) {
    return Optional.ofNullable(entityManager.find(AuthUserEntity.class, id));
  }

  @Override
  public List<AuthUserEntity> findAll() {
    return entityManager.createQuery(
            "SELECT u FROM AuthUserEntity u",
            AuthUserEntity.class
        )
        .getResultList();
  }

  @Override
  public void delete(UUID id) {
    entityManager.joinTransaction();
    entityManager.remove(entityManager.find(AuthUserEntity.class, id));
  }
}
