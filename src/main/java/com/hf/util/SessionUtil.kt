package com.hf.util

import com.hf.dto.Session

class SessionUtil {
    companion object {
        var session: ThreadLocal<Session> = ThreadLocal()
    }
}