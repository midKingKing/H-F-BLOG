package com.hf.authorize.session

import org.apache.shiro.web.servlet.SimpleCookie
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager

class CustomerSessionManager : DefaultWebSessionManager() {
    init {
        val cookie = SimpleCookie("hf-session")
        cookie.isHttpOnly = true
        this.sessionIdCookie = cookie
    }
}