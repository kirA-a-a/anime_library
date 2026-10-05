package io.student.rococo.controller;

import io.student.rococo.model.SessionJson;
import java.util.Date;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/session")
public class SessionController {

  @GetMapping
  public SessionJson getSession(@AuthenticationPrincipal Jwt principal) {

    if (principal != null) {
      return new SessionJson(
          principal.getClaim("sub"),
          Date.from(principal.getIssuedAt()),
          Date.from(principal.getExpiresAt())
      );
    } else  {
      return new SessionJson(
          null,
          null,
          null
      );
    }
  }

}
