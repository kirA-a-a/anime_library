package io.student.rcc.data.rococoApi.repository.iml;

import static io.student.rcc.data.jpa.EntityManagers.em;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.entity.CountryEntity;
import io.student.rcc.data.rococoApi.repository.CountryRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CountryRepositoryHibernate implements CountryRepository {

  private static final Config CFG = Config.getInstance();
  private final EntityManager entityManager = em(CFG.rccApiJdbcUrl());

  @Override
  public CountryEntity create(CountryEntity entity) {
    entityManager.joinTransaction();
    entityManager.persist(entity);
    return entity;
  }

  @Override
  public Optional<CountryEntity> findById(UUID id) {
    return Optional.ofNullable(entityManager.find(CountryEntity.class, id));
  }

  @Override
  public List<CountryEntity> findAll() {
    return entityManager.createQuery(
            "SELECT c FROM CountryEntity c",
            CountryEntity.class
        )
        .getResultList();
  }

  @Override
  public Optional<CountryEntity> findByName(String name) {
    return entityManager.createQuery(
            "SELECT c FROM CountryEntity c WHERE c.name = :name",
            CountryEntity.class
        )
        .setParameter("name", name)
        .getResultStream()
        .findFirst();
  }

  @Override
  public CountryEntity update(CountryEntity entity) {
    entityManager.joinTransaction();
    return entityManager.merge(entity);
  }

  @Override
  public void delete(CountryEntity entity) {
    entityManager.joinTransaction();
    entityManager.remove(entity);
  }
}
