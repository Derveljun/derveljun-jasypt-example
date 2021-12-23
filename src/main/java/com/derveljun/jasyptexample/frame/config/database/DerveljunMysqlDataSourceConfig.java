package com.derveljun.jasyptexample.frame.config.database;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
@MapperScan(basePackages = {"com.derveljun.jasyptexample.module.**.*"})
public class DerveljunMysqlDataSourceConfig extends DataSourceConfigHelper {

    public static final String DATASOURCE_NAME = "derveljunMysqlDatasource";
    public static final String SQL_SESSION_FACTORY_NAME = "derveljunMysqlSqlSessionFactory";
    public static final String SQL_SESSION_TEMPLATE_NAME = "derveljunMysqlSqlSessionTemplate";

    @Bean(DATASOURCE_NAME)
    @ConfigurationProperties(prefix = "derveljun.datasource.derveljun-mysql-db")
    public DataSource dataSource() {
        log.info("{} created.", DATASOURCE_NAME);
        return new HikariDataSource();
    }

    @Bean(SQL_SESSION_FACTORY_NAME)
    public SqlSessionFactory sqlSessionFactory (
            @Autowired @Qualifier(DATASOURCE_NAME) DataSource dataSource) throws Exception {
        log.info("{} created.", SQL_SESSION_FACTORY_NAME);
        return getSessionFactoryBean(dataSource).getObject();
    }

    @Bean(SQL_SESSION_TEMPLATE_NAME)
    public SqlSessionTemplate sqlSessionTemplate (
            @Autowired @Qualifier(SQL_SESSION_FACTORY_NAME) SqlSessionFactory factory) {
        log.info("{} created.", SQL_SESSION_TEMPLATE_NAME);
        return new SqlSessionTemplate(factory);
    }

}
