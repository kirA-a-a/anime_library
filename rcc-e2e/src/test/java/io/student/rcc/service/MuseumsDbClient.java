package io.student.rcc.service;

import io.qameta.allure.Step;
import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.entity.MuseumEntity;
import io.student.rcc.data.rococoApi.repository.MuseumRepository;
import io.student.rcc.data.tpl.XaTransactionTemplate;
import io.student.rcc.model.MuseumJson;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class MuseumsDbClient implements MuseumsClient {

  private static final Config CFG = Config.getInstance();

  private final XaTransactionTemplate xaTemplate = new XaTransactionTemplate(
      CFG.rccApiJdbcUrl()
  );

  private final MuseumRepository museumRepository = MuseumRepository.getInstance();

  @Override
  @Step("Создать музей: {museum.title}")
  public @Nonnull MuseumJson create(MuseumJson museum) {
    return xaTemplate.execute(() ->
        MuseumJson.fromEntity(museumRepository.create(MuseumEntity.fromJson(museum)))
    );
  }

  @Override
  @Step("Найти музей по id: {id}")
  public @Nonnull Optional<MuseumJson> findById(UUID id) {
    return xaTemplate.execute(() ->
        museumRepository.findById(id).map(MuseumJson::fromEntity)
    );
  }

  @Override
  @Step("Найти музей по названию: {title}")
  public @Nonnull Optional<MuseumJson> findByTitle(String title) {
    return xaTemplate.execute(() ->
        museumRepository.findByTitle(title).map(MuseumJson::fromEntity)
    );
  }

  @Override
  @Step("Получить все музеи")
  public @Nonnull List<MuseumJson> findAll() {
    return xaTemplate.execute(() ->
        museumRepository.findAll().stream().map(MuseumJson::fromEntity).toList()
    );
  }

  @Override
  @Step("Обновить музей: {museum.title}")
  public @Nonnull MuseumJson update(MuseumJson museum) {
    return xaTemplate.execute(() ->
        MuseumJson.fromEntity(museumRepository.update(MuseumEntity.fromJson(museum)))
    );
  }

  @Override
  @Step("Удалить музей: {museum.title}")
  public void delete(MuseumJson museum) {
    xaTemplate.execute(() -> {
      museumRepository.findById(museum.id()).ifPresent(museumRepository::delete);
      return null;
    });
  }
}
