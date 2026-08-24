package io.student.rcc.service;

import static io.student.rcc.data.Databases.transaction;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.ArtistJdbcDao;
import io.student.rcc.data.rococoApi.entity.ArtistEntity;
import io.student.rcc.model.ArtistJson;
import java.sql.Connection;
import java.util.Optional;
import java.util.UUID;

public class ArtistsDbClient implements ArtistsClient {

  private static final Config CFG = Config.getInstance();

  @Override
  public ArtistJson create(ArtistJson artist) {
    return transaction(
        connection -> {
          return ArtistJson.fromEntity(
              new ArtistJdbcDao(connection).create(ArtistEntity.fromJson(artist))
          );
        },
        CFG.rccApiJdbcUrl(),
        Connection.TRANSACTION_READ_COMMITTED
    );
  }

  @Override
  public Optional<ArtistJson> findById(UUID id) {
    return transaction(
        connection -> {
          return new ArtistJdbcDao(connection).findById(id).map(ArtistJson::fromEntity);
        },
        CFG.rccApiJdbcUrl(),
        Connection.TRANSACTION_READ_COMMITTED
    );
  }

  @Override
  public Optional<ArtistJson> findByName(String name) {
    return transaction(
        connection -> {
          return new ArtistJdbcDao(connection).findByName(name).map(ArtistJson::fromEntity);
        },
        CFG.rccApiJdbcUrl(),
        Connection.TRANSACTION_READ_COMMITTED
    );
  }

  @Override
  public void delete(ArtistJson artist) {
    transaction(
        connection -> {
          new ArtistJdbcDao(connection).delete(ArtistEntity.fromJson(artist));
        },
        CFG.rccApiJdbcUrl(),
        Connection.TRANSACTION_READ_COMMITTED
    );
  }
}
