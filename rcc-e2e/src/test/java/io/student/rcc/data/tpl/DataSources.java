package io.student.rcc.data.tpl;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;

public class DataSources {

  private DataSources() {
  }

  private static final Map<String, DataSource> dataSources = new ConcurrentHashMap<>();

  public static DataSource dataSource(String jdbcUrl) {
    return dataSources.computeIfAbsent(jdbcUrl, key -> {
      MysqlDataSource dataSource = new MysqlDataSource();
      dataSource.setURL(key);
      dataSource.setUser("root");
      dataSource.setPassword("secret");
      return dataSource;
    });
  }
}
