package io.student.rcc.service;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.entity.ArtistEntity;
import io.student.rcc.data.rococoApi.repository.ArtistRepository;
import io.student.rcc.data.rococoApi.repository.iml.ArtistRepositoryHibernate;
import io.student.rcc.data.tpl.DataSources;
import io.student.rcc.model.ArtistJson;
import java.util.List;
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

  private final ArtistRepository artistRepository = new ArtistRepositoryHibernate();

  @Override
  public ArtistJson create(ArtistJson artist) {
    return txTemplate.execute(status ->
        ArtistJson.fromEntity(artistRepository.create(ArtistEntity.fromJson(artist)))
    );
  }

  @Override
  public Optional<ArtistJson> findById(UUID id) {
    return txTemplate.execute(status ->
        artistRepository.findById(id).map(ArtistJson::fromEntity)
    );
  }

  @Override
  public Optional<ArtistJson> findByName(String name) {
    return txTemplate.execute(status ->
        artistRepository.findByName(name).map(ArtistJson::fromEntity)
    );
  }

  @Override
  public List<ArtistJson> findAll() {
    return txTemplate.execute(status ->
        artistRepository.findAll().stream().map(ArtistJson::fromEntity).toList()
    );
  }

  @Override
  public ArtistJson update(ArtistJson artist) {
    return txTemplate.execute(status ->
        ArtistJson.fromEntity(artistRepository.update(ArtistEntity.fromJson(artist)))
    );
  }

  @Override
  public void delete(ArtistJson artist) {
    txTemplate.executeWithoutResult(status ->
        artistRepository.delete(ArtistEntity.fromJson(artist))
    );
  }
}
