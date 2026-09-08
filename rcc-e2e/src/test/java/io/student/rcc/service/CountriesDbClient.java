package io.student.rcc.service;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.entity.CountryEntity;
import io.student.rcc.data.rococoApi.impl.CountryDaoSpringJdbc;
import io.student.rcc.data.tpl.DataSources;
import io.student.rcc.model.CountryJson;
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

  private final CountryDaoSpringJdbc countryDao = new CountryDaoSpringJdbc(
      DataSources.dataSource(CFG.rccApiJdbcUrl())
  );

  @Override
  public CountryJson create(CountryJson country) {
    return txTemplate.execute(status ->
        CountryJson.fromEntity(countryDao.create(CountryEntity.fromJson(country)))
    );
  }

  @Override
  public Optional<CountryJson> findById(UUID id) {
    return txTemplate.execute(status ->
        countryDao.findById(id).map(CountryJson::fromEntity)
    );
  }

  @Override
  public Optional<CountryJson> findByName(String name) {
    return txTemplate.execute(status ->
        countryDao.findByName(name).map(CountryJson::fromEntity)
    );
  }

  @Override
  public void delete(CountryJson country) {
    txTemplate.executeWithoutResult(status ->
        countryDao.delete(CountryEntity.fromJson(country))
    );
  }
}
