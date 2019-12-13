package com.hf.config

import com.alibaba.druid.pool.DruidDataSource
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
@EnableConfigurationProperties(DruidProperties::class)
@ConditionalOnClass(DruidDataSource::class)
@ConditionalOnProperty(prefix = "druid", name = ["url"])
@AutoConfigureBefore(DataSourceAutoConfiguration::class)
open class DruidConfiguration {
    @Bean
    open fun dataSource(properties: DruidProperties): DataSource {
        return DruidDataSource().apply {
            url = properties.url
            username = properties.username
            password = properties.password
            isTestOnBorrow = properties.testOnBorrow
            initialSize = properties.initialSize
            minIdle = properties.minIdle
            maxActive = properties.maxActive
            init()
        }
    }
}

@ConfigurationProperties(prefix = "druid")
class DruidProperties {
    var url: String? = null
    var username: String? = null
    var password: String? = null
    var maxActive: Int = 0
    var minIdle: Int = 0
    var initialSize: Int = 0
    var testOnBorrow: Boolean = false
}