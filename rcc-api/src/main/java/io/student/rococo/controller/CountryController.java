package io.student.rococo.controller;

import io.student.rococo.model.ArtistJson;
import io.student.rococo.model.CountryJson;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/country")
public class CountryController {

  @GetMapping
  public Page<CountryJson> getAllCountries(@PageableDefault Pageable pageable) {
    return new PageImpl<>(
        List.of(
            new CountryJson(
                UUID.randomUUID(),
                "Россия"
            ),
            new CountryJson(
                UUID.randomUUID(),
                "Вьетнам"
            )
        ),
        pageable,
        1
    );
  }

}
