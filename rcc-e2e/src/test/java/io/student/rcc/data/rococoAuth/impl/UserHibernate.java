package io.student.rcc.data.rococoAuth.impl;

import static io.student.rcc.data.jpa.EntityManagers.em;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoAuth.UserDao;
import io.student.rcc.data.rococoAuth.entity.UserEntity;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserHibernate implements UserDao {

  private static final Config CFG = Config.getInstance();
  private final EntityManager entityManager = em(CFG.rccApiJdbcUrl());

  @Override
  public UserEntity create(UserEntity entity) {
    entityManager.joinTransaction();
    entityManager.persist(entity);
    return entity;
  }

  @Override
  public Optional<UserEntity> findById(UUID id) {
    return Optional.ofNullable(entityManager.find(UserEntity.class, id));
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
  public List<UserEntity> findAll() {
    return entityManager.createQuery(
            "SELECT u FROM UserEntity u",
            UserEntity.class
        )
        .getResultList();
  }

  @Override
  public UserEntity update(UserEntity entity) {
    entityManager.joinTransaction();
    return entityManager.merge(entity);
  }

  @Override
  public void delete(UserEntity entity) {
    entityManager.joinTransaction();
    entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
  }
}
