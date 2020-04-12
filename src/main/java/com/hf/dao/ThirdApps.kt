package com.hf.dao

import com.hf.dto.ThirdApp
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.long
import me.liuwj.ktorm.schema.varchar

object ThirdApps : Table<ThirdApp>("third_app") {
    val appId by long("app_id").primaryKey().bindTo { it.appId }
    val thirdAppId by long("third_app_id").bindTo { it.thirdAppId }
    val uid by long("uid").bindTo { it.uid }
    val appType by varchar("app_type").bindTo { it.appType }
}