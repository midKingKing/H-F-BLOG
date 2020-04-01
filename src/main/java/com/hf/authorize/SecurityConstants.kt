@file:JvmName("SecurityConstants")

package com.hf.authorize

const val SESSION_KEEP_ALIVE_TIME = 1800 * 1000L

const val SESSION_KEY_PREFIX = "hf:session:"

//TODO hard code is not a good idea
object GitHubApp {
    private const val client_id = "cb48d9968f50fdcd754d"

    private const val client_secret = "1922eb3762108a84831729f273d9e1c6a6fe7519"

    private const val callback_url = "http://localhost:9090/third/app/github/callback"

    private const val redirect_url = "http://localhost:9090/index.html"

    const val request_url = "https://github.com/login/oauth/authorize?client_id=$client_id&redirect_url=$callback_url&scope=user"

    const val token_url = "https://github.com/login/oauth/access_token?client_id=$client_id&redirect_url=$redirect_url&client_secret=$client_secret&code=%s"

    const val user_info_url = "https://api.github.com/user"
}