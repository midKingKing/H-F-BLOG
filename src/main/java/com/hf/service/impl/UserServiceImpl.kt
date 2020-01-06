package com.hf.service.impl

import com.hf.dao.UserMapper
import com.hf.dto.User
import com.hf.helper.SessionHelper
import com.hf.helper.VerificationHelper
import com.hf.service.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class UserServiceImpl @Autowired constructor(
    private val userMapper: UserMapper,
    private val sessionHelper: SessionHelper
) : BaseServiceImpl<User>(), IUserService {
    override fun findByUserName(username: String): User =
        userMapper.selectOne(User().apply { this.username = username })

    override fun loginByUsernameAndPassword(username: String, password: String): User =
        userMapper.selectOne(User().apply { this.username = username }).apply {
            VerificationHelper.verify(password, this.password)
            sessionHelper.createSession(this)
        }
}