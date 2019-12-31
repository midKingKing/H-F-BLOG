package com.hf.interceptors

import org.apache.ibatis.plugin.Interceptor
import org.apache.ibatis.plugin.Invocation
import org.apache.ibatis.plugin.Plugin
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.Properties

//TODO: add sql exec time log
class SqlInterceptor : Interceptor {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(this.javaClass)
    }

    @Throws(Throwable::class)
    override fun intercept(invocation: Invocation): Any {
        val cts = System.currentTimeMillis()
        return invocation.proceed().apply {
            logger.info("statement exec: {} ms ", System.currentTimeMillis() - cts)
        }
    }

    override fun plugin(o: Any): Any {
        return Plugin.wrap(o, this)
    }

    override fun setProperties(properties: Properties) {
    }
}