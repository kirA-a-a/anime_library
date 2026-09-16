package io.student.rcc.data.rococoApi.repository.iml;

import static io.student.rcc.data.jpa.EntityManagers.em;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.entity.PaintingEntity;
import io.student.rcc.data.rococoApi.repository.PaintingRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PaintingRepositoryHibernate implements PaintingRepository {

  private static final Config CFG = Config.getInstance();
  private final EntityManager entityManager = em(CFG.rccApiJdbcUrl());

  @Override
  public PaintingEntity create(PaintingEntity entity) {
    entityManager.joinTransaction();
    entityManager.persist(entity);
    return entity;
  }

  @Override
  public Optional<PaintingEntity> findById(UUID id) {
    return entityManager.createQuery(
            "SELECT p FROM PaintingEntity p JOIN FETCH p.artist WHERE p.id = :id",
            PaintingEntity.class
        )
        .setParameter("id", id)
        .getResultStream()
        .findFirst();
  }

  @Override
  public List<PaintingEntity> findAll() {
    return entityManager.createQuery(
            "SELECT p FROM PaintingEntity p JOIN FETCH p.artist",
            PaintingEntity.class
        )
        .getResultList();
  }

  @Override
  public Optional<PaintingEntity> findByTitle(String title) {
    return entityManager.createQuery(
            "SELECT p FROM PaintingEntity p JOIN FETCH p.artist WHERE p.title = :title",
            PaintingEntity.class
        )
        .setParameter("title", title)
        .getResultStream()
        .findFirst();
  }

  @Override
  public PaintingEntity update(PaintingEntity entity) {
    entityManager.joinTransaction();
    return entityManager.merge(entity);
  }

  @Override
  public void delete(PaintingEntity entity) {
    entityManager.joinTransaction();
    entityManager.remove(entity);
  }
}
