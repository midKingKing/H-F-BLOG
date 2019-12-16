package com.hf.dto

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var uuid: String? = null
    var createTime: Long? = null
    var expireTime: Long? = null
    var disable: Boolean = true
}