package com.hf.service

import com.hf.authorize.GitHubApp
import com.hf.dao.ThirdApps
import com.hf.dao.Users
import com.hf.dto.GitHubUser
import com.hf.dto.ThirdApp
import com.hf.dto.User
import com.hf.exception.HfExceptions
import com.hf.type.AppType
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.sequenceOf
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
open class GithupAppService @Autowired constructor(
        private val database: Database,
        private val restTemplate: RestTemplate
) : ThirdAppCodeAuthService<GitHubUser>() {
    override val appType: AppType = AppType.GITHUB

    override fun getAccessToken(code: String): String {
        val paraMap = mapOf("code" to code, "client_id" to GitHubApp.CLIENT_ID, "client_secret" to GitHubApp.CLIENT_SECRET)
        val httpEntity = HttpEntity(paraMap, HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON_UTF8
        })
        val result = restTemplate.postForObject(GitHubApp.TOKEN_URL, httpEntity, HashMap::class.java)
        return result["access_token"] as String
    }

    override fun getInfo(accessToken: String): GitHubUser {
        return restTemplate.getForObject(GitHubApp.USER_URL + accessToken, GitHubUser::class.java)
    }

    override fun getOrCreate(info: GitHubUser): User {
        val thirdApp: ThirdApp? = database.sequenceOf(ThirdApps).find { it.thirdAppId eq info.id!! }
        return if (null != thirdApp) {
            database.sequenceOf(Users).find { it.id eq thirdApp.uid }
                    ?: throw HfExceptions.thirdAppUserNotFound()
        } else {
            val user = User {
                username = info.name!!
            }
            database.sequenceOf(Users).add(user)
            database.sequenceOf(ThirdApps).add(ThirdApp {
                appType = this@GithupAppService.appType.code
                thirdAppId = info.id!!
                uid = user.id
            })
            user
        }
    }
}