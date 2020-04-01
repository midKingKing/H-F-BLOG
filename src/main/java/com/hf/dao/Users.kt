package com.hf.dao

import com.hf.dto.User
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.long
import me.liuwj.ktorm.schema.varchar

object Users : Table<User>("user") {
    val id by long("id").primaryKey().bindTo { it.id }
    val username by varchar("username").bindTo { it.username }
    val password by varchar("password").bindTo { it.password }
}