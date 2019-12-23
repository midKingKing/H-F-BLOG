package com.hf.authorize;

/**
 * qq的配置信息
 *
 * @author jingmin.feng@hand-china.com
 * @create 2018-04-30 23:33
 **/
public interface SecurityConstants {

    Long SESSION_KEEP_ALIVE_TIME = 1800 * 1000L;

    /**
     * 提供商的ID
     */
    String DEFAULT_SOCIAL_QQ_PROVIDER_ID = "qq";
    /**
     * qq appID
     */
    String DEFAULT_SOCIAL_QQ_APP_ID = "101448999";

    /**
     * qq appsECRET
     */
    String DEFAULT_SOCIAL_QQ_APP_SECRET = "1d958787a87559bad371c0a9e26eef61";

}
