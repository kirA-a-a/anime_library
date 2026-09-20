package io.student.rcc.data.rococoApi;

import io.student.rcc.data.rococoApi.entity.CountryEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface CountryDao {

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
