package com.hf.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

@Configuration
@EnableConfigurationProperties(RedisProperties::class)
@ConditionalOnClass(Jedis::class)
@ConditionalOnProperty(prefix = "redis", name = ["host"])
open class RedisConfiguration {
    @Bean
    open fun redisService(properties: RedisProperties): RedisService = RedisService(JedisPool(JedisPoolConfig().apply {
        maxIdle = properties.maxIdleSize
        minIdle = properties.minIdleSize
        maxTotal = properties.maxTotal
        maxWaitMillis = properties.maxWaitMillis
    }, properties.host))
}

@ConfigurationProperties(prefix = "redis")
class RedisProperties {
    var host: String = "0.0.0.0"
    var port: Int = 6379
    var maxIdleSize: Int = 5
    var minIdleSize: Int = 0
    var maxTotal: Int = 5
    var maxWaitMillis: Long = 3000
}

class RedisService(private val jedisPool: JedisPool) {
    private val jedis
        get() = jedisPool.resource

    fun get(key: String): String? {
        return jedis.get(key).also { jedis.close() }
    }

    fun set(key: String, value: String) {
        jedis.set(key, value).also { jedis.close() }
    }

    fun psetex(key: String, expireTime: Long, value: String) {
        jedis.psetex(key, expireTime, value).also { jedis.close() }
    }

    fun del(key: String) {
        jedis.del(key).also { jedis.close() }
    }
}