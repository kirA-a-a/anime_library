package io.student.rcc.service;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.entity.MuseumEntity;
import io.student.rcc.data.rococoApi.repository.MuseumRepository;
import io.student.rcc.data.tpl.XaTransactionTemplate;
import io.student.rcc.model.MuseumJson;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MuseumsDbClient implements MuseumsClient {

  private static final Config CFG = Config.getInstance();

  private final XaTransactionTemplate xaTemplate = new XaTransactionTemplate(
      CFG.rccApiJdbcUrl()
  );

  private final MuseumRepository museumRepository = MuseumRepository.getInstance();

  @Override
  public MuseumJson create(MuseumJson museum) {
    return xaTemplate.execute(() ->
        MuseumJson.fromEntity(museumRepository.create(MuseumEntity.fromJson(museum)))
    );
  }

  @Override
  public Optional<MuseumJson> findById(UUID id) {
    return xaTemplate.execute(() ->
        museumRepository.findById(id).map(MuseumJson::fromEntity)
    );
  }

  @Override
  public Optional<MuseumJson> findByTitle(String title) {
    return xaTemplate.execute(() ->
        museumRepository.findByTitle(title).map(MuseumJson::fromEntity)
    );
  }

  @Override
  public List<MuseumJson> findAll() {
    return xaTemplate.execute(() ->
        museumRepository.findAll().stream().map(MuseumJson::fromEntity).toList()
    );
  }

  @Override
  public MuseumJson update(MuseumJson museum) {
    return xaTemplate.execute(() ->
        MuseumJson.fromEntity(museumRepository.update(MuseumEntity.fromJson(museum)))
    );
  }

  @Override
  public void delete(MuseumJson museum) {
    xaTemplate.execute(() -> {
      museumRepository.findById(museum.id()).ifPresent(museumRepository::delete);
      return null;
    });
  }
}
