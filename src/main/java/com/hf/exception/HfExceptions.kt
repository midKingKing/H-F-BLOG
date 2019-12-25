package com.hf.exception

import java.lang.RuntimeException

class HfExceptions {
    companion object {
        @Throws(RuntimeException::class)
        fun sessionExpiredException() = RuntimeException("session expired")
        @Throws(RuntimeException::class)
        fun usernameOrPasswordErrorException() = RuntimeException("username or passowrd error")
    }
}