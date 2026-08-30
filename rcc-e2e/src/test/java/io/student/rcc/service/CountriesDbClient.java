package io.student.rcc.service;

import static io.student.rcc.data.Databases.transaction;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.impl.CountryJdbcDao;
import io.student.rcc.data.rococoApi.entity.CountryEntity;
import io.student.rcc.model.CountryJson;
import java.sql.Connection;
import java.util.Optional;
import java.util.UUID;

public class CountriesDbClient implements CountriesClient {

  private static final Config CFG = Config.getInstance();

  @Override
  public CountryJson create(CountryJson country) {
    return transaction(
        connection -> {
          return CountryJson.fromEntity(
              new CountryJdbcDao(connection).create(CountryEntity.fromJson(country))
          );
        },
        CFG.rccApiJdbcUrl(),
        Connection.TRANSACTION_READ_COMMITTED
    );
  }

  @Override
  public Optional<CountryJson> findById(UUID id) {
    return transaction(
        connection -> {
          return new CountryJdbcDao(connection).findById(id).map(CountryJson::fromEntity);
        },
        CFG.rccApiJdbcUrl(),
        Connection.TRANSACTION_READ_COMMITTED
    );
  }

  @Override
  public Optional<CountryJson> findByName(String name) {
    return transaction(
        connection -> {
          return new CountryJdbcDao(connection).findByName(name).map(CountryJson::fromEntity);
        },
        CFG.rccApiJdbcUrl(),
        Connection.TRANSACTION_READ_COMMITTED
    );
  }

  @Override
  public void delete(CountryJson country) {
    transaction(
        connection -> {
          new CountryJdbcDao(connection).delete(CountryEntity.fromJson(country));
        },
        CFG.rccApiJdbcUrl(),
        Connection.TRANSACTION_READ_COMMITTED
    );
  }
}
