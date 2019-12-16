package com.hf.authorize;

import com.hf.authorize.session.CustomerSessionDao;
import com.hf.authorize.session.CustomerSessionFactory;
import com.hf.authorize.session.CustomerSessionManager;
import com.hf.dao.SessionMapper;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

/**
 * Created by fjm on 2017/12/25.
 */
@Configuration
public class ShiroConfiguration {

    //定义shiro的拦截器
    @Bean
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //设置SecurityManager
        bean.setSecurityManager(securityManager);
        //配置登录的url和登录成功的url
        bean.setLoginUrl("/login.html");
        bean.setSuccessUrl("/index.html");
        //配置访问权限
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        //anon表示可以匿名访问,authc表示需要认证才能访问
        filterChainDefinitionMap.put("/login.html", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/login?logout", "anon");
        filterChainDefinitionMap.put("/login/qq", "anon");
        filterChainDefinitionMap.put("/index.html", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/images/**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/bootstrap-3.3.7/**", "anon");
        filterChainDefinitionMap.put("/font-awesome-4.6.3/**", "anon");
        filterChainDefinitionMap.put("/signin/**", "anon");//关闭shiro对spring-social的拦截
        filterChainDefinitionMap.put("/*", "authc");
        filterChainDefinitionMap.put("/**", "authc");
        filterChainDefinitionMap.put("/*.*", "authc");
        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean;
    }

    //核心安全事务管理器；即所有与安全有关的操作都会与SecurityManager交互；且它管理着所有Subject
    @Bean(name = "securityManager")
    public SecurityManager securityManager(@Qualifier("authRealm") AuthRealm authRealm,
            @Qualifier("sessionManager") CustomerSessionManager customerSessionManager) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setSessionManager(customerSessionManager);
        //SecurityManager要验证用户身份，那么它需要从Realm获取相应的用户进行比较以确定用户身份是否合法；
        //也需要从Realm得到用户相应的角色/权限进行验证用户是否能进行操作；可以把Realm看成DataSource，即安全数据源。
        manager.setRealm(authRealm);
        return manager;
    }

    @Bean(name = "sessionManager")
    public CustomerSessionManager customerSessionManager(SessionMapper sessionMapper) {
        CustomerSessionManager customerSessionManager = new CustomerSessionManager();
        CustomerSessionDao customerSessionDao = new CustomerSessionDao(sessionMapper);
        customerSessionManager.setSessionDAO(customerSessionDao);
        customerSessionManager.setSessionFactory(new CustomerSessionFactory());
        return customerSessionManager;
    }


    //配置自定义的权限登录器
    @Bean(name = "authRealm")
    public AuthRealm authRealm(@Qualifier("credentialsMatcher") CredentialsMatcher matcher) {
        AuthRealm authRealm = new AuthRealm();
        authRealm.setCredentialsMatcher(matcher);//放入shiro.调用CredentialsMatcher检验密码
        return authRealm;
    }

    //配置自定义的密码比较器
    @Bean(name = "credentialsMatcher")
    public CredentialsMatcher credentialsMatcher() {
        return new CredentialsMatcher();
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

//    改为使用注解配置的方式
//    @Bean
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
//        DefaultAdvisorAutoProxyCreator creator=new DefaultAdvisorAutoProxyCreator();
//        creator.setProxyTargetClass(true);
//        return creator;
//    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            @Qualifier("securityManager") SecurityManager manager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(manager);
        return advisor;
    }
}
