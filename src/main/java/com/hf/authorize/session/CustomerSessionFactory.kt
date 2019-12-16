package com.hf.authorize.session

import org.apache.shiro.session.Session
import org.apache.shiro.session.mgt.SessionContext
import org.apache.shiro.session.mgt.SessionFactory
import java.util.UUID

class CustomerSessionFactory() : SessionFactory {
    override fun createSession(initData: SessionContext?): Session =
        CustomerSession(com.hf.dto.Session().apply {
            uuid = UUID.randomUUID().toString().replace("-", "")
            createTime = System.currentTimeMillis()
            expireTime = createTime!! + 1800L * 1000
            host = initData?.host
        })

}