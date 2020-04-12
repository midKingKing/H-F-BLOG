package com.hf.controllers

import com.hf.authorize.GitHubApp
import com.hf.exception.HfExceptions
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("third/app/{appType}")
open class ThirdAppController {
    @GetMapping
    fun redirectToApp(@PathVariable appType: String): String = when (appType) {
        "github" -> "redirect:${GitHubApp.REQUEST_URL}"
        else -> throw HfExceptions.unsupportedAppType()
    }

    @GetMapping("callback")
    fun callback(@PathVariable appType: String, @RequestParam(name = "code") code: String) {
        TODO("request and get token,  then register user and login")
    }
}