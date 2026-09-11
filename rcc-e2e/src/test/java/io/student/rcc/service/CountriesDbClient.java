package io.student.rcc.service;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.entity.CountryEntity;
import io.student.rcc.data.rococoApi.repository.CountryRepository;
import io.student.rcc.data.rococoApi.repository.iml.CountryRepositoryHibernate;
import io.student.rcc.data.tpl.DataSources;
import io.student.rcc.model.CountryJson;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

public class CountriesDbClient implements CountriesClient {

  private static final Config CFG = Config.getInstance();

  private final TransactionTemplate txTemplate = new TransactionTemplate(
      new JdbcTransactionManager(
          DataSources.dataSource(CFG.rccApiJdbcUrl())
      )
  );

  private final CountryRepository countryRepository = new CountryRepositoryHibernate();

  @Override
  public CountryJson create(CountryJson country) {
    return txTemplate.execute(status ->
        CountryJson.fromEntity(countryRepository.create(CountryEntity.fromJson(country)))
    );
  }

  @Override
  public Optional<CountryJson> findById(UUID id) {
    return txTemplate.execute(status ->
        countryRepository.findById(id).map(CountryJson::fromEntity)
    );
  }

  @Override
  public Optional<CountryJson> findByName(String name) {
    return txTemplate.execute(status ->
        countryRepository.findByName(name).map(CountryJson::fromEntity)
    );
  }

  @Override
  public List<CountryJson> findAll() {
    return txTemplate.execute(status ->
        countryRepository.findAll().stream().map(CountryJson::fromEntity).toList()
    );
  }

  @Override
  public CountryJson update(CountryJson country) {
    return txTemplate.execute(status ->
        CountryJson.fromEntity(countryRepository.update(CountryEntity.fromJson(country)))
    );
  }

  @Override
  public void delete(CountryJson country) {
    txTemplate.executeWithoutResult(status ->
        countryRepository.delete(CountryEntity.fromJson(country))
    );
  }
}
