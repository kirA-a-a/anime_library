package io.student.rococo.controller;

import io.student.rococo.model.ArtistJson;
import io.student.rococo.model.CountryJson;
import io.student.rococo.model.GeoJson;
import io.student.rococo.model.MuseumJson;
import io.student.rococo.model.PaintingJson;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/painting")
public class PaintingController {

  @GetMapping("/{id}")
  public PaintingJson getPaintingById(@PathVariable String id) {
    return new PaintingJson(
        UUID.fromString(id), null, null, null, null, null
    );
  }

  @PostMapping("author/{artistId}")
  public Page<PaintingJson> getPaintingsByArtist(
      @PathVariable String artistId, @PageableDefault Pageable pageable
  ) {
    return new PageImpl<>(
        List.of(
            new PaintingJson(
                UUID.randomUUID(),
                "Заглушка заглушковна",
                null,
                null,
                new ArtistJson(
                    UUID.fromString(artistId),
                    "Заглушка Артистовна",
                    null,
                    null
                ),
                null
            )
        ),
        pageable,
        1
    );
  }

  @GetMapping(params = "title")
  public Page<PaintingJson> getPaintingsByTitle(
      @RequestParam String title, @PageableDefault Pageable pageable
  ) {
    return new PageImpl<>(
        List.of(
            new PaintingJson(
                UUID.randomUUID(),
                title,
                null,
                null,
                null,
                null
            )
        ),
        pageable,
        1
    );
  }


  @GetMapping
  public Page<PaintingJson> getPaintings(
      @PageableDefault Pageable pageable,
      @AuthenticationPrincipal Jwt principal
  ) {
    return new PageImpl<>(
        List.of(
            new PaintingJson(
                UUID.fromString("44444444-4444-4444-4444-444444444444"),
                "Water Lilies",
                "Series of approximately 250 oil paintings",
                "https://example.com/painting/water-lilies.png",
                new ArtistJson(
                    UUID.fromString("33333333-3333-3333-3333-333333333333"),
                    "Claude Monet",
                    "French painter, founder of impressionism",
                    "https://example.com/artist/monet.jpg"
                ),
                new MuseumJson(
                    UUID.fromString("22222222-2222-2222-2222-222222222222"),
                    "Louvre Museum",
                    "World's largest art museum",
                    "https://example.com/museum/louvre.jpg",
                    new GeoJson(
                        "Paris",
                        new CountryJson(UUID.fromString(
                            "11111111-1111-1111-1111-111111111111"
                        ), "France"
                        )
                    )
                )
            )
        ),
        pageable,
        1
    );
  }

  @PostMapping
  public PaintingJson createPainting(
      @AuthenticationPrincipal Jwt principal, @RequestBody PaintingJson painting
  ) {
    return painting;
  }

  @PutMapping
  public PaintingJson updatePainting(
      @AuthenticationPrincipal Jwt principal, @RequestBody PaintingJson painting
  ) {
    return painting;
  }

}
