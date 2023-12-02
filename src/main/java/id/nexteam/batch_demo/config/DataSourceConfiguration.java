package id.nexteam.batch_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Bean
    public JdbcTransactionManager transactionManager(DataSource dataSource) {
        return new JdbcTransactionManager(dataSource);
    }

}
