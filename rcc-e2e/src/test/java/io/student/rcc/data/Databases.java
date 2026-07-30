package io.student.rcc.data;

import com.mysql.cj.jdbc.MysqlDataSource;
import io.student.rcc.config.Config;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;

public class Databases {
  private Databases() {
  }

  private static final Map<String, DataSource> dataSources = new ConcurrentHashMap<>();

  private static DataSource dataSource(String jdbcUrl) {
    return dataSources.computeIfAbsent(jdbcUrl, key -> {
      MysqlDataSource ds = new MysqlDataSource();
      ds.setURL(key);
      ds.setUser("root");
      ds.setPassword("secret");
      return ds;
    });
  }

  public static Connection connection(String jdbcUrl) throws SQLException {
    return dataSource(jdbcUrl).getConnection();
  }
}
