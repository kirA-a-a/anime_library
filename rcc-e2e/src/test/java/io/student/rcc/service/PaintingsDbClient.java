package io.student.rcc.service;

import static io.student.rcc.data.Databases.transaction;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.PaintingJdbcDao;
import io.student.rcc.data.rococoApi.entity.PaintingEntity;
import io.student.rcc.model.PaintingJson;
import java.sql.Connection;
import java.util.Optional;
import java.util.UUID;

public class PaintingsDbClient implements PaintingsClient {

  private static final Config CFG = Config.getInstance();

  @Override
  public PaintingJson create(PaintingJson painting) {
    return transaction(
        connection -> {
          return PaintingJson.fromEntity(
              new PaintingJdbcDao(connection).create(PaintingEntity.fromJson(painting))
          );
        },
        CFG.rccApiJdbcUrl(),
        Connection.TRANSACTION_READ_COMMITTED
    );
  }

  @Override
  public Optional<PaintingJson> findById(UUID id) {
    return transaction(
        connection -> {
          return new PaintingJdbcDao(connection).findById(id).map(PaintingJson::fromEntity);
        },
        CFG.rccApiJdbcUrl(),
        Connection.TRANSACTION_READ_COMMITTED
    );
  }

  @Override
  public Optional<PaintingJson> findByTitle(String title) {
    return transaction(
        connection -> {
          return new PaintingJdbcDao(connection).findByTitle(title).map(PaintingJson::fromEntity);
        },
        CFG.rccApiJdbcUrl(),
        Connection.TRANSACTION_READ_COMMITTED
    );
  }

  @Override
  public void delete(PaintingJson painting) {
    transaction(
        connection -> {
          new PaintingJdbcDao(connection).delete(PaintingEntity.fromJson(painting));
        },
        CFG.rccApiJdbcUrl(),
        Connection.TRANSACTION_READ_COMMITTED
    );
  }
}
