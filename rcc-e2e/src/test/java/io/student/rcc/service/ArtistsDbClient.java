package io.student.rcc.service;

import io.qameta.allure.Step;
import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.entity.ArtistEntity;
import io.student.rcc.data.rococoApi.repository.ArtistRepository;
import io.student.rcc.data.tpl.XaTransactionTemplate;
import io.student.rcc.model.ArtistJson;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ArtistsDbClient implements ArtistsClient {

  private static final Config CFG = Config.getInstance();

  private final XaTransactionTemplate xaTemplate = new XaTransactionTemplate(
      CFG.rccApiJdbcUrl()
  );

  private final ArtistRepository artistRepository = ArtistRepository.getInstance();

  @Override
  @Step("Создать художника: {artist.name}")
  public @Nonnull ArtistJson create(ArtistJson artist) {
    return xaTemplate.execute(() ->
        ArtistJson.fromEntity(artistRepository.create(ArtistEntity.fromJson(artist)))
    );
  }

  @Override
  @Step("Найти художника по id: {id}")
  public @Nonnull Optional<ArtistJson> findById(UUID id) {
    return xaTemplate.execute(() ->
        artistRepository.findById(id).map(ArtistJson::fromEntity)
    );
  }

  @Override
  @Step("Найти художника по имени: {name}")
  public @Nonnull Optional<ArtistJson> findByName(String name) {
    return xaTemplate.execute(() ->
        artistRepository.findByName(name).map(ArtistJson::fromEntity)
    );
  }

  @Override
  @Step("Получить всех художников")
  public @Nonnull List<ArtistJson> findAll() {
    return xaTemplate.execute(() ->
        artistRepository.findAll().stream().map(ArtistJson::fromEntity).toList()
    );
  }

  @Override
  @Step("Обновить художника: {artist.name}")
  public @Nonnull ArtistJson update(ArtistJson artist) {
    return xaTemplate.execute(() ->
        ArtistJson.fromEntity(artistRepository.update(ArtistEntity.fromJson(artist)))
    );
  }

  @Override
  @Step("Удалить художника: {artist.name}")
  public void delete(ArtistJson artist) {
    xaTemplate.execute(() -> {
      artistRepository.findById(artist.id()).ifPresent(artistRepository::delete);
      return null;
    });
  }
}
