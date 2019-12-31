package com.hf.util

import com.hf.dto.Session
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SessionUtil {
    companion object {
        var session: ThreadLocal<Session> = ThreadLocal()
        var request: ThreadLocal<HttpServletRequest> = ThreadLocal()
        var response: ThreadLocal<HttpServletResponse> = ThreadLocal()
    }
}