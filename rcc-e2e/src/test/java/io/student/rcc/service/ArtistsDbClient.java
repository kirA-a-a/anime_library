package io.student.rcc.service;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.entity.ArtistEntity;
import io.student.rcc.data.rococoApi.impl.ArtistDaoSpringJdbc;
import io.student.rcc.data.tpl.DataSources;
import io.student.rcc.model.ArtistJson;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

public class ArtistsDbClient implements ArtistsClient {

  private static final Config CFG = Config.getInstance();

  private final TransactionTemplate txTemplate = new TransactionTemplate(
      new JdbcTransactionManager(
          DataSources.dataSource(CFG.rccApiJdbcUrl())
      )
  );

  private final ArtistDaoSpringJdbc artistDao = new ArtistDaoSpringJdbc(
      DataSources.dataSource(CFG.rccApiJdbcUrl())
  );

  @Override
  public ArtistJson create(ArtistJson artist) {
    return txTemplate.execute(status ->
        ArtistJson.fromEntity(artistDao.create(ArtistEntity.fromJson(artist)))
    );
  }

  @Override
  public Optional<ArtistJson> findById(UUID id) {
    return txTemplate.execute(status ->
        artistDao.findById(id).map(ArtistJson::fromEntity)
    );
  }

  @Override
  public Optional<ArtistJson> findByName(String name) {
    return txTemplate.execute(status ->
        artistDao.findByName(name).map(ArtistJson::fromEntity)
    );
  }

  @Override
  public void delete(ArtistJson artist) {
    txTemplate.executeWithoutResult(status ->
        artistDao.delete(ArtistEntity.fromJson(artist))
    );
  }
}
