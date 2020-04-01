package com.hf.config

import com.hf.util.DefaultInvocationHandler
import com.hf.util.ProxyUtils
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import redis.clients.jedis.commands.JedisCommands
import redis.clients.jedis.commands.MultiKeyCommands
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap

@Configuration
@EnableConfigurationProperties(RedisProperties::class)
@ConditionalOnClass(Jedis::class)
@ConditionalOnProperty(prefix = "redis", name = ["host"])
open class RedisConfiguration {
    @Bean
    open fun jedisClient(properties: RedisProperties): JedisClient = JedisClient(JedisPool(JedisPoolConfig().apply {
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

private val multiKeyCommandMaps: MutableMap<Jedis, Any> = ConcurrentHashMap()

private val jedisCommandMaps: MutableMap<Jedis, Any> = ConcurrentHashMap()

fun JedisPool.wrappedJedisCommands(): JedisCommands = jedisCommandMaps.computeIfAbsent(resource) {
    Proxy.newProxyInstance(
        javaClass.classLoader,
        arrayOf(JedisCommands::class.java),
        WrappedJedis(resource)
    )
} as JedisCommands

fun JedisPool.wrappedMultiKeyCommands(): MultiKeyCommands = multiKeyCommandMaps.computeIfAbsent(resource) {
    Proxy.newProxyInstance(
        javaClass.classLoader,
        arrayOf(MultiKeyCommands::class.java),
        WrappedJedis(it)
    )
} as MultiKeyCommands

class WrappedJedis(private val jedis: Jedis) : DefaultInvocationHandler(targetObject = jedis) {
    @Throws(Exception::class)
    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any? {
        return ProxyUtils.safeMethodInvoke(jedis, method, args).apply {
            if (method.declaringClass == JedisCommands::class.java || method.declaringClass == MultiKeyCommands::class.java) {
                jedis.close()
            }
        }
    }
}

class JedisClient constructor(val jedisPool: JedisPool) : JedisCommands by jedisPool.wrappedJedisCommands(),
    MultiKeyCommands by jedisPool.wrappedMultiKeyCommands()
