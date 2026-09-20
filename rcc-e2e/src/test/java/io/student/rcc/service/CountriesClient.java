package io.student.rcc.service;

import io.student.rcc.model.CountryJson;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface CountriesClient {

  @Nonnull
  CountryJson create(CountryJson country);

  @Nonnull
  Optional<CountryJson> findById(UUID id);

  @Nonnull
  Optional<CountryJson> findByName(String name);

  @Nonnull
  List<CountryJson> findAll();

  @Nonnull
  CountryJson update(CountryJson country);

  void delete(CountryJson country);
}
