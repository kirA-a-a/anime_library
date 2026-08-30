package io.student.rcc.service;

import static io.student.rcc.data.Databases.transaction;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.impl.MuseumJdbcDao;
import io.student.rcc.data.rococoApi.entity.MuseumEntity;
import io.student.rcc.model.MuseumJson;
import java.sql.Connection;
import java.util.Optional;
import java.util.UUID;

public class MuseumsDbClient implements MuseumsClient {

  private static final Config CFG = Config.getInstance();

  @Override
  public MuseumJson create(MuseumJson museum) {
    return transaction(
        connection -> {
          return MuseumJson.fromEntity(
              new MuseumJdbcDao(connection).create(MuseumEntity.fromJson(museum))
          );
        },
        CFG.rccApiJdbcUrl(),
        Connection.TRANSACTION_READ_COMMITTED
    );
  }

  @Override
  public Optional<MuseumJson> findById(UUID id) {
    return transaction(
        connection -> {
          return new MuseumJdbcDao(connection).findById(id).map(MuseumJson::fromEntity);
        },
        CFG.rccApiJdbcUrl(),
        Connection.TRANSACTION_READ_COMMITTED
    );
  }

  @Override
  public Optional<MuseumJson> findByTitle(String title) {
    return transaction(
        connection -> {
          return new MuseumJdbcDao(connection).findByTitle(title).map(MuseumJson::fromEntity);
        },
        CFG.rccApiJdbcUrl(),
        Connection.TRANSACTION_READ_COMMITTED
    );
  }

  @Override
  public void delete(MuseumJson museum) {
    transaction(
        connection -> {
          new MuseumJdbcDao(connection).delete(MuseumEntity.fromJson(museum));
        },
        CFG.rccApiJdbcUrl(),
        Connection.TRANSACTION_READ_COMMITTED
    );
  }
}
