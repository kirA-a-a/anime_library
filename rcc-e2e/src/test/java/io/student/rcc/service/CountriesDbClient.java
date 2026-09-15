package io.student.rcc.service;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.entity.CountryEntity;
import io.student.rcc.data.rococoApi.repository.CountryRepository;
import io.student.rcc.data.tpl.XaTransactionTemplate;
import io.student.rcc.model.CountryJson;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CountriesDbClient implements CountriesClient {

  private static final Config CFG = Config.getInstance();

  private final XaTransactionTemplate xaTemplate = new XaTransactionTemplate(
      CFG.rccApiJdbcUrl()
  );

  private final CountryRepository countryRepository = CountryRepository.getInstance();

  @Override
  public CountryJson create(CountryJson country) {
    return xaTemplate.execute(() ->
        CountryJson.fromEntity(countryRepository.create(CountryEntity.fromJson(country)))
    );
  }

  @Override
  public Optional<CountryJson> findById(UUID id) {
    return xaTemplate.execute(() ->
        countryRepository.findById(id).map(CountryJson::fromEntity)
    );
  }

  @Override
  public Optional<CountryJson> findByName(String name) {
    return xaTemplate.execute(() ->
        countryRepository.findByName(name).map(CountryJson::fromEntity)
    );
  }

  @Override
  public List<CountryJson> findAll() {
    return xaTemplate.execute(() ->
        countryRepository.findAll().stream().map(CountryJson::fromEntity).toList()
    );
  }

  @Override
  public CountryJson update(CountryJson country) {
    return xaTemplate.execute(() ->
        CountryJson.fromEntity(countryRepository.update(CountryEntity.fromJson(country)))
    );
  }

  @Override
  public void delete(CountryJson country) {
    xaTemplate.execute(() -> {
      countryRepository.delete(CountryEntity.fromJson(country));
      return null;
    });
  }
}
