package com.hf.helper

import com.hf.authorize.SESSION_KEEP_ALIVE_TIME
import com.hf.authorize.SESSION_KEY_PREFIX
import com.hf.config.RedisService
import com.hf.dto.Session
import com.hf.exception.HfExceptions
import com.hf.util.JsonConverter
import com.hf.util.SessionUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.UUID
import javax.servlet.http.Cookie

@Component
class SessionHelper @Autowired constructor(private val redisService: RedisService) {
    fun findSession(sessionId: String): Session {
        val sessionValue: String = redisService.get(SESSION_KEY_PREFIX + sessionId)
                ?: throw HfExceptions.sessionExpiredException()
        return JsonConverter.deserialize<Session>(sessionValue)!!
    }

    fun initSession(userId: Long, username: String): Session {
        val session = Session().apply {
            this.userId = userId
            this.host = SessionUtil.request.get().remoteHost
            this.username = username
            this.expireTime = System.currentTimeMillis().also {
                createTime = it
            } + SESSION_KEEP_ALIVE_TIME
        }
        val sessionId = UUID.randomUUID().toString().replace("-", "")
        redisService.psetex(SESSION_KEY_PREFIX + sessionId, SESSION_KEEP_ALIVE_TIME, JsonConverter.serialize(session)!!)
        SessionUtil.session.set(session)
        SessionUtil.response.get()?.addCookie(Cookie("hf-session", sessionId).apply {
            isHttpOnly = true
            maxAge = SESSION_KEEP_ALIVE_TIME.toInt() * 48
        })
        return session
    }

    fun destroySession(sessionId: String) = redisService.del(SESSION_KEY_PREFIX + sessionId)

    fun touchSession(sessionId: String) {
        val session = findSession(sessionId)
        session.expireTime = session.expireTime!! + SESSION_KEEP_ALIVE_TIME
        redisService.psetex(SESSION_KEY_PREFIX + sessionId, SESSION_KEEP_ALIVE_TIME, JsonConverter.serialize(session)!!)
    }
}

