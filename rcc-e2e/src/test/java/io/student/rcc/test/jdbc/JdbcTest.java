package io.student.rcc.test.jdbc;

import static io.student.rcc.utils.RandomUserDataUtils.randomUserName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoAuth.entity.AuthAuthorityEntity;
import io.student.rcc.data.rococoAuth.entity.Authority;
import io.student.rcc.data.rococoAuth.entity.AuthUserEntity;
import io.student.rcc.data.rococoAuth.repository.iml.AuthUserRepositoryHibernate;
import io.student.rcc.service.UsersDbClient;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class JdbcTest {

  private static final Config CFG = Config.getInstance();
  private static final String PASSWORD = "12345";
  private final UsersDbClient usersDbClient = new UsersDbClient();
  private final AuthUserRepositoryHibernate authUserRepository = new AuthUserRepositoryHibernate();
  private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
  private final JdbcTemplate jdbcTemplateAuth = new JdbcTemplate(
      new SingleConnectionDataSource(
          CFG.authJdbcUrl(),
          CFG.dbUserName(),
          CFG.dbPassword(),
          true
      )
  );
  private final JdbcTemplate jdbcTemplateApi = new JdbcTemplate(
      new SingleConnectionDataSource(
          CFG.rccApiJdbcUrl(),
          CFG.dbUserName(),
          CFG.dbPassword(),
          true
      )
  );

  @Test
  void shouldCreateUserInAuthDatabase() {
    String username = randomUserName();

    UUID id = usersDbClient.createUser(username, PASSWORD);

    AuthUserRow user = findUser(id);
    assertNotNull(id);
    assertEquals(username, user.username());
    assertTrue(user.enabled());
    assertTrue(user.accountNonExpired());
    assertTrue(user.accountNonLocked());
    assertTrue(user.credentialsNonExpired());
    assertTrue(passwordEncoder.matches(PASSWORD, user.password()));
    assertEquals(0, countAuthorities(id));
    assertEquals(1, countApiUsers(username));
  }

  @ParameterizedTest
  @EnumSource(Authority.class)
  void shouldCreateUserWithAuthorityInAuthDatabase(Authority authority) {
    String username = randomUserName();

    UUID id = usersDbClient.createUser(username, PASSWORD, authority);

    AuthUserRow user = findUser(id);
    assertNotNull(id);
    assertEquals(username, user.username());
    assertTrue(passwordEncoder.matches(PASSWORD, user.password()));
    assertEquals(authority.name(), findAuthority(id));
  }

  @Test
  void shouldCreateUserWithAllAuthorities() {
    String username = randomUserName();

    UUID id = usersDbClient.createUser(username, PASSWORD, Authority.read, Authority.write);

    AuthUserRow user = findUser(id);
    assertNotNull(id);
    assertEquals(username, user.username());
    List<AuthorityRow> authoritiesRows = checkAuthorityForUser(id);

    assertEquals(2, authoritiesRows.size());

    Set<Authority> actual = authoritiesRows.stream()
        .map(AuthorityRow::authority)
        .collect(Collectors.toSet());

    assertEquals(Set.of(Authority.read, Authority.write), actual);
  }

  @Test
  void findAllShouldReturnSingleUserObjectForJoinedAuthorities() {
    String username = randomUserName();
    UUID id = usersDbClient.createUser(username, PASSWORD, Authority.read, Authority.write);

    List<AuthUserEntity> matched = authUserRepository.findAll().stream()
        .filter(user -> id.equals(user.getId()))
        .toList();

    assertEquals(1, matched.size());

    AuthUserEntity found = matched.getFirst();
    Set<Authority> actualAuthorities = found.getAuthorities().stream()
        .map(AuthAuthorityEntity::getAuthority)
        .collect(Collectors.toSet());

    assertEquals(2, found.getAuthorities().size());
    assertEquals(Set.of(Authority.read, Authority.write), actualAuthorities);
  }

  private List<AuthorityRow> checkAuthorityForUser(UUID userId) {

    return jdbcTemplateAuth.query(
        "SELECT BIN_TO_UUID(id) AS id, BIN_TO_UUID(user_id) AS user_id, authority "
            + "FROM `authority` WHERE user_id = UUID_TO_BIN(?)",
        (rs, rowNum) -> new AuthorityRow(
            rs.getObject("id", UUID.class),
            rs.getObject("user_id", UUID.class),
            Authority.valueOf(rs.getString("authority"))
        ),
        userId.toString());

  }

  @Test
  void shouldRollbackAuthWhenApiUserCreationFails() {
    String username = randomUserName();

    jdbcTemplateApi.update(
        "INSERT INTO `user`(id, username) VALUES (UUID_TO_BIN(?), ?)",
        UUID.randomUUID().toString(),
        username
    );

    try {
      assertThrows(RuntimeException.class, () ->
          usersDbClient.createUser(username, PASSWORD, Authority.read)
      );

      assertEquals(0, countAuthUsers(username));
      assertEquals(0, countAuthAuthorities(username));
      assertEquals(1, countApiUsers(username));
    } finally {
      jdbcTemplateApi.update(
          "DELETE FROM `user` WHERE username = ?",
          username
      );
    }
  }

  private int countAuthUsers(String username) {
    return jdbcTemplateAuth.queryForObject(
        "SELECT COUNT(*) FROM `user` WHERE username = ?",
        Integer.class,
        username
    );
  }

  private int countAuthAuthorities(String username) {
    return jdbcTemplateAuth.queryForObject(
        "SELECT COUNT(*) FROM `authority` a "
            + "JOIN `user` u ON u.id = a.user_id "
            + "WHERE u.username = ?",
        Integer.class,
        username
    );
  }

  private int countApiUsers(String username) {
    return jdbcTemplateApi.queryForObject(
        "SELECT COUNT(*) FROM `user` WHERE username = ?",
        Integer.class,
        username
    );
  }

  private AuthUserRow findUser(UUID id) {
    return jdbcTemplateAuth.queryForObject(
        "SELECT BIN_TO_UUID(id) AS id, username, password, enabled, "
            + "account_non_expired, account_non_locked, credentials_non_expired "
            + "FROM `user` WHERE id = UUID_TO_BIN(?)",
        (rs, rowNum) -> new AuthUserRow(
            UUID.fromString(rs.getString("id")),
            rs.getString("username"),
            rs.getString("password"),
            rs.getBoolean("enabled"),
            rs.getBoolean("account_non_expired"),
            rs.getBoolean("account_non_locked"),
            rs.getBoolean("credentials_non_expired")
        ),
        id.toString()
    );
  }

  private String findAuthority(UUID userId) {
    return jdbcTemplateAuth.queryForObject(
        "SELECT authority FROM `authority` WHERE user_id = UUID_TO_BIN(?)",
        String.class,
        userId.toString()
    );
  }

  private Integer countAuthorities(UUID userId) {
    return jdbcTemplateAuth.queryForObject(
        "SELECT COUNT(*) FROM `authority` WHERE user_id = UUID_TO_BIN(?)",
        Integer.class,
        userId.toString()
    );
  }

  private record AuthUserRow(
      UUID id,
      String username,
      String password,
      boolean enabled,
      boolean accountNonExpired,
      boolean accountNonLocked,
      boolean credentialsNonExpired
  ) {

  }

  private record AuthorityRow(
      UUID id,
      UUID userId,
      Authority authority
  ) {

  }
}
