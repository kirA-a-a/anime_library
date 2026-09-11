package io.student.rcc.data.rococoApi.repository.iml;

import static io.student.rcc.data.jpa.EntityManagers.em;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.entity.ArtistEntity;
import io.student.rcc.data.rococoApi.repository.ArtistRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ArtistRepositoryHibernate implements ArtistRepository {

  private static final Config CFG = Config.getInstance();
  private final EntityManager entityManager = em(CFG.rccApiJdbcUrl());

  @Override
  public ArtistEntity create(ArtistEntity entity) {
    entityManager.joinTransaction();
    entityManager.persist(entity);
    return entity;
  }

  @Override
  public Optional<ArtistEntity> findById(UUID id) {
    return Optional.ofNullable(entityManager.find(ArtistEntity.class, id));
  }

  @Override
  public List<ArtistEntity> findAll() {
    return entityManager.createQuery(
            "SELECT a FROM ArtistEntity a",
            ArtistEntity.class
        )
        .getResultList();
  }

  @Override
  public Optional<ArtistEntity> findByName(String name) {
    return entityManager.createQuery(
            "SELECT a FROM ArtistEntity a WHERE a.name = :name",
            ArtistEntity.class
        )
        .setParameter("name", name)
        .getResultStream()
        .findFirst();
  }

  @Override
  public ArtistEntity update(ArtistEntity entity) {
    entityManager.joinTransaction();
    return entityManager.merge(entity);
  }

  @Override
  public void delete(ArtistEntity entity) {
    entityManager.joinTransaction();
    entityManager.remove(entity);
  }
}
