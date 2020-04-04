package com.hf.interceptors

import com.hf.authorize.HfSession
import com.hf.dto.User
import com.hf.helper.SessionHelper
import com.hf.util.SessionUtil
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SessionInterceptor(private val sessionHelper: SessionHelper) : HandlerInterceptorAdapter() {
    override fun preHandle(request: HttpServletRequest?, response: HttpServletResponse?, handler: Any?): Boolean {
        SessionUtil.request.set(request)
        SessionUtil.response.set(response)
        if (handler is HandlerMethod && handler.hasMethodAnnotation(HfSession::class.java)) {
            request?.cookies?.find { it.name == "hf-session" }?.let {
                val session = sessionHelper.findSession(it.value)
                if (session.expireTime!! > System.currentTimeMillis()) {
                    sessionHelper.touchSession(it.value)
                    SessionUtil.session.set(session)
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
            username = SessionUtil.session.get()?.username ?: "unLogin"
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
        SessionUtil.request.remove()
        SessionUtil.response.remove()
    }
}