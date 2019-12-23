package com.hf.helper

import com.hf.exception.HfExceptions
import org.apache.tomcat.util.security.MD5Encoder

class VerificationHelper {
    companion object {
        private const val salt: String = "HF_SALT_996_ICU"

        fun verify(password: String, passwordInDb: String) {
            if (MD5Encoder.encode((password + salt).toByteArray()) != passwordInDb) {
                throw HfExceptions.usernameOrPasswordErrorException()
            }
        }
    }
}