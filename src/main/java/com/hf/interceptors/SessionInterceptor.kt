package com.hf.interceptors

import com.hf.helper.SessionHelper
import com.hf.util.SessionUtil
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SessionInterceptor(private val sessionHelper: SessionHelper) : HandlerInterceptorAdapter() {
    override fun preHandle(request: HttpServletRequest?, response: HttpServletResponse?, handler: Any?): Boolean {
        request?.getHeader("hf-session")?.let {
            val session = sessionHelper.getBySessionId(it)
            if (session != null) {
                if (session.expireTime > System.currentTimeMillis()) {
                    sessionHelper.touchSession(session)
                    SessionUtil.session.set(session)
                } else {
                    sessionHelper.deleteSession(session)
                }
            }
        }
        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        handler: Any?,
        ex: Exception?
    ) {
        SessionUtil.session.remove()
    }
}