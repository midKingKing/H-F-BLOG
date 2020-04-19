package com.hf.service

import com.hf.dao.Users
import com.hf.dto.User
import com.hf.helper.SessionHelper
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.sequenceOf
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class UserService constructor(
        @Autowired private val database: Database,
        @Autowired private val sessionHelper: SessionHelper
) {
    open fun logoutBySessionId(sessionId: String) = sessionHelper.destroySession(sessionId)

    open fun createUser(username: String): User {
        val user = User {
            this.username = username
        }
        database.sequenceOf(Users).add(user)
        return user
    }
}