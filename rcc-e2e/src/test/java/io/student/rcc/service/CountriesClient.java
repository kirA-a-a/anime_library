package io.student.rcc.service;

import io.student.rcc.model.CountryJson;
import java.util.Optional;
import java.util.UUID;

public interface CountriesClient {

  CountryJson create(CountryJson country);

  Optional<CountryJson> findById(UUID id);

  Optional<CountryJson> findByName(String name);

  void delete(CountryJson country);
}
