package com.hf.service

import com.hf.dao.Users
import com.hf.dto.User
import com.hf.exception.HfExceptions
import com.hf.helper.SessionHelper
import com.hf.helper.VerificationHelper
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.from
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.where
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.sequenceOf
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class UserService constructor(
        @Autowired private val database: Database,
        @Autowired private val sessionHelper: SessionHelper
) {
    open fun loginByUsernameAndPassword(username: String, password: String): User =
            database.sequenceOf(Users).find { it.username eq username }?.apply {
                VerificationHelper.verify(password, this.password)
                sessionHelper.initSession(id, username)
            } ?: throw HfExceptions.usernameOrPasswordErrorException()

    open fun logoutBySessionId(sessionId: String) = sessionHelper.destroySession(sessionId)
}