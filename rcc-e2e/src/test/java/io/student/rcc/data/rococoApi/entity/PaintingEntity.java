package io.student.rcc.data.rococoApi.entity;

import io.student.rcc.model.PaintingJson;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nonnull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

@Getter
@Setter
@Entity
@Table(name = "painting")
public class PaintingEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, unique = true, columnDefinition = "BINARY(16)")
  private UUID id;

  @Column(name = "title", nullable = false, length = 255)
  private String title;

  @Column(name = "description", length = 1000)
  private String description;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "artist_id", nullable = false)
  private ArtistEntity artist;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "museum_id")
  private MuseumEntity museum;

  @Lob
  @Column(name = "content", columnDefinition = "LONGBLOB")
  private byte[] content;

  public static @Nonnull PaintingEntity fromJson(PaintingJson json) {
    PaintingEntity entity = new PaintingEntity();
    entity.setId(json.id());
    entity.setTitle(json.title());
    entity.setDescription(json.description());
    entity.setContent(
        json.content() != null ? json.content().getBytes(StandardCharsets.UTF_8) : null
    );
    if (json.artistId() != null) {
      ArtistEntity artist = new ArtistEntity();
      artist.setId(json.artistId());
      entity.setArtist(artist);
    }
    if (json.museumId() != null) {
      MuseumEntity museum = new MuseumEntity();
      museum.setId(json.museumId());
      entity.setMuseum(museum);
    }
    return entity;
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> oEffectiveClass = o instanceof HibernateProxy
        ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
        : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
        : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) {
      return false;
    }
    PaintingEntity that = (PaintingEntity) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}
