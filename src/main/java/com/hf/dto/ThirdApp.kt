package com.hf.dto

import me.liuwj.ktorm.entity.Entity

interface ThirdApp : Entity<ThirdApp> {
    companion object: Entity.Factory<ThirdApp>()
    val appId : Long
    var appType : String
    var thirdAppId : Long
    var uid : Long
}