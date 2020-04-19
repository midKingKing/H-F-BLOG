package com.hf.helper

import com.hf.exception.HfExceptions
import org.apache.tomcat.util.security.MD5Encoder
import java.security.MessageDigest
import kotlin.experimental.and

@Deprecated(message = "useless")
class VerificationHelper {
    companion object {
        private const val salt: String = "HF_SALT_996_ICU"

        private val HEX_CHARS =
            charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

        fun verify(password: String, passwordInDb: String) {
            if (md5Encode(password) != passwordInDb) {
                throw HfExceptions.usernameOrPasswordErrorException()
            }
        }

        private fun md5Encode(password: String): String {
            val digest = MessageDigest.getInstance("MD5").digest((password + salt).toByteArray())
            val chars = CharArray(32)
            var i = 0
            while (i < chars.size) {
                val b = digest[i / 2]
                chars[i] = HEX_CHARS[b.toInt() ushr (0x4) and 0xf]
                chars[i + 1] = HEX_CHARS[(b and 0xf).toInt()]
                i += 2
            }
            return String(chars)
        }
    }
}