package io.student.rococo.controller;

import io.student.rococo.model.UserJson;
import io.student.rococo.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public UserJson getUser(@AuthenticationPrincipal Jwt principal) {
    return userService.createNewUserIfNotPresent(principal.getClaim("sub"));
  }

  @PatchMapping
  public UserJson updateUser(
      @AuthenticationPrincipal Jwt principal, @RequestBody UserJson user
  ) {
    return userService.update(
        new UserJson(
            user.id(),
            principal.getClaim("sub"),
            user.firstname(),
            user.lastname(),
            user.avatar()
        )
    );
  }
}
