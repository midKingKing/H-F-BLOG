package com.hf.controllers

import com.hf.logging.Logging
import com.hf.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest

@Controller
class LoginController @Autowired constructor(private val userService: UserService) {

    /**
     * 登录操作(表单登录为post请求)
     */
    @RequestMapping(value = ["/login"], method = [RequestMethod.POST])
    fun login(username: String, password: String): ModelAndView {
        return ModelAndView().apply {
            try {
                userService.loginByUsernameAndPassword(username, password)
                viewName = "redirect:$VIEW_INDEX.html"
            } catch (e: Exception) {
                log.error(e.message, e)
                viewName = VIEW_LOGIN
                addObject("message", "账号或密码错误")
            }
        }
    }

    /**
     * 登出并进入登录页面
     */
    @RequestMapping(value = ["/login"], params = ["logout"], method = [RequestMethod.GET])
    fun logOut(request: HttpServletRequest): String {
        request.cookies.find { it.name == "hf-session" }?.value?.apply {
            userService.logoutBySessionId(this)
        }
        return VIEW_LOGIN //如果session为null，返回login
    }

    companion object : Logging() {
        private const val VIEW_LOGIN = "/login"
        private const val VIEW_INDEX = "/index"
        private const val CILENT_CODE_URL = "https://github.com/login/oauth/authorize?client_id="
        private const val CILENT_ID = "72ea7f69f048a9a2485e"
    }

    /**
     * github第三方登陆获取code
     */
    @RequestMapping(value = ["/third/app/github"], method = [RequestMethod.GET])
    fun githubLogin(): String {
        return "redirect:$CILENT_CODE_URL$CILENT_ID"
    }

    /**
     * github第三方登陆回调地址
     */
    @RequestMapping(value = ["/third/app/github/callback"], method = [RequestMethod.GET])
    fun githubLoginCallback(request: HttpServletRequest, code: String): String {
        userService.githubLogin(code)
        return "redirect:$VIEW_INDEX.html"
    }

}
