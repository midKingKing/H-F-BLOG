package com.hf.exception

import java.lang.RuntimeException

class HfExceptions {
    companion object {
        @Throws(RuntimeException::class)
        fun sessionExpiredException() = RuntimeException("session expired")

        @Throws(RuntimeException::class)
        fun usernameOrPasswordErrorException() = RuntimeException("username or password error")

        @Throws(RuntimeException::class)
        fun invalidRoute() = RuntimeException("invalid route")

        @Throws(RuntimeException::class)
        fun unsupportedAppType() = RuntimeException("unsupported app type")

        @Throws(RuntimeException::class)
        fun thirdAppUserNotFound() = RuntimeException("third app user not found")

    }
}