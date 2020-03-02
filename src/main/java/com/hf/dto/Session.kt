package com.hf.dto

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "session")
class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var uuid: String? = null
    var username: String? = null
    var createTime: Long? = null
    var expireTime: Long? = null
    var host: String? = null
}