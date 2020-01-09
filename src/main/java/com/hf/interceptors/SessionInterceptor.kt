package com.hf.interceptors

import com.hf.dto.User
import com.hf.helper.SessionHelper
import com.hf.util.SessionUtil
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SessionInterceptor(private val sessionHelper: SessionHelper) : HandlerInterceptorAdapter() {
    override fun preHandle(request: HttpServletRequest?, response: HttpServletResponse?, handler: Any?): Boolean {
        SessionUtil.request.set(request)
        SessionUtil.response.set(response)
        request?.cookies?.toList()?.find { it.name == "hf-session" }?.let {
            val session = sessionHelper.getBySessionId(it.value)
            if (session != null) {
                if (session.expireTime ?: 0 > System.currentTimeMillis()) {
                    sessionHelper.touchSession(session)
                    SessionUtil.session.set(session)
                } else {
                    session.uuid?.run {
                        sessionHelper.deleteSession(this)
                    }
                }
            }
        }
        return true
    }

    override fun postHandle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        handler: Any?,
        modelAndView: ModelAndView?
    ) {
        modelAndView?.model?.set("user", User().apply {
            username = SessionUtil.session.get()?.username
        })
        super.postHandle(request, response, handler, modelAndView)
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