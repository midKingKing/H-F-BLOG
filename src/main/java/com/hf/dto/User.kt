package com.hf.dto

import me.liuwj.ktorm.entity.Entity

interface User : Entity<User> {
    companion object: Entity.Factory<User>()
    val id: Long
    var username: String
    var password: String
}