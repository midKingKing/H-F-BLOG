package com.hf.service

import com.hf.dto.User
import com.hf.helper.SessionHelper
import com.hf.type.AppType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

/**
 * just support oauth2 code mode
 */
abstract class ThirdAppCodeAuthService<T> {
    @Autowired
    protected lateinit var sessionHelper: SessionHelper

    abstract val appType: AppType

    @Transactional(rollbackFor = [Throwable::class])
    open fun login(code: String) {
        val accessToken = getAccessToken(code)
        val thirdAppInfo = getInfo(accessToken)
        val user = getOrCreate(thirdAppInfo)
        sessionHelper.initSession(username = user.username, userId = user.id)
    }

    protected abstract fun getAccessToken(code: String): String

    protected abstract fun getInfo(accessToken: String): T

    protected abstract fun getOrCreate(info: T): User
}