package com.hf.config

import com.hf.helper.SessionHelper
import com.hf.interceptors.SessionInterceptor
import com.hf.interceptors.SimpleAuthInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.DispatcherServlet
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
@EnableWebMvc
open class WebMvcConfig(@Autowired private val sessionHelper: SessionHelper) : WebMvcConfigurerAdapter() {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        super.addResourceHandlers(registry.apply {
            addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/")
            addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/")
            addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/")
            addResourceHandler("/src/**").addResourceLocations("classpath:/static/src/")
            addResourceHandler("/bootstrap-3.3.7/**").addResourceLocations("classpath:/static/bootstrap-3.3.7/")
            addResourceHandler("/font-awesome-4.6.3/**").addResourceLocations("classpath:/static/font-awesome-4.6.3/")
        })
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        super.addInterceptors(registry.apply {
            //make sure the order from session is before then auth
            addInterceptor(SessionInterceptor(sessionHelper)).addPathPatterns("/**")
            addInterceptor(SimpleAuthInterceptor())
        })
    }

    @Bean
    open fun dispatcherRegistration(dispatcherServlet: DispatcherServlet): ServletRegistrationBean {
        return ServletRegistrationBean(
            dispatcherServlet.apply { setThrowExceptionIfNoHandlerFound(true) }
        )
    }
}