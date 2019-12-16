package com.hf.authorize.session

import org.apache.shiro.session.Session
import java.io.Serializable
import java.util.Date
import java.util.UUID

class CustomerSession(private val customerSession: com.hf.dto.Session) : Session {
    companion object {
       fun convertFromShiroSession(session: Session): com.hf.dto.Session = com.hf.dto.Session().apply {
           uuid = UUID.randomUUID().toString().replace("-","")
           createTime = System.currentTimeMillis()
           expireTime = createTime!! + 10000L
       }
    }

    override fun getTimeout(): Long = customerSession.expireTime ?: -1L

    override fun touch() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getId(): Serializable = customerSession.uuid!!

    override fun removeAttribute(key: Any?): Any = ""

    override fun getHost(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAttributeKeys(): MutableCollection<Any> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setTimeout(maxIdleTimeInMillis: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLastAccessTime(): Date {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAttribute(key: Any?): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setAttribute(key: Any?, value: Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getStartTimestamp(): Date = Date(customerSession.createTime ?: -1L)
}