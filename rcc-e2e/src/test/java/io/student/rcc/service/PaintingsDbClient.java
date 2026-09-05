package io.student.rcc.service;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.entity.PaintingEntity;
import io.student.rcc.data.rococoApi.impl.PaintingDaoSpringJdbc;
import io.student.rcc.data.tpl.DataSources;
import io.student.rcc.model.PaintingJson;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

public class PaintingsDbClient implements PaintingsClient {

  private static final Config CFG = Config.getInstance();

  private final TransactionTemplate txTemplate = new TransactionTemplate(
      new JdbcTransactionManager(
          DataSources.dataSource(CFG.rccApiJdbcUrl())
      )
  );

  private final PaintingDaoSpringJdbc paintingDao = new PaintingDaoSpringJdbc(
      DataSources.dataSource(CFG.rccApiJdbcUrl())
  );

  @Override
  public PaintingJson create(PaintingJson painting) {
    return txTemplate.execute(status ->
        PaintingJson.fromEntity(paintingDao.create(PaintingEntity.fromJson(painting)))
    );
  }

  @Override
  public Optional<PaintingJson> findById(UUID id) {
    return txTemplate.execute(status ->
        paintingDao.findById(id).map(PaintingJson::fromEntity)
    );
  }

  @Override
  public Optional<PaintingJson> findByTitle(String title) {
    return txTemplate.execute(status ->
        paintingDao.findByTitle(title).map(PaintingJson::fromEntity)
    );
  }

  @Override
  public void delete(PaintingJson painting) {
    txTemplate.executeWithoutResult(status ->
        paintingDao.delete(PaintingEntity.fromJson(painting))
    );
  }
}
