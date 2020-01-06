package com.hf.dto

import javax.persistence.Id

class User {
    @Id
    var uid: Int? = null

    var username: String? = null

    var password: String? = null
}