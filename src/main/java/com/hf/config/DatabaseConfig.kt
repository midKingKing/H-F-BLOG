package com.hf.config

import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import javax.sql.DataSource

@Configuration
@EnableConfigurationProperties(DatabaseConfig::class)
@ConditionalOnClass(DataSource::class)
@ConditionalOnProperty(prefix = "database", name = ["url"])
@AutoConfigureBefore(DataSourceAutoConfiguration::class)
open class DatabaseConfiguration {
    @Bean
    open fun dataSource(properties: DatabaseConfig): DataSource {
        return HikariDataSource().apply {
            jdbcUrl = properties.url
            username = properties.username
            password = properties.password
            minimumIdle = 5
            maximumPoolSize = 10
            connectionTimeout = 30000
            addDataSourceProperty("cachePrepStmts", "true");
            addDataSourceProperty("prepStmtCacheSize", "250");
            addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        }
    }
}

@ConfigurationProperties(prefix = "database")
class DatabaseConfig {
    var url: String? = null
    var username: String? = null
    var password: String? = null
}