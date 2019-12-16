package com.hf.authorize.session

import com.hf.dao.SessionMapper
import org.apache.shiro.session.Session
import org.apache.shiro.session.mgt.eis.SessionDAO
import tk.mybatis.mapper.entity.Example
import java.io.Serializable

class CustomerSessionDao(private val sessionMapper: SessionMapper) : SessionDAO {
    override fun update(session: Session?) {
        session?.let {
            sessionMapper.updateByPrimaryKey(CustomerSession.convertFromShiroSession(it))
        }
    }

    override fun getActiveSessions(): List<CustomerSession> {
        return sessionMapper.selectByExample(Example(com.hf.dto.Session::class.java).createCriteria().andCondition("expire_time > " + System.currentTimeMillis())).map { CustomerSession(it) }
    }

    override fun create(session: Session?): Serializable {
        return session?.let {
            var shiroSession = CustomerSession.convertFromShiroSession(it)
            sessionMapper.insert(shiroSession)
            shiroSession.uuid
        } ?: ""
    }

    override fun readSession(sessionId: Serializable?): Session {
        return sessionMapper.selectByExample(Example(com.hf.dto.Session::class.java).createCriteria().andCondition("uuid = $sessionId")).map { CustomerSession(it) }.first()
    }

    override fun delete(session: Session?) {
        sessionMapper.deleteByPrimaryKey(session?.id)
    }
}