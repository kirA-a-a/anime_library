package io.student.rcc.data.rococoAuth.repository.iml;

import static io.student.rcc.data.jpa.EntityManagers.em;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoAuth.entity.UserEntity;
import io.student.rcc.data.rococoAuth.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryHibernate implements UserRepository {

  public static final Config CFG = Config.getInstance();
  private final EntityManager entityManager = em(CFG.rccApiJdbcUrl());

  @Override
  public UserEntity create(UserEntity user) {

    entityManager.joinTransaction();
    entityManager.persist(user);

    return user;
  }

  @Override
  public Optional<UserEntity> findById(UUID id) {

    return Optional.ofNullable(entityManager.find(UserEntity.class, id));

  }

  @Override
  public List<UserEntity> findAll() {
    return entityManager.createQuery(
            "SELECT u FROM UserEntity u",
            UserEntity.class
        )
        .getResultList();
  }

  @Override
  public Optional<UserEntity> findByUsername(String username) {
    return entityManager.createQuery(
            "SELECT u FROM UserEntity u WHERE u.username = :username",
            UserEntity.class
        )
        .setParameter("username", username)
        .getResultStream()
        .findFirst();
  }

  @Override
  public UserEntity update(UserEntity entity) {
    entityManager.joinTransaction();
    return entityManager.merge(entity);
  }

  @Override
  public void delete(UserEntity entity) {
    entityManager.joinTransaction();
    entityManager.remove(entity);
  }
}
