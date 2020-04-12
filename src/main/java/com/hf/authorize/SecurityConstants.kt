@file:JvmName("SecurityConstants")

package com.hf.authorize

const val SESSION_KEEP_ALIVE_TIME = 1800 * 1000L

const val SESSION_KEY_PREFIX = "hf:session:"

//TODO hard code is not a good idea
object GitHubApp {
    const val CLIENT_ID = "72ea7f69f048a9a2485e"

    const val CLIENT_SECRET = "22592362c6bbb055a357e66f3b0ee83ef7472ea6"

    private const val CALLBACK_URL = "http://localhost:9090/third/app/github/callback"

    private const val REDIRECT_URL = "http://localhost:9090/index.html"

    const val TOKEN_URL = "https://github.com/login/oauth/access_token"

    const val USER_URL = "https://api.github.com/user?access_token="

    const val REQUEST_URL = "https://github.com/login/oauth/authorize?client_id=$CLIENT_ID&redirect_url=$CALLBACK_URL&scope=user"

    const val USER_INFO_URL = "https://api.github.com/user"

    const val CILENT_CODE_URL = "https://github.com/login/oauth/authorize?client_id="
}