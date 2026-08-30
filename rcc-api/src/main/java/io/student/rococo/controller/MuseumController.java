package io.student.rococo.controller;

import io.student.rococo.model.CountryJson;
import io.student.rococo.model.GeoJson;
import io.student.rococo.model.MuseumJson;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/museum")
public class MuseumController {

  @GetMapping
  public Page<MuseumJson> getAllMuseums(@PageableDefault Pageable pageable) {

    return new PageImpl<>(
        List.of(
            new MuseumJson(null, null, null, null, new GeoJson(
                null, new CountryJson(null, null)))
        ),
        pageable,
        1
    );
  }

  @GetMapping("/{id}")
  public MuseumJson getMuseumById(@PathVariable String id) {
    return new MuseumJson(UUID.fromString(id), null, null, null, new GeoJson(
        null, new CountryJson(null, null)));
  }

  @GetMapping(params = "title")
  public Page<MuseumJson> getMuseumsByTitle(
      @RequestParam String title, @PageableDefault Pageable pageable
  ) {
    return  new PageImpl<>(
        List.of(
            new MuseumJson(UUID.randomUUID(), title, "Заглушка 1", null, new GeoJson(
                null, null)),
            new MuseumJson(UUID.randomUUID(), title, "Заглушка 2", null, new GeoJson(
                null, null))
        ),
        pageable,
        1
    );
  }

  @PatchMapping
  public MuseumJson updateMuseum(
      @AuthenticationPrincipal Jwt principal, @RequestBody MuseumJson museum
  ) {
    return museum;
  }

  @PostMapping
  public MuseumJson createMuseum(
      @AuthenticationPrincipal Jwt principal, @RequestBody MuseumJson museum
  ) {
    return museum;
  }

}
