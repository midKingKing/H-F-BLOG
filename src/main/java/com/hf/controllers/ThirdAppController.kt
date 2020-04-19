package com.hf.controllers

import com.hf.authorize.GitHubApp
import com.hf.authorize.SESSION_NAME
import com.hf.authorize.VIEW_INDEX
import com.hf.exception.HfExceptions
import com.hf.service.ThirdAppCodeAuthService
import com.hf.service.UserService
import com.hf.type.AppType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("third/app/{appType}")
open class ThirdAppController @Autowired constructor(
        private val userService: UserService,
        private val thirdAppServices: List<ThirdAppCodeAuthService<*>>
) {
    private var serviceMap: MutableMap<AppType, ThirdAppCodeAuthService<*>> = mutableMapOf()

    @PostConstruct
    open fun init() {
        thirdAppServices.forEach {
            serviceMap[it.appType] = it
        }
    }

    @GetMapping
    fun redirectToApp(@PathVariable appType: String): String = when (AppType.valueOf(appType)) {
        AppType.GITHUB -> "redirect:${GitHubApp.CILENT_CODE_URL}${GitHubApp.CLIENT_ID}"
    }

    @GetMapping("callback")
    fun callback(@PathVariable appType: String, @RequestParam(name = "code") code: String): String {
        serviceMap[AppType.valueOf(appType)]?.login(code) ?: throw HfExceptions.unsupportedAppType()
        return "redirect:$VIEW_INDEX"
    }

    @GetMapping("logout")
    fun logOut(request: HttpServletRequest): String {
        request.cookies.find { it.name == SESSION_NAME }?.value?.apply {
            userService.logoutBySessionId(this)
        }
        return VIEW_INDEX //如果session为null，返回login
    }
}