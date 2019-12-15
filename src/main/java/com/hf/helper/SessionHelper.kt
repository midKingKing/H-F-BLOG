package com.hf.helper

import com.hf.dao.SessionMapper
import com.hf.dto.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SessionHelper @Autowired constructor (private val sessionMapper: SessionMapper) {
    fun getBySessionId(sessionId: String): Session? = sessionMapper.selectByPrimaryKey(sessionId)
}