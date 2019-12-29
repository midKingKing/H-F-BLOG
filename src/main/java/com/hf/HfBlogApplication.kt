package com.hf

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.transaction.annotation.EnableTransactionManagement

@MapperScan(basePackages = ["com.hf.dao"]) //mapper扫描包路径
@EnableTransactionManagement //启动事物注解
@EnableAspectJAutoProxy(exposeProxy = true) //暴露当前aop代理
@SpringBootApplication
open class HfblogApplication {
}
fun main(args: Array<String>) {
    SpringApplication.run(HfblogApplication::class.java, *args)
}
