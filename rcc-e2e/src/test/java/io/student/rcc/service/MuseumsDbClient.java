package io.student.rcc.service;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.entity.MuseumEntity;
import io.student.rcc.data.rococoApi.impl.MuseumDaoSpringJdbc;
import io.student.rcc.data.tpl.DataSources;
import io.student.rcc.model.MuseumJson;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

public class MuseumsDbClient implements MuseumsClient {

  private static final Config CFG = Config.getInstance();

  private final TransactionTemplate txTemplate = new TransactionTemplate(
      new JdbcTransactionManager(
          DataSources.dataSource(CFG.rccApiJdbcUrl())
      )
  );

  private final MuseumDaoSpringJdbc museumDao = new MuseumDaoSpringJdbc(
      DataSources.dataSource(CFG.rccApiJdbcUrl())
  );

  @Override
  public MuseumJson create(MuseumJson museum) {
    return txTemplate.execute(status ->
        MuseumJson.fromEntity(museumDao.create(MuseumEntity.fromJson(museum)))
    );
  }

  @Override
  public Optional<MuseumJson> findById(UUID id) {
    return txTemplate.execute(status ->
        museumDao.findById(id).map(MuseumJson::fromEntity)
    );
  }

  @Override
  public Optional<MuseumJson> findByTitle(String title) {
    return txTemplate.execute(status ->
        museumDao.findByTitle(title).map(MuseumJson::fromEntity)
    );
  }

  @Override
  public void delete(MuseumJson museum) {
    txTemplate.executeWithoutResult(status ->
        museumDao.delete(MuseumEntity.fromJson(museum))
    );
  }
}
