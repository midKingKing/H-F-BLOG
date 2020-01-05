package com.hf.pojo

import com.fasterxml.jackson.annotation.JsonInclude
import com.github.pagehelper.Page
import kotlin.reflect.full.memberFunctions

class ResponseData {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var code: String? = null
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var message: String? = null
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var rows: List<*>? = null
        private set(value) {
            field = value
            total = if (rows is Page<*>?) {
                //jdk 1.6的pagehelper源码貌似不支持
                Page::class.memberFunctions.find { it.name == "getTotal" }?.call(rows) as Long
            } else {
                rows?.size?.toLong()
            }
        }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var total: Long? = null

    private var isSuccess = true

    @JvmOverloads
    constructor(success: Boolean = true) {
        this.isSuccess = success
    }

    constructor(list: List<*>) : this(true) {
        rows = list
    }
}
