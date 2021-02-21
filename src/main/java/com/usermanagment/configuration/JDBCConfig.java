package com.usermanagment.configuration;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JDBCConfig {

	@Bean
	public DataSource getDataSource() {
		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.url(System.getenv("JDBC_URL"));
		dataSourceBuilder.username(System.getenv("JDBC_USERNAME"));
		dataSourceBuilder.password(System.getenv("JDBC_PASSWORD"));
		DataSource build = dataSourceBuilder.build();
		return build;
	}
}
