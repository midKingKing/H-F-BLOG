package com.hf.config

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

/**
 * @author:  ximufeng
 * @version: v1.0
 * @description:
 * @date:2020/4/7
 */

@Configuration
open class RestTemplateConfig {

    /**
     * 构建一个使用默认配置RestTemplate Bean
     */
    @Bean
    open fun restTemplate(): RestTemplate {
        return RestTemplateBuilder().build()
    }
}