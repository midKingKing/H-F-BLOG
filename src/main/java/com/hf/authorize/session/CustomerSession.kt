package com.hf.authorize.session

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.shiro.session.Session
import java.io.Serializable
import java.util.Date
import java.util.concurrent.ConcurrentHashMap

class CustomerSession(private val customerSession: com.hf.dto.Session) : Session {
    companion object {
       fun convertFromShiroSession(session: Session): com.hf.dto.Session = com.hf.dto.Session().apply {
           uuid = session.id as String
           createTime = session.startTimestamp.time
           expireTime = createTime!! + session.timeout
           host = session.host
           attr = ObjectMapper().writeValueAsString(session.attr)
       }
    }

    var attr: Map<String, Any> = ConcurrentHashMap()

    override fun getTimeout(): Long = customerSession.expireTime ?: -1L

    override fun touch() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getId(): Serializable = customerSession.uuid!!

    override fun removeAttribute(key: Any?): Any = ""

    override fun getHost(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAttributeKeys(): Set<String> = attr.keys

    override fun setTimeout(maxIdleTimeInMillis: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLastAccessTime(): Date {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAttribute(key: Any?): Any? = attr[key]

    override fun setAttribute(key: Any?, value: Any?) {
    }

    override fun getStartTimestamp(): Date = Date(customerSession.createTime ?: -1L)
}