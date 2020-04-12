package com.hf.service

import com.hf.authorize.GitHubApp
import com.hf.dao.ThirdApps
import com.hf.dao.Users
import com.hf.dto.GitHubUser
import com.hf.dto.ThirdApp
import com.hf.dto.User
import com.hf.exception.HfExceptions
import com.hf.helper.SessionHelper
import com.hf.helper.VerificationHelper
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
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

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

    @Transactional(rollbackFor = [Exception::class])
    open fun githubLogin(code: String) {
        val paraMap = hashMapOf("code" to code, "client_id" to GitHubApp.CLIENT_ID, "client_secret" to GitHubApp.CLIENT_SECRET)
        val httoHeaders = HttpHeaders()
        httoHeaders.contentType = MediaType.APPLICATION_JSON_UTF8
        val httpEntity = HttpEntity(paraMap, httoHeaders)
        val result = restTemplate.postForObject(GitHubApp.TOKEN_URL, httpEntity, HashMap::class.java)
        val gitHubUser= restTemplate.getForObject(GitHubApp.USER_URL + result["access_token"], GitHubUser::class.java)

        val thirdApp:ThirdApp? = database.sequenceOf(ThirdApps).find { it.thirdAppId eq gitHubUser.id!!}
        if(null !=thirdApp ){
             database.sequenceOf(Users).find { it.id eq thirdApp.uid }?.apply {
                sessionHelper.initSession(this.id,this.username)
            }?:throw HfExceptions.thirdAppUserNotFound()
        }else{
            val userSequence = database.sequenceOf(Users)
            val user = User {
                username = gitHubUser.name!!
                password = UUID.randomUUID().toString()
            }
            userSequence.add(user)

            val thirdAppsSequence = database.sequenceOf(ThirdApps)
            val thirdApp = ThirdApp {
                appType = "gitHub"
                thirdAppId = gitHubUser?.id!!
                uid = user.id
            }
            thirdAppsSequence.add(thirdApp)
            sessionHelper.initSession(user.id, gitHubUser.name!!)
        }
    }
}