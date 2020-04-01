package com.hf.dto

data class Session(var userId: Long? = null,
        var username: String? = null,
        var createTime: Long? = null,
        var expireTime: Long? = null,
        var host: String? = null)