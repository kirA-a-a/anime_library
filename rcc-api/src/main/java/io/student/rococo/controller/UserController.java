package io.student.rococo.controller;

import io.student.rococo.model.UserJson;
import java.util.UUID;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

  @GetMapping
  public UserJson getUser(@AuthenticationPrincipal Jwt principal) {
    return new UserJson(
        UUID.randomUUID(),
        principal.getClaim("sub"),
        "Ivan",
        "Zaglushka",
        ""
    );
  }

  @PatchMapping
  public UserJson updateUser(
      @AuthenticationPrincipal Jwt principal, @RequestBody UserJson user
  ) {
    return user;
  }

}
