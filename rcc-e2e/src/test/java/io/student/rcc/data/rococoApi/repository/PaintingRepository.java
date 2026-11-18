package io.student.rcc.data.rococoApi.repository;

import io.student.rcc.data.rococoApi.entity.PaintingEntity;
import io.student.rcc.data.rococoApi.repository.iml.PaintingRepositoryHibernate;
import io.student.rcc.data.rococoApi.repository.iml.PaintingRepositoryJdbc;
import io.student.rcc.data.rococoApi.repository.iml.PaintingRepositorySpringJdbc;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface PaintingRepository {

  static @Nonnull PaintingRepository getInstance() {
    return switch (System.getProperty("repository.impl", "jpa")) {
      case "jdbc" -> new PaintingRepositoryJdbc();
      case "spring-jdbc" -> new PaintingRepositorySpringJdbc();
      default -> new PaintingRepositoryHibernate();
    };
  }

  @Nonnull
  PaintingEntity create(PaintingEntity entity);

  @Nonnull
  Optional<PaintingEntity> findById(UUID id);

  @Nonnull
  List<PaintingEntity> findAll();

  @Nonnull
  Optional<PaintingEntity> findByTitle(String title);

  @Nonnull
  PaintingEntity update(PaintingEntity entity);

  void delete(PaintingEntity entity);
}
