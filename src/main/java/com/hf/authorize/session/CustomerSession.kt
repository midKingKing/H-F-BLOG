package com.hf.authorize.session

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.shiro.session.Session
import org.apache.shiro.session.mgt.ValidatingSession
import java.io.Serializable
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class CustomerSession(private val sessionDto: com.hf.dto.Session) : ValidatingSession {
    companion object {
        fun convertFromShiroSession(session: Session): com.hf.dto.Session = com.hf.dto.Session().apply {
            uuid = session.id as String
            createTime = session.startTimestamp.time
            expireTime = createTime!! + session.timeout
            host = session.host
            session as CustomerSession
            attr = ObjectMapper().writeValueAsString(session.attr)
        }
    }

    val attr: ConcurrentHashMap<Any?, Any?> = ConcurrentHashMap()

    override fun getTimeout(): Long = sessionDto.expireTime ?: -1L

    override fun touch() {
        sessionDto.expireTime = System.currentTimeMillis() + 3600 * 1000L
    }

    override fun getId(): Serializable = sessionDto.uuid!!

    override fun removeAttribute(key: Any?): Any? {
        return attr.remove(key)
    }

    override fun getHost(): String = sessionDto.host ?: ""

    override fun getAttributeKeys(): ConcurrentHashMap.KeySetView<Any?, Any?> {
        return attr.keys
    }

    override fun setTimeout(maxIdleTimeInMillis: Long) {
        sessionDto.expireTime = System.currentTimeMillis() + maxIdleTimeInMillis
    }

    override fun getLastAccessTime(): Date {
        return Date(sessionDto.expireTime?.minus(1800 * 1000L)?:-1L)
    }

    override fun stop() {
        sessionDto.expireTime = System.currentTimeMillis()
    }

    override fun getAttribute(key: Any?): Any? = attr[key]

    override fun setAttribute(key: Any?, value: Any?) {
        attr[key ?: ""] = value ?: ""
    }

    override fun getStartTimestamp(): Date = Date(sessionDto.createTime ?: -1L)

    override fun isValid(): Boolean {
        return sessionDto.expireTime > System.currentTimeMillis()
    }

    override fun validate() {

    }

}