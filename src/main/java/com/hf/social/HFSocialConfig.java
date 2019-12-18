package com.hf.social;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ConnectSupport;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

import java.lang.reflect.Field;

/**
 * 社交登录配置类
 *
 * @author jingmin.feng@hand-china.com
 * @create 2018-04-30 23:29
 **/
@Configuration
@EnableSocial
public class HFSocialConfig extends SocialAutoConfigurerAdapter implements InitializingBean {

    @Autowired
    private ProviderSignInController providerSignInController;

    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        return new QQConnectionFactory();
    }

    @Bean
    public UsersConnectionRepository myUsersConnectionRepository() {
        return new MyJdbcUserConnectionRepository();
    }

    @Bean
    public ProviderSignInController providerSignInController(ConnectionFactoryLocator connectionFactoryLocator, @Qualifier("myUsersConnectionRepository") UsersConnectionRepository usersConnectionRepository) {
        ProviderSignInController providerSignInController = new ProviderSignInController(connectionFactoryLocator, usersConnectionRepository, new SignInAdapter() {
            @Override
            public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
                return "index.html";
            }
        });
        return providerSignInController;
    }

    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator factoryLocator) {
        return new ProviderSignInUtils(factoryLocator, getUsersConnectionRepository(factoryLocator));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            Field connectSupport = providerSignInController.getClass().getDeclaredField("connectSupport");
            connectSupport.setAccessible(true);
            ConnectSupport support = (ConnectSupport) connectSupport.get(providerSignInController);
            support.setCallbackUrl("http://www.merryyou.cn/login/qq");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
