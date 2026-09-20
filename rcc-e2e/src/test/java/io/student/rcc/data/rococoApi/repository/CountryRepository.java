package io.student.rcc.data.rococoApi.repository;

import io.student.rcc.data.rococoApi.entity.CountryEntity;
import io.student.rcc.data.rococoApi.repository.iml.CountryRepositoryHibernate;
import io.student.rcc.data.rococoApi.repository.iml.CountryRepositoryJdbc;
import io.student.rcc.data.rococoApi.repository.iml.CountryRepositorySpringJdbc;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface CountryRepository {

  static @Nonnull CountryRepository getInstance() {
    return switch (System.getProperty("repository.impl", "jpa")) {
      case "jdbc" -> new CountryRepositoryJdbc();
      case "spring-jdbc" -> new CountryRepositorySpringJdbc();
      default -> new CountryRepositoryHibernate();
    };
  }

  @Nonnull
  CountryEntity create(CountryEntity entity);

  @Nonnull
  Optional<CountryEntity> findById(UUID id);

  @Nonnull
  List<CountryEntity> findAll();

  @Nonnull
  Optional<CountryEntity> findByName(String name);

  @Nonnull
  CountryEntity update(CountryEntity entity);

  void delete(CountryEntity entity);
}
