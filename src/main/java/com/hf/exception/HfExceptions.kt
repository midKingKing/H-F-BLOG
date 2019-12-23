package com.hf.exception

import java.lang.RuntimeException

class HfExceptions {
    companion object {
        fun sessionExpiredException() = RuntimeException("session expired")
        fun usernameOrPasswordErrorException() = RuntimeException("username or passowrd error")
    }
}