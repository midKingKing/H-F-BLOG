package com.hf.authorize

import com.hf.util.DefaultInvocationHandler
import com.hf.util.ProxyUtils
import org.apache.shiro.session.mgt.SessionContext
import org.apache.shiro.subject.SubjectContext
import org.apache.shiro.web.mgt.DefaultWebSecurityManager
import org.apache.shiro.web.subject.support.DefaultWebSubjectContext
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

class CustomerSecurityManager : DefaultWebSecurityManager() {
    override fun createSessionContext(subjectContext: SubjectContext?): SessionContext {
        return getSessionContextProxy(super.createSessionContext(subjectContext))
    }

    override fun createSubjectContext(): SubjectContext {
        return getSubjectContextProxy(super.createSubjectContext())
    }

    private fun getSubjectContextProxy(subjectContext: SubjectContext): SubjectContext {
        return Proxy.newProxyInstance(
            subjectContext.javaClass.classLoader,
            arrayOf<Class<*>>(SubjectContext::class.java), SubjectContextWrapper(subjectContext)
        ) as SubjectContext
    }

    private fun getSessionContextProxy(sessionContext: SessionContext): SessionContext {
        return Proxy.newProxyInstance(
            sessionContext.javaClass.classLoader,
            arrayOf<Class<*>>(SubjectContext::class.java), SessionContextWrapper(sessionContext)
        ) as SessionContext
    }

    private inner class SubjectContextWrapper(subjectContext: Any?) : DefaultInvocationHandler(subjectContext) {
        @Throws(Throwable::class)
        override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any? {
            return ProxyUtils.safeMethodInvoke(targetObject, method, args)
        }
    }

    private inner class SessionContextWrapper(sessionContext: Any?) : DefaultInvocationHandler(sessionContext) {
        @Throws(Throwable::class)
        override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any? {
            return ProxyUtils.safeMethodInvoke(targetObject, method, args)
        }
    }
}