package com.hf.service

import com.hf.dao.UserMapper
import com.hf.dto.User
import com.hf.helper.SessionHelper
import com.hf.helper.VerificationHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class UserService constructor(
        @Autowired private val userMapper: UserMapper,
        @Autowired private val sessionHelper: SessionHelper
) : BaseService<User>() {
    open fun findByUserName(username: String): User =
            userMapper.selectOne(User().apply { this.username = username })

    open fun loginByUsernameAndPassword(username: String, password: String): User =
            userMapper.selectOne(User().apply { this.username = username }).apply {
                VerificationHelper.verify(password, this.password ?: "")
                sessionHelper.createSession(this)
            }
}