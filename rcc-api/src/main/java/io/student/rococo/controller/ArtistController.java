package io.student.rococo.controller;

import io.student.rococo.model.ArtistJson;
import io.student.rococo.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping("/api/artist")
public class ArtistController {

  private final ArtistService artistService;

  @Autowired
  public ArtistController(ArtistService artistService) {
    this.artistService = artistService;
  }

  @GetMapping("/{id}")
  public ArtistJson getArtistById(@PathVariable String id) {
    return artistService.getArtistById(id);
  }

  @GetMapping(params = "name")
  public Page<ArtistJson> getArtistByName(@PageableDefault Pageable pageable, @RequestParam String name) {
    return artistService.getArtistByName(pageable, name);
  }

  @PostMapping
  public ArtistJson createArtist(
      @AuthenticationPrincipal Jwt principal, @RequestBody ArtistJson artist
  ) {
    return artistService.saveArtist(artist);
  }

  @PatchMapping
  public ArtistJson updateArtist(
      @AuthenticationPrincipal Jwt principal, @RequestBody ArtistJson artist
  ){
    return artistService.updateArtist(artist);
  }

}
