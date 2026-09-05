package io.student.rcc.data.tpl;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;

public class DataSources {

  private DataSources() {
  }

  private static final Map<String, DataSource> dataSources = new ConcurrentHashMap<>();

  public static DataSource dataSource(String jdbcUrl) {
    return dataSources.computeIfAbsent(jdbcUrl, key -> {
      AtomikosDataSourceBean dsBean = new AtomikosDataSourceBean();
      final String uniqId = key.substring(key.indexOf("3306/") + 5).split("\\?")[0];
      dsBean.setUniqueResourceName(uniqId);
      dsBean.setXaDataSourceClassName("com.mysql.cj.jdbc.MysqlXADataSource");
      Properties props = new Properties();
      props.put("URL", key);
      props.put("user", "root");
      props.put("password", "secret");
      props.put("pinGlobalTxToPhysicalConnection", "true");
      dsBean.setXaProperties(props);
      dsBean.setPoolSize(3);
      dsBean.setMaxPoolSize(10);
      return dsBean;
    });
  }
}
