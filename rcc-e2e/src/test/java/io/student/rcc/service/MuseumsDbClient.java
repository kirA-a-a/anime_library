package io.student.rcc.service;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.entity.MuseumEntity;
import io.student.rcc.data.rococoApi.repository.MuseumRepository;
import io.student.rcc.data.rococoApi.repository.iml.MuseumRepositoryHibernate;
import io.student.rcc.data.tpl.DataSources;
import io.student.rcc.model.MuseumJson;
import java.util.List;
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

  private final MuseumRepository museumRepository = new MuseumRepositoryHibernate();

  @Override
  public MuseumJson create(MuseumJson museum) {
    return txTemplate.execute(status ->
        MuseumJson.fromEntity(museumRepository.create(MuseumEntity.fromJson(museum)))
    );
  }

  @Override
  public Optional<MuseumJson> findById(UUID id) {
    return txTemplate.execute(status ->
        museumRepository.findById(id).map(MuseumJson::fromEntity)
    );
  }

  @Override
  public Optional<MuseumJson> findByTitle(String title) {
    return txTemplate.execute(status ->
        museumRepository.findByTitle(title).map(MuseumJson::fromEntity)
    );
  }

  @Override
  public List<MuseumJson> findAll() {
    return txTemplate.execute(status ->
        museumRepository.findAll().stream().map(MuseumJson::fromEntity).toList()
    );
  }

  @Override
  public MuseumJson update(MuseumJson museum) {
    return txTemplate.execute(status ->
        MuseumJson.fromEntity(museumRepository.update(MuseumEntity.fromJson(museum)))
    );
  }

  @Override
  public void delete(MuseumJson museum) {
    txTemplate.executeWithoutResult(status ->
        museumRepository.delete(MuseumEntity.fromJson(museum))
    );
  }
}
