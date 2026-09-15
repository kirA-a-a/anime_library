package io.student.rcc.data.rococoApi.repository;

import io.student.rcc.data.rococoApi.entity.CountryEntity;
import io.student.rcc.data.rococoApi.repository.iml.CountryRepositoryHibernate;
import io.student.rcc.data.rococoApi.repository.iml.CountryRepositoryJdbc;
import io.student.rcc.data.rococoApi.repository.iml.CountryRepositorySpringJdbc;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CountryRepository {

  static CountryRepository getInstance() {
    return switch (System.getProperty("repository.impl", "jpa")) {
      case "jdbc" -> new CountryRepositoryJdbc();
      case "spring-jdbc" -> new CountryRepositorySpringJdbc();
      default -> new CountryRepositoryHibernate();
    };
  }

  CountryEntity create(CountryEntity entity);

  Optional<CountryEntity> findById(UUID id);

  List<CountryEntity> findAll();

  Optional<CountryEntity> findByName(String name);

  CountryEntity update(CountryEntity entity);

  void delete(CountryEntity entity);
}
