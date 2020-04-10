package com.hf.service

import com.hf.dao.ThirdApps
import com.hf.dao.Users
import com.hf.dao.Users.username
import com.hf.dto.ThirdApp
import com.hf.dto.User
import com.hf.exception.HfExceptions
import com.hf.helper.SessionHelper
import com.hf.helper.VerificationHelper
import com.hf.logging.Logging
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.sequenceOf
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
open class UserService constructor(
        @Autowired private val database: Database,
        @Autowired private val sessionHelper: SessionHelper,
        @Autowired private val restTemplate:RestTemplate
) {
    open fun loginByUsernameAndPassword(username: String, password: String): User =
            database.sequenceOf(Users).find { it.username eq username }?.apply {
                VerificationHelper.verify(password, this.password)
                sessionHelper.initSession(id, username)
            } ?: throw HfExceptions.usernameOrPasswordErrorException()

    open fun logoutBySessionId(sessionId: String) = sessionHelper.destroySession(sessionId)

    open fun githubLogin(code: String) {
        val paraMap = hashMapOf("code" to code, "client_id" to CILENT_ID, "client_secret" to CILENT_SERCRET)
        val httoHeaders = HttpHeaders()
        val resultMap = hashMapOf<String, String>()
        httoHeaders.contentType = MediaType.APPLICATION_JSON_UTF8
        val httpEntity = HttpEntity(paraMap, httoHeaders)
        val result = restTemplate.postForObject(TOKEN_URL, httpEntity, resultMap.javaClass)
        val map = restTemplate.getForObject(USER_URL + result["access_token"], resultMap.javaClass)
        val id = map["id"].toString().toLong()

        val thirdApp:ThirdApp? = database.sequenceOf(ThirdApps).find { it.thirdAppId eq id }
        if(null !=thirdApp ){
             database.sequenceOf(Users).find { it.id eq thirdApp.uid }?.apply {
                sessionHelper.initSession(this.id,this.username)
            }?:throw HfExceptions.usernameOrPasswordErrorException()
        }else{
            val userSequence = database.sequenceOf(Users)
            val user = User {
                username = map["name"].toString()
                password = UUID.randomUUID().toString()
            }
            userSequence.add(user)

            val thirdAppsSequence = database.sequenceOf(ThirdApps)
            val thirdApp = ThirdApp {
                appType = "gitHub"
                thirdAppId = id
                uid = user["id"].toString().toLong()
            }
            thirdAppsSequence.add(thirdApp)
            sessionHelper.initSession(user["id"].toString().toLong(), map["name"].toString())
        }
    }

    companion object : Logging() {
        private const val CILENT_SERCRET = "22592362c6bbb055a357e66f3b0ee83ef7472ea6"
        private const val CILENT_ID = "72ea7f69f048a9a2485e"
        private const val TOKEN_URL = "https://github.com/login/oauth/access_token"
        private const val USER_URL = "https://api.github.com/user?access_token="
    }
}