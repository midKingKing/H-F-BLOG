package com.hf.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.full.companionObject

open class Logging {
    val log: Logger = getLogger(this.javaClass)

    private fun <T> unwrapCompanionClass(clazz: Class<T>): Class<*> {
        return if (clazz.enclosingClass?.kotlin?.companionObject?.java == clazz) {
            clazz.enclosingClass
        } else {
            clazz
        }
    }

    private fun <T> getLogger(clazz: Class<T>): Logger {
        return LoggerFactory.getLogger(unwrapCompanionClass(clazz).simpleName)
    }
}
