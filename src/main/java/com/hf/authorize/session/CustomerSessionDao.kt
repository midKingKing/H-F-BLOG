package com.hf.authorize.session

import com.hf.dao.SessionMapper
import org.apache.shiro.session.Session
import org.apache.shiro.session.mgt.eis.SessionDAO
import java.io.Serializable

class CustomerSessionDao(private val sessionMapper: SessionMapper) : SessionDAO {
    override fun update(session: Session?) {
        session?.let {
            sessionMapper.updateByPrimaryKey(CustomerSession.convertFromShiroSession(it))
        }
    }

    override fun getActiveSessions(): List<CustomerSession> =
        sessionMapper.select(com.hf.dto.Session().apply { disable = false }).map { CustomerSession(it) }

    override fun create(session: Session?): Serializable {
        return session?.let {
            var shiroSession = CustomerSession.convertFromShiroSession(it)
            sessionMapper.insert(shiroSession)
            shiroSession.uuid
        } ?: ""
    }

    override fun readSession(sessionId: Serializable?): Session {
        return CustomerSession(sessionMapper.selectByPrimaryKey(sessionId as String))
    }

    override fun delete(session: Session?) {
        sessionMapper.deleteByPrimaryKey(session?.id)
    }
}