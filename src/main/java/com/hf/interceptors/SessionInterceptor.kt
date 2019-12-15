package com.hf.interceptors

import com.hf.exception.HfExceptions
import com.hf.helper.SessionHelper
import com.hf.util.SessionUtil
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SessionInterceptor(private val sessionHelper: SessionHelper) : HandlerInterceptorAdapter() {
    override fun preHandle(request: HttpServletRequest?, response: HttpServletResponse?, handler: Any?): Boolean {
        return request?.getHeader("hf-session")?.let {
            val session = sessionHelper.getBySessionId(it)
            (session?.expireTime?.compareTo(System.currentTimeMillis())!! > 0).apply {
                if (this) SessionUtil.session.set(session) else throw HfExceptions.sessionExpiredException()
            }
        } ?: throw HfExceptions.sessionExpiredException()
    }

    override fun afterCompletion(request: HttpServletRequest?, response: HttpServletResponse?, handler: Any?, ex: Exception?) {
        SessionUtil.session.remove()
    }
}