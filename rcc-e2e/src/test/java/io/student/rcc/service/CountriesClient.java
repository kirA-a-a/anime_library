package io.student.rcc.service;

import io.student.rcc.model.CountryJson;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CountriesClient {

  CountryJson create(CountryJson country);

  Optional<CountryJson> findById(UUID id);

  Optional<CountryJson> findByName(String name);

  List<CountryJson> findAll();

  CountryJson update(CountryJson country);

  void delete(CountryJson country);
}
