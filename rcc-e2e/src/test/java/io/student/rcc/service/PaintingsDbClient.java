package io.student.rcc.service;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.entity.PaintingEntity;
import io.student.rcc.data.rococoApi.repository.PaintingRepository;
import io.student.rcc.data.tpl.XaTransactionTemplate;
import io.student.rcc.model.PaintingJson;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PaintingsDbClient implements PaintingsClient {

  private static final Config CFG = Config.getInstance();

  private final XaTransactionTemplate xaTemplate = new XaTransactionTemplate(
      CFG.rccApiJdbcUrl()
  );

  private final PaintingRepository paintingRepository = PaintingRepository.getInstance();

  @Override
  public PaintingJson create(PaintingJson painting) {
    return xaTemplate.execute(() ->
        PaintingJson.fromEntity(paintingRepository.create(PaintingEntity.fromJson(painting)))
    );
  }

  @Override
  public Optional<PaintingJson> findById(UUID id) {
    return xaTemplate.execute(() ->
        paintingRepository.findById(id).map(PaintingJson::fromEntity)
    );
  }

  @Override
  public Optional<PaintingJson> findByTitle(String title) {
    return xaTemplate.execute(() ->
        paintingRepository.findByTitle(title).map(PaintingJson::fromEntity)
    );
  }

  @Override
  public List<PaintingJson> findAll() {
    return xaTemplate.execute(() ->
        paintingRepository.findAll().stream().map(PaintingJson::fromEntity).toList()
    );
  }

  @Override
  public PaintingJson update(PaintingJson painting) {
    return xaTemplate.execute(() ->
        PaintingJson.fromEntity(paintingRepository.update(PaintingEntity.fromJson(painting)))
    );
  }

  @Override
  public void delete(PaintingJson painting) {
    xaTemplate.execute(() -> {
      paintingRepository.findById(painting.id()).ifPresent(paintingRepository::delete);
      return null;
    });
  }
}
