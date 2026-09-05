package io.student.rcc.data.rococoApi;

import io.student.rcc.data.rococoApi.entity.CountryEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CountryDao {

  CountryEntity create(CountryEntity entity);

  Optional<CountryEntity> findById(UUID id);

  List<CountryEntity> findAll();

  Optional<CountryEntity> findByName(String name);

  CountryEntity update(CountryEntity entity);

  void delete(CountryEntity entity);
}
