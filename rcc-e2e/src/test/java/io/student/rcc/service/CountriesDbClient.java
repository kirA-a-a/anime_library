package io.student.rcc.service;

import io.qameta.allure.Step;
import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.entity.CountryEntity;
import io.student.rcc.data.rococoApi.repository.CountryRepository;
import io.student.rcc.data.tpl.XaTransactionTemplate;
import io.student.rcc.model.CountryJson;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CountriesDbClient implements CountriesClient {

  private static final Config CFG = Config.getInstance();

  private final XaTransactionTemplate xaTemplate = new XaTransactionTemplate(
      CFG.rccApiJdbcUrl()
  );

  private final CountryRepository countryRepository = CountryRepository.getInstance();

  @Override
  @Step("Создать страну: {country.name}")
  public @Nonnull CountryJson create(CountryJson country) {
    return xaTemplate.execute(() ->
        CountryJson.fromEntity(countryRepository.create(CountryEntity.fromJson(country)))
    );
  }

  @Override
  @Step("Найти страну по id: {id}")
  public @Nonnull Optional<CountryJson> findById(UUID id) {
    return xaTemplate.execute(() ->
        countryRepository.findById(id).map(CountryJson::fromEntity)
    );
  }

  @Override
  @Step("Найти страну по имени: {name}")
  public @Nonnull Optional<CountryJson> findByName(String name) {
    return xaTemplate.execute(() ->
        countryRepository.findByName(name).map(CountryJson::fromEntity)
    );
  }

  @Override
  @Step("Получить все страны")
  public @Nonnull List<CountryJson> findAll() {
    return xaTemplate.execute(() ->
        countryRepository.findAll().stream().map(CountryJson::fromEntity).toList()
    );
  }

  @Override
  @Step("Обновить страну: {country.name}")
  public @Nonnull CountryJson update(CountryJson country) {
    return xaTemplate.execute(() ->
        CountryJson.fromEntity(countryRepository.update(CountryEntity.fromJson(country)))
    );
  }

  @Override
  @Step("Удалить страну: {country.name}")
  public void delete(CountryJson country) {
    xaTemplate.execute(() -> {
      countryRepository.findById(country.id()).ifPresent(countryRepository::delete);
      return null;
    });
  }
}
