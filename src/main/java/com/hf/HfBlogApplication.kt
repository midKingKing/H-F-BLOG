package com.hf

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.transaction.annotation.EnableTransactionManagement

@EnableTransactionManagement
@EnableAspectJAutoProxy(exposeProxy = true) //暴露当前aop代理
@SpringBootApplication
open class HfblogApplication
fun main(args: Array<String>) {
    SpringApplication.run(HfblogApplication::class.java, *args)
}
