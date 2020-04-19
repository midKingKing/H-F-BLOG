package com.hf.dao

import com.hf.dto.User
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.long
import me.liuwj.ktorm.schema.varchar

object Users : Table<User>("user") {
    val id by long("uid").primaryKey().bindTo { it.id }
    val username by varchar("username").bindTo { it.username }
}