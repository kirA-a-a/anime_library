package io.student.rcc.data.rococoApi.repository.iml;

import static io.student.rcc.data.jpa.EntityManagers.em;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.entity.MuseumEntity;
import io.student.rcc.data.rococoApi.repository.MuseumRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MuseumRepositoryHibernate implements MuseumRepository {

  private static final Config CFG = Config.getInstance();
  private final EntityManager entityManager = em(CFG.rccApiJdbcUrl());

  @Override
  public MuseumEntity create(MuseumEntity entity) {
    entityManager.joinTransaction();
    entityManager.persist(entity);
    return entity;
  }

  @Override
  public Optional<MuseumEntity> findById(UUID id) {
    return Optional.ofNullable(entityManager.find(MuseumEntity.class, id));
  }

  @Override
  public List<MuseumEntity> findAll() {
    return entityManager.createQuery(
            "SELECT m FROM MuseumEntity m",
            MuseumEntity.class
        )
        .getResultList();
  }

  @Override
  public Optional<MuseumEntity> findByTitle(String title) {
    return entityManager.createQuery(
            "SELECT m FROM MuseumEntity m WHERE m.title = :title",
            MuseumEntity.class
        )
        .setParameter("title", title)
        .getResultStream()
        .findFirst();
  }

  @Override
  public MuseumEntity update(MuseumEntity entity) {
    entityManager.joinTransaction();
    return entityManager.merge(entity);
  }

  @Override
  public void delete(MuseumEntity entity) {
    entityManager.joinTransaction();
    entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
  }
}
