package io.student.rcc.service;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.entity.PaintingEntity;
import io.student.rcc.data.rococoApi.repository.PaintingRepository;
import io.student.rcc.data.rococoApi.repository.iml.PaintingRepositoryHibernate;
import io.student.rcc.data.tpl.DataSources;
import io.student.rcc.model.PaintingJson;
import java.util.List;
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

  private final PaintingRepository paintingRepository = new PaintingRepositoryHibernate();

  @Override
  public PaintingJson create(PaintingJson painting) {
    return txTemplate.execute(status ->
        PaintingJson.fromEntity(paintingRepository.create(PaintingEntity.fromJson(painting)))
    );
  }

  @Override
  public Optional<PaintingJson> findById(UUID id) {
    return txTemplate.execute(status ->
        paintingRepository.findById(id).map(PaintingJson::fromEntity)
    );
  }

  @Override
  public Optional<PaintingJson> findByTitle(String title) {
    return txTemplate.execute(status ->
        paintingRepository.findByTitle(title).map(PaintingJson::fromEntity)
    );
  }

  @Override
  public List<PaintingJson> findAll() {
    return txTemplate.execute(status ->
        paintingRepository.findAll().stream().map(PaintingJson::fromEntity).toList()
    );
  }

  @Override
  public PaintingJson update(PaintingJson painting) {
    return txTemplate.execute(status ->
        PaintingJson.fromEntity(paintingRepository.update(PaintingEntity.fromJson(painting)))
    );
  }

  @Override
  public void delete(PaintingJson painting) {
    txTemplate.executeWithoutResult(status ->
        paintingRepository.delete(PaintingEntity.fromJson(painting))
    );
  }
}
