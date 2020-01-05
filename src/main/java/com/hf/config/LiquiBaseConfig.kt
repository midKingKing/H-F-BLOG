package com.hf.config

import liquibase.integration.spring.SpringLiquibase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
open class LiquiBaseConfig {
    @Value("\${liquibase.run:false}")
    private val isShouldRun: Boolean? = null

    @Bean
    open fun liquibase(@Autowired dataSource: DataSource?): SpringLiquibase {
        return SpringLiquibase().apply{
            this.dataSource = dataSource
            changeLog = "classpath:db/liquibase/master.xml"
            contexts = "development,test,production"
            setShouldRun(isShouldRun!!)
        }
    }
}